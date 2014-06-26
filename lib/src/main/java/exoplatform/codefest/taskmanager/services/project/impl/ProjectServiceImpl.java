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

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.ProjectExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
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

  @Override
  public Project addProject(String pName, String creator) throws ProjectExistException, TaskManagerException {
    Node pRoot = getProjectRootNode();
    if (getProject(creator, pName) != null) {
      throw new ProjectExistException();
    }
    try {
      int newId = ProjectIdGenerator.getProjectIdGenerator().generateId();
      Node pNode = pRoot.addNode(newId + "", NodeTypes.PROJECT);
      pNode.setProperty(NodeTypes.PROJECT_ID, newId);
      pNode.setProperty(NodeTypes.PROJECT_CREATOR, creator);
      pNode.setProperty(NodeTypes.PROJECT_NAME, pName);
      pRoot.save();
      return convertToEntity(pNode);
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Project rename(Project project, String newName) {
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
      Value[] members = pNode.getProperty(NodeTypes.PROJECT_MEMBERS).getValues();
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
  public Project removeMember(Project project, String member) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project addStage(Project project, String stage) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project removeStage(Project project, String stage) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project getProject(String creator, String pName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project getProjectById(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void storeProject(Project project) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Task> getTask(Project project) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Task> getTaskByStage(Project project, String stage) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setTaskOrderInStage(Project project, String stage, List<Task> task) {
    // TODO Auto-generated method stub
    
  }
  
  private Node getProjectRootNode() {
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
  
  private Project convertToEntity(Node pNode) throws ValueFormatException, PathNotFoundException, RepositoryException {
    Project ret = new Project();
    ret.setCreator(pNode.hasProperty(NodeTypes.PROJECT_CREATOR) ? 
                   pNode.getProperty(NodeTypes.PROJECT_CREATOR).getString() : "");
    ret.setId(pNode.hasProperty(NodeTypes.PROJECT_ID) ? 
              (int)pNode.getProperty(NodeTypes.PROJECT_ID).getLong() : 0);
    ret.setMembers(pNode.hasProperty(NodeTypes.PROJECT_MEMBERS) ?
                   toStringList(pNode.getProperty(NodeTypes.PROJECT_MEMBERS).getValues()) : 
                   new ArrayList<String>());
    ret.setName(pNode.hasProperty(NodeTypes.PROJECT_NAME) ? 
                pNode.getProperty(NodeTypes.PROJECT_NAME).getString() : "");
    ret.setStageList(pNode.hasProperty(NodeTypes.PROJECT_STAGELIST) ?
                   toStringList(pNode.getProperty(NodeTypes.PROJECT_STAGELIST).getValues()) : 
                   new ArrayList<String>());
    return ret;
  }
  
  private List<String> toStringList(Value[] values) throws ValueFormatException, IllegalStateException, RepositoryException {
    List<String> ret = new ArrayList<String>();
    for (Value v : values) {
      ret.add(v.getString());
    }
    return ret;
  }
  
  static class ProjectIdGenerator {
    private static ProjectIdGenerator idGenerator;
    private int id = 1;
    private ProjectIdGenerator() {
      idGenerator = new ProjectIdGenerator();
    }
    
    public static ProjectIdGenerator getProjectIdGenerator() {
      return idGenerator;
    }
    
    public int generateId() {
      return id++;
    }
  }

  @Override
  public Node getProjectNode(String creator, String pName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Node getProjectNodeById(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project addPermission(Project project, String permission) {
    // TODO Auto-generated method stub
    return null;
  }

}
