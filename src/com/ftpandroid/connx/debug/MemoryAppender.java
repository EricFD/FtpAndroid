package com.ftpandroid.connx.debug;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class MemoryAppender extends StreamAppender {
	
	public final static int DEFAULT_BUFFER_SIZE = 100*1024;
	
	public MemoryAppender(int initSize){
		super(new ByteArrayOutputStream(initSize));
	}
	
	public MemoryAppender(){
		this(DEFAULT_BUFFER_SIZE);
	}
	
	public byte[] getBuffer(){
		synchronized(log){
			log.flush();
			return((ByteArrayOutputStream)outStr).toByteArray();
		}
	}
	
	public Reader getReader(){
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(getBuffer())));
	}
	
	public String toString(){
		return new String(getBuffer());
	}
	
	public void print(PrintStream stream){
		BufferedReader reader = new BufferedReader(getReader());
		String line = null;
		try{
			while((line = reader.readLine())!= null)
				stream.println(line);
		}catch(IOException e){
			e.printStackTrace(stream);
		}
	}
	/**
	 * Writeing a buffer to stdout
	 */
	public void print() {
		print(System.out);
	}
}
