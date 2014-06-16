package com.ftpandroid.net.ftp;

public interface DirectoryListCallback {
    
    /**
     * List a directory entry
     * 
     * @param arg
     */
    public void listDirectoryEntry(DirectoryListArgument arg);
    
}
