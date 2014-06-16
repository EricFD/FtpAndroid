package com.ftpandroid.net.ftp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.ftpandroid.connx.debug.Logger;

/**
 *  Factory for creating FTPFile objects
 *
 *  @author      Eric
 */
public class FTPFileFactory {
    
  
    /**
     * Logging object
     */
    private static Logger log = Logger.getLogger("FTPFileFactory");

    /**
     * Windows server comparison string
     */
    final static String WINDOWS_STR = "WINDOWS";
    
    /**
     * Netware server comparison string
     */
    final static String NETWARE_STR = "NETWARE";
                  
    /**
     * UNIX server comparison string
     */
    final static String UNIX_STR = "UNIX";
    
    /**
     * VMS server comparison string
     */
    final static String VMS_STR = "VMS";
    
    /**
     * AIX server comparison string
     */
    final static String AIX_STR = "AIX";
    
    /**
     * MVS server comparison string
     */
    final static String MVS_STR = "MVS";
    
    /**
     * OS/400 server comparison string
     */
    final static String OS400_STR = "OS/400";
        
    /**
     * SYST string
     */
    private String system;
    
    /**
     * Cached windows parser
     */
    private WindowsFileParser windows = new WindowsFileParser();
    
    /**
     * Cached unix parser
     */
    private FTPFileParser unix = new UnixFileParser();
    
    /**
     * Cached vms parser
     */
    private VMSFileParser vms = new VMSFileParser();
    
    
    /**
     * Cached netware parser
     */
    private NetwareFileParser netware = new NetwareFileParser();
    
    /**
     * Cached MVS parser
     */
    private MVSFileParser mvs = new MVSFileParser();
    
   
    /**
     * Current parser
     */
    private FTPFileParser parser = null;
                   
    /**
     * User set the parser - don't detect
     */
    private boolean userSetParser = false;
    
    /**
     * Has the parser been detected?
     */
    private boolean parserDetected = false;
    
    /**
     * Locales to try out
     */
    private Locale[] localesToTry;
    
    /**
     * Index of locale to try next
     */
    private int localeIndex = 0;
    
    /**
     * All the parsers
     */
    private List parsers = new ArrayList();
    
    {
        parsers.add(unix);
        parsers.add(windows);
        parsers.add(vms);
        parsers.add(netware);
        parsers.add(mvs);
    }
     
    /**
     * Constructor
     * 
     * @param system    SYST string
     */
    public FTPFileFactory(String system) throws FTPException {
        setParser(system);
    }
    
    /**
     * Constructor. User supplied parser. Note that parser
     * rotation (in case of a ParseException) is disabled if
     * a parser is explicitly supplied
     * 
     * @param parser   the parser to use
     */
    public FTPFileFactory(FTPFileParser parser) {
        this.parser = parser;
        userSetParser = true;
    } 
    
    public String toString() {
        return parser.getClass().getName();
    }
    
    
    /**
     * Return a reference to the VMS parser being used.
     * This allows the user to set VMS-specific settings on
     * the parser.
     * 
     * @return  VMSFileParser object
     */
    public VMSFileParser getVMSParser() {
        return vms;
    }
    
    /**
     * Rather than forcing a parser (as in the constructor that accepts
     * a parser), this adds a parser to the list of those used.
     * 
     * @param parser   user supplied parser to add
     */
    public void addParser(FTPFileParser parser) {
        parsers.add(parser);
    }


    /**
     * Set the locale for date parsing of listings
     * 
     * @param locale    locale to set
     */
    public void setLocale(Locale locale) {        
        parser.setLocale(locale); // might be user supplied
        Iterator i = parsers.iterator();
        while (i.hasNext()) {
            FTPFileParser p = (FTPFileParser)i.next();
            p.setLocale(locale);
        }
    }
    
    /**
     * Set the locales to try for date parsing of listings
     * 
     * @param locales    locales to try
     */
    public void setLocales(Locale[] locales) {
        this.localesToTry = locales;
        setLocale(locales[0]); 
        localeIndex = 1;
    }
    
