package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.UUID;

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
        UIPopupContainer container = addChild(UIPopupContainer.class, null, "UIPopupContainer"+ "-" + UUID.randomUUID().toString().replaceAll("-", ""));
        UIPopupWindow uiPopup = container.getChild(UIPopupWindow.class);
        uiPopup.setId(uiPopup.getId() + "-" + UUID.randomUUID().toString().replaceAll("-", ""));
    } 
}
