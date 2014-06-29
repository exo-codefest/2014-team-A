package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.utils.Utils;
 
@ComponentConfig(
    lifecycle = Lifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UIProjectsList.gtmpl",
	events = {
     @EventConfig(listeners = UIProjectsList.ShowTaskDetailActionListener.class),
     @EventConfig(listeners = UIProjectsList.AddProjectActionListener.class)
  }
)
public class UIProjectsList extends UIComponent {

	private String DEFAULT_PROJECT_NAME = "First Project";
	private String DEFAULT_PROJECT_DESCRIPTION = "Add tasks to this project";
	
    public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    	super.processRender(context);
    }
    
    public List<Project> getProjects() throws Exception{
      try {
    	List<Project> projects = Utils.getService(ProjectService.class).getAllProjectByUser(
                								ConversationState.getCurrent().getIdentity().getUserId());
    	if (projects.size() == 0) {
	      String user = ConversationState.getCurrent().getIdentity().getUserId();
	      Utils.getService(ProjectService.class).addProject(DEFAULT_PROJECT_NAME, user, DEFAULT_PROJECT_DESCRIPTION);
    	}
      	return Utils.getService(ProjectService.class).getAllProjectByUser(
                     ConversationState.getCurrent().getIdentity().getUserId());
      } catch (TaskManagerException e) {
        //LOG
        return new ArrayList<Project>();
      }
    }
 
	public static class ShowTaskDetailActionListener extends EventListener<UIProjectsList> {		
		@Override
		public void execute(Event<UIProjectsList> event) throws Exception {
			UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
	    uiParent.getChild(UIProjectsList.class).setRendered(false);
	    UITasksBoard taskBoard = uiParent.getChild(UITasksBoard.class);
	    taskBoard.setProjectId(Integer.parseInt(event.getRequestContext().getRequestParameter(OBJECTID)));
	    taskBoard.setRendered(true);
	    event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
		}
	}
	
  public static class AddProjectActionListener extends EventListener<UIProjectsList> {   
    @Override
    public void execute(Event<UIProjectsList> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      UIPopupContainer uiPopupContainer = uiParent.getChild(UIPopupContainer.class);
      UIAddProjectForm form = uiPopupContainer.createUIComponent(UIAddProjectForm.class, null, null);
      uiPopupContainer.activate(form, 400, 500);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
    }
  }
 
	
}
