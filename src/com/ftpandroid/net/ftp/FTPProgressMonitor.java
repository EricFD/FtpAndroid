package com.ftpandroid.net.ftp;

/**
 *  Allows the reporting of progress of the
 *  transfer of data
 *
 *  @author      Eric
 */
public interface FTPProgressMonitor {

    /**
     * Report the number of bytes transferred so far. This may
     * not be entirely accurate for transferring text files in ASCII
     * mode, as new line representations can be represented differently
     * on different platforms.
     * 
     * @param count  count of bytes transferred
     */
    public void bytesTransferred(long count);
}
