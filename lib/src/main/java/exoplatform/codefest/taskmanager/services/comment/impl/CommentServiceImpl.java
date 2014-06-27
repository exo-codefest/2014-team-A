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

import javax.jcr.Node;

import exoplatform.codefest.taskmanager.entities.Comment;
import exoplatform.codefest.taskmanager.entities.Task;
import exoplatform.codefest.taskmanager.services.comment.CommentService;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class CommentServiceImpl implements CommentService {

  @Override
  public Comment addComment(Task task, String content, String owner) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Comment setContent(Comment comment, String content) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Comment setOwner(Comment comment, String owner) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void storeComment(Task task, Comment comment) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Comment getCommentByNode(Node commentNode) {
    // TODO Auto-generated method stub
    return null;
  }

}
