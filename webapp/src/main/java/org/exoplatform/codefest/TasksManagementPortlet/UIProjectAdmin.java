package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;
import java.util.ArrayList;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.commons.utils.ListAccess;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.utils.Utils;
 
@ComponentConfig(
    lifecycle = Lifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UIProjectAdmin.gtmpl"
)
public class UIProjectAdmin extends UIComponent {

    public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    	super.processRender(context);
    }
    
    public List<Identity> getMembersOfProject() throws Exception{
		Project currentProject = this.getAncestorOfType(UITasksBoard.class).getProject();
		List<String> members = currentProject.getMembers();
		List<Identity> memberUsers = Utils.getMembersIdentity(members);
    	return memberUsers;
    }
    
    public List<User> searchUsers(String keyword){
    	List<User> memberUsers = new ArrayList<User>();
    	try {
    		OrganizationService orgService = (OrganizationService) ExoContainerContext.getCurrentContainer()
																.getComponentInstanceOfType(OrganizationService.class);
    		ListAccess<User> usersListAccess = orgService.getUserHandler().findAllUsers();
    		if (usersListAccess.getSize() > 0) {
    			User[] users = usersListAccess.load(0, usersListAccess.getSize());
    			for (User user: users){
    				String username = user.getUserName();
    				String firstname = user.getFirstName();
    				String lastname = user.getLastName();
    				String displayname = user.getDisplayName();
    				
    				if (username.contains(keyword) || firstname.contains(keyword) || lastname.contains(keyword) || displayname.contains(keyword)){
    					memberUsers.add(user);
    				}
    			}
    		}
    	} catch (Exception e){
    		// LOG info
    	}
    	return memberUsers;
    }
}