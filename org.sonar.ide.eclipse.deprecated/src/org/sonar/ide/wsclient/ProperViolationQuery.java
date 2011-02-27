/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010-2011 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.ide.wsclient;

import org.sonar.wsclient.services.ViolationQuery;

/**
 * Workaround for SONAR-1793: Wrong URL construction in ViolationQuery, when depth parameter used.
 * 
 * @deprecated Should be removed after release of sonar-ws-client 2.3
 */
@Deprecated
public class ProperViolationQuery extends ViolationQuery {
  public ProperViolationQuery(String resourceKeyOrId) {
    super(resourceKeyOrId);
  }

  @Override
  public String getUrl() {
    String url = super.getUrl();
    if (getDepth() != 0) {
      url += "depth=" + getDepth();
    }
    return url;
  }
}
