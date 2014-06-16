package com.ftpandroid.net.ftp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import com.ftpandroid.connx.debug.Logger;

/**
 * MVS Folder Listing Parser
 * The purpose of this parser is to be able handle responses
 * from an MVS z/OS mainframe FTP server.
 *
 */

public class MVSFileParser extends FTPFileParser {

  private static Logger log = Logger.getLogger("MVSFileParser");

  /*** DO NOT TRANSLATE THESE ***/
  private static final String PARSER_KEY = "MVS"; //$NON-NLS-1$
  private static final String HEADER_VOLUME = "Volume"; //$NON-NLS-1$
  private static final String HEADER_NAME = "Name"; //$NON-NLS-1$
  private static final String LINE_TYPE_ARCIVE = "ARCIVE";  //$NON-NLS-1$  *** NOT MISSPELLED ***
  private static final String ENTRY_FILE_TYPE = "PS"; //$NON-NLS-1$
  private static final String LINE_TYPE_MIGRATED = "Migrated";  //$NON-NLS-1$
  /*** ^^^ DO NOT TRANSLATE THESE ^^^ ***/
  private static final int FOLDER_HEADER_TYPE_IDX = 0;
  private static final int FOLDER_LISTING_LENGTH_NORMAL = 10;
  private static final int FOLDER_LISTING_LENGTH_ARCIVE = 8;
  
  
  private String dateFormatString; // String used to parse file dates
  private String alternateFormatString; // Alternate form of date string in case month/day are switched
  private SimpleDateFormat dateFormat; // The DateFormat object to parse dates with
  private SimpleDateFormat dateTimeFormat; // The DateFormat object to parse "last modified" date+time with.
  
  private boolean partitionedDataset = false; // If true, It's a partitioned data set listing
  
  /************************ Abstract Class Implementations *************************/
  

  public boolean isValidFormat(String[] listing) {

    log.debug("Checking MVS Parser for Valid Format..."); 
    if (listing.length > 0) {
      String[] header = splitMVSLine(listing[0]); // first line of MVS listings is a header
      if ( (header.length == FOLDER_LISTING_LENGTH_NORMAL) || (header.length == FOLDER_LISTING_LENGTH_ARCIVE) ) {
        if (header[FOLDER_HEADER_TYPE_IDX].equals(HEADER_VOLUME) ) { 
          this.partitionedDataset = false; // This is a directory listing, not PDS listing
          log.info("Detected MVS Directory Listing Format"); 
          return isValidDirectoryFormat(listing);
        } 
        else if (header[FOLDER_HEADER_TYPE_IDX].equals(HEADER_NAME)) { 
          this.partitionedDataset = true; // Suspect PDS listing.
          log.info("Detected MVS Partitioned Dataset Listing Format"); 
          return isValidPDSFormat(listing);
        }
      }
    }
    return false;
  }

  
  
  /**
   * This parses an individual line from the directory listing.
   * 
   */
  public FTPFile parse(String raw) throws ParseException {
    String[] aLine = splitMVSLine(raw);
    FTPFile rtn = null;
    if (this.partitionedDataset) {
      rtn = parsePDSLine(aLine, raw); // where the real work is done.
    } else { // Folder List
      rtn = parseFolder(aLine, raw);
    }
    return rtn;
  }
  
  /**
   * Could in theory be used to figure out the format of the date/time
   * except that I'd need time on the server to see if this actually 
   * works that way. For now, we ignore the locale and try to figure
   * out the date format ourselves.
   */
  public void setLocale(Locale arg0) {
    //
    log.debug("Ignoring the set locale."); //$NON-NLS-1$
  }

  /**
   * Returns parser name. By extensibility oversight in the third-party library we use,
   * this isn't used to match the on the server (unfortunately).
   */
  public String toString() {
    return PARSER_KEY;
  }

  /************************ Worker Methods *************************/

  /**
   * Parses a Partitioned Dataset Entry, and returns an FTPFile object. 
   * @param aLine Split line
   * @param raw Unparsed raw string
   * @return FTPFile unless it's the header row.
   * @throws ParseException
   */
  protected FTPFile parsePDSLine(String[] aLine, String raw) throws ParseException {
    FTPFile rtn = null;
    if (aLine[0].equals(HEADER_NAME)) {
      log.debug("Skipping header"); 
      return null;
    }
    rtn = new FTPFile(raw);
    rtn.setName(aLine[0]);
    if (dateTimeFormat == null) {
      dateTimeFormat = new SimpleDateFormat(dateFormatString + " HH:mm"); 
    }
    rtn.setCreated(dateFormat.parse(aLine[2]));
    String modDateTime = aLine[3] + ' ' + aLine[4];
    rtn.setLastModified(dateTimeFormat.parse(modDateTime));
    rtn.setDir(false);
    return rtn;
  }

