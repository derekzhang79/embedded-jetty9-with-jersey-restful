/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tamnd.core.exception;

/**
 *
 * @author namnq
 */
public class ZException extends Exception {

	private int _error;
	private Object _attachment;

	public ZException() {
		super();
	}

	public ZException(String message) {
		super(message);
	}

	public ZException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZException(Throwable cause) {
		super(cause);
	}

	////////////////////////////////////////////////////////////////////////////
	//additional constructors
	public ZException(int error) {
		super();
		_error = error;
	}

	public ZException(int error, String message) {
		super(message);
		_error = error;
	}

	public ZException(int error, String message, Throwable cause) {
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
