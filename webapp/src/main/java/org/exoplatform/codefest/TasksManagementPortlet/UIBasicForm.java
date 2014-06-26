package org.exoplatform.codefest.TasksManagementPortlet;
 
import javax.portlet.PortletPreferences;
 
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
 
@ComponentConfig(
    lifecycle = Lifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UIProjectsList.gtmpl"
)
public class UIBasicForm extends UIComponent {
 
    public String getText() {
        return "Hello";
    }
}
