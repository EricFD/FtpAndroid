package com.ftpandroid.net.ftp;

/**
 *  Listens for and is notified of FTP commands and replies. 
 *
 *  @author      Eric
 */
public interface FTPMessageListener {
    
    /**
     * Log an FTP command being sent to the server
     * 
     * @param cmd   command string
     */
    public void logCommand(String cmd); 
    
    /**
     * Log an FTP reply being sent back to the client
     * 
     * @param reply   reply string
     */
    public void logReply(String reply); 

}
