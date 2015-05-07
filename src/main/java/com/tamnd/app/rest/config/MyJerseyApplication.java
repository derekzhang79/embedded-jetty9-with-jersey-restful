/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app.rest.config;

import com.tamnd.app.rest.exceptions.InvalidRequestExceptionMapper;
import com.tamnd.app.rest.jackson.MyObjectMapperProvider;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author tamnd2
 */
@ApplicationPath("resources")
public class MyJerseyApplication extends ResourceConfig {

	public MyJerseyApplication() {
		register(InvalidRequestExceptionMapper.class);
		register(MyObjectMapperProvider.class);
		register(JacksonFeature.class);
		packages(true, "com.tamnd.app.rest.controller");
	}
}
