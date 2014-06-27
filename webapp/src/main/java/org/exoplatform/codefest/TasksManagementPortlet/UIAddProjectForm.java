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

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;

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
                 template = "app:/groovy/webui/TasksManagementPortlet/UIAddProjectForm.gtmpl",
                 events = {
                   @EventConfig(listeners = UIAddProjectForm.SaveActionListener.class),
                   @EventConfig(listeners = UIAddProjectForm.BackActionListener.class, phase = Phase.DECODE)
                 }
             )
public class UIAddProjectForm extends UIForm implements UIPopupComponent {
  
  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";

  public UIAddProjectForm() throws Exception {
    addUIFormInput(new UIFormStringInput(NAME, NAME, null).addValidator(MandatoryValidator.class));
    addUIFormInput(new UIFormTextAreaInput(DESCRIPTION, DESCRIPTION, null).addValidator(MandatoryValidator.class));
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
  
  public static class SaveActionListener extends EventListener<UIAddProjectForm> {   
    @Override
    public void execute(Event<UIAddProjectForm> event) throws Exception {
      UIAddProjectForm form = event.getSource();
      UITasksManagementPortlet uiParent = form.getAncestorOfType(UITasksManagementPortlet.class);
      String name = form.getUIStringInput(NAME).getValue();
      String description = form.getUIFormTextAreaInput(DESCRIPTION).getValue();
      String user = ConversationState.getCurrent().getIdentity().getUserId();
      Utils.getService(ProjectService.class).addProject(name, user, description);
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
