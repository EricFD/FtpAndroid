package com.ftpandroid.net.ftp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.ftpandroid.connx.debug.Logger;

abstract public class AbstractFTPInputStream extends FileTransferInputStream {
    
    private static Logger log = Logger.getLogger("AbstractFTPInputStream");
       
    /**
     * The client being used to perform the transfer
     */
    protected FTPClient client; 
    
    /**
     * The input stream from the FTP server
     */
    protected BufferedInputStream in;

        
    /**
     * Constructor. A connected FTPClient instance must be supplied. This sets up the
     * download. If an offset > 0 is supplied, must be a binary transfer.
     * 
     * @param client            connected FTPClient instance
     * @param remoteFile        remote file
     * @throws IOException
     * @throws FTPException
     */
    public AbstractFTPInputStream(FTPClient client, String remoteFile) throws IOException, FTPException {
        this.client = client;
        this.remoteFile = remoteFile;
        this.monitorInterval = client.getMonitorInterval();
        this.monitor = client.getProgressMonitor();
    }
    
    protected void start() throws IOException {
        start(true);
    }
    
    /**
     * Start the transfer
     * 
     * @throws IOException
     */
    protected void start(boolean firstTime) throws IOException {
        try {
            if (pos > 0)
                client.resumeNextDownload(pos);
            
            client.initGet(remoteFile);

            // get an input stream to read data from ... AFTER we have
            // the ok to go ahead AND AFTER we've successfully opened a
            // stream for the local file
            in = new BufferedInputStream(new DataInputStream(client.getInputStream()));

        } 
        catch (FTPException ex) {
            throw new IOException(ex.getMessage());
        }
        catch (IOException ex) {
            try {
                client.validateTransferOnError(ex);
            }
            catch (FTPException ex2) {
                throw new IOException(ex2.getMessage());
            }
            throw ex;
        }
        if (firstTime && monitorEx != null) {
            monitorEx.transferStarted(TransferDirection.DOWNLOAD, remoteFile);
        }
        started = true;
        closed = false;
        monitorCount = 0;
    }
    

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream. This <b>must</b> be called before any other operations
     * are initiated on the FTPClient.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    public void close() throws IOException {
        
        if (!closed) {
            closed = true;
            
            client.forceResumeOff();
    
            // close streams
            client.closeDataSocket(in);
            
            if (monitor != null)
                monitor.bytesTransferred(pos);  
    
            // log bytes transferred
            log.debug("Transferred " + pos + " bytes from remote host");
            
            // read the reply - may be a 426 as we could have closed early
            try {
                client.readReply();
            }
            catch (FTPException ex) {
                throw new IOException(ex.getMessage());
            }
            
            // don't know if it is truly complete for whole file but our transfer is
            if (monitorEx != null)
                monitorEx.transferComplete(TransferDirection.DOWNLOAD, remoteFile);
        }
    }
    
}
