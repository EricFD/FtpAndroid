package com.ftpandroid;

import java.io.IOException;


/**
 * Exception of java.io.IOException which adds Cause functionality.
 * @author eric
 *
 */
public class BaseIOException extends IOException {
	
	/**
	 * verifier l'utiliter de cette serialVersionUID!!
	 */
	private static final long serialVersionUID = 1L;
	private Throwable _cause;
	
	/**
	 * Create a BaseIOException
	 */
	public BaseIOException(){
		super();
	}
	/**
	 * Create a BaseIOException with the given message
	 * @param message Exception message
	 */
	public BaseIOException(String message){
		super(message);
	}
	/**
	 * Create a BaseIOException with the given cause.
	 * @param cause Throwable which caused this exception to be thrown
	 */
	public BaseIOException(Throwable cause){
		super(cause.getMessage());
		_cause = cause;
	}
	/**
	 * Create a BaseIOException with the given cause
	 * @param message Exception message
	 * @param cause Throwable which caused this exception to be thrown
	 */
	public BaseIOException(String message, Throwable cause){
		super(message);
		_cause = cause;
	}
	/**
	 * Return the cause of this exception
	 * @return cause
	 */
	public Throwable getCause() { return _cause; }
	/**
	 * Return the cause of this exception same as getCause()
	 * @return cause
	 */
	public Throwable getInnerThrowable() { return _cause; }
	
	/***
	 *	Initialise la cause
	 * @param cause Throwable which caused this exception to be thrown. 
	 **/
	public Throwable initCause(Throwable cause){
		_cause = cause;
		return _cause;
	}
}
