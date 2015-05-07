/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.exception;

/**
 *
 * @author namnq
 */
public class InvalidParamException extends ZException {

	public InvalidParamException() {
		super();
	}

	public InvalidParamException(String message) {
		super(message);
	}

	public InvalidParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParamException(Throwable cause) {
		super(cause);
	}

	public InvalidParamException(int error) {
		super(error);
	}

	public InvalidParamException(int error, String message) {
		super(error, message);
	}

	public InvalidParamException(int error, String message, Throwable cause) {
		super(error, message, cause);
	}
}
