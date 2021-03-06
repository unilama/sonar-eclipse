/*
 * Sonar Eclipse
 * Copyright (C) 2010-2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.ide.eclipse.core.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.osgi.framework.BundleContext;
import org.sonar.ide.eclipse.core.AbstractPlugin;
import org.sonar.ide.eclipse.core.internal.resources.SonarFile;
import org.sonar.ide.eclipse.core.internal.resources.SonarProject;
import org.sonar.ide.eclipse.core.internal.resources.SonarProjectManager;
import org.sonar.ide.eclipse.core.internal.resources.SonarResource;
import org.sonar.ide.eclipse.core.internal.servers.ISonarServersManager;
import org.sonar.ide.eclipse.core.internal.servers.ServersManager;
import org.sonar.ide.eclipse.core.resources.ISonarFile;
import org.sonar.ide.eclipse.core.resources.ISonarResource;
import org.sonar.ide.eclipse.wsclient.SonarConnectionTester;

public class SonarCorePlugin extends AbstractPlugin {
  public static final String PLUGIN_ID = "org.sonar.ide.eclipse.core";

  /**
   * Godin: It would be better to use only one MARKER_ID at least at first time.
   */
  public static final String MARKER_ID = PLUGIN_ID + ".sonarProblem";
  public static final String NEW_VIOLATION_MARKER_ID = PLUGIN_ID + ".sonarProblemNewViolation";

  /**
   * Minimal supported version of Sonar server for local analysis.
   */
  public static final String LOCAL_MODE_MINIMAL_SONAR_VERSION = "3.4"; //$NON-NLS-1$

  private static SonarCorePlugin plugin;

  public SonarCorePlugin() {
    plugin = this;
  }

  public static SonarCorePlugin getDefault() {
    return plugin;
  }

  private ServersManager serversManager;
  private SonarConnectionTester sonarConnectionTester;

  @Override
  public void start(BundleContext context) {
    super.start(context);

    serversManager = new ServersManager();
    sonarConnectionTester = new SonarConnectionTester();
  }

  private static SonarProjectManager projectManager;

  public synchronized SonarProjectManager getProjectManager() {
    if (projectManager == null) {
      projectManager = new SonarProjectManager();
    }
    return projectManager;
  }

  public static ISonarServersManager getServersManager() {
    return getDefault().serversManager;
  }

  public static SonarConnectionTester getServerConnectionTester() {
    return getDefault().sonarConnectionTester;
  }

  public static ISonarResource createSonarResource(IResource resource, String key, String name) {
    return new SonarResource(resource, key, name);
  }

  public static ISonarFile createSonarFile(IFile file, String key, String name) {
    return new SonarFile(file, key, name);
  }

  /**
   * Create a new Sonar project from the given project. Enable Sonar nature.
   * @param project
   * @param url
   * @param key
   * @param analysedLocally
   * @return
   * @throws CoreException
   */
  public static SonarProject createSonarProject(IProject project, String url, String key, boolean analysedLocally) throws CoreException {
    SonarProject sonarProject = SonarProject.getInstance(project);
    sonarProject.setUrl(url);
    sonarProject.setKey(key);
    sonarProject.setAnalysedLocally(analysedLocally);
    sonarProject.save();
    SonarNature.enableNature(project);
    return sonarProject;
  }

}
