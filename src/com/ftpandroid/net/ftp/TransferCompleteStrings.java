package com.ftpandroid.net.ftp;

/**
 *  Contains fragments of server replies that indicate no files were
 *  found in a supplied directory.
 *
 *  @author      Eric
 */
final public class TransferCompleteStrings extends ServerStrings {

    /**
     * Server string transfer complete (proFTPD/TLS)
     */
    final private static String TRANSFER_COMPLETE = "TRANSFER COMPLETE";
    
    /**
     * Constructor. Adds the fragments to match on
     */
    public TransferCompleteStrings() {
        add(TRANSFER_COMPLETE);
    }

}
