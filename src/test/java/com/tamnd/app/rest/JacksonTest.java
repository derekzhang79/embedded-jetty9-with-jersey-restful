/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app.rest;

import com.tamnd.app.JettyRunner;
import com.tamnd.app.common.Common;
import com.tamnd.app.rest.config.MyJerseyApplication;
import com.tamnd.app.rest.entities.Account;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
/**
 *
 * @author tamnd2
 */
public class JacksonTest {

	public static Server server;
	private final Client client;
	private static final String PATH = String.format("http://localhost:%d/", Common.JETTY_PORT);

	public JacksonTest() {
		ResourceConfig config = new ResourceConfig(MyJerseyApplication.class);
		ClientConfig conf = new ClientConfig();
		conf.register(config);
		client = ClientBuilder.newClient(conf);
	}

	@BeforeClass
	public static void init() throws Exception {
		server = new Server(Common.JETTY_PORT);
		server.setHandler(JettyRunner.buildWebAppContext());
		server.start();
		System.out.println(String.format("Jetty server is running at port %d...", Common.JETTY_PORT));
	}

	@AfterClass
	public static void cleanUp() throws Exception {
		server.stop();
		server.join();
	}

	@Test
	public void testJSONPPresent() {
		WebTarget target = client.target(PATH);
		String responseMsg = target.path("/rest/account").request("application/javascript").get(String.class);
		assertTrue(responseMsg.startsWith("callback("));
	}

	@Test
	public void testJSONDoesNotReflectJSONPWrapper() {
		WebTarget target = client.target(PATH);
		String responseMsg = target.path("/rest/account").request("application/json").get(String.class);
		assertTrue(!responseMsg.contains("jsonSource"));
	}

	@Test
	@Ignore
	// TODO un-ignore once a JSON reader for "application/javascript" is supported
	public void testJSONPBean() {
		WebTarget target = client.target(PATH);
		Account responseMsg = target.path("/rest/account").request("application/javascript").get(Account.class);
		assertNotNull(responseMsg);
	}
}
