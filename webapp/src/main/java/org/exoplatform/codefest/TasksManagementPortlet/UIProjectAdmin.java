package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.social.core.identity.model.Identity;

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
}