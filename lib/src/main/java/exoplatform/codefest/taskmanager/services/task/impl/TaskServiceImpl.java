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
package exoplatform.codefest.taskmanager.services.task.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.Query;

import exoplatform.codefest.taskmanager.entities.Comment;
import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.id.IdGeneratorService;
import exoplatform.codefest.taskmanager.services.comment.CommentService;
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
public class TaskServiceImpl implements TaskService {

  @Override
  public Task addTask(Project project, Task task) throws TaskExistException, TaskManagerException {
    try {
      ProjectService pService = Utils.getService(ProjectService.class);
      Node pNode = pService.getProjectNodeById(project.getId());
      Node tasks = pNode.hasNode(NodeTypes.PROJECT_TASKS) ? 
                          pNode.getNode(NodeTypes.PROJECT_TASKS) :
                          pNode.addNode(NodeTypes.PROJECT_TASKS, NodeTypes.TASK_FOLDER);
      task.setId(Utils.getService(IdGeneratorService.class).generateId(NodeTypes.TASK));
      if (tasks.hasNode(task.getId() + "")) {
        throw new TaskExistException();
      }
      
      Node taskNode = tasks.addNode(task.getId() + "", NodeTypes.TASK);
      //---------------------------------------------------------------
      //private int id;
        taskNode.setProperty(NodeTypes.TASK_ID, task.getId());
      //private int projectId; - no need
      //private String name;
      taskNode.setProperty(NodeTypes.TASK_NAME, task.getName());
      //private String description;
      taskNode.setProperty(NodeTypes.TASK_DESCRIPTION, task.getDescription());
      //private String type;
      taskNode.setProperty(NodeTypes.TASK_TYPE, task.getType());
      //private List<String>labels;
      if (task.getLabels() != null)
        taskNode.setProperty(NodeTypes.TASK_LABELS, Utils.toValues(task.getLabels(), taskNode.getSession().getValueFactory()));
      //private List<String> members;
      if (task.getMembers() != null)
        taskNode.setProperty(NodeTypes.TASK_MEMBERS, Utils.toValues(task.getMembers(), taskNode.getSession().getValueFactory()));
      //private Date dueDate;
      taskNode.setProperty(NodeTypes.TASK_DUE_DATE, task.getDueDate());
      //private List<Integer> requiredTasks;
      if (task.getRequiredTasks() != null)
        taskNode.setProperty(NodeTypes.TASK_REQUIRED_TASKS, Utils.toIntValues(task.getRequiredTasks(), taskNode.getSession().getValueFactory()));
      //private String stage;
      taskNode.setProperty(NodeTypes.TASK_STAGE, task.getStage());
      //private int stageOrder;
      taskNode.setProperty(NodeTypes.TASK_STAGE_ORDER, task.getStageOrder());
      pNode.save();
    } catch (Exception e) {
      throw new TaskManagerException();
    }
    return task;
  }

  @Override
  public Task addTask(Project project, String name, String stage) throws TaskExistException, TaskManagerException {
    Task task = new Task();
    task.setId(Utils.getService(IdGeneratorService.class).generateId(NodeTypes.TASK));
    task.setName(name);
    task.setStage(stage);
    return addTask(project, task);
  }
  
  @Override
  public Task addTask(Project project, String name) throws TaskExistException, TaskManagerException {
    return addTask(project, name, null);
  }
  
