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
package exoplatform.codefest.taskmanager.id;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 27, 2014  
 */
public class IdGeneratorService {
  
  private Map<String, Integer> counter;
  
  public IdGeneratorService() {
    counter = new HashMap<String, Integer>();
  }

  public int generateId(String name) {
    if (counter.get(name) == null) {
      counter.put(name, (int)(new GregorianCalendar().getTimeInMillis()));
    }
    int ret = counter.get(name);
    counter.put(name, ++ret);
    return ret;
  }
  
}
