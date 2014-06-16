package com.ftpandroid.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;

import junit.framework.TestSuite;

public class FileZip  {

 
    private String zipArchive ;
    private String zipArchiveRemote;
    private String zipDir;
    private String zipFile;

    /**
     *  Get name of log file
     *
     *  @return name of file to log to
     */
    protected String getLogName() {
        return "TestZip.log";
    }
    
    public FileZip() {
        initProperties();
    }
    
    private void initProperties() {
        zipArchive = props.getProperty("ftptest.ziparchive");
        zipArchiveRemote = props.getProperty("ftptest.ziparchive.remote");
        zipDir = props.getProperty("ftptest.zipdir");
        zipFile = props.getProperty("ftptest.zipfile");
    }

    /**
     *  Test retrieving a file from zip file
     */
    public void testRetrieveZip() throws Exception {

        log.info("testRetrieveZip()");
        
        
        
        try {

            connect();
            
            // monitor transfer progress
            ftp.setProgressMonitor(new FTPProgressMonitor(), 250000);
    
            // move to test directory
            ftp.chdir(testdir);
            ftp.setType(FTPTransferType.BINARY);
            
            // put the zip file
            ftp.put(localDataDir + zipArchive, zipArchive);
            
            // navigate into zipfile
            ftp.chdir(zipArchiveRemote);
            
            // and to a dir within the zip file
            ftp.chdir(zipDir);
            
            // get internal zip file to a random filename
            String filename = generateRandomFilename();
            ftp.get(localDataDir + filename, zipFile);
            
            // back out of zip file
            ftp.chdir("..");
            ftp.chdir("..");
    
            // now get that file out of the local zip archive     
            ZipInputStream zipinputstream = 
                 new ZipInputStream(
                    new FileInputStream(localDataDir + zipArchive));
            
            ZipEntry zipentry = null;
            String zipFilePath = zipDir + "/" + zipFile;
            while ((zipentry = zipinputstream.getNextEntry()) != null) {
               if (zipentry.getName().equals(zipFilePath)) {
                    byte[] buf = new byte[1024];
                    FileOutputStream out = new FileOutputStream(localDataDir + zipFile);
                    int count = 0;
                    while ((count = zipinputstream.read(buf)) >= 0) {
                        out.write(buf, 0, count);
                    }
                    out.close();  
                    break;
                }    
            }
            zipinputstream.close();
            
       
            // delete remote file
            ftp.delete(zipArchive);
            
    
            // check equality of local files
            assertIdentical(localDataDir + zipFile, localDataDir + filename);
    
            // and delete local file
            File local = new File(localDataDir + filename);
            local.delete();
            local = new File(localDataDir + zipFile);
            local.delete();
    
            ftp.quit();
        }
        finally {
            ftp.quitImmediately();
        }
    }    
    
    
    /**
     *  Automatic test suite construction
     *
     *  @return  suite of tests for this class
     */
    public static Test suite() {
        return new TestSuite(FileZip.class);
    } 

    /**
     *  Enable our class to be run, doing the
     *  tests
     */
    public static void main(String[] args) {       
        junit.textui.TestRunner.run(suite());
    }
}

