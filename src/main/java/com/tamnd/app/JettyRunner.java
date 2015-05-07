/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app;

import com.tamnd.app.common.Common;
import com.tamnd.core.server.EmbeddedJettyServer;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author tamnd2
 */
public class JettyRunner {

	public boolean setupAndStart() {
		EmbeddedJettyServer server = new EmbeddedJettyServer(Common.PROJECT_NAME);
		server.setup(buildWebAppContext());
		return server.start();
	}

	public static WebAppContext buildWebAppContext() {
		//Create a WebApp
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase(System.getProperty("user.dir") + "/src/main/webapp/");
		webapp.setParentLoaderPriority(true);
		// Important! make sure Jetty scans all classes under ./classes looking for annotations. Classes
		// directory is generated running 'mvn package'
		webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
		/* Disable directory listings if no index.html is found. */
		webapp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

		//Add Jersey Restful Webservice support
		webapp.addServlet(getJerseryServletHolder(), "/rest/*");

		return webapp;
	}

	public static ServletHolder getJerseryServletHolder() {
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		//Set the package where the services reside
//		sh.setInitParameter("jersey.config.server.provider.packages", REST_BASE_PACKAGE);
		sh.setInitParameter("javax.ws.rs.Application", "com.tamnd.app.rest.config.MyJerseyApplication");
		return sh;
	}
}
