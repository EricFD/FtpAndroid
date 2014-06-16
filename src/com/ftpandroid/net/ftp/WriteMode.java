package com.ftpandroid.net.ftp;

/**
 *  Enumerates the write modes that are possible when
 *  transferring files.
 *
 *  @author     Eric
 *
 */
 public class WriteMode {

     
     /**
      *   Overwrite the file
      */
     public static final WriteMode OVERWRITE = new WriteMode("OVERWRITE");

     /**
      *   Append the file
      */
     public static final WriteMode APPEND = new WriteMode("APPEND");

     /**
      *   Resume the file
      */
     public static final WriteMode RESUME = new WriteMode("RESUME");
     
     /**
      *   Description of the write-mode
      */
     private String description;

     /**
      *  Private so no-one else can instantiate this class
      */
     private WriteMode(String description) {
    	 this.description = description;
     }
     
     public boolean equals(Object obj) {
         if (this == obj) return true;
         if (!(obj instanceof WriteMode )) return false;
         WriteMode mode = (WriteMode)obj;
         if (mode.description.equals(description)) return true;
         return false;
     }
     
     public String toString() {
    	 return description;
     }
 }
