package org.exoplatform.codefest.TasksManagementPortlet;
 
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
 
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
 
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UIProjectDetail.gtmpl"
)
public class UIBasicConfig extends UIForm {
 
	private String projectId;
	
	public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
		super.processRender(context);
    }	
		
	public void setProjectId(String id){
		this.projectId = id;
	}
	
	public String getProjectId(){
		return projectId;
	}
}
