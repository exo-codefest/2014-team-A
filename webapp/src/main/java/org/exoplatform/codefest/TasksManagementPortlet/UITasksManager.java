package org.exoplatform.codefest.TasksManagementPortlet;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig(lifecycle = UIApplicationLifecycle.class, template = "app:/groovy/UITasksManager.gtmpl")
public class UITasksManager extends UIPortletApplication {
    private static final Log LOG = ExoLogger.getExoLogger(UITasksManager.class);

    public UITasksManager() throws Exception {
    	super();
    }
}
