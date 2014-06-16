package com.ftpandroid.net.ftp.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public interface FTPDataSocket {
	 public void setTimeout(int millis) throws IOException;
	    
	    /**
	     * Set the size of the data socket's receive buffer.
	     * 
	     * @param size  must be > 0
	     */
	    public void setReceiveBufferSize(int size) throws IOException;
	        
	    /**
	     * Set the size of the data socket's send buffer.
	     * 
	     * @param size  must be > 0
	     */
	    public void setSendBufferSize(int size) throws IOException;
	    
	    /**
	     * Returns the local port to which this socket is bound. 
	     * 
	     * @return the local port number to which this socket is bound
	     */
	    public int getLocalPort();
	    
	    /**
	     * Returns the local address to which this socket is bound. 
	     * 
	     * @return the local address to which this socket is bound
	     */
	    public InetAddress getLocalAddress();

	    /**
	     *  Get the appropriate output stream for writing to
	     *
	     *  @return  output stream for underlying socket.
	     */
	    public OutputStream getOutputStream() throws IOException;

	    /**
	     *  Get the appropriate input stream for reading from
	     *
	     *  @return  input stream for underlying socket.
	     */
	    public InputStream getInputStream() throws IOException;

	     /**
	      *  Closes underlying socket(s)
	      */
	    public void close() throws IOException;
	    
	    /**
	     * Closes child socket
	     * 
	     * @throws IOException
	     */
	    public void closeChild() throws IOException;
	}
