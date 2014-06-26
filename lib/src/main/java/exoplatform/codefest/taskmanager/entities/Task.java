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
package exoplatform.codefest.taskmanager.entities;

import java.util.Date;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class Task {

  private int id;
  private int projectId;
  private String name;
  private String description;
  private String type;
  private List<String>labels;
  private List<String> members;
  private Date dueDate;
  private List<Integer> requiredTasks;
  private String stage;
  private int stageOrder;
    
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getProjectId() {
    return projectId;
  }
  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public List<String> getLabels() {
    return labels;
  }
  public void setLabels(List<String> labels) {
    this.labels = labels;
  }
  public List<String> getMembers() {
    return members;
  }
  public void setMembers(List<String> members) {
    this.members = members;
  }
  public Date getDueDate() {
    return dueDate;
  }
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }
  public List<Integer> getRequiredTasks() {
    return requiredTasks;
  }
  public void setRequiredTasks(List<Integer> requiredTasks) {
    this.requiredTasks = requiredTasks;
  }
  public String getStage() {
    return stage;
  }
  public void setStage(String stage) {
    this.stage = stage;
  }
  public int getStageOrder() {
    return stageOrder;
  }
  public void setStageOrder(int stageOrder) {
    this.stageOrder = stageOrder;
  }
  
  public void clone(Task t){
    this.name = t.getName();
    this.stage = t.getStage();
    this.dueDate = t.dueDate;    
  }
  
  public String toString(){
    return "Task: id = "+id+" name = "+name+" stage = "+stage;
  }
}
