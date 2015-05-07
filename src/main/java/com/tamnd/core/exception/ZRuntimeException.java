/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.exception;

/**
 *
 * @author namnq
 */
public class ZRuntimeException extends RuntimeException {

	private int _error;
	private Object _attachment;

	public ZRuntimeException() {
		super();
	}

	public ZRuntimeException(String message) {
		super(message);
	}

	public ZRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZRuntimeException(Throwable cause) {
		super(cause);
	}

	////////////////////////////////////////////////////////////////////////////
	//additional constructors
	public ZRuntimeException(int error) {
		super();
		_error = error;
	}

	public ZRuntimeException(int error, String message) {
		super(message);
		_error = error;
	}

	public ZRuntimeException(int error, String message, Throwable cause) {
		super(message, cause);
		_error = error;
	}

	////////////////////////////////////////////////////////////////////////////
	//get/set methods
	public void setAttachment(Object attachment) {
		_attachment = attachment;
	}

	public Object getAttachment() {
		return _attachment;
	}

	public int getError() {
		return _error;
	}

	public void setError(int error) {
		_error = error;
	}
}
