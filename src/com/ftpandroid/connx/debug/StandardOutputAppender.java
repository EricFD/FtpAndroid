package com.ftpandroid.connx.debug;

import java.io.PrintWriter;


/**
 * Appends log statement
 * standard output
 * 
 * @author eric
 *
 */
public class StandardOutputAppender implements Appender {
	
	private PrintWriter log = new PrintWriter(System.out);
	
	public StandardOutputAppender(){
		
	}
	
	public synchronized void log(String msg){
		log.println(msg);
		log.flush();
	}
	
	public synchronized void log(Throwable t){
		t.printStackTrace(log);
		log.flush();
	}
	
	public synchronized void close(){
		log.flush();
	}
	
}
