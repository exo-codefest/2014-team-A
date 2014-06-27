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
package exoplatform.codefest.taskmanager.services.comment.impl;

import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.Query;

import exoplatform.codefest.taskmanager.entities.Comment;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.exceptions.TaskManagerException;
import exoplatform.codefest.taskmanager.id.IdGeneratorService;
import exoplatform.codefest.taskmanager.services.comment.CommentService;
import exoplatform.codefest.taskmanager.services.project.ProjectService;
import exoplatform.codefest.taskmanager.services.task.TaskService;
import exoplatform.codefest.taskmanager.utils.NodeTypes;
import exoplatform.codefest.taskmanager.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class CommentServiceImpl implements CommentService {

  @Override
  public Comment addComment(Task task, String content, String owner) throws TaskManagerException {
    try {
      TaskService taskService = Utils.getService(TaskService.class);
      Node tNode = taskService.getTaskNodeById(task.getId());
      Node commentFolder = tNode.hasNode(NodeTypes.TASK_COMMENTS) ? 
                                 tNode.getNode(NodeTypes.TASK_COMMENTS) : 
                                 tNode.addNode(NodeTypes.TASK_COMMENTS, NodeTypes.COMMENT_FOLDER);
                                 
      int id = Utils.getService(IdGeneratorService.class).generateId(NodeTypes.COMMENT);
      Node commentNode = commentFolder.addNode(id+"", NodeTypes.COMMENT);
      commentNode.setProperty(NodeTypes.COMMENT_CONTENT, content);
      commentNode.setProperty(NodeTypes.COMMENT_CREATED_DATE, new GregorianCalendar());
      commentNode.setProperty(NodeTypes.COMMENT_ID, id);
      commentNode.setProperty(NodeTypes.COMMENT_OWNER, owner);
      
      tNode.save();
      return this.getCommentByNode(commentNode);
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }

  @Override
  public Comment setContent(Comment comment, String content) throws TaskManagerException {
    comment.setContent(content);
    this.storeComment(comment);
    return comment;
  }

  @Override
  public Comment setOwner(Comment comment, String owner) throws TaskManagerException {
    comment.setOwner(owner);
    this.storeComment(comment);
    return comment;
  }

  @Override
  public Comment getCommentById(int id) throws TaskManagerException {
    Node cNode = getCommentNodeById(id);
    return this.getCommentByNode(cNode);
  }
  
  @Override
  public Node getCommentNodeById(int id) throws TaskManagerException {
    try {
      Node pRoot = Utils.getService(ProjectService.class).getProjectRootNode();
      String statement = "SELECT * FROM " + NodeTypes.TASK + " WHERE jcr:path LIKE '" + 
          pRoot.getPath() + "/%' AND " + NodeTypes.COMMENT_ID + "='" + id + "'";

      Query query = pRoot.getSession().getWorkspace().getQueryManager().createQuery(statement, Query.SQL);
      for (NodeIterator iter = query.execute().getNodes(); iter.hasNext();) {
        Node c = iter.nextNode();
        return c;
      }
      return null;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }
  
  @Override
  public void storeComment(Comment comment) throws TaskManagerException {
    try {
      TaskService tService = Utils.getService(TaskService.class);
      Node tNode = tService.getTaskNodeById(comment.getTaskId());
      Node comments = tNode.hasNode(NodeTypes.TASK_COMMENTS) ? 
                          tNode.getNode(NodeTypes.TASK_COMMENTS) :
                          tNode.addNode(NodeTypes.TASK_COMMENTS, NodeTypes.COMMENT_FOLDER);
      
      Node commentNode = comments.hasNode(comment.getId() + "") ? 
                            comments.getNode(comment.getId() + "") : 
                            comments.addNode(comment.getId() + "", NodeTypes.COMMENT);
      commentNode.setProperty(NodeTypes.COMMENT_CONTENT, comment.getContent());
      commentNode.setProperty(NodeTypes.COMMENT_CREATED_DATE, comment.getCreatedDate());
      commentNode.setProperty(NodeTypes.COMMENT_ID, comment.getId());
      commentNode.setProperty(NodeTypes.COMMENT_OWNER, comment.getOwner());
      tNode.save();
    } catch (Exception e) {
      throw new TaskManagerException();
    }
    
  }

  @Override
  public Comment getCommentByNode(Node commentNode) throws TaskManagerException {
    try {
      Comment comment = new Comment();
      comment.setContent(commentNode.getProperty(NodeTypes.COMMENT_CONTENT).getString());
      comment.setCreatedDate(commentNode.getProperty(NodeTypes.COMMENT_CREATED_DATE).getDate());
      comment.setId((int)commentNode.getProperty(NodeTypes.COMMENT_ID).getLong());
      comment.setOwner(commentNode.getProperty(NodeTypes.COMMENT_OWNER).getString());
      comment.setTaskId((int)commentNode.getParent().getParent().getProperty(NodeTypes.TASK_ID).getLong());
      return comment;
    } catch (Exception e) {
      throw new TaskManagerException();
    }
  }


}
