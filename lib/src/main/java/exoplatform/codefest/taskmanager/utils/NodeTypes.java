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
package exoplatform.codefest.taskmanager.utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public interface NodeTypes {

  String PROJECT = "codefest:project";
  String PROJECT_ID = "id";
  String PROJECT_NAME = "name";
  String PROJECT_DESCRIPTION = "description";
  String PROJECT_MEMBERS = "members"; 
  String PROJECT_CREATOR= "creator";
  String PROJECT_STAGELIST= "stageList"; 
  String PROJECT_TASKS= "tasks";
  
  String TASK_FOLDER = "codefest:taskFolder";
  String TASK = "codefest:task";
  String TASK_ID = "id";
  String TASK_NAME = "name";
  String TASK_DESCRIPTION = "description";
  String TASK_TYPE = "type";
  String TASK_LABELS = "labels";
  String TASK_MEMBERS = "members";
  String TASK_DUE_DATE = "dueDate";
  String TASK_START_DATE = "startDate";
  String TASK_REQUIRED_TASKS = "requiredTasks";
  String TASK_STAGE = "stage";
  String TASK_STAGE_ORDER = "stageOrder";
  String TASK_ATTCHMENTS = "attachments";
  String TASK_COMMENTS = "comments";
  
  String ATTACHMENT_FOLDER = "codefest:attachmentFolder";
  String COMMENT_FOLDER = "codefest:commentFolder";
  
  String COMMENT = "codefest:comment";
  String COMMENT_ID = "id";
  String COMMENT_CONTENT = "content"; 
  String COMMENT_OWNER = "owner";
  String COMMENT_CREATED_DATE = "createdDate";
}
