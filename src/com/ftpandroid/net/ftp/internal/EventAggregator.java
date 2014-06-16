package com.ftpandroid.net.ftp.internal;

import com.ftpandroid.net.ftp.EventListener;
import com.ftpandroid.net.ftp.FTPProgressMonitor;
import com.ftpandroid.net.ftp.FTPProgressMonitorEx;
import com.ftpandroid.net.ftp.TransferDirection;

/**
 *  Implements the listener interfaces and aggregates them into
 *  the one EventListener interface
 *  @author Eric
 */
public class EventAggregator implements FTPMessageListener, FTPProgressMonitor, FTPProgressMonitorEx {

    private EventListener eventListener; 
    
    private String connId;
    private String remoteFile;
    
    /**
     * @param eventListener
     */
    public EventAggregator(EventListener eventListener) {
        this(null, eventListener);
    }
    
    /**
     * @param eventListener
     */
    public EventAggregator(String connId, EventListener eventListener) {
        this.connId = connId;
        this.eventListener = eventListener;
    }
    
    /**
     * Set the connection id 
     * 
     * @param connId  connection id
     */
    public void setConnId(String connId) {
        this.connId = connId;
    }

    public void logCommand(String cmd) {
        if (eventListener != null)
            eventListener.commandSent(connId, cmd);
    }

    public void logReply(String reply) {
        if (eventListener != null)
            eventListener.replyReceived(connId, reply);
        
    }
    
    public void bytesTransferred(long count) {
        if (eventListener != null)
            eventListener.bytesTransferred(connId, remoteFile, count);        
    }

    public void transferComplete(TransferDirection direction, String remoteFile) {
        if (eventListener != null) {
            if (direction.equals(TransferDirection.DOWNLOAD))
                eventListener.downloadCompleted(connId, remoteFile);
            else if (direction.equals(TransferDirection.UPLOAD))
                eventListener.uploadCompleted(connId, remoteFile);
        }
    }

    public void transferStarted(TransferDirection direction, String remoteFile) {
        this.remoteFile = remoteFile;
        if (eventListener != null) {
            if (direction.equals(TransferDirection.DOWNLOAD))
                eventListener.downloadStarted(connId, remoteFile);
            else if (direction.equals(TransferDirection.UPLOAD))
                eventListener.uploadStarted(connId, remoteFile);
        }            
    }

   
}
