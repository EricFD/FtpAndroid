package com.ftpandroid.net.ftp;

/**
 *  Enhances FTPProgressMonitor to add notifications for start and
 *  completion of the transfer.
 *
 *  @author      Eric
 */
public interface FTPProgressMonitorEx extends FTPProgressMonitor {

    /**
     * Notify that a transfer has started
     * 
     * @param  direction  the transfer direction
     * @param  remoteFile  name of the remote file
     */
    public void transferStarted(TransferDirection direction, String remoteFile);
    
    /**
     * Notify that a transfer has completed
     * 
     * @param  direction  the transfer direction
     * @param  remoteFile  name of the remote file
     */
    public void transferComplete(TransferDirection direction, String remoteFile);
}