  /**
   * Parses a line from a folder listing.
   * 
   * Note: Returns NULL if it's the header line, if it is ARCIVE or Migrated,
   * if the record format doesn't start with 'F' or 'V', and if the dsorg doesn't
   * start with 'P'.
   * 
   * @param aLine Line split apart
   * @param raw Raw line from the transport
   * @return FTPFile for the line unless it is expressly excluded
   */
  protected FTPFile parseFolder(String[] aLine, String raw) {
    if (aLine[0].equals(HEADER_VOLUME)) {
      log.debug("Skipping header"); 
      return null;
    }
    // Directory format
    if (aLine[0].equals(LINE_TYPE_ARCIVE)) { 
      log.debug("Skipping ARCIVE file - not supported"); 
      return null;
    }
    if (aLine[0].equals(LINE_TYPE_MIGRATED)) { 
      log.debug("Skipping Migrated file - not supported"); 
      return null;
    }
    if (aLine[5].charAt(0) != 'F' && aLine[5].charAt(0) != 'V') {
      log.debug("Skipping unsupported recf - needs to start with F or V"); 
      return null;
    }
    if (aLine[8].charAt(0) != 'P') { 
      log.debug("Skipping unsupported organization - must be PS, PO, or PO-E");
      return null;
    }
    // OK, I think I can handle this.
    FTPFile rtn = new FTPFile(raw);
    rtn.setName(aLine[9]);
    // Fake out dates - these are all newly created files / folders
    rtn.setCreated(new Date());
    rtn.setLastModified(new Date());
    if (aLine[8].equals(ENTRY_FILE_TYPE)) {
      log.debug("Found a file (PS) - " + aLine[9]); 
      // This is a file...
      rtn.setDir(false);
      long l = -1;
      try {
        l = Long.parseLong(aLine[4]);
      } catch (Exception ignored) { }
      rtn.setSize(l);
    } 
    else {
      log.debug("Found a folder (PO/PO-E) - " + aLine[9]); //$NON-NLS-1$
      rtn.setDir(true);
    }
    // Left this code here in case last time accessed becomes important.
    // For directory items, this is just the last time accessed
    // Date dt = dateFormat.parse(aLine[2]);
    // 
    return rtn;
  }
  
  /************************ Utility Methods *************************/

  /**
   * This is a split + trim function. The String.split method
   * doesn't work well if there are a multiple contiguous white-space
   * characters. StringTokenizer handles this very well. This should
   * never fail to return an array, even if the array is empty. In
   * other words, this should never return null.
   * @param raw The string to tokenize from the MainFrame
   * @return String array of all the elements from the parse.
   */
  protected String[] splitMVSLine(String raw) {
    if (raw == null) {
      return new String[] {};
    }
    StringTokenizer st = new StringTokenizer(raw);
    String[] rtn = new String[st.countTokens()];
    int i = 0;
    while (st.hasMoreTokens()) {
      String nextToken = st.nextToken();
      rtn[i] = nextToken.trim();
      i++;
    }
    return rtn;
  }


  /**
   * Returns true if this seems to be a recognized MVS folder (not PDS) listing.
   * 
   * @param listing
   * @return true if by all appearances this is a listing of an MVS folder
   */
  protected boolean isValidDirectoryFormat(String[] listing) {
    String[] aLine;
    for (int i=1; i<listing.length; i++) {
      aLine = splitMVSLine(listing[i]);
      if ( (aLine.length == 2) && (aLine[0].equals(LINE_TYPE_MIGRATED))) {
        log.debug("Detected a migrated entry"); //$NON-NLS-1$
      } 
      else if (aLine.length != 10 && ( !aLine[0].equals(LINE_TYPE_ARCIVE))) { // 10 = regular, ARCIVE=on tape
        log.error("Invalid Directory Line: " + listing[i]); //$NON-NLS-1$
        return false;
      }
      if (dateFormatString != null) {
        // validate date
        if (!checkDateFormat(aLine[2])) {
          return false;
        }
      } else {
        if (aLine.length == 10) {
          // Try to parse the date.
          guessDateFormat(aLine[2]);
        }
      }
    }
    return true;
  }

