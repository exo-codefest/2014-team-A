package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.List;
import java.util.ArrayList;
import javax.portlet.PortletMode;
 
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

import exoplatform.codefest.taskmanager.services.project.ProjectService;
 
@ComponentConfig(
    lifecycle = UIApplicationLifecycle.class
)
public class UITasksManagementPortlet extends UIPortletApplication {
	private ProjectService prjService;
 
    public UITasksManagementPortlet() throws Exception {
    	  UIProjectsList basicForm = addChild(UIProjectsList.class, null, null);
        basicForm.setRendered(true);
        UITasksBoard basicConfig = addChild(UITasksBoard.class, null, null);
        basicConfig.setRendered(false);
        addChild(UIPopupContainer.class, null, null);
        UIPopupWindow uiPopup = addChild(UIPopupWindow.class, null, null);
        uiPopup.setId(uiPopup.getId() + "-" + UUID.randomUUID().toString().replaceAll("-", ""));
    } 
}
