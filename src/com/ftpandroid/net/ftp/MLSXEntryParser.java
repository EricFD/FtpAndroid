package com.ftpandroid.net.ftp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *  Parses the string returned from the MLSD or MLST command 
 *  (defined in the "Extensions to FTP" IETF draft). Just grabs
 *  the basic fields, as most servers don't support anything else.
 *
 */
public class MLSXEntryParser extends FTPFileParser {

 
    /**
     * Fields that are possible
     */
    final private static String SIZE = "Size";
    
    final private static String MODIFY = "Modify";
    
    final private static String CREATE = "Create";
    
    final private static String TYPE = "Type";
    
    final private static String UNIQUE = "Unique";
    
    final private static String PERM = "Perm";
    
    final private static String LANG = "Lang";
    
    final private static String MEDIA_TYPE = "Media-Type";
    
    final private static String CHARSET = "CharSet";
        
    /**
     * File type constants
     */
    final private static String FILE_TYPE  = "file"; // a file entry
    final private static String LISTED_DIR_TYPE  = "cdir"; // the listed directory
    final private static String PARENT_DIR_TYPE = "pdir"; // a parent directory
    final private static String SUB_DIR_TYPE = "dir";   // a directory or sub-directory
    
    /**
     *  Format to interpret MTDM timestamp
     */
    private SimpleDateFormat tsFormat1 =
        new SimpleDateFormat("yyyyMMddHHmmss");
    
    /**
     *  Format to interpret MTDM timestamp
     */
    private SimpleDateFormat tsFormat2 =
        new SimpleDateFormat("yyyyMMddHHmmss.SSS");
    
    /**
     *  Instance initializer. Sets formatters to GMT.
     */
    {
        tsFormat1.setTimeZone(TimeZone.getTimeZone("GMT"));
        tsFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));
    }  
            
    /**
     * Parse server supplied string that is returned from MLST/D
     * 
     * @param raw   raw string to parse
     */
    public FTPFile parse(String raw) throws ParseException {
        String[] fields = split(raw, ';');
        String path = null;
        FTPFile ftpFile = new FTPFile(raw);
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (i+1 == fields.length) {
                path = field.trim();  // last field is the path
                String name = path;
                int pos = name.lastIndexOf('/');
                if (pos >= 0) {
                    path = name.substring(0, pos);
                    ftpFile.setPath(path);
                    name = name.substring(++pos);
                }
                ftpFile.setName(name);                
            }
            else {
                int pos = field.indexOf('=');
                if (pos > 0) {
                    String name = field.substring(0, pos);
                    String value = field.substring(++pos);
                    if (name.equalsIgnoreCase(SIZE)) {
                        ftpFile.setSize(parseSize(value));
                    }
                    else if (name.equalsIgnoreCase(MODIFY)) {
                        ftpFile.setLastModified(parseDate(value));
                    }
                    else if (name.equalsIgnoreCase(TYPE)) {
                        if (value.equalsIgnoreCase(FILE_TYPE))
                            ftpFile.setDir(false);
                        else // assume a dir if not a file for the moment
                            ftpFile.setDir(true);
                    }
                    else if (name.equalsIgnoreCase(PERM)) {
                        ftpFile.setPermissions(value);
                    }
                    else if (name.equalsIgnoreCase(CREATE)) {
                        ftpFile.setCreated(parseDate(value));
                    }
                }
            }
        }
        return ftpFile;
    }
    
    /**
     * Parse the size string
     * 
     * @param value   string containing size value
     * @return
     * @throws ParseException
     */
    private long parseSize(String value) throws ParseException {
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ex) {
            throw new ParseException("Failed to parse size: " + value, 0);
        }
    }
    
    /**
     * Parse the date string. In format YYYYMMDDHHMMSS.sss
     * 
     * @param value     string to parse
     * @return Date from string
     * @throws ParseException
     */
    private Date parseDate(String value) throws ParseException {
        try {
            return tsFormat1.parse(value);
        }
        catch (ParseException ex) {
            return tsFormat2.parse(value);
        }
    }
    
    /**
     * Set the locale for date parsing of listings. As
     * the timestamps follow a standard without names of months,
     * this is not used in this parser.
     * 
     * @param locale    locale to set
     */
    public void setLocale(Locale locale) {
        // not required
    }  
}
