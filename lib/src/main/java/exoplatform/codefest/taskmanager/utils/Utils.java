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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;

import exoplatform.codefest.taskmanager.entities.Project;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2014  
 */
public class Utils {
  
  private static final Log LOG = ExoLogger.getLogger(Utils.class.getName());  

  public static <T> T getService(Class<T> clazz) {
    return getService(clazz, null);
  }
  
  public static <T> T getService(Class<T> clazz, String containerName) {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    if (containerName != null) {
      container = RootContainer.getInstance().getPortalContainer(containerName);
    }
    if (container.getComponentInstanceOfType(clazz)==null) {
      containerName = PortalContainer.getCurrentPortalContainerName();
      container = RootContainer.getInstance().getPortalContainer(containerName);
    }
    return clazz.cast(container.getComponentInstanceOfType(clazz));
  }

  public static SessionProvider getSystemSessionProvider() {
    SessionProviderService sessionProviderService = getService(SessionProviderService.class);
    return sessionProviderService.getSystemSessionProvider(null);
  }

  public static SessionProvider getUserSessionProvider() {
    SessionProviderService sessionProviderService = getService(SessionProviderService.class);
    return sessionProviderService.getSessionProvider(null);
  }
  
  public static ManageableRepository getRepository() {
    try {
      RepositoryService repositoryService = getService(RepositoryService.class);
      return repositoryService.getCurrentRepository();
    } catch (Exception e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("getRepository() failed because of ", e);
      }
    }
    return null;
  }
  
  public static Session getSystemSession(String workspace) throws LoginException, NoSuchWorkspaceException, RepositoryException {
    return getSystemSessionProvider().getSession(workspace,
                                                 getRepository());
  }

  public static Session getUserSession(String workspace) throws LoginException, NoSuchWorkspaceException, RepositoryException {
    return getUserSessionProvider().getSession(workspace,
                                                 getRepository());
  }
  
  public static List<String> toStringList(Value[] values) throws ValueFormatException, IllegalStateException, RepositoryException {
    List<String> ret = new ArrayList<String>();
    for (Value v : values) {
      ret.add(v.getString());
    }
    return ret;
  }
  
  public static List<Integer> toIntList(Value[] values) throws ValueFormatException, IllegalStateException, RepositoryException {
    List<Integer> ret = new ArrayList<Integer>();
    for (Value v : values) {
      ret.add((int)v.getLong());
    }
    return ret;
  }
  
  public static Value[] toValues(List<String> src, ValueFactory v) {
    Value[] ret = new Value[src.size()];
    for (int i = 0; i < src.size(); i++)
      ret[i] = v.createValue(src.get(i));
    return ret;
  }

  public static Value[] toIntValues(List<Integer> src, ValueFactory v) {
    Value[] ret = new Value[src.size()];
    for (int i = 0; i < src.size(); i++)
      ret[i] = v.createValue(src.get(i));
    return ret;
  }
  
  public static List<Identity> getMembersIdentity(List<String> members) {
	List<Identity> memberUsers = new ArrayList<Identity>();
  	try {
  		if (members != null && members.size() > 0) {
  			for (String member : members) {
  				IdentityManager identityManager = (IdentityManager) ExoContainerContext.getCurrentContainer()
                          									.getComponentInstanceOfType(IdentityManager.class);
  				Identity identity = identityManager.getOrCreateIdentity(OrganizationIdentityProvider.NAME,
                          															member, true);
  				if (identity != null) memberUsers.add(identity);
  			}
  		}
  	} catch (Exception e){
  		// LOG info
  	}
  	return memberUsers;
  }

}
