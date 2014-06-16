package com.ftpandroid.net.ftp;

public class DirectoryListArgument {

    private FTPFile entry;
    private boolean listingAborted = false;
    
    public DirectoryListArgument(FTPFile entry) {
        this.entry = entry;
    }
    
    public FTPFile getEntry() {
        return entry;
    }
    
    public void abortListing() {
        listingAborted = true;
    }
    
    public boolean isListingAborted() {
        return listingAborted;
    }
}

