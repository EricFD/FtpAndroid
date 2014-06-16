package com.ftpandroid.net.ftp;

public interface DataChannelCallback {
    
    /**
     * If this callback is implemented, it should return the endpoint that the user
     * wishes to connect to. The supplied endpoint is what the server is returning.
     * 
     * @param endpoint  the endpoint specified by the server's response to the PASV command
     * @return  the actual endpoint that should be used
     */
    public IPEndpoint onPASVResponse(IPEndpoint endpoint);
    
    /**
     * If this callback is implemented, it should return the endpoint that the server
     * should connect to in active (PORT) mode. The supplied endpoint is what the client 
     * is going to use..
     * 
     * @param endpoint  the endpoint the client will begin listening on 
     * @return  the actual endpoint that should be used
     */
    public IPEndpoint onPORTCommand(IPEndpoint endpoint);

}
