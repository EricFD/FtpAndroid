package com.ftpandroid.net.ftp;

/**
 *  Encapsulates the possible transfer directions
 *
 *  @author      Eric
 */
public class TransferDirection {
    
    private String direction;
    
    /**
     *   Represents upload
     */
    public static final TransferDirection UPLOAD = new TransferDirection("Upload");

    /**
     *   Represents download
     */
    public static final TransferDirection DOWNLOAD = new TransferDirection("Download");

    /**
     *  Private so no-one else can instantiate this class
     */
    private TransferDirection(String direction) {
        this.direction = direction;
    }
    
    public String toString() {
        return direction;
    }
    
  

}
