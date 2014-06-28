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
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.service.LinkProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import exoplatform.codefest.taskmanager.entities.Project;
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
  
  @GET
  @Path("/search/{keyword}")
  @RolesAllowed("users")
  public Response searchUser(@PathParam("keyword") String keyword) {
	JSONArray jArray = new JSONArray();
  	try {
  		OrganizationService orgService = (OrganizationService) ExoContainerContext.getCurrentContainer()
																.getComponentInstanceOfType(OrganizationService.class);
  		IdentityManager identityManager = (IdentityManager) ExoContainerContext.getCurrentContainer()
																.getComponentInstanceOfType(IdentityManager.class);

  		ListAccess<User> usersListAccess = orgService.getUserHandler().findAllUsers();
  		if (usersListAccess.getSize() > 0) {
  			User[] users = usersListAccess.load(0, usersListAccess.getSize());
  			for (User user: users){
  				String username = user.getUserName();
  				String firstname = user.getFirstName();
  				String lastname = user.getLastName();
  				String displayname = user.getDisplayName();
  				
  				if (username.contains(keyword) || firstname.contains(keyword) || lastname.contains(keyword) || displayname.contains(keyword)){
  					Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME,
  																									username, true);
  					String avatarUrl = identity.getProfile().getAvatarUrl();
  					if (avatarUrl == null || avatarUrl.isEmpty()) {
  						avatarUrl = LinkProvider.PROFILE_DEFAULT_AVATAR_URL;
  			        }
  					JSONObject jObj = new JSONObject();
  					jObj.put("userId", username);
  					jObj.put("fullname", displayname);
  					jObj.put("avatar", avatarUrl);
  					jArray.put(jObj);
  				}
  			}
  		}
  	} catch (Exception e){
  		// LOG info
  	}
  	return Response.ok(jArray.toString(), MediaType.APPLICATION_JSON).build();
  }
  
  @POST
  @Path("/addmembers")
  @RolesAllowed("users")
  public Response createProject(@FormParam("projectId") int projectId, @FormParam("listusers") String listusers) {
	  try {
	    Project project = projectService.getProjectById(projectId);
	    List<String> members = project.getMembers();
	    boolean isExist = false;
	    if (listusers != null && listusers != "") {
	    	String[] users = listusers.split(";");
	    	for (String user: users){
	    		if (user != null && user != "") {
	    			if (!isMemberExist(members, user)) {
	    				projectService.addMember(project, user);
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
}
