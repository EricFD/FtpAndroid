package com.ftpandroid.net.ftp;

/**
 *  Holds details of an IP endpoint, i.e. IP address
 *  and port number. Immutuable class.
 *
 */
public class IPEndpoint {

    private String ipAddress;
    private int port = 0;
    
    /**
     * Constructor
     * 
     * @param ipAddress   ip address
     * @param port        port number
     */
    public IPEndpoint(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    /**
     * Get the ip address
     * 
     * @return ip address
     */
    public String getIPAddress() {
        return ipAddress;
    }
    
    /**
     *  Get the port number
     *  
     * @return port number
     */
    public int getPort() {
        return port;
    }
    
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("[").append(ipAddress).append(":").
            append(port).append("]");
        return result.toString();
    }
}
