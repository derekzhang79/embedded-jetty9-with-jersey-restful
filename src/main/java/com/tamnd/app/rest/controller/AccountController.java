/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app.rest.controller;

import com.tamnd.app.rest.entities.Account;
import com.tamnd.app.rest.exceptions.InvalidRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.JSONP;

/**
 *
 * @author tamnd2
 */
@Path("/account")
public class AccountController {

	@GET
	@JSONP
	@Produces({"application/javascript", MediaType.APPLICATION_JSON})
	public Response getAccount() throws InvalidRequestException {
		Account acc = new Account();
		acc.setId("1");
		acc.setUserName("tamnd2");
		acc.setPassword("password");
		acc.setEnabled(Boolean.TRUE);
		return Response.status(Response.Status.OK).entity(acc).build();
	}
}
