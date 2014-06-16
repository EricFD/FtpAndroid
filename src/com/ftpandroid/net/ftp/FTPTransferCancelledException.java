package com.ftpandroid.net.ftp;

/**
 *  Thrown when an FTP transfer has been cancelled
 *
 *  @author     Eric
 */
 public class FTPTransferCancelledException extends FTPException {



    /**
     * Serial uid
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Message
     */
    private static final String MESSAGE = "Transfer was cancelled";

    /**
     *   Constructor. Delegates to super.
     */
    public FTPTransferCancelledException() {
        super(MESSAGE);
    }
}
