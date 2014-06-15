package com.example.ftpandroid.connx.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.ftpandroid.debug.Logger;

public class PlainSocket extends Socket implements StreamSocket{
	/**
	 * Object Logging  
	 */
	private static Logger log = Logger.getLogger("PlainSocket");
	
	protected String remoteHostname;
	
	/**
	 * Creates a new obkect PlainSocket
	 * @throws IOException
	 */
	public PlainSocket(){}
	
	/**
	 * Create a new object PlainSOcket
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public PlainSocket(String host, int port) throws IOException{
			super(host, port);
	}
	
	/**
	 * Creates a new object PlainSocket
	 * 
	 * @param addr
	 * @param port
	 * 
	 * @throws IOException
	 */
	public PlainSocket(InetAddress addr, int port) throws IOException{
		super(addr, port);
	}
	
	/**
	 * Socket in secure mode
	 * @return true if secure
	 */
	public boolean isSecureMode(){
		return false;
	}
	
	/**
	 * Get the actual hostname
	 * @return remote hostname
	 */
	public String getRemoteHost(){
		return remoteHostname;
	}
	
	/**
	 * set the remote host
	 * @param remoteHost
	 */
	public void setRemoteHost(String remoteHost){
		this.remoteHostname = remoteHost;
	}
	
	public String getDetail(){
		return toString();
	}
	
	/**
	 * create connected socket, using timeaout for available
	 * witch tested by tryinf to create instance 
	 * @param host
	 * @param port
	 * @param timeout
	 * @exception IOException
	 */
	public static PlainSocket createPlainSocket(String host, int port, int timeout) throws IOException{
		PlainSocket sock = new PlainSocket();
		InetSocketAddress addr = new InetSocketAddress(host, port);
		sock.connect(addr, timeout);
		return sock;
	}
	
	/**
	 * Create connection socket
	 */
	public static PlainSocket createPlainSocket(InetAddress host, int port, int timeout) throws IOException{
		PlainSocket sock = new PlainSocket();
		InetSocketAddress addr = new InetSocketAddress(host, port);
		sock.connect(addr, timeout);
		return sock;
	}

}
