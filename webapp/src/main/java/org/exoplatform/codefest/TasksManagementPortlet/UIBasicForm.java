package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

import exoplatform.codefest.taskmanager.entities.Project;
 
@ComponentConfig(
    lifecycle = Lifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UIProjectsList.gtmpl",
	events = {
    	@EventConfig(listeners = UIBasicForm.ShowTaskDetailActionListener.class)
    }
)
public class UIBasicForm extends UIComponent {

	
    public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    	//Get Project lists
    	super.processRender(context);
    }
    
    public List<Project> getProjects(){
    	return null;
    }
 
 
	public static class ShowTaskDetailActionListener extends EventListener<UIBasicForm> {		
		@Override
		public void execute(Event<UIBasicForm> event) throws Exception {
			// TODO Auto-generated method stub
			UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
		    uiParent.getChild(UIBasicForm.class).setRendered(false);
		    UIBasicConfig config = uiParent.getChild(UIBasicConfig.class);
		    //config.setProjectName(event.getRequestContext().getRequestParameter(OBJECTID));
		    config.setProjectName("demo");
		    config.setRendered(true);
		}
	}
}
