package org.exoplatform.codefest.TasksManagementPortlet; 
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.Utils;

 
@ComponentConfig(
    template = "app:/groovy/webui/TasksManagementPortlet/UITasksBoard.gtmpl",
    events = {
      @EventConfig(listeners = UITasksBoard.CreateTaskActionListener.class),
      @EventConfig(listeners = UITasksBoard.UpdateTaskActionListener.class),
      @EventConfig(listeners = UITasksBoard.BackToProjectListActionListener.class),
      @EventConfig(listeners = UITasksBoard.AddStageActionListener.class)
    }
)
public class UITasksBoard extends UIContainer {
  
	private ProjectService prjService;
	private TaskService taskService;
	
	private int projectId;
	
	public UITasksBoard() throws Exception{
	  this.prjService = Utils.getService(ProjectService.class);
	  this.taskService = Utils.getService(TaskService.class);
	  this.addChild(UIProjectAdmin.class, null, null);
	}
	
	public void setProjectId(int id) {
	  this.projectId = id;
	}
	
  public int getProjectId() {
    return projectId;
  }
	
	public Project getProject() throws TaskManagerException {
	  return prjService.getProjectById(projectId);
	}
	
	public List<String> getStages() throws TaskManagerException {
	  return prjService.getProjectById(projectId).getStageList();
	}
	
	public List<Task> getTasksByStage(String stage) throws TaskManagerException {
	  Project project = prjService.getProjectById(projectId);
	  return prjService.getTasksByStage(project, stage);
	}

	public static class AddStageActionListener extends EventListener<UITasksBoard> {
	  @Override
	  public void execute(Event<UITasksBoard> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      UIPopupContainer uiPopupContainer = uiParent.getChild(UIPopupContainer.class);
      UIAddStageForm form = uiPopupContainer.createUIComponent(UIAddStageForm.class, null, null);
      uiPopupContainer.activate(form, 400, 500);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
	  }
	}
	
	/*
	 *  Listeners 
	 */
	public static class CreateTaskActionListener extends EventListener<UITasksBoard> {    
    @Override
    public void execute(Event<UITasksBoard> event) throws Exception {
        UITasksBoard uicomponent = event.getSource();
        //Need get name and stage
        String stage = event.getRequestContext().getRequestParameter(OBJECTID);
        TaskService service = Utils.getService(TaskService.class);            
        service.addTask(uicomponent.getProject(), "Task"+stage, stage);
        Task t = new Task();
        t.setStage(stage);
        t.setName("Task"+stage);
        event.getRequestContext().addUIComponentToUpdateByAjax(uicomponent);  
    }
  }
	
	public static class UpdateTaskActionListener extends EventListener<UITasksBoard> {    
    @Override
    public void execute(Event<UITasksBoard> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);  
    }
  }
	
	public static class BackToProjectListActionListener extends EventListener<UITasksBoard> {
	  @Override
	  public void execute(Event<UITasksBoard> event) throws Exception {
	    UITasksManagementPortlet uiPortlet = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
	    uiPortlet.getChild(UIProjectsList.class).setRendered(true);
	    uiPortlet.getChild(UITasksBoard.class).setRendered(false);
	    event.getRequestContext().addUIComponentToUpdateByAjax(uiPortlet);
	  }
	}
}
