package com.ftpandroid.net.ftp;

import java.util.Enumeration;
import java.util.Vector;

public class FileStatistics {
    
    /**
     * Client with stats
     */
    private Vector clients = new Vector();
    
    /**
     * Default constructor
     * 
     * @param downloadCount
     * @param uploadCount
     * @param deleteCount
     */
    public FileStatistics() {
    }

    /**
     * Constructor
     * 
     * @param downloadCount
     * @param uploadCount
     * @param deleteCount
     */
    FileStatistics(FTPClientInterface client) {
        clients.add(client);
    }
    
    /**
     * Add a client to be used in calculating statistics
     * 
     * @param client    extra client
     */
    public synchronized void addClient(FTPClientInterface client) {
        clients.add(client);
    }

    /**
     * Get the number of files downloaded since the count was
     * reset
     * 
     * @return  download file count
     */
    public synchronized int getDownloadCount() {
        int count = 0;
        Enumeration e = clients.elements();
        while (e.hasMoreElements()) {
            FTPClientInterface client = (FTPClientInterface)e.nextElement();
            count += client.getDownloadCount();
        }
        return count;
    }
        
    /**
     * Get the number of files uploaded since the count was
     * reset
     * 
     * @return  upload file count
     */
    public synchronized int getUploadCount() {
        int count = 0;
        Enumeration e = clients.elements();
        while (e.hasMoreElements()) {
            FTPClientInterface client = (FTPClientInterface)e.nextElement();
            count += client.getUploadCount();
        }
        return count;
    }
    
    
    /**
     * Get the number of files deleted since the count was
     * reset
     * 
     * @return  deleted file count
     */
    public synchronized int getDeleteCount() {
        int count = 0;
        Enumeration e = clients.elements();
        while (e.hasMoreElements()) {
            FTPClientInterface client = (FTPClientInterface)e.nextElement();
            count += client.getDeleteCount();
        }
        return count;
    }
    
    /**
     * Reset the statistics back to zero
     */
    public synchronized void clear() {
        Enumeration e = clients.elements();
        while (e.hasMoreElements()) {
            FTPClientInterface client = (FTPClientInterface)e.nextElement();
            client.resetDownloadCount();
            client.resetDeleteCount();
            client.resetUploadCount();
        }
    }

}
