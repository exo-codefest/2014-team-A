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
package exoplatform.codefest.taskmanager.REST;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.exoplatform.services.rest.resource.ResourceContainer;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskExistException;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
@Path("/taskManager/task")
public class TaskRESTService implements ResourceContainer{
  private TaskService taskService;
  private ProjectService projectService;
  
  public TaskRESTService(TaskService taskService) {
    this.taskService = taskService;
  }    
  
  @POST
  @Path("/{projectId}")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response createTask(@PathParam("projectId") int projId, @FormParam("stage") String stage, @FormParam("task") String title) {    
    //Project proj = projectService.getProjectById(id);
    Project proj = new Project();
    proj.setId(projId);
    try {
      Task t = taskService.addTask(proj, title, stage);
      return Response.ok(t.toString()).build();
    } catch (TaskExistException e) {
      return Response.serverError().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }
  
  @POST
  @Path("/order")
  @Consumes("application/x-www-form-urlencoded") 
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response changeTaskOrder(MultivaluedMap<Integer, Integer> formParams) {
    try {
      Task task;
      StringBuilder data = new StringBuilder();
      for (int id : formParams.keySet()){
        task = taskService.getTaskById(id);    
        task.setStageOrder(formParams.getFirst(id));
        data.append(task.toString());
      }            
      return Response.ok().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }
  
  @POST
  @Path("/stage")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response updateTaskStage(@QueryParam("taskId") Integer id, @QueryParam("stage") String stage) {
    try {
      Task task = taskService.getTaskById(id);
      task.setStage(stage);
      return Response.ok().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }   
  
  @POST
  @Consumes("application/x-www-form-urlencoded")  
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response updateTask(MultivaluedMap<String, String> formParams) {
    //Task task = taskService.getTaskById(t.getId());
    //task.clone(t);
    return Response.ok(formParams.toString()).build();
  }   
  
}