  /**
   * Returns true if this seems to be a recognized MVS PDS listing (not folder).
   * 
   * @param listing
   * @return true if by all appearances this is a listing of the contents of a PDS
   */
  protected boolean isValidPDSFormat(String[] listing) {
    String[] aLine;
    for (int i=1; i<listing.length; i++) {
      aLine = splitMVSLine(listing[i]);
      if (aLine.length != 9) { // 9 because there are two fields for changed...
        log.error("Invalid PDS Line: " + listing[i]); 
        return false;
      }
      if (dateFormatString != null) {
        if (!checkDateFormat(aLine[3])) {
          return false;
        }
      } else {
        guessDateFormat(aLine[2]);
      }
    }
    return true;
  }
  

  protected boolean checkDateFormat(String dateStr) {
    try {
      dateFormat.parse(dateStr);
    } 
    catch (ParseException ex) {
      log.debug("Date parse exception - probably swapped day/month"); //$NON-NLS-1$
      if ( (alternateFormatString != null) ) {
        log.debug("Choosing alternate format"); //$NON-NLS-1$
        dateFormatString = alternateFormatString;
        dateFormat = new SimpleDateFormat(dateFormatString);
        alternateFormatString = null;
        try {
          dateFormat.parse(dateStr);
        } 
        catch (ParseException ex2) {
          return false;
        }
      } 
      else {
        log.error("FAIL - no alternate to fall back on. Date cannot be parsed - " + dateStr);
        return false;
      }
    }
    return true;
  }

  
  protected void guessDateFormat(String dateStr) {
    log.debug("Guessing the date format..."); 
    String[] dateSplit = dateStr.split("/"); 
    String yrFmt = null;
    int yrPos = -1;
    int dayPos = -1;
    // quick look for either yyyy/xx/xx or xx/xx/yyyy
    for (int i=0; i<dateSplit.length; i++) {
      int aDigit = Integer.parseInt(dateSplit[i]); 
      if (dateSplit[i].length() == 4) {
        yrFmt = "yyyy"; //$NON-NLS-1$
        yrPos = i;
      } 
      else if (aDigit>31) {
        // found 2-digit year
        yrFmt = "yy"; //$NON-NLS-1$
        yrPos = i;
      } 
      else if (aDigit > 12) {
        // definitely found a # <=31,
        dayPos = i;
      }
    }
    if (yrFmt != null) {
      StringBuffer fmt = new StringBuffer();
      if (dayPos >=0) {
        // OK, we know everything.
        String[] tmp = new String[3];
        tmp[yrPos] = yrFmt;
        tmp[dayPos] = "dd"; //$NON-NLS-1$
        for (int i=0; i<tmp.length; i++) {
          fmt.append(i>0?"/":""); //$NON-NLS-1$ //$NON-NLS-2$
          fmt.append(tmp[i] == null ? "MM":tmp[i]); //$NON-NLS-1$
        }
        log.debug("Easy one - found obvious year, and obvious day"); //$NON-NLS-1$
      } 
      else {
        // OK, we have something like 2010/01/01 - I can't
        // tell month from day. So, we'll guess. If it doesn't work on a later
        // date, we'll flip it (the alternate).
        
        StringBuffer altFmt = new StringBuffer();
        if (yrPos == 0) {
          fmt.append(yrFmt).append("/MM/dd"); //$NON-NLS-1$
          altFmt.append(yrFmt).append("/dd/MM"); //$NON-NLS-1$
        } 
        else {
          fmt.append("MM/dd/").append(yrFmt); //$NON-NLS-1$
          altFmt.append("dd/MM/").append(yrFmt); //$NON-NLS-1$
        }
        this.alternateFormatString = altFmt.toString();
        log.debug("Ambiguous one - found obvious year, but day/month could be wrong so created alternate"); //$NON-NLS-1$
      }
      this.dateFormatString = fmt.toString();
      this.dateFormat = new SimpleDateFormat(dateFormatString);
      log.debug("Decided on " + this.dateFormatString); //$NON-NLS-1$
      try {
        dateFormat.parse(dateStr);
      } 
      catch (ParseException ex) {
        log.debug("Parse failed - Invalid Date: " + dateStr); //$NON-NLS-1$
      }
    } 
    else {
      // looks like something like 01/02/05 - where's the year?
      log.debug("Could not figure out year from this date format"); //$NON-NLS-1$
      return;
    }
    
  }

  
  /*************************** Getters and Setters **************************/ 
  
  /**
   * @return true if listing is a PDS
   */
  public boolean isPartitionedDataset() {
    return this.partitionedDataset;
  }

  /**
   * Returns the date format string in use for parsing
   * date in the listing.
   * @return string format
   */
  public String getDateFormatString() {
    return this.dateFormatString;
  }
  
  /**
   * Provides ability to pre-specify the format
   * that the parser will use to parse dates.
   * @param value the string to set.
   */
  public void setDateFormatString(String value) {
    this.dateFormatString = value;
  }
  
}