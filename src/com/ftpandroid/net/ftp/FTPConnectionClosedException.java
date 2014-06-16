package com.ftpandroid.net.ftp;
/**
 *  Thrown when an FTP transfer has been closed by the server
 *
 *  @author     Eric
 *
 */
 public class FTPConnectionClosedException extends FTPException {

    /**
     * Serial uid
     */
    private static final long serialVersionUID = 1L;
    
    /**
     *   Constructor. Delegates to super.
     */
    public FTPConnectionClosedException(String msg) {
        super(msg);
    }
}
