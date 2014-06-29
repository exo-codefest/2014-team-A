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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
@ComponentConfig(
                 lifecycle = Lifecycle.class,
                 template = "app:/groovy/webui/TasksManagementPortlet/UIChartForm.gtmpl"
             )
public class UIChartForm extends UIComponent implements UIPopupComponent {
  
  public UIChartForm() throws Exception {
  }
  
  public int getProjecId() {
    return this.getAncestorOfType(UITasksManagementPortlet.class).
                getChild(UITasksBoard.class).getProjectId();
  }
  
  public void activate() {
//    try {
////      Task task = Utils.getService(TaskService.class).getTaskById(taskId);
////      this.getUIStringInput(NAME).setValue(task.getName());
////      this.getUIStringInput(DESCRIPTION).setValue(task.getDescription());
////      this.getUIFormDateTimeInput(DUEDATE).setCalendar(task.getDueDate());
//    } catch (TaskManagerException e) {
//      // TODO : LOG 
//    }
  }

  @Override
  public void deActivate() {
    // TODO Auto-generated method stub
  }
  
  public static class RemoveRequiredActionListener extends EventListener<UIChartForm> {   
    @Override
    public void execute(Event<UIChartForm> event) throws Exception {      
      UIChartForm taskForm = event.getSource();            
      event.getRequestContext().addUIComponentToUpdateByAjax(taskForm);
    }
  }
  
}
