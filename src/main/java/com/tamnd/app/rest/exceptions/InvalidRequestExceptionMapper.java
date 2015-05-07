/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.app.rest.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author tamnd2
 */
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException> {

	public static final String ERROR_MESSAGE = "This request was no good.";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response toResponse(final InvalidRequestException e) {
		return Response.status(Response.Status.BAD_REQUEST).entity(ERROR_MESSAGE).build();
	}
}