    /**
     * Set the remote server type
     * 
     * @param system    SYST string
     */
    private void setParser(String system) {
        parserDetected = false;
        this.system = system != null ? system.trim() : null;
        if (system.toUpperCase().startsWith(WINDOWS_STR)) {
            log.debug("Selected Windows parser");
            parser = windows;
        }
        else if (system.toUpperCase().indexOf(UNIX_STR) >= 0 ||
                system.toUpperCase().indexOf(AIX_STR) >= 0) {
            log.debug("Selected Unix parser");
            parser = unix;
        }
        else if (system.toUpperCase().indexOf(VMS_STR) >= 0) {
            log.debug("Selected VMS parser");
            parser = vms;
        }
        else if (system.toUpperCase().indexOf(NETWARE_STR) >= 0) {
            log.debug("Selected Netware parser");
            parser = netware;
        }
        else if (system.toUpperCase().indexOf(MVS_STR) >= 0) {
            log.debug("Selected MVS parser");
            parser = mvs;
        }
        else {
            parser = unix;
            log.warn("Unknown SYST '" + system + "' - defaulting to Unix parsing");
        }
    }
    
    /**
     * Reinitialize the parsers
     */
    private void reinitializeParsers() {        
        parser.setIgnoreDateParseErrors(false);
        Iterator i = parsers.iterator();
        while (i.hasNext()) {
            FTPFileParser p = (FTPFileParser)i.next();
            p.setIgnoreDateParseErrors(false);
        }
    }
    
    private void detectParser(String[] files) {
        // use the initially set parser (from SYST)
        if (parser.isValidFormat(files)) {
            log.debug("Confirmed format " + parser.toString());
            parserDetected = true;
            return;
        }   
        Iterator i = parsers.iterator();
        while (i.hasNext()) {
            FTPFileParser p = (FTPFileParser)i.next();
            if (p.isValidFormat(files)) {
                parser = p;
                log.debug("Detected format " + parser.toString());
                parserDetected = true;
                return;
            }
        }
        parser = unix;
        log.warn("Could not detect format. Using default " + parser.toString());
    }
    
    /**
     * Parse a single line of file listing
     * 
     * @param line
     * @return FTPFile
     * @throws ParseException
     */
    public FTPFile parse(String line) throws ParseException {
        if (parser.isMultiLine())
            throw new ParseException("Cannot use this method with multi-line parsers", 0);
        FTPFile file = null;
        try {
            file = parser.parse(line);
        }
        catch (DateParseException ex) {
            parser.setIgnoreDateParseErrors(true);
            file = parser.parse(line);
        }
        return file;
    }
    
    
    /**
     * Parse an array of raw file information returned from the
     * FTP server
     * 
     * @param files     array of strings
     * @return array of FTPFile objects
     */
    public FTPFile[] parse(String[] files) throws ParseException {
               
        reinitializeParsers();
        
        FTPFile[] temp = new FTPFile[files.length];
        
        // quick check if no files returned
        if (files.length == 0)
            return temp;
        
        if (!userSetParser && !parserDetected)
            detectParser(files);
                
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            
            if (files[i] == null || files[i].trim().length() == 0)
                continue;

            try {
                FTPFile file = null;
                if(parser.isMultiLine()) {
                    // vms uses more than 1 line for some file listings. We must keep going
                    // thru till we've got everything
                    StringBuffer filename = new StringBuffer(files[i]);
                    while (i+1 < files.length && files[i+1].indexOf(';') < 0) {
                        filename.append(" ").append(files[i+1]);
                        i++;
                    }
                    file = parser.parse(filename.toString());
                }
                else {
                    file = parser.parse(files[i]);
                }
                // we skip null returns - these are duff lines we know about and don't
                // really want to throw an exception
                if (file != null) {
                    temp[count++] = file;
                }
            }
            catch (DateParseException ex) {
                // try going thru the locales               
                if (localesToTry != null && localesToTry.length > localeIndex) {
                    log.info("Trying " + localesToTry[localeIndex].toString() + " locale");
                    setLocale(localesToTry[localeIndex]);
                    localeIndex++;  
                    count = 0;
                    i = -1; // account for the increment to set i back to 0
                    continue;
                }  
                // from this point start again ignoring date errors (we've tried all our locales)
                count = 0;
                i = -1; // account for the increment to set i back to 0
                parser.setIgnoreDateParseErrors(true);
                log.debug("Ignoring date parsing errors");
                continue;
            }
        }
        FTPFile[] result = new FTPFile[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }
    
    

    /**
     * Get the SYST string
     * 
     * @return the system string.
     */
    public String getSystem() {
        return system;
    }


}
