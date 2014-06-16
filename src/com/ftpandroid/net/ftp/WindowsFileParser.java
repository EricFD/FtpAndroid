package com.ftpandroid.net.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.ParseException;

import com.ftpandroid.connx.debug.Logger;

/**
 *  Represents a remote Windows file parser
 *
 */
public class WindowsFileParser extends FTPFileParser {

    
    /**
     * Logging object
     */
    private static Logger log = Logger.getLogger("WindowsFileParser");

    /**
     * Date formatter
     */
    private SimpleDateFormat formatter;
    
    /**
     * Directory field
     */
    private final static String DIR = "<DIR>";
    
    /**
     * Number of expected fields
     */
    private final static int MIN_EXPECTED_FIELD_COUNT = 4;

    /**
     * Constructor
     */
     public WindowsFileParser() {
         setLocale(Locale.getDefault());
     }
     
     public String toString() {
         return "Windows";
     }
     
     
    /**
     * Set the locale for date parsing of listings
     * 
     * @param locale    locale to set
     */
    public void setLocale(Locale locale) {
        formatter = new SimpleDateFormat("MM-dd-yy hh:mma", locale);
    }    
    
    /**
     * Valid format for this parser
     * 
     * @param listing
     * @return true if valid
     */
    public boolean isValidFormat(String[] listing) {
        int count = Math.min(listing.length, 10);
        
        boolean dateStart = false;
        boolean timeColon = false;
        boolean dirOrFile = false;

        for (int i = 0; i < count; i++) {
            if (listing[i].trim().length() == 0)
                continue;
            String[] fields = split(listing[i]);
            if (fields.length < MIN_EXPECTED_FIELD_COUNT)
                continue;
            // first & last chars are digits of first field
            if (Character.isDigit(fields[0].charAt(0)) && Character.isDigit(fields[0].charAt(fields[0].length()-1)))
                dateStart = true;
            if (fields[1].indexOf(':') > 0)
                timeColon = true;
            if (fields[2].equalsIgnoreCase(DIR) || Character.isDigit(fields[2].charAt(0)) )
                dirOrFile = true;
        }
        if (dateStart && timeColon && dirOrFile)
            return true;
        log.debug("Not in Windows format");
        return false;
    }


    /**
     * Parse server supplied string. Should be in
     * form 
     * 
     * 05-17-03  02:47PM                70776 ftp.jar
     * 08-28-03  10:08PM       <DIR>          EDT SSLTest
     * 
     * @param raw   raw string to parse
     */
    public FTPFile parse(String raw) throws ParseException {
        String[] fields = split(raw);
        
        if (fields.length < MIN_EXPECTED_FIELD_COUNT)
            return null;
         
        // first two fields are date time
        Date lastModified = null;
        try {
            lastModified = formatter.parse(fields[0] + " " + fields[1]);
        }
        catch (ParseException ex) {
            if (!ignoreDateParseErrors) {
                throw new DateParseException(ex.getMessage());
            }
        }
        
        // dir flag
        boolean isDir = false;
        long size = 0L;
        if (fields[2].equalsIgnoreCase(DIR))
            isDir = true;
        else {
            try {
                size = Long.parseLong(fields[2]);
            }
            catch (NumberFormatException ex) {
                log.warn("Failed to parse size: " + fields[2]);
            }
        }
        
        // we've got to find the starting point of the name. We
        // do this by finding the pos of all the date/time fields, then
        // the name - to ensure we don't get tricked up by a date or dir the
        // same as the filename, for example
        int pos = 0;
        boolean ok = true;
        for (int i = 0; i < 3; i++) {
            pos = raw.indexOf(fields[i], pos);
            if (pos < 0) {
                ok = false;
                break;
            }
            else { // move on the length of the field
                pos += fields[i].length();
            }
        }
        String name = null;
        if (ok) {
            name = trimStart(raw.substring(pos));           
        }
        else {
            log.warn("Failed to retrieve name: " + raw);  
        }
        return new FTPFile(raw, name, size, isDir, lastModified); 
    }
  
}
