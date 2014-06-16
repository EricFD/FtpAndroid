package com.ftpandroid.connx.debug;

/**
 * Interface for classes output log statements
 * @author eric
 *
 */
public interface Appender {
	
	/**
	 * close this appender
	 */
	public void close();
	
	/**
	 * Log a message
	 * 
	 * @param msg
	 */
	public void log(String msg);
	
	/**
	 * Log a stack trace
	 * @param t
	 */
	public void log(Throwable t);
}
