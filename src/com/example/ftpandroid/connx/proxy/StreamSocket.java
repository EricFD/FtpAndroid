package com.example.ftpandroid.connx.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;

public interface StreamSocket {
	/**
	 * Close socket
	 */
	public void close() throws IOException;
	
	
	public boolean isConnected();
	
	/**
	 * Get socket input stream
	 * @return stream
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;
	
	/**
	 * Enable/diseable SO_TIMEOUT
	 * @param timeout
	 * @throws SocketException
	 */
	public void setSoTimeout(int timeout) throws SocketException;
	
	public int getSoTimeout() throws SocketException;
	
	public InetAddress getLocalAddress();
	
	public int getLocalPort();
	
	public InetAddress getInetAddress();
	
	public String getRemoteHost();
	
	public void setRemoteHost(String remoteHost);
	
	public int getReceiveBufferSize() throws SocketException;
	
	public void setReceiveBufferSize(int size) throws SocketException;
	
	public void setSendBufferSize(int size) throws SocketException;
	
	public boolean isSecureMode();
	
	public String getDetail();
	
}
