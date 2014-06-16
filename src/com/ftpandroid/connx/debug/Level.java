package com.ftpandroid.connx.debug;

public class Level {

	final static int OFF_INT = -1;
	final private static String OFF_STR = "OFF";
	final static int FATAL_INT = 0;
	final private static String FATAL_STR = "FATAL";
	final static int ERROR_INT = 1;
	final private static String ERROR_STR = "ERROR";
	final static int WARN_INT = 2;
	final private static String WARN_STR = "WARN";
	final static int INFO_INT = 3;
	final private static String INFO_STR = "INFO";
	final static int DEBUG_INT = 4;
	final private static String DEBUG_STR = "DEBUG";
	final static int ALL_INT = 10;
	final private static String ALL_STR = "ALL";
	final static int LEVEL_COUNT = 5;
	 				/***** Levek *******/
	/**
	 * OFF
	 */
	final public static Level OFF = new Level(OFF_INT, OFF_STR);
	
	/**
	 * FATAL
	 */
	final public static Level FATAL = new Level(FATAL_INT, FATAL_STR);
	
	/**
	 * OFF
	 */
	final public static Level ERROR = new Level(ERROR_INT, ERROR_STR);
	
	final public static Level WARN = new Level(WARN_INT, WARN_STR);
	
	/**
	 * info
	 */
	final public static Level INFO = new Level(INFO_INT, INFO_STR);
	
	/**
	 * Debug
	 */
	final public static Level DEBUG = new Level(DEBUG_INT, DEBUG_STR);
	
	/**
	 * ALL
	 */
	final public static Level ALL = new Level(ALL_INT, ALL_STR);
	
	private int level = OFF_INT;
	
	private String string;
	
	private Level(int level, String string){
		this.level = level;
		this.string = string;
	}
	
	public int getLevel(){return level;}
	
	/***
	 * Greater or equal to the supplied level
	 * @param l level test against
	 * @return true for if greater or equal, false for less than
	 */
	boolean isGreaterOrEqual(Level l){
		if(this.level >= l.level){
			return true;
		}return false;
	}
	
	/**
	 * Get level from supplied string
	 * @param level
	 * @return
	 */
	public static Level getLEvel(String level){
		if(OFF.toString().equalsIgnoreCase(level))
			return OFF;
		if(FATAL.toString().equalsIgnoreCase(level))
			return FATAL;
		if(ERROR.toString().equalsIgnoreCase(level))
			return ERROR;
		if(WARN.toString().equalsIgnoreCase(level))
			return WARN;
		if(INFO.toString().equalsIgnoreCase(level))
			return INFO;
		if(DEBUG.toString().equalsIgnoreCase(level))
			return DEBUG;
		if(ALL.toString().equalsIgnoreCase(level))
			return ALL;
		return null;
	}
	
	/**
	 * String representation
	 * @return string
	 */
	public String toString(){
		return string;
	}
}

