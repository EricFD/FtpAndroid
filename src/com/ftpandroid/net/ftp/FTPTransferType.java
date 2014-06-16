package com.ftpandroid.net.ftp;

/**
 *  Enumerates the transfer types possible. We
 *  support only the two common types, ASCII and
 *  Image (often called binary).
 *
 *  @author         Eric
 */
 public class FTPTransferType {

     
     /**
      * Readable description
      */
     private String desc;

     /**
      *   Represents ASCII transfer type
      */
     public static final FTPTransferType ASCII = new FTPTransferType("ASCII");

     /**
      *   Represents Image (or binary) transfer type
      */
     public static final FTPTransferType BINARY = new FTPTransferType("Binary");

     /**
      *   The char sent to the server to set ASCII
      */
     static String ASCII_CHAR = "A";

     /**
      *   The char sent to the server to set BINARY
      */
     static String BINARY_CHAR = "I";

     /**
      *  Private so no-one else can instantiate this class
      */
     private FTPTransferType(String desc) {
         this.desc = desc;
     }
     
     public String toString() {
         return desc;
     }
 }
