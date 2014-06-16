package com.ftpandroid.net.ftp;

/**
 *  A malformed reply was received from the server
 *

 */
 public class MalformedReplyException extends FTPException {

    /**
     * Serial uid
     */
    private static final long serialVersionUID = 1L;

    /**
     *   Constructor. Delegates to super.
     *
     *   @param   msg   Message that the user will be
     *                  able to retrieve
     */
    public MalformedReplyException(String msg) {
        super(msg);
    }

}
