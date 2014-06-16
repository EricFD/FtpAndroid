package com.ftpandroid.net.ftp;


/**
 *  Encapsulates the FTP server reply
 *
 *  @author      Eric
 */
public class FTPReply {

    /**
     *  Reply code
     */
    private String replyCode;

    /**
     *  Reply text
     */
    private String replyText;
    
    /**
     * Raw reply
     */
    private String rawReply;

    /**
     * Lines of data returned, e.g. FEAT
     */
    private String[] data;

    /**
     *  Constructor. Only to be constructed
     *  by this package, hence package access
     *
     *  @param  replyCode  the server's reply code
     *  @param  replyText  the server's reply text
     */
    FTPReply(String replyCode, String replyText) throws MalformedReplyException {
        this.replyCode = replyCode;
        this.replyText = replyText;
        rawReply = replyCode + " " + replyText;
        validateCode(replyCode);
    }
    
    
    /**
     *  Constructor. Only to be constructed
     *  by this package, hence package access
     *
     *  @param  replyCode  the server's reply code
     *  @param  replyText  the server's full reply text
     *  @param  data       data lines contained in reply text
     */
    FTPReply(String replyCode, String replyText, String[] data) throws MalformedReplyException {
        this.replyCode = replyCode;
        this.replyText = replyText;
        this.data = data;
        validateCode(replyCode);
    }
    
    
    /**
     *  Constructor. Only to be constructed
     *  by this package, hence package access
     *
     *  @param  rawReply  the server's raw reply
     */
    FTPReply(String reply) throws MalformedReplyException {        
        // all reply codes are 3 chars long
        this.rawReply = reply.trim();
        replyCode = rawReply.substring(0, 3);
        if (rawReply.length() > 3)
            replyText = rawReply.substring(4);
        else
            replyText = "";
        validateCode(replyCode);
    }
    
    /**
     *  Getter for raw reply
     *
     *  @return server's raw reply
     */
    public String getRawReply() {
        return rawReply;
    }

    /**
     *  Getter for reply code
     *
     *  @return server's reply code
     */
    public String getReplyCode() {
        return replyCode;
    }

    /**
     *  Getter for reply text
     * 
     *  @return server's reply text
     */
    public String getReplyText() {
        return replyText;
    }
    
    /**
     * Getter for reply data lines
     * 
     * @return array of data lines returned (if any). Null
     *          if no data lines
     */
    public String[] getReplyData() {
        return data;
    }
    
    private void validateCode(String code) throws MalformedReplyException {
        try {
            Integer.parseInt(code);
        }
        catch (NumberFormatException ex) {
            throw new MalformedReplyException("Invalid reply code '" + code + "'");
        }
    }

}
