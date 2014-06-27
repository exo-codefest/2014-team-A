package org.exoplatform.codefest.TasksManagementPortlet; 
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.Utils;

 
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "app:/groovy/webui/TasksManagementPortlet/UITasksBoard.gtmpl",
    events = {
      @EventConfig(listeners = UITasksBoard.CreateTaskActionListener.class),
      @EventConfig(listeners = UITasksBoard.UpdateTaskActionListener.class)
    }
)
public class UITasksBoard extends UIForm {
  
	private Project project;
	private ProjectService prjService;
	private TaskService taskService;
	
	public UITasksBoard(){
	  this.prjService = Utils.getService(ProjectService.class);
	  this.taskService = Utils.getService(TaskService.class);
	  this.project = new Project();
	  this.project.setId(0);
	  this.project.setName("demo");	  	  	  
	}
	
	public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {	  	  
		super.processRender(context);
  }	
	
	public List<Task> getTasks(){	  
	  try {
      //return prjService.getTasks(project);	    
	    List<Task> tasks = new ArrayList<Task>();
	    Task t1 = new Task();
	    Task t2 = new Task();
	    t1.setName("Kill all");
	    t2.setName("Kill myself");
	    tasks.add(t1);
	    tasks.add(t2);
	    return tasks;
    } catch (Exception e) {
      return null;
    }
	}	
		
	public void setProjectId(int id) throws TaskManagerException{
      //this.project = prjService.getProjectById(id);
	  this.project = new Project();	  
	  this.project.setId(id);
	  this.project.setName("demo_"+id);
	}
	
	public Project getProject(){
		return project;
	}
	
	public Map<String,List<Task>> getTasksMapByStage(){
	  Map<String,List<Task>> tasks = new HashMap<String,List<Task>>();
	  /*
	  for (String stage : project.getStageList()){
	    try {
        tasks.put(stage, prjService.getTasksByStage(project, stage));
      } catch (TaskManagerException e) {
        
      }
	  }*/
	  Task t1 = new Task();
    Task t2 = new Task();
    t1.setName("Kill all");
    t2.setName("Kill myself");
    List l1 = new ArrayList<Task>();
    l1.add(t1);
    List l2 = new ArrayList<Task>();
    l2.add(t2);
    tasks.put("TODO",l1);
    tasks.put("DONE",l2);        
	  return tasks;
	}
	
	/*
	 *  Listeners 
	 */
	public static class CreateTaskActionListener extends EventListener<UIProjectsList> {    
    @Override
    public void execute(Event<UIProjectsList> event) throws Exception {
        UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);        
        event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);  
    }
  }
	
	public static class UpdateTaskActionListener extends EventListener<UIProjectsList> {    
    @Override
    public void execute(Event<UIProjectsList> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);  
    }
  }
}
