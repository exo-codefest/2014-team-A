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

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
  
  public TaskRESTService(TaskService taskService, ProjectService projectService) {
    this.taskService = taskService;
    this.projectService = projectService;
  }    
  
  @GET
  @Path("/addTask")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response createTask(@QueryParam("projectId") int projId, 
                             @QueryParam("taskName") String taskName, @QueryParam("stage") String stage) {    
    try {
      Project proj = projectService.getProjectById(projId);
      Task t = taskService.addTask(proj, taskName, stage);
      return Response.ok(t.toString()).build();
    } catch (TaskExistException e) {
      return Response.serverError().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }
  
  @GET
  @Path("/changeStage")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response updateTaskStage(@QueryParam("taskId") Integer id, @QueryParam("stage") String stage) {
    try {
      Task task = taskService.getTaskById(id);
      task.setStage(stage);
      taskService.storeTask(task);
      return Response.ok().build();
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
  @Consumes("application/x-www-form-urlencoded")  
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response updateTask(MultivaluedMap<String, String> formParams) {
    //Task task = taskService.getTaskById(t.getId());
    //task.clone(t);
    return Response.ok(formParams.toString()).build();
  }
  
  @GET
  @Path("/searchtask/{projectId}/{keyword}")
  @RolesAllowed("users")
  public Response searchTasks(@PathParam("projectId") int projectId, @PathParam("keyword") String keyword) {
  	JSONArray searchResults = new JSONArray();
	try {
		Project project = projectService.getProjectById(projectId);
		List<Task> tasks = projectService.getTasks(project);
	    if (tasks != null && tasks.size() > 0) {
	    	for (Task task : tasks) {
	    		String taskName = (task.getName() == null) ? "" : task.getName();
	    		String taskDesc = (task.getDescription() == null) ? "" : task.getDescription();
	    		if (taskName.contains(keyword) || taskDesc.contains(keyword)) {
	    			JSONObject obj = new JSONObject();
	    			obj.put("id", task.getId());
	    			searchResults.put(obj);
	    		}
	    	}
	    }
	} catch (TaskManagerException e) {
		//
	} catch (JSONException e) {
		//
	}
	return Response.ok(searchResults.toString(), MediaType.APPLICATION_JSON).build();
  }
  
}
