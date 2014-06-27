/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package exoplatform.codefest.taskmanager.services.project.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Value;
import javax.jcr.query.Query;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.ProjectExistException;
import exoplatform.codefest.taskmanager.exceptions.StageExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.id.IdGeneratorService;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.NodeTypes;
import exoplatform.codefest.taskmanager.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class ProjectServiceImpl implements ProjectService {

  private NodeHierarchyCreator nodeCreator;
  private String projectRootNodeName;
  
  public ProjectServiceImpl(NodeHierarchyCreator nodeCreator, InitParams initParams) {
    this.nodeCreator = nodeCreator;
    this.projectRootNodeName = initParams.getValueParam("projectRootNodeName").getValue();
  }
  
  public List<Project> getAllProjectByUser(String user) throws TaskManagerException {
    List<Project> ret = new ArrayList<Project>();
    try {
      Node pRoot = getProjectRootNode();
      String statement = "SELECT * FROM " + NodeTypes.PROJECT + " WHERE jcr:path LIKE '" + 
                         pRoot.getPath() + "/%' AND CONTAINS(" + NodeTypes.PROJECT_MEMBERS +
                         ",'" + user + "')";
      Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
      for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
        Node p = iter.nextNode();
        ret.add(convertToEntity(p));
      }
      return ret;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
    
  }

  @Override
  public Project addProject(String pName, String creator, String description) throws ProjectExistException, TaskManagerException {
    Node pRoot = getProjectRootNode();
    if (getProject(creator, pName) != null) {
      throw new ProjectExistException();
    }
    try {
      int newId = Utils.getService(IdGeneratorService.class).generateId(NodeTypes.PROJECT);
      Node pNode = pRoot.addNode(newId + "", NodeTypes.PROJECT);
      pNode.setProperty(NodeTypes.PROJECT_ID, newId);
      pNode.setProperty(NodeTypes.PROJECT_CREATOR, creator);
      pNode.setProperty(NodeTypes.PROJECT_NAME, pName);
      pNode.setProperty(NodeTypes.PROJECT_DESCRIPTION, description);
      pRoot.save();
      return convertToEntity(pNode);
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public boolean removeProject(Project project) throws TaskManagerException {
    try {
      Node pRoot = getProjectRootNode();
      Node pNode = getProjectNodeById(project.getId());
      pNode.remove();
      pRoot.save();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  
  @Override
  public Project rename(Project project, String newName) throws TaskManagerException {
    project.setName(newName);
    storeProject(project);
    return project;
  }

  @Override
  public Project addPermission(Project project, String identity, String[] perm) throws TaskManagerException{
    try {
      Node pNode = getProjectNodeById(project.getId());
      ExtendedNode node = (ExtendedNode)pNode;
      node.setPermission(identity, perm);
      node.save();
      return project;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project removePermission(Project project, String identity, String[] perm) throws TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      ExtendedNode node = (ExtendedNode)pNode;
      for (String p : perm) {
        node.removePermission(identity, p);
      }
      node.save();
      return project;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project addMember(Project project, String member) throws TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      Value[] members = pNode.hasProperty(NodeTypes.PROJECT_MEMBERS) ? 
                          pNode.getProperty(NodeTypes.PROJECT_MEMBERS).getValues() :
                          new Value[]{};
      for (Value m : members) {
        if (m.getString().equals(member))
          return project;
      }
      Value[] newMems = new Value[members.length + 1];
      System.arraycopy(members, 0, newMems, 0, members.length);
      newMems[members.length] = pNode.getSession().getValueFactory().createValue(member);
      pNode.setProperty(NodeTypes.PROJECT_MEMBERS, newMems);
      pNode.save();
      return getProjectById(project.getId());
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project removeMember(Project project, String member) throws TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      if (!pNode.hasProperty(NodeTypes.PROJECT_MEMBERS)) return project;
      Value[] members = pNode.getProperty(NodeTypes.PROJECT_MEMBERS).getValues();
      Value[] newMems = new Value[members.length - 1];
      int count = 0;
      for (Value m : members) {
        if (!m.getString().equals(member))
          newMems[count++] = m;
      }
      pNode.setProperty(NodeTypes.PROJECT_MEMBERS, newMems);
      pNode.save();
      return getProjectById(project.getId());
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project addStage(Project project, String stage) throws StageExistException, TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      Value[] stages = pNode.hasProperty(NodeTypes.PROJECT_STAGELIST) ? 
                          pNode.getProperty(NodeTypes.PROJECT_STAGELIST).getValues() :
                          new Value[]{};
      for (Value s : stages) {
        if (s.getString().equals(stage))
          throw new StageExistException();
      }
      Value[] newStages = new Value[stages.length + 1];
      System.arraycopy(stages, 0, newStages, 0, stages.length);
      newStages[stages.length] = pNode.getSession().getValueFactory().createValue(stage);
      pNode.setProperty(NodeTypes.PROJECT_STAGELIST, newStages);
      pNode.save();
      return getProjectById(project.getId());
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project removeStage(Project project, String stage) throws TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      Value[] stages = pNode.hasProperty(NodeTypes.PROJECT_STAGELIST) ? 
                          pNode.getProperty(NodeTypes.PROJECT_STAGELIST).getValues() :
                          new Value[]{};
      for (Value s : stages) {
        if (s.getString().equals(stage))
          throw new StageExistException();
      }
      Value[] newStages = new Value[stages.length + 1];
      System.arraycopy(stages, 0, newStages, 0, stages.length);
      newStages[stages.length] = pNode.getSession().getValueFactory().createValue(stage);
      pNode.setProperty(NodeTypes.PROJECT_STAGELIST, newStages);
      pNode.save();
      return getProjectById(project.getId());
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project getProject(String creator, String pName) throws TaskManagerException {
    Node pNode = getProjectNode(creator, pName);
    return convertToEntity(pNode);
  }

  @Override
  public Project getProjectById(int id) throws TaskManagerException {
    Node pNode = getProjectNodeById(id);
    return convertToEntity(pNode);
  }

  @Override
  public void storeProject(Project project) throws TaskManagerException {
    try {
      Node pNode = getProjectNodeById(project.getId());
      pNode.setProperty(NodeTypes.PROJECT_CREATOR, project.getCreator());
      
      pNode.setProperty(NodeTypes.PROJECT_MEMBERS, Utils.toValues(project.getMembers(), pNode.getSession().getValueFactory()));
      
      pNode.setProperty(NodeTypes.PROJECT_NAME, project.getName());
      
      pNode.setProperty(NodeTypes.PROJECT_STAGELIST, Utils.toValues(project.getStageList(), pNode.getSession().getValueFactory()));
      pNode.save();
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public List<Task> getTasks(Project project)  throws TaskManagerException {
    try {
      TaskService taskService = Utils.getService(TaskService.class);
      List<Task> ret = new ArrayList<Task>();
      Node pNode = getProjectNodeById(project.getId());
      for (NodeIterator iter = pNode.getNode(NodeTypes.PROJECT_TASKS).getNodes(); iter.hasNext();) {
        ret.add(taskService.getTaskByNode(iter.nextNode()));
      }
      return ret;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public List<Task> getTasksByStage(Project project, String stage) throws TaskManagerException {
    try {
      List<Task> ret = new ArrayList<Task>();
      for (Task t : getTasks(project)) {
        if (StringUtils.equals(t.getStage(), stage)) {
          ret.add(t);
        }
      }
      return ret;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public void setTaskOrderInStage(String stage, List<Task> tasks) throws TaskManagerException {
    try {
      TaskService taskService = Utils.getService(TaskService.class);
      int count = 0;
      for (Task t : tasks) {
        t.setStageOrder(count++);
        taskService.storeTask(t);
      }
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }
  
  public Node getProjectRootNode() {
    SessionProvider sessionProvider = Utils.getSystemSessionProvider();
    try {
      return nodeCreator.getPublicApplicationNode(sessionProvider).getNode(projectRootNodeName);
    } catch (PathNotFoundException e) {
      try {
        Node appNode = nodeCreator.getPublicApplicationNode(sessionProvider);
        Node ret = appNode.addNode(projectRootNodeName);
        appNode.save();
        return ret;
      } catch(Exception ex) {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }
  
  private Project convertToEntity(Node pNode) throws TaskManagerException {
    try {
      Project ret = new Project();
      ret.setCreator(pNode.hasProperty(NodeTypes.PROJECT_CREATOR) ? 
                     pNode.getProperty(NodeTypes.PROJECT_CREATOR).getString() : "");
      ret.setId(pNode.hasProperty(NodeTypes.PROJECT_ID) ? 
                (int)pNode.getProperty(NodeTypes.PROJECT_ID).getLong() : 0);
      ret.setMembers(pNode.hasProperty(NodeTypes.PROJECT_MEMBERS) ?
                     Utils.toStringList(pNode.getProperty(NodeTypes.PROJECT_MEMBERS).getValues()) : 
                     new ArrayList<String>());
      ret.setName(pNode.hasProperty(NodeTypes.PROJECT_NAME) ? 
                  pNode.getProperty(NodeTypes.PROJECT_NAME).getString() : "");
      ret.setStageList(pNode.hasProperty(NodeTypes.PROJECT_STAGELIST) ?
                     Utils.toStringList(pNode.getProperty(NodeTypes.PROJECT_STAGELIST).getValues()) : 
                     new ArrayList<String>());
      return ret;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }
  
  @Override
  public Node getProjectNode(String creator, String pName) throws TaskManagerException{
    try {
      Node pRoot = getProjectRootNode();
      String statement = "SELECT * FROM " + NodeTypes.PROJECT + " WHERE jcr:path LIKE '" + 
          pRoot.getPath() + "/%' AND " + NodeTypes.PROJECT_CREATOR + "='" + creator +
          "' AND " + NodeTypes.PROJECT_NAME + "'" + pName + "'";

      Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
      for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
        Node p = iter.nextNode();
        return p;
      }
      return null;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Node getProjectNodeById(int id) throws TaskManagerException {
    try {
      Node pRoot = getProjectRootNode();
      String statement = "SELECT * FROM " + NodeTypes.PROJECT + " WHERE jcr:path LIKE '" + 
          pRoot.getPath() + "/%' AND " + NodeTypes.PROJECT_ID + "='" + id +"'";

      Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
      for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
        Node p = iter.nextNode();
        return p;
      }
      return null;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

}
