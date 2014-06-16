package com.ftpandroid.net.ftp;

import java.text.ParseException;
import java.util.Locale;

/**
 *  Root class of all file parsers
 *
 *  @author      Eric
 */
abstract public class FTPFileParser {
    
    /**
     * Maximum number of fields in raw string
     */
    private final static int MAX_FIELDS = 100;
    
    /**
     * Ignore date parsing errors
     */
    protected boolean ignoreDateParseErrors = false;
    
    /**
     * Parse server supplied string
     * 
     * @param raw   raw string to parse
     */
    abstract public FTPFile parse(String raw) throws ParseException;
    
    /**
     * Set the locale for date parsing of listings
     * 
     * @param locale    locale to set
     */
    abstract public void setLocale(Locale locale);
    
    /**
     * Ignore date parse errors
     * 
     * @param ignore
     */
    public void setIgnoreDateParseErrors(boolean ignore) {
        this.ignoreDateParseErrors = ignore;
    }
    
    /**
     * Valid format for this parser
     * 
     * @param listing   listing to test
     * @return true if valid
     */
    public boolean isValidFormat(String[] listing) {
        return false;
    }
    
    /**
     * Does this parser parse multiple lines to get one listing?
     * 
     * @return  
     */
    public boolean isMultiLine() {
        return false;
    }
    
    /**
     * Trim the start of the supplied string
     * 
     * @param str   string to trim
     * @return string trimmed of whitespace at the start
     */
    protected String trimStart(String str) {
        StringBuffer buf = new StringBuffer();
        boolean found = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!found & Character.isWhitespace(ch))
                continue;
            found = true;
            buf.append(ch);
        }
        return buf.toString();
    }
      
    /**
     * Splits string consisting of fields separated by
     * whitespace into an array of strings. Yes, we could
     * use String.split() but this would restrict us to 1.4+
     * 
     * @param str   string to split
     * @return array of fields
     */
    protected String[] split(String str) {
        return split(str, new WhitespaceSplitter());
    }
    
    /**
     * Splits string consisting of fields separated by
     * whitespace into an array of strings. Yes, we could
     * use String.split() but this would restrict us to 1.4+
     * 
     * @param str   string to split
     * @return array of fields
     */
    protected String[] split(String str, char token) {
        return split(str, new CharSplitter(token));
    }
    
    /**
     * Splits string consisting of fields separated by
     * whitespace into an array of strings. Yes, we could
     * use String.split() but this would restrict us to 1.4+
     * 
     * @param str   string to split
     * @return array of fields
     */
    protected String[] split(String str, Splitter splitter) {
        String[] fields = new String[MAX_FIELDS];
        int pos = 0;
        StringBuffer field = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!splitter.isSeparator(ch))
                field.append(ch);
            else {
                if (field.length()> 0) {
                    fields[pos++] = field.toString();
                    field.setLength(0);
                }
            }
        }
        // pick up last field
        if (field.length() > 0) {
            fields[pos++] = field.toString();
        }
        String[] result = new String[pos];
        System.arraycopy(fields, 0, result, 0, pos);
        return result;
    }
    
    
    interface Splitter {
        boolean isSeparator(char ch); 
    }
    
    class CharSplitter implements Splitter {
        private char token;

        CharSplitter(char token) {
            this.token = token;
        }

        public boolean isSeparator(char ch) {
            if (ch == token)
                return true;
            return false;
        }
        
    }
    
    class WhitespaceSplitter implements Splitter {
        
        public boolean isSeparator(char ch) {
            if (Character.isWhitespace(ch))
                return true;
            return false;
        }
    }
}
