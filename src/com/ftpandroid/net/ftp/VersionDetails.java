package com.ftpandroid.net.ftp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.CodeSource;

/**
 *  Aggregates the version information
 *
 *  @author      Eric
 */
public class VersionDetails {
    
    /**
     * Major version (substituted by ant)
     */
    final private static String majorVersion = "2";
    
    /**
     * Middle version (substituted by ant)
     */
    final private static String middleVersion = "4";
    
    /**
     * Middle version (substituted by ant)
     */
    final private static String minorVersion = "0";
    
    /**
     * Full version
     */
    private static int[] version;
    
    /**
     * Full version string
     */
    private static String versionString;
    
    /**
     * Timestamp of build
     */
    final private static String buildTimestamp = "24-Dec-2012 12:36:06 EST";
    
    /**
     * Work out the version array
     */
    static {
        try {
            version = new int[3];
            version[0] = Integer.parseInt(majorVersion);
            version[1] = Integer.parseInt(middleVersion);
            version[2] = Integer.parseInt(minorVersion);
            versionString = version[0] + "." + version[1] + "." + version[2];
        }
        catch (NumberFormatException ex) {
            System.err.println("Failed to calculate version: " + ex.getMessage());
            versionString = "Unknown";
        }
    }
    
    /**
     * Get the product version
     * 
     * @return int array of {major,middle,minor} version numbers 
     */
    final public static int[] getVersion() {
        return version;
    }
    
    /**
     * Get the product version string
     * 
     * @return version as string 
     */
    final public static String getVersionString() {
        return versionString;
    }
    
    /**
     * Get the build timestamp
     * 
     * @return d-MMM-yyyy HH:mm:ss z build timestamp 
     */
    final public static String getBuildTimestamp() {
        return buildTimestamp;
    }
    
    /**
     * Report on useful things for debugging purposes
     * 
     * @param obj     instance to report on
     * @return string
     */
    final public static String report(Object obj) {
        StringWriter result = new StringWriter();
        PrintWriter report = new PrintWriter(result, true);
        try {     
            if (obj != null) {
                report.print("Class: ");
                report.println(obj.getClass().getName());
                report.print("Location: ");
                CodeSource cs = obj.getClass().getProtectionDomain().getCodeSource();
                if (cs != null)
                    report.println(cs.getLocation().toString());
                else
                    report.println("unknown");
            }
            else {
                report.print("Null object supplied");                
            }
            report.print("Version: ");
            report.println(versionString);
            report.print("Build timestamp: ");
            report.println(buildTimestamp);
        
            report.print("Java version: ");
            report.println(System.getProperty("java.version"));
            report.print("CLASSPATH: ");
            report.println(System.getProperty("java.class.path"));
            report.print("OS name: ");
            report.println(System.getProperty("os.name"));
            report.print("OS arch: ");
            report.println(System.getProperty("os.arch"));
            report.print("OS version: ");
            report.println(System.getProperty("os.version"));
        }
        catch (Throwable ex) {
            report.println("Could not obtain version details: " + ex.getMessage());
        }
        return result.toString();
    }
}
