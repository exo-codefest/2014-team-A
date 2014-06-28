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

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;

import exoplatform.codefest.taskmanager.entities.Project;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
@ComponentConfig(
                 lifecycle = UIFormLifecycle.class,
                 template = "app:/groovy/webui/TasksManagementPortlet/UIAddStageForm.gtmpl",
                 events = {
                   @EventConfig(listeners = UIAddStageForm.SaveActionListener.class),
                   @EventConfig(listeners = UIAddStageForm.BackActionListener.class, phase = Phase.DECODE)
                 }
             )
public class UIAddStageForm extends UIForm implements UIPopupComponent {
  
  private static final String NAME = "name";

  public UIAddStageForm() throws Exception {
    addUIFormInput(new UIFormStringInput(NAME, NAME, null).addValidator(MandatoryValidator.class));
    this.setActions(new String[]{"Save", "Back"});
  }
  
  @Override
  public void activate() {
    // TODO Auto-generated method stub
  }

  @Override
  public void deActivate() {
    // TODO Auto-generated method stub
  }
  
  public static class SaveActionListener extends EventListener<UIAddStageForm> {   
    @Override
    public void execute(Event<UIAddStageForm> event) throws Exception {
      UIAddStageForm form = event.getSource();
      UITasksManagementPortlet uiParent = form.getAncestorOfType(UITasksManagementPortlet.class);
      UITasksBoard taskBoard = uiParent.getChild(UITasksBoard.class);
      String name = form.getUIStringInput(NAME).getValue();
      Project project = taskBoard.getProject();
      Utils.getService(ProjectService.class).addStage(project, name);
      form.getAncestorOfType(UIPopupContainer.class).deActivate();
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
    }
  }  

  public static class BackActionListener extends EventListener<UIAddProjectForm> {   
    @Override
    public void execute(Event<UIAddProjectForm> event) throws Exception {
      UITasksManagementPortlet uiParent = event.getSource().getAncestorOfType(UITasksManagementPortlet.class);
      event.getSource().getAncestorOfType(UIPopupContainer.class).cancelPopupAction();
      event.getRequestContext().addUIComponentToUpdateByAjax(uiParent);
    }
  }  
}
