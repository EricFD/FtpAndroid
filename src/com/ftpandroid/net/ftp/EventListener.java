package com.ftpandroid.net.ftp;

public interface EventListener {
    
    /**
     * Log an FTP command being sent to the server. Not used for SFTP.
     * 
     * @param connID Identifier of FTP connection
     * @param cmd   command string
     */
    public void commandSent(String connId, String cmd); 
    
    /**
     * Log an FTP reply being sent back to the client. Not used for
     * SFTP.
     * 
     * @param connID Identifier of FTP connection
     * @param reply   reply string
     */
    public void replyReceived(String connId, String reply); 
    
    /**
     * Report the number of bytes transferred so far. This may
     * not be entirely accurate for transferring text files in ASCII
     * mode, as new line representations can be represented differently
     * on different platforms.
     * 
     * @param connID Identifier of FTP connection
     * @param remoteFilename Name of remote file
     * @param count  count of bytes transferred
     */
    public void bytesTransferred(String connId, String remoteFilename, long count);
    
    /**
     * Notifies that a download has started
     * 
     * @param connID Identifier of FTP connection
     * @param remoteFilename   remote file name
     */
    public void downloadStarted(String connId, String remoteFilename);
    
    /**
     * Notifies that a download has completed
     * 
     * @param connID Identifier of FTP connection
     * @param remoteFilename   remote file name
     */
    public void downloadCompleted(String connId, String remoteFilename);
    
    /**
     * Notifies that an upload has started
     * 
     * @param connID Identifier of FTP connection
     * @param remoteFilename   remote file name
     */
    public void uploadStarted(String connId, String remoteFilename);
    
    /**
     * Notifies that an upload has completed
     * 
     * @param connID Identifier of FTP connection
     * @param remoteFilename   remote file name
     */
    public void uploadCompleted(String connId, String remoteFilename);

}
