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
package exoplatform.codefest.taskmanager.services.project;

import java.util.List;

import javax.jcr.Node;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.ProjectExistException;
import exoplatform.codefest.taskmanager.exceptions.StageExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public interface ProjectService {
  
  public List<Project> getAllProjectByUser(String user) throws TaskManagerException;

  public Project addProject(String pName, String creator) throws ProjectExistException, TaskManagerException;
  
  public boolean removeProject(Project project) throws TaskManagerException;

  public Project rename(Project project, String newName) throws TaskManagerException;
  
  public Project addPermission(Project project, String identity, String[] perm) throws TaskManagerException;
  
  public Project removePermission(Project project, String identity, String[] perm) throws TaskManagerException;
  
  public Project addMember(Project project, String member) throws TaskManagerException;
  
  public Project removeMember(Project project, String member) throws TaskManagerException;
  
  public Project addStage(Project project, String stage) throws StageExistException, TaskManagerException;
  
  public Project removeStage(Project project, String stage) throws TaskManagerException;
  
  public Project getProject(String creator, String pName) throws TaskManagerException;
  
  public Project getProjectById(int id) throws TaskManagerException;
  
  public Node getProjectNode(String creator, String pName) throws TaskManagerException;
  
  public Node getProjectNodeById(int id) throws TaskManagerException;
  
  public void storeProject(Project project) throws TaskManagerException;
  
  public List<Task> getTasks(Project project) throws TaskManagerException;
  
  public List<Task> getTasksByStage(Project project, String stage) throws TaskManagerException;
  
  public void setTaskOrderInStage(String stage, List<Task> task) throws TaskManagerException;
  
  public Node getProjectRootNode();
  
}
