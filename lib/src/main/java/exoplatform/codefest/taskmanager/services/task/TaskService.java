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
package exoplatform.codefest.taskmanager.services.task;

import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;

import exoplatform.codefest.taskmanager.entities.Comment;
import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public interface TaskService {

  public Task addTask(Project project, Task task) throws TaskExistException, TaskManagerException;
  
  public Task addTask(Project project, String name, String stage) throws TaskExistException, TaskManagerException;
  
  public Task addTask(Project project, String name) throws TaskExistException, TaskManagerException;
  
  public boolean removeTask(Task task) throws TaskManagerException;
  
  public Task rename(Task task, String newName) throws TaskExistException, TaskManagerException;
  
  public Task setDescription(Task task, String description) throws TaskManagerException;
  
  public Task setType(Task task, String type) throws TaskManagerException;
  
  public Task addLabel(Task task, String label) throws TaskManagerException;
  
  public Task removeLabel(Task task, String label) throws TaskManagerException;
  
  public Task addMember(Task task, String member) throws TaskManagerException;

  public Task removeMember(Task task, String member) throws TaskManagerException;
  
  public Task setDueDate(Task task, Calendar dueDate) throws TaskManagerException;

  public Task addRequiredTask(Task task, int requiredTaskId) throws TaskManagerException;
  
  public Task removeRequiredTask(Task task, int requiredTaskId) throws TaskManagerException;

  public Task setStage(Task task, String stage) throws TaskManagerException;
  
  public Task setStageOrder(Task task, int order) throws TaskManagerException;
  
  public Task getTaskById(int taskId) throws TaskManagerException ;
  
  public Node getTaskNodeById(int taskId) throws TaskManagerException;
  
  public void storeTask(Task task) throws TaskManagerException;
  
  public List<Comment> getCommentsByTask(Task task) throws TaskManagerException;
  
  public Task getTaskByNode(Node taskNode) throws TaskManagerException;

}
