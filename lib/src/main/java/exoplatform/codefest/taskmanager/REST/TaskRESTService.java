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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
  
  @GET
  @Path("/changeRequired")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response updateTaskRequired(@QueryParam("taskId") Integer id, @QueryParam("option") String option,
                                     @QueryParam("requiredId") int requiredId) {
    try {
      Task task = taskService.getTaskById(id);
      if ("add".equals(option)) {
        taskService.addRequiredTask(task, requiredId);
      } else if ("remove".equals(option)) {
        taskService.removeRequiredTask(task, requiredId);
      }
      taskService.storeTask(task);
      return Response.ok().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }  
  
  @GET
  @Path("/savedata")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response saveData(@QueryParam("taskId") int id, @QueryParam("inputtype") String inputtype, 
                           @QueryParam("value") String value, @QueryParam("option") String opt) {
    try {
      Task task = taskService.getTaskById(id);
      if ("name".equals(inputtype)) 
        task.setName(value);
      else if ("description".equals(inputtype))
        task.setDescription(value);
      else if ("duedate".equals(inputtype)) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(value));//        
        task.setDueDate(cal);
      }
      else if ("startdate".equals(inputtype)) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(value));//        
        task.setStartDate(cal);
      }
      else if ("type".equals(inputtype)) 
        task.setType(value);
      else if ("labels".equals(inputtype)) 
        task.setLabels(Arrays.asList(value.split(";")));
      else if ("members".equals(inputtype)) 
        task.setLabels(Arrays.asList(value.split(";")));
      else if ("required".equals(inputtype)) {
        if ("add".equals(opt)) {
          taskService.addRequiredTask(task, Integer.parseInt(value));
        } else if ("remove".equals(opt)) {
          taskService.removeRequiredTask(task, Integer.parseInt(value));
        }
      }
      taskService.storeTask(task);
      return Response.ok().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    } catch (Exception e) {
      return Response.serverError().build();
    }
  }
  
  private List<Integer> getInts(String values) {
    List<Integer> ret = new ArrayList<Integer>();
    for (String s : values.split(";")) {
      try {
        ret.add(Integer.parseInt(s));
      } catch (Exception e) {
        //LOG
      }
    }
    return ret;
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
  
  @POST
  @Path("/addmembers")
  @RolesAllowed("users")
  public Response addMembersToTask(@FormParam("taskId") int taskId, @FormParam("listusers") String listusers) {
	  try {
	    Task task = taskService.getTaskById(taskId);
	    List<String> members = task.getMembers();
	    boolean isExist = false;
	    if (listusers != null && listusers != "") {
	    	String[] users = listusers.split(";");
	    	for (String user: users){
	    		if (user != null && user != "") {
	    			if (!isMemberExist(members, user)) {
	    				taskService.addMember(task, user);
	    			} else {
	    				isExist = true;
	    			}
	    		}
	    	}
	    	if (users.length == 1 && isExist) return Response.ok("exist").build();
	    	else return Response.ok("true").build();
	    }
	  } catch (TaskManagerException e) {
		  //
	  }
	  return Response.ok("false").build();
  }
  
  private boolean isMemberExist(List<String> members, String id){
	  if (members != null && members.size() > 0) {
		  for (String memberId : members) {
			  if (memberId.equals(id)) return true;
		  }
	  }
	  return false;
  }
  
  @GET
  @Path("/tasksInfo")
  @RolesAllowed("users")
  public Response tasksInfo(@QueryParam("projectId") int projectId) {
    JSONArray searchResults = new JSONArray();
    try {
      Project project = projectService.getProjectById(projectId);
      List<Task> tasks = projectService.getTasks(project);
      if (tasks != null && tasks.size() > 0) {
        int count = 0;
        for (Task task : tasks) {
          JSONObject t = new JSONObject();
          t.put("id", count++);
          t.put("name", task.getName());
          
          JSONArray item = new JSONArray();

          JSONObject obj = new JSONObject();
          obj.put("name", task.getName());
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
          Date start = task.getStartDate() == null? new Date() : task.getStartDate().getTime();
          Date due = task.getDueDate() == null? new Date() : task.getDueDate().getTime();
          obj.put("start", "new Date(" + sdf.format(start) +")");
          obj.put("end", "new Date(" + sdf.format(due) + ")");
          
          item.put(obj);
          t.put("series", item);
          
          searchResults.put(t);
        }
      }
    } catch (TaskManagerException e) {
      //
    } catch (JSONException e) {
      //
    }
    return Response.ok(searchResults.toString(), MediaType.APPLICATION_JSON).build();
  }
//  id: 6, name: "Feature 6", series: [
//                                     { name: "Planned", start: new Date(2010,00,05), end: new Date(2010,00,20) },
//                                     { name: "Actual", start: new Date(2010,00,06), end: new Date(2010,00,17), color: "#f0f0f0" },
//                                     { name: "Projected", start: new Date(2010,00,06), end: new Date(2010,00,20), color: "#e0e0e0" }
//                                   ]


  
}
