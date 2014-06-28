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
      @EventConfig(listeners = UITasksBoard.BackToProjectListActionListener.class)
    }
)
public class UITasksBoard extends UIContainer {
  
	private Project project;
	private ProjectService prjService;
	private TaskService taskService;
	//FIXME:to remove
	public static Map<String,List<Task>> mockTasks;
	
	public UITasksBoard(){
	  this.prjService = Utils.getService(ProjectService.class);
	  this.taskService = Utils.getService(TaskService.class);
	  //FIXME:to remove
	  mockData();
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
	  //FIXME: to remove
	  return mockTasks;
	}
	
	/**
	 * MOCKDATA
	 */
	private void mockData(){
	  this.project = new Project();
    this.project.setId(0);
    this.project.setName("demo");           
    mockTasks = new HashMap<String,List<Task>>();
    Task t1 = new Task();
    Task t2 = new Task();
    t1.setName("Kill all");
    t2.setName("Kill myself");
    List l1 = new ArrayList<Task>();
    l1.add(t1);
    List l2 = new ArrayList<Task>();
    l2.add(t2);
    mockTasks.put("TODO",l1);
    mockTasks.put("DONE",l2); 
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
        mockTasks.get(stage).add(t);
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
