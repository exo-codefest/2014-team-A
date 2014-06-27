package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.ArrayList;
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
    	@EventConfig(listeners = UIProjectsList.ShowTaskDetailActionListener.class)
    }
)
public class UIProjectsList extends UIComponent {

	
    public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    	super.processRender(context);
    }
    
    public List<Project> getProjects(){
    	List<Project> ps = new ArrayList<Project>();
        /*Project p1 = new Project(1, "Task one", "This is first task to test", null, "maydt", null);
        Project p2 = new Project(2, "Task two", "This is second task to test", null, "maydt", null);
        Project p3 = new Project(3, "Task three", "This is third task to test", null, "maydt", null);
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);*/
    	return ps;
    }
 
	public static class ShowTaskDetailActionListener extends EventListener<UIProjectsList> {		
		@Override
		public void execute(Event<UIProjectsList> event) throws Exception {
			UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
		    uiParent.getChild(UIProjectsList.class).setRendered(false);
		    UIBasicConfig config = uiParent.getChild(UIBasicConfig.class);
		    config.setProjectId(event.getRequestContext().getRequestParameter(OBJECTID));
		    config.setRendered(true);
		    event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
		}
	}
}
