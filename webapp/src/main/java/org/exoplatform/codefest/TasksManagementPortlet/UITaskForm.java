/*
 * Copyright (C) 2003-2014 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.codefest.TasksManagementPortlet;

import java.util.Calendar;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormDateTimeInput;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;

import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
@ComponentConfig(
                 lifecycle = UIFormLifecycle.class,
                 template = "app:/groovy/webui/TasksManagementPortlet/UITaskForm.gtmpl",
                 events = {
                   @EventConfig(listeners = UITaskForm.SaveTaskActionListener.class),
                   @EventConfig(listeners = UITaskForm.BackActionListener.class, phase = Phase.DECODE)
                 }
             )
public class UITaskForm extends UIForm implements UIPopupComponent {
  
  private Task task;
  private int taskId;
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String DUEDATE = "duedate";

  public UITaskForm() throws Exception {
    task = new Task();
    addUIFormInput(new UIFormStringInput(NAME, NAME, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormStringInput(DESCRIPTION, DESCRIPTION, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormDateTimeInput(DUEDATE, DUEDATE, null).addValidator(MandatoryValidator.class));    
  }
  
  public void setTaskId(Integer taskId) throws TaskManagerException{
    this.task = Utils.getService(TaskService.class).getTaskById(taskId);
    this.taskId = taskId;
    updateFormInputValues();
  }
  
  public Task getTask(){
    return this.task;       
  }
  
  @Override
  public void activate() {
    try {
      Task task = Utils.getService(TaskService.class).getTaskById(taskId);
      this.getUIStringInput(NAME).setValue(task.getName());
      this.getUIStringInput(DESCRIPTION).setValue(task.getDescription());
      this.getUIFormDateTimeInput(DUEDATE).setCalendar(task.getDueDate());
    } catch (TaskManagerException e) {
      // TODO : LOG 
    }
  }

  @Override
  public void deActivate() {
    // TODO Auto-generated method stub
  }
  
  public static class SaveTaskActionListener extends EventListener<UITaskForm> {   
    @Override
    public void execute(Event<UITaskForm> event) throws Exception {      
      UITaskForm taskForm = event.getSource();            
      UITasksBoard taskBoard = taskForm.getAncestorOfType(UITasksBoard.class);
      String name = taskForm.getUIStringInput(NAME).getValue();
      String description = taskForm.getUIStringInput(DESCRIPTION).getValue();
      Calendar duedate = taskForm.getUIFormDateTimeInput(DUEDATE).getCalendar();
      taskForm.updateTask(name,description,duedate);
      Utils.getService(TaskService.class).addTask(taskBoard.getProject(), taskForm.getTask());
      taskForm.getAncestorOfType(UIPopupContainer.class).deActivate();
      event.getRequestContext().addUIComponentToUpdateByAjax(taskBoard);
    }
  }  

  public static class BackActionListener extends EventListener<UITaskForm> {   
    @Override
    public void execute(Event<UITaskForm> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      event.getSource().getAncestorOfType(UIPopupContainer.class).cancelPopupAction();
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
    }
  }  
  
  public void updateTask(String name, String description, Calendar dueDate){
    this.task.setName(name);
    this.task.setDescription(name);
    this.task.setDueDate(dueDate);
  }
  
  public void updateFormInputValues(){
    getUIStringInput(NAME).setValue(this.task.getName());
    getUIStringInput(DESCRIPTION).setValue(this.task.getDescription());
    getUIFormDateTimeInput(DUEDATE).setCalendar(this.task.getDueDate());
  }
}
