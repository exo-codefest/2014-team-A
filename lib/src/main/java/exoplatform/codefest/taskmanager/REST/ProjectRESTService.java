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

import java.util.Arrays;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.services.rest.resource.ResourceContainer;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
@Path("/taskManager")
public class ProjectRESTService implements ResourceContainer{
  private ProjectService projectService;
  private TaskService taskService;
  
  public ProjectRESTService(ProjectService projectService) {
    this.projectService = projectService;
  }
  
  @GET
  @Path("/project/changeStages")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response changeStages(@QueryParam("id") int id,
                               @QueryParam("stages") String stages) {
    //Project proj = projectService.getProjectById(id);
    try {
      Project prj = projectService.getProjectById(id);
      projectService.changeStages(prj, Arrays.asList(stages.split("_")));
      return Response.ok().build();
    } catch (TaskManagerException e) {
      return Response.serverError().build();
    }
  }

  
  @POST
  @Path("/project/{name}")
  @Produces(MediaType.TEXT_HTML)
  @RolesAllowed("users")
  public Response createProject(@PathParam("name") String name) {
    //Project proj = projectService.getProjectById(id);
    Project proj = new Project();
    proj.setName(name);
    return Response.ok(proj.toString()).build();
  }
  
}
