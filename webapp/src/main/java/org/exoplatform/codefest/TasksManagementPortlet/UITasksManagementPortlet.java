package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;
import java.util.ArrayList;
import javax.portlet.PortletMode;
 
import org.exoplatform.codefest.TasksManagementPortlet.UIBasicConfig;
import org.exoplatform.codefest.TasksManagementPortlet.UIProjectsList;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
 
@ComponentConfig(
    lifecycle = UIApplicationLifecycle.class
)
public class UITasksManagementPortlet extends UIPortletApplication {
 
    public UITasksManagementPortlet() throws Exception {
    	UIProjectsList basicForm = addChild(UIProjectsList.class, null, null);
        basicForm.setRendered(true);
        UIBasicConfig basicConfig = addChild(UIBasicConfig.class, null, null);
        basicConfig.setRendered(false);
    } 
}
