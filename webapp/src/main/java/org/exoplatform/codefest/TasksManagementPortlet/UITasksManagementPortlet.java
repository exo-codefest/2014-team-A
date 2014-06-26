package org.exoplatform.codefest.TasksManagementPortlet;
 
import javax.portlet.PortletMode;
 
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
 
@ComponentConfig(
    lifecycle = UIApplicationLifecycle.class
)
public class UITasksManagementPortlet extends UIPortletApplication {
 
    public UITasksManagementPortlet() throws Exception {
        super();
    }
 
    public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    	getChildren().clear();
        PortletRequestContext pContext = (PortletRequestContext) context ;
        PortletMode currentMode = pContext.getApplicationMode() ;
        if(PortletMode.VIEW.equals(currentMode)) {
            if (getChild(UIBasicForm.class) == null) addChild(UIBasicForm.class, null, null);
        } else {
            if (getChild(UIBasicConfig.class) == null) addChild(UIBasicConfig.class, null, null);
        }
        super.processRender(app, context) ;
    }
 
}
