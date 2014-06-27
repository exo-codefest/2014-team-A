package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;
import java.util.ArrayList;
import javax.portlet.PortletMode;
 
import org.exoplatform.codefest.TasksManagementPortlet.UIBasicConfig;
import org.exoplatform.codefest.TasksManagementPortlet.UIBasicForm;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

import exoplatform.codefest.taskmanager.entities.Project;
 
@ComponentConfig(
    lifecycle = UIApplicationLifecycle.class
)
public class UITasksManagementPortlet extends UIPortletApplication {
	
	private List<Project> ps = new ArrayList<Project>();
 
    public UITasksManagementPortlet() throws Exception {
        UIBasicForm basicForm = addChild(UIBasicForm.class, null, null);
        basicForm.setRendered(true);
        UIBasicConfig basicConfig = addChild(UIBasicConfig.class, null, null);
        basicConfig.setRendered(false);
        
        
        //List<Project> ps = new ArrayList<Project>();
        //ps.add(p1);ps.add(p2);
    } 
}
