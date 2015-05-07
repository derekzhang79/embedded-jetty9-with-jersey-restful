/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.exception;

/**
 *
 * @author namnq
 */
public class NotExistException extends ZException {

	public NotExistException() {
		super();
	}

	public NotExistException(String message) {
		super(message);
	}

	public NotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotExistException(Throwable cause) {
		super(cause);
	}

	public NotExistException(int error) {
		super(error);
	}

	public NotExistException(int error, String message) {
		super(error, message);
	}

	public NotExistException(int error, String message, Throwable cause) {
		super(error, message, cause);
	}
}
