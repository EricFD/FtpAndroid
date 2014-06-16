package com.ftpandroid.net.ftp.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.ftpandroid.connx.debug.Logger;


/**
 * Active data socket handling class
 * 
 * @author Eric
 */
public class FTPActiveDataSocket implements FTPDataSocket {

    
    /**
     * Logging object
     */
    private static Logger log = Logger.getLogger("FTPActiveDataSocket");

    /**
     * The underlying socket for Active connection.
     */
    protected ServerSocket sock = null;

    /**
     * The socket accepted from server.
     */
    protected Socket acceptedSock = null;
    
    protected int sendBufferSize = 0;
    
    private InetAddress localAddress = null;
    
    /**
     * Constructor
     * 
     * @param sock
     *            the server socket to use
     */
    public FTPActiveDataSocket(ServerSocket sock) {
        this.sock = sock;
    }

    /**
     * Set the TCP timeout on the underlying data socket(s).
     * 
     * If a timeout is set, then any operation which takes longer than the
     * timeout value will be killed with a java.io.InterruptedException.
     * 
     * @param millis
     *            The length of the timeout, in milliseconds
     */
    public void setTimeout(int millis) throws IOException {
        sock.setSoTimeout(millis);
        if (acceptedSock != null)
            acceptedSock.setSoTimeout(millis);
    }
    
    /**
     * Set the size of the data socket's receive buffer.
     * 
     * @param size  must be > 0
     */
    public void setReceiveBufferSize(int size) throws IOException {
        sock.setReceiveBufferSize(size);
        if (acceptedSock != null)
            acceptedSock.setReceiveBufferSize(size);
    }
    
    /**
     * Set the size of the data socket's send buffer.
     * 
     * @param size  must be > 0
     */
    public void setSendBufferSize(int size) throws IOException {
        this.sendBufferSize = size;
        if (acceptedSock != null)
            acceptedSock.setSendBufferSize(size);
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
        if (localAddress != null)
            return localAddress;
        return sock.getInetAddress();
    }     
    
    /**
     * Set the local address to be used
     * 
     * @param addr   address to set
     */
    public void setLocalAddress(InetAddress addr) {
        localAddress = addr;
    }

    /**
     * Waits for a connection from the server and then sets the timeout when the
     * connection is made.
     * 
     * @throws IOException  There was an error while 
     * 						waiting for or accepting a
     *             			connection from the server.
     */
    protected void acceptConnection() throws IOException {
        log.debug("Calling accept()");
        acceptedSock = sock.accept();
        acceptedSock.setSoTimeout(sock.getSoTimeout());
        acceptedSock.setReceiveBufferSize(sock.getReceiveBufferSize());
        if (sendBufferSize > 0)
            acceptedSock.setSendBufferSize(sendBufferSize);
        log.debug("accept() succeeded");
    }
    

    /**
     * If active mode, accepts the FTP server's connection - in PASV, we are
     * already connected. Then gets the output stream of the connection
     * 
     * @return output stream for underlying socket.
     */
    public OutputStream getOutputStream() throws IOException {
        acceptConnection();
        return acceptedSock.getOutputStream();
    }

    /**
     * If active mode, accepts the FTP server's connection - in PASV, we are
     * already connected. Then gets the input stream of the connection
     * 
     * @return input stream for underlying socket.
     */
    public InputStream getInputStream() throws IOException {
        acceptConnection();
        return acceptedSock.getInputStream();
    }

    /**
     * Closes underlying sockets
     */
    public void close() throws IOException {
        closeChild();
        sock.close();
        log.debug("close() succeeded");
    }

    /**
     * Closes child socket
     */
    public void closeChild() throws IOException {
        if (acceptedSock != null) {
            acceptedSock.close();
            acceptedSock = null;
            log.debug("closeChild() succeeded");
        }
    }
}
