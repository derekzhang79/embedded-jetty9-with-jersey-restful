/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.exception;

/**
 *
 * @author namnq
 */
public class CircularReferenceException extends ZRuntimeException {

	public CircularReferenceException() {
	}

	public CircularReferenceException(String message) {
		super(message);
	}

	public CircularReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CircularReferenceException(Throwable cause) {
		super(cause);
	}

	public CircularReferenceException(int error) {
		super(error);
	}

	public CircularReferenceException(int error, String message) {
		super(error, message);
	}

	public CircularReferenceException(int error, String message, Throwable cause) {
		super(error, message, cause);
	}

}
