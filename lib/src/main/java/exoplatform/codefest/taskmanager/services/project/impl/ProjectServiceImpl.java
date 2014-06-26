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

import java.util.List;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.services.project.ProjectService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class ProjectServiceImpl implements ProjectService {

  @Override
  public Project addProject(String pName, String creator) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project rename(Project project, String newName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project addPermission(Project project, String permission) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project removePermission(Project project, String permission) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project addMember(Project project, String member) {
    // TODO Auto-generated method stub
    return null;
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

}
