package com.ftpandroid.net.ftp;

final public class FileNotFoundStrings extends ServerStrings {

    /**
     * Server string indicating file not found
     */
    final public static String FILE_NOT_FOUND = "NOT FOUND";
    
    /**
     * Server string indicating file not found
     */
    final public static String NO_SUCH_FILE = "NO SUCH FILE";
    
    /**
     * Server string indicating file not found 
     */
    final public static String CANNOT_FIND_THE_FILE = "CANNOT FIND THE FILE";
    
    /**
     * Server string indicating file not found 
     */
    final public static String CANNOT_FIND = "CANNOT FIND";
    
    /**
     * Server string indicating file not found
     */
    final public static String FAILED_TO_OPEN_FILE = "FAILED TO OPEN FILE";
    
    /**
     * Server string indicating file not found
     */
    final public static String COULD_NOT_GET_FILE = "COULD NOT GET FILE";
    
    /**
     * Server string indicating file not found
     */
    final public static String DOES_NOT_EXIST = "DOES NOT EXIST";
    
    /**
     * Server string indicating file not found
     */
    final public static String NOT_REGULAR_FILE = "NOT A REGULAR FILE";

    
    /**
     * Constructor. Adds the fragments to match on
     */
    public FileNotFoundStrings() {
        add(FILE_NOT_FOUND);
        add(NO_SUCH_FILE);
        add(CANNOT_FIND_THE_FILE);
        add(FAILED_TO_OPEN_FILE);
        add(COULD_NOT_GET_FILE);
        add(DOES_NOT_EXIST);
        add(NOT_REGULAR_FILE);            
        add(CANNOT_FIND);
    }

}
