/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
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
package org.exoplatform.commons.settings.model.api;

import org.exoplatform.commons.settings.model.api.Context;
import org.exoplatform.commons.settings.model.api.Scope;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform bangnv@exoplatform.com
 * Nov 22, 2012
 */
public class SettingKey extends SettingScope {

  /**
   * 
   */
  private static final long serialVersionUID = 7109224384495691388L;
  private String key;

  public SettingKey(Context context, Scope scope, String key) {
    super(context, scope);
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (obj instanceof SettingKey) {
      SettingKey that = (SettingKey) obj;
      return key.equals(that.getKey());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = super.repositoryName.hashCode();
    result = 31 * result + scopePath.hashCode();
    result = 31 * result + key.hashCode();
    return result;
  }

}
