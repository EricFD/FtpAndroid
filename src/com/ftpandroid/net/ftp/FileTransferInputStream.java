package com.ftpandroid.net.ftp;


import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 *  Super class of all input streams supported
 *
 *  @author      Eric
 */
abstract public class FileTransferInputStream extends InputStream {

    /** 
     * Name of remote file being transferred
     */
    protected String remoteFile;
    
    /**
     * Has the stream been closed?
     */
    protected boolean closed = false;
    
    /**
     * Interval that we notify the monitor of progress
     */
    protected long monitorInterval;
          
    /**
     * Byte position in file
     */
    protected long pos = 0;
        
    /**
     * Count of byte since last the progress monitor was notified.
     */
    protected long monitorCount = 0; 

    /**
     * Progress monitor reference
     */
    protected FTPProgressMonitor monitor;
    
    /**
     * Progress monitor reference
     */
    protected FTPProgressMonitorEx monitorEx;
    
    /**
     * Flag to indicated we've started downloading
     */
    protected boolean started = false;

    /**
     * Get the name of the remote file 
     * 
     * @return remote filename
     */
    public String getRemoteFile() {
        return remoteFile;
    }
    
    /**
     * The input stream uses the progress monitor currently owned by the FTP client.
     * This method allows a different progress monitor to be passed in, or for the
     * monitor interval to be altered.
     * 
     * @param monitor               progress monitor reference
     * @param monitorInterval       
     */
    public void setMonitor(FTPProgressMonitorEx monitor, long monitorInterval) {
        this.monitor = monitor;
        this.monitorEx = monitor;
        this.monitorInterval = monitorInterval;
    }
    
    /**
     * Check if time to invoke the monitor
     */
    protected void checkMonitor() {
        if (monitor != null && monitorCount > monitorInterval) {
            monitor.bytesTransferred(pos); 
            monitorCount = 0;  
       } 
    }
    
}
