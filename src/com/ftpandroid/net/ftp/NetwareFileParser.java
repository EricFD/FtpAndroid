package com.ftpandroid.net.ftp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ftpandroid.connx.debug.Logger;

/**
 *  Represents a remote Netware file parser
 *
 */
public class NetwareFileParser extends FTPFileParser {

    
	/**
     * Logging object
     */
    private static Logger log = Logger.getLogger("NetwareFileParser");
        
    /**
     * These chars indicates ordinary files
     */
    private final static char FILE_CHAR = '-';
    
    /**
     * Indicates directory
     */
    private final static char DIRECTORY_CHAR = 'd';
    
    /**
     * Prefix e.g. ./WebshotsData
     */
    private final static String CURRENT_DIR_PREFIX = "./";
    
    /**
     * Date formatter
     */
    private SimpleDateFormat dateFormatter;
        
    /**
     * Minimum number of expected fields
     */
    private final static int MIN_FIELD_COUNT = 8;
    
    /**
     * Constructor
     */
    public NetwareFileParser() {
        setLocale(Locale.getDefault());
    }
    
    /**
     * Set the locale for date parsing of listings
     * 
     * @param locale    locale to set
     */
    public void setLocale(Locale locale) {
        dateFormatter = new SimpleDateFormat("MMM-dd-yyyy-HH:mm", locale);
    }  
    
    public String toString() {
        return "NETWARE";
    }
    
    
    /**
     * Valid format for this parser
     * 
     * @param listing
     * @return true if valid
     */
    public boolean isValidFormat(String[] listing) {
        int count = Math.min(listing.length, 10);      

        for (int i = 0; i < count; i++) {            
            if (listing[i].trim().length() == 0)
                continue;
            if (isNetware(listing[i]))
                return true;            
        }
        log.debug("Not in Netware format");
        return false;
    }
    
    /**
     * Is this a Netware format listing?
     * 
     * @param raw   raw listing line
     * @return true if Netware, false otherwise
     */
    public static boolean isNetware(String raw) {
        raw = raw.trim();
        if (raw.length() < 3)
            return false;
        char ch1 = raw.charAt(0);
        char ch2 = raw.charAt(2);
        if ((ch1 == '-' || ch1 == 'd') && (ch2 == '['))
            return true;
        return false;
    } 
    
    
    /**
     * Parse server supplied string, e.g.:
     * 
     * d [RWCEAFMS] PhilliJb                          512 May 10  2007 2007 Upgrade
     * - [RWCEAFMS] PhilliJb                       700730 Jun 26  2008 xtag_manual_v1.5.pdf
     * 
     * @param raw   raw string to parse
     */
    public FTPFile parse(String raw) throws ParseException {
        
        // test it is a valid line, e.g. "total 342522" is invalid
        if (!isNetware(raw))
            return null;
        
        String[] fields = split(raw);
         
        if (fields.length < MIN_FIELD_COUNT) {
            StringBuffer msg = new StringBuffer("Unexpected number of fields in listing '");
            msg.append(raw).append("' - expected minimum ").append(MIN_FIELD_COUNT). 
                    append(" fields but found ").append(fields.length).append(" fields");
            log.warn(msg.toString());
            return null;
        }
        
        // field pos
        int index = 0;
        
        // first field is perms
        boolean isDir = false;
        char ch = raw.charAt(0);
        if (ch == DIRECTORY_CHAR)
            isDir = true;
        
        String permissions = fields[++index];
        if (permissions.charAt(0) == '[' && permissions.charAt(permissions.length()-1) == ']') {
            permissions = permissions.substring(1);
            permissions = permissions.substring(0, permissions.length()-1);
        }
                
        // owner and group
        String owner = fields[++index];
        
        // size
        long size = 0L;
        String sizeStr = fields[++index];
        try {
            size = Long.parseLong(sizeStr);
        }
        catch (NumberFormatException ex) {
            log.warn("Failed to parse size: " + sizeStr);
        }
        
        // next 3 are the date time
        String month = fields[++index];
        String day = fields[++index];
        String year = fields[++index];
        String time = "00:00";
        
        Calendar cal = Calendar.getInstance();
        if (year.indexOf(':') > 0) {
            time = year;
            year = Integer.toString(cal.get(Calendar.YEAR));
        }
            
        // put together & parse        
        StringBuffer stamp = new StringBuffer(month);
        stamp.append('-');
        stamp.append(day);
        stamp.append('-');
        stamp.append(year);
        stamp.append('-');
        stamp.append(time);
        
        Date lastModified = null;
        try {
            lastModified = dateFormatter.parse(stamp.toString());
        }
        catch (ParseException ex) {
            if (!ignoreDateParseErrors)
                throw new DateParseException(ex.getMessage());
        }
                  
        // can't be in the future - must be the previous year
        // add 2 days just to allow for different time zones
        cal.add(Calendar.DATE, 2);
        if (lastModified != null && lastModified.after(cal.getTime())) {
            cal.setTime(lastModified);
            cal.add(Calendar.YEAR, -1);
            lastModified = cal.getTime();
        }
                
        // we've got to find the starting point of the name. 
        String name = raw.trim();
        for (int i = 0; i < MIN_FIELD_COUNT-1; i++) {
            int pos = name.indexOf(' ');
            if (pos > 0)
            {
                name = name.substring(pos).trim();
            }     
            else 
            {
                name = null;
                log.debug("Failed to extract filename");
                break;
            }     
        }
        
        // trim off './' if it is there
        if (name.startsWith(CURRENT_DIR_PREFIX))
            name = name.substring(CURRENT_DIR_PREFIX.length());
         
        FTPFile file = new FTPFile(raw, name, size, isDir, lastModified);
        file.setOwner(owner);
        file.setPermissions(permissions);
        return file;
    }
}
