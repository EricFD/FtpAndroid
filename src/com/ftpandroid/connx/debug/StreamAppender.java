package com.ftpandroid.connx.debug;

import java.io.OutputStream;
import java.io.PrintWriter;

public class StreamAppender implements Appender {
	
	/**
	 * Writer used to write to the buffer.
	 */
	protected PrintWriter log;
	protected OutputStream outStr;
	
	/**
	 * Create s StreamAppender using stream
	 * @param outStr
	 */
	public StreamAppender(OutputStream outStr){
		this.outStr = outStr;
		this.log = new PrintWriter(outStr);
	}
	
	
	@Override
	public void close() {
		synchronized (log){
			log.flush();
			log.close();
		}

	}

	@Override
	public void log(String msg) {
		synchronized(log){
			log.println(msg);
		}

	}

	@Override
	public void log(Throwable t) {
		synchronized(log){
			t.printStackTrace(log);
			log.println();
		}

	}

}
