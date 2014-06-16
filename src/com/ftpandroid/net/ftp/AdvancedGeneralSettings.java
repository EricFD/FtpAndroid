package com.ftpandroid.net.ftp;

import com.ftpandroid.net.ftp.internal.*;

public class AdvancedGeneralSettings {

    private ConnectionContext context;
    
    /**
     * Constructor
     * 
     * @param context  context that settings are kept in
     */
    AdvancedGeneralSettings(ConnectionContext context) {
        this.context = context;
    }
    
    /**
     * Determine if auto login is switched on
     * 
     * @return true if auto login
     */
    public boolean isAutoLogin() {
        return context.isAutoLogin();
    }

    /**
     * Set the autoLogin flag
     * 
     * @param autoLogin   true if logging in automatically
     */
    public void setAutoLogin(boolean autoLogin) {
        context.setAutoLogin(autoLogin);
    }
        
    /**
     * Listen on all interfaces for active mode transfers (the default).
     * 
     * @param listenOnAll   true if listen on all interfaces, false to listen on the control interface
     */
    public void setListenOnAllInterfaces(boolean listenOnAll) {
        context.setListenOnAllInterfaces(listenOnAll);
    }
    
    /**
     * Are we listening on all interfaces in active mode, which is the default?
     * 
     * @return true if listening on all interfaces, false if listening just on the control interface
     */
    public boolean isListenOnAllInterfaces() {
        return context.isListenOnAllInterfaces();
    }
    
    /**
     * If true, delete partially written files when exceptions are thrown
     * during a download
     * 
     * @return true if delete local file on error
     */
    public boolean isDeleteOnFailure() {
        return context.isDeleteOnFailure();
    }

    /**
     * Switch on or off the automatic deletion of partially written files 
     * that are left when an exception is thrown during a download
     * 
     * @param deleteOnFailure  true if delete when a failure occurs
     */
    public void setDeleteOnFailure(boolean deleteOnFailure) {
        context.setDeleteOnFailure(deleteOnFailure);
    }
    
    /**
     * Get the encoding used for the control channel
     * 
     * @return Returns the current controlEncoding.
     */
    public String getControlEncoding() {
        return context.getControlEncoding();
    }
    
    /**
     * Set the control channel encoding. 
     * 
     * @param controlEncoding The controlEncoding to set, which is the name of a Charset
     */
    public void setControlEncoding(String controlEncoding) {
        context.setControlEncoding(controlEncoding);
    }
    
    /**
     * Set the size of the data buffers used in reading and writing to the server
     * 
     * @param size  new size of buffer in bytes
     */
    public void setTransferBufferSize(int size) {
        context.setTransferBufferSize(size);
    }
    
    /**
     * Get the size of the data buffers used in reading and writing to the server
     * 
     * @return  transfer buffer size
     */
    public int getTransferBufferSize() {
        return context.getTransferBufferSize();
    }
    
    /**
     * Get the interval used for progress notification of transfers.
     * 
     * @return number of bytes between each notification.
     */
    public int getTransferNotifyInterval() {
        return context.getTransferNotifyInterval();
    }

    /**
     * Set the interval used for progress notification of transfers.
     * 
     * @param notifyInterval  number of bytes between each notification
     */
    public void setTransferNotifyInterval(int notifyInterval) {
        context.setTransferNotifyInterval(notifyInterval);
    }
    
    /**
     * Set file locking to enabled or disabled. When downloading files, by default
     * the local file is locked for exclusive writing to prevent other processes
     * corrupting it. 
     * 
     * @param lockingEnabled true to enable locking, false to disable
     */
    public void setFileLockingEnabled(boolean lockingEnabled) {
        context.setFileLockingEnabled(lockingEnabled);
    }
    
    /**
     * Determine if file locking on local downloaded files is being used or not. Default is true.
     * 
     * @return true if file locking is enabled, false otherwise
     */
    public boolean getFileLockingEnabled() {
        return context.getFileLockingEnabled();
    }

}