  @Override
  public boolean removeTask(Task task) throws TaskManagerException {
    try {
      Node pNode = Utils.getService(ProjectService.class).getProjectNodeById(task.getProjectId());
      Node tNode = this.getTaskNodeById(task.getId());
      tNode.remove();
      pNode.save();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public Task rename(Task task, String newName) throws TaskManagerException {
    task.setName(newName);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task setDescription(Task task, String description) throws TaskManagerException {
    task.setDescription(description);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task setType(Task task, String type) throws TaskManagerException {
    task.setType(type);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task addLabel(Task task, String label) throws TaskManagerException {
    task.getLabels().add(label);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task removeLabel(Task task, String label) throws TaskManagerException {
    task.getLabels().remove(label);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task addMember(Task task, String member) throws TaskManagerException {
    task.getMembers().add(member);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task removeMember(Task task, String member) throws TaskManagerException {
    task.getMembers().remove(member);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task setDueDate(Task task, Calendar dueDate) throws TaskManagerException {
    task.setDueDate(dueDate);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task addRequiredTask(Task task, int requiredTaskId) throws TaskManagerException {
    task.getRequiredTasks().add(requiredTaskId);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task removeRequiredTask(Task task, int requiredTaskId) throws TaskManagerException {
    task.getRequiredTasks().remove(requiredTaskId);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task setStage(Task task, String stage) throws TaskManagerException {
    task.setStage(stage);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task setStageOrder(Task task, int order) throws TaskManagerException {
    task.setStageOrder(order);
    this.storeTask(task);
    return task;
  }

  @Override
  public Task getTaskById(int tagId) throws TaskManagerException {
    Node tNode = getTaskNodeById(tagId);
    return this.getTaskByNode(tNode);
  }
  
  @Override
  public Node getTaskNodeById(int taskId) throws TaskManagerException {
    try {
      Node pRoot = Utils.getService(ProjectService.class).getProjectRootNode();
      String statement = "SELECT * FROM " + NodeTypes.TASK + " WHERE jcr:path LIKE '" + 
          pRoot.getPath() + "/%' AND " + NodeTypes.TASK_ID + "='" + taskId + "'";

      Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
      for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
        Node t = iter.nextNode();
        return t;
      }
      return null;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }
  

  @Override
  public void storeTask(Task task) throws TaskManagerException {
    try {
      ProjectService pService = Utils.getService(ProjectService.class);
      Node pNode = pService.getProjectNodeById(task.getProjectId());
      Node tasks = pNode.hasNode(NodeTypes.PROJECT_TASKS) ? 
                          pNode.getNode(NodeTypes.PROJECT_TASKS) :
                          pNode.addNode(NodeTypes.PROJECT_TASKS, NodeTypes.TASK_FOLDER);
      
      Node taskNode = tasks.hasNode(task.getId() + "") ? 
                            tasks.getNode(task.getId() + "") : 
                            tasks.addNode(task.getId() + "", NodeTypes.TASK);
      //---------------------------------------------------------------
      //private int id;
        taskNode.setProperty(NodeTypes.TASK_ID, task.getId());
      //private int projectId; - no need
      //private String name;
      taskNode.setProperty(NodeTypes.TASK_NAME, task.getName());
      //private String description;
      taskNode.setProperty(NodeTypes.TASK_DESCRIPTION, task.getDescription());
      //private String type;
      taskNode.setProperty(NodeTypes.TASK_TYPE, task.getType());
      //private List<String>labels;
      if (task.getLabels() != null)
        taskNode.setProperty(NodeTypes.TASK_LABELS, Utils.toValues(task.getLabels(), taskNode.getSession().getValueFactory()));
      //private List<String> members;
      if (task.getMembers() != null)
        taskNode.setProperty(NodeTypes.TASK_MEMBERS, Utils.toValues(task.getMembers(), taskNode.getSession().getValueFactory()));
      //private Date dueDate;
      taskNode.setProperty(NodeTypes.TASK_DUE_DATE, task.getDueDate());
      //private List<Integer> requiredTasks;
      if (task.getRequiredTasks() != null)
        taskNode.setProperty(NodeTypes.TASK_REQUIRED_TASKS, Utils.toIntValues(task.getRequiredTasks(), taskNode.getSession().getValueFactory()));
      //private String stage;
      taskNode.setProperty(NodeTypes.TASK_STAGE, task.getStage());
      //private int stageOrder;
      taskNode.setProperty(NodeTypes.TASK_STAGE_ORDER, task.getStageOrder());
      pNode.save();
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public List<Comment> getCommentsByTask(Task task) throws TaskManagerException {
    try {
      List<Comment> ret = new ArrayList<Comment>();
      CommentService commentService = Utils.getService(CommentService.class);
      Node tNode = this.getTaskNodeById(task.getId());
      if (!tNode.hasNode(NodeTypes.TASK_COMMENTS)) return ret;
      for(NodeIterator iter = tNode.getNode(NodeTypes.TASK_COMMENTS).getNodes(); iter.hasNext();) {
        ret.add(commentService.getCommentByNode(iter.nextNode()));
      }
      return ret;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }
  
  @Override
  public Task getTaskByNode(Node taskNode) throws TaskManagerException {
    try {
      Task task = new Task();
      //private int id;
      task.setId((int)taskNode.getProperty(NodeTypes.TASK_ID).getLong());
      //private int projectId;
      task.setProjectId((int)taskNode.getParent().getParent().getProperty(NodeTypes.PROJECT_ID).getLong());
      //private String name;
      task.setName(taskNode.getProperty(NodeTypes.TASK_NAME).getString());
      //private String description;
      task.setDescription(taskNode.getProperty(NodeTypes.TASK_DESCRIPTION).getString());
      //private String type;
      task.setType(taskNode.getProperty(NodeTypes.TASK_TYPE).getString());
      //private List<String>labels;
      task.setLabels(Utils.toStringList(taskNode.getProperty(NodeTypes.TASK_LABELS).getValues()));
      //private List<String> members;
      task.setMembers(Utils.toStringList(taskNode.getProperty(NodeTypes.TASK_MEMBERS).getValues()));
      //private Calendar dueDate;
      task.setDueDate(taskNode.getProperty(NodeTypes.TASK_DUE_DATE).getDate());
      //private List<Integer> requiredTasks;
      task.setRequiredTasks(Utils.toIntList(taskNode.getProperty(NodeTypes.TASK_REQUIRED_TASKS).getValues()));
      //private String stage;
      task.setStage(taskNode.getProperty(NodeTypes.TASK_STAGE).getString());
      //private int stageOrder;
      task.setStageOrder((int)taskNode.getProperty(NodeTypes.TASK_STAGE_ORDER).getLong());
      return task;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

}
