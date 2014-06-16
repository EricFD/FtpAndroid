package com.ftpandroid.net.ftp;
/**
 *  Enumerates the connect modes that are possible,
 *  active & PASV
 *
 *  @author     Eric
 */
 public class FTPConnectMode {

     private String desc;

     /**
      *   Represents active connect mode
      */
     public static final FTPConnectMode ACTIVE = new FTPConnectMode("Active");

     /**
      *   Represents PASV connect mode
      */
     public static final FTPConnectMode PASV = new FTPConnectMode("Passive");

     /**
      *  Private so no-one else can instantiate this class
      */
     private FTPConnectMode(String desc) {
         this.desc = desc;
     }
     
     public String toString() {
      return desc;   
     }
 }
