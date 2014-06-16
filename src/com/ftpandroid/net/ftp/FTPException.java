package com.ftpandroid.net.ftp;
/**
 *  FTP specific exceptions
 *
 *  @author    Eric
 *
 */
 public class FTPException extends Exception {

   
    /**
     * Serial uid
     */
    private static final long serialVersionUID = 1L;

    /**
     *  Integer reply code
     */
    private int replyCode = -1;

    /**
     *   Constructor. Delegates to super.
     *
     *   @param   msg   Message that the user will be
     *                  able to retrieve
     */
    public FTPException(String msg) {
        super(msg);
    }

    /**
     *  Constructor. Permits setting of reply code
     *
     *   @param   msg        message that the user will be
     *                       able to retrieve
     *   @param   replyCode  string form of reply code
     */
    public FTPException(String msg, String replyCode) {

        super(msg);

        // extract reply code if possible
        try {
            this.replyCode = Integer.parseInt(replyCode);
        }
        catch (NumberFormatException ex) {
            this.replyCode = -1;
        }
    }
    
    /**
     *  Constructor. Permits setting of reply code
     *
     *   @param   reply     reply object
     */
    public FTPException(FTPReply reply) {

        super(reply.getReplyText());

        // extract reply code if possible
        try {
            this.replyCode = Integer.parseInt(reply.getReplyCode());
        }
        catch (NumberFormatException ex) {
            this.replyCode = -1;
        }
    }    
    


    /**
     *   Get the reply code if it exists
     *
     *   @return  reply if it exists, -1 otherwise
     */
    public int getReplyCode() {
        return replyCode;
    }
    
    /**
     * Returns a short description of this exception
     *
     * @return a string representation of this exception.
     */
    public String toString() {
        StringBuffer b = new StringBuffer(getClass().getName());
        b.append(": ");
        if (replyCode > 0)
            b.append(replyCode).append(" ");
        b.append(getMessage());
        return b.toString();
    }

}
