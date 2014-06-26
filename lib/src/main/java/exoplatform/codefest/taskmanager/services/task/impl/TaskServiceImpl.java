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

import java.util.Date;
import java.util.List;

import exoplatform.codefest.taskmanager.entities.Comment;
import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.services.task.TaskService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class TaskServiceImpl implements TaskService {

  @Override
  public Task addTask(Project project,
                      String name,
                      String description,
                      String type,
                      String[] labels,
                      String[] members,
                      Date dueDate,
                      int[] requiredTasks,
                      String stage,
                      int stageOrder) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task addTask(Project project, String name, String description) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task rename(Task task, String oldName, String newName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task setDescription(Task task, String description) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task setType(Task task, String type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task addLabel(Task task, String label) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task removeLabel(Task task, String label) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task addMember(Task task, String member) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task removeMember(Task task, String member) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task setDueDate(Task task, Date dueDate) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task addRequiredTask(Task task, int requiredTaskId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task removeRequiredTask(Task task, int requiredTaskId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task setStage(Task task, String stage) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task setStageOrder(Task task, int order) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Task getTaskById(int tagId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void storeTask(Project project, Task task) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Comment> getComments() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Comment> getCommentsByTask(Task task) {
    // TODO Auto-generated method stub
    return null;
  }

}
