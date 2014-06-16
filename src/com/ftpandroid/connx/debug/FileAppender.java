package com.ftpandroid.connx.debug;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileAppender implements Appender {
	
	public String file;
	
	protected boolean closed = false;
	
	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public FileAppender(String file) throws IOException {
		this.file = file;
		open();
	}
	
	

	protected synchronized void open() throws IOException{
		log = new PrintWriter(new FileWriter(file, true), true);
		closed = false;
	}
	
	/**
	 * Log message
	 * @param msg message to log
	 */
	public synchronized void log(String msg){
		if(!closed)
			log.println(msg);
	}
	
	public synchronized void log(Throwable t){
		if(!closed)
			t.printStackTrace(log);
			Log.println();
	}
	
	public synchronized void close(){
		if(!closed){
			closed = true;
			log.flush();
			log.close();
			log = null;
		}
	}
	
	/**
	 * Return the path for logging file
	 * @return path of logging file
	 */
	public String getFile(){ return file; }
	
 }
