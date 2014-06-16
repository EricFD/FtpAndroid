package com.ftpandroid.net.ftp.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import com.ftpandroid.connx.proxy.StreamSocket;

/**
 *  Passive data socket handling class
 *
 *  @author      Eric
 */
public class FTPPassiveDataSocket implements FTPDataSocket {

   
    /**
     *  The underlying socket 
     */
    protected StreamSocket sock = null;
    
    /**
     *  Constructor
     * 
     *  @param  sock  client socket to use
     */
    public FTPPassiveDataSocket(StreamSocket sock) {
        this.sock = sock;
    }

    /**
     *   Set the TCP timeout on the underlying control socket.
     *
     *   If a timeout is set, then any operation which
     *   takes longer than the timeout value will be
     *   killed with a java.io.InterruptedException.
     *
     *   @param millis The length of the timeout, in milliseconds
     */
    public void setTimeout(int millis) throws IOException {
        sock.setSoTimeout(millis);
    }
    
    /**
     * Set the size of the data socket's receive buffer.
     * 
     * @param size  must be > 0
     */
    public void setReceiveBufferSize(int size) throws IOException {
        sock.setReceiveBufferSize(size);
    }
    
    /**
     * Set the size of the data socket's send buffer.
     * 
     * @param size  must be > 0
     */
    public void setSendBufferSize(int size) throws IOException {
        sock.setSendBufferSize(size);
    }
    
    /**
     * Returns the local port to which this socket is bound. 
     * 
     * @return the local port number to which this socket is bound
     */
    public int getLocalPort() {
        return sock.getLocalPort();
    }        

    /**
     * Returns the local address to which this socket is bound. 
     * 
     * @return the local address to which this socket is bound
     */
    public InetAddress getLocalAddress() {
        return sock.getLocalAddress();
    }        
    
    /**
     *  If active mode, accepts the FTP server's connection - in PASV,
     *  we are already connected. Then gets the output stream of
     *  the connection
     *
     *  @return  output stream for underlying socket.
     */
    public OutputStream getOutputStream() throws IOException {        
        return sock.getOutputStream();
    }

    /**
     *  If active mode, accepts the FTP server's connection - in PASV,
     *  we are already connected. Then gets the input stream of
     *  the connection
     *
     *  @return  input stream for underlying socket.
     */
    public InputStream getInputStream() throws IOException {
        return sock.getInputStream();
    }

    /**
     *  Closes underlying socket
     */
    public void close() throws IOException {
        sock.close();
    }

    /**
     * Does nothing in passive mode
     */
    public void closeChild() throws IOException {
        // does nothing
    }
    
}
