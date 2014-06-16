package com.ftpandroid.connx.debug;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ftpandroid.*;

public class Logger {

	private static Level globalLevel;
	
	private static boolean logThreadNames = false;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
	
	private static Hashtable loggers = new Hashtable(10);
	
	private static Vector appenders = new Vector(2);
	
	private boolean useLog4j = false;
	
	private boolean logThreadName = false;
	
	private Date ts = new Date();
	
	private String claps;
	
	private Method [][] logMethods = null;
	
	private Method toLevelMethod = null;
	
	private Method isEnabledForMethod = null;
	
	private Object logget = null;
	
	private Object[] argsPlain = new Object[1];
	
	private Object[] argsThrowable = new Object[2];
	
	private static String prefix;
	
	static{
		String level = Level.OFF.toString();
		try{
			level = System.getProperty("FtpAndroid.log.level", Level.OFF.toString());
		}catch(SecurityException ex){
			System.out.println("Could not read propety log.level due to security permissions");
		}
		try{
			prefix = System.getProperty("FTPANDROID.log.prefix");
			if(prefix == null)
				prefix = "";
		}catch(Throwable t){
			System.out.println("Could not read property FTPANdoid.log.prefix due to security permissions");
			prefix = "";
		}
		globalLevel = Level.getLevel(level);
		if(globalLevel == null)
			globalLevel = Level.OFF;
	}
	
	private Logger(String claps, boolean uselog4j){
		this.claps = claps;
		this.useLog4j = uselog4j;
		if(uselog4j)
			setupLog4j();
	}
	
	private synchronized void setupLog4j{
		logMethods = new Method[Level.LEVEL_COUNT][2];
		try{
			Class log4jLogger = Class.forName("org.apache.log4j.Logger");
			Class log4Level = Class.forName("org.apache.log4j.Level");
			Class log4Prioriry = Class.forName("org.apache.log4j.Priority");
			
			Class[] args = {String.class};
			Method getLogger = log4jLogger.getMethod("getLoger", args);
			Object[] invokeArgs = {claps};
			logger = getLogger.invoke(null, invokeArgs);
			
			  Class[] plainArgs = {Object.class};
	            Class[] throwableArgs = {Object.class,Throwable.class};
	            logMethods[Level.FATAL_INT][0] = log4jLogger.getMethod("fatal", plainArgs);
	            logMethods[Level.FATAL_INT][1] = log4jLogger.getMethod("fatal", throwableArgs);
	            logMethods[Level.ERROR_INT][0] = log4jLogger.getMethod("error", plainArgs);
	            logMethods[Level.ERROR_INT][1] = log4jLogger.getMethod("error", throwableArgs);
	            logMethods[Level.WARN_INT][0] = log4jLogger.getMethod("warn", plainArgs);
	            logMethods[Level.WARN_INT][1] = log4jLogger.getMethod("warn", throwableArgs);
	            logMethods[Level.INFO_INT][0] = log4jLogger.getMethod("info", plainArgs);
	            logMethods[Level.INFO_INT][1] = log4jLogger.getMethod("info", throwableArgs);
	            logMethods[Level.DEBUG_INT][0] = log4jLogger.getMethod("debug", plainArgs);
	            logMethods[Level.DEBUG_INT][1] = log4jLogger.getMethod("debug", throwableArgs);
	            
	            // get the toLevel and isEnabledFor methods
	            Class[] toLevelArgs = {String.class};
	            toLevelMethod = log4jLevel.getMethod("toLevel", toLevelArgs);
	            Class[] isEnabledForArgs = {log4jPriority};
	            isEnabledForMethod = log4jLogger.getMethod("isEnabledFor", isEnabledForArgs);
	        } 
	        catch (Exception ex) {
	            useLog4j = false;
	            error("Failed to initialize log4j logging", ex);
	        } 
	    }
	/**
     * Returns the logging level for all loggers.
     * 
     * @return current logging level.
     */
    public static synchronized Level getLevel() {
    		return globalLevel;
    }
    
    /**
    * Set all loggers to this level
    * 
    * @param level  new level
    */
   public static synchronized void setLevel(Level level) {
       globalLevel = level;
   }
   
   public static Logger getLogger(Class claps) {
       return getLogger(claps.getName());
   }
   
   /**
    * Get a logger for the supplied class
    * 
    * @param clazz    full class name
    * @return  logger for class
    */
   public static synchronized Logger getLogger(String claps) {
       claps = prefix + claps;
       Logger logger = (Logger)loggers.get(claps);
       if (logger == null) {
           boolean useLog4j = false;
           try {
               String log4j = System.getProperty("edtftp.log.log4j");
               if (log4j != null && log4j.equalsIgnoreCase("true")) {
                   useLog4j = true;
               }
           }
           catch (SecurityException ex) {
               System.out.println("Could not read property 'edtftp.log.log4j' due to security permissions");
           }
           logger = new Logger(claps, useLog4j);
           loggers.put(claps, logger);
       }
       return logger;
   }
   
   /**
    * Add an appender to our list
    * 
    * @param newAppender
    */
   public static synchronized void addAppender(Appender newAppender) {
   	if (!appenders.contains(newAppender))
   		appenders.addElement(newAppender);
   }
   
   /**
    * Add a file-appender to our list
    * 
    * @param fileName Path of file.
    * @throws IOException
    */
   public static synchronized void addFileAppender(String fileName) throws IOException {
   	addAppender(new FileAppender(fileName));
   }
   
  public static synchronized void addStandardOutputAppender() {
  	addAppender(new StandardOutputAppender());
  }
  
  /**
   * Remove an appender to from list
   * 
   * @param appender
   */
  public static synchronized void removeAppender(Appender appender) {
  	appender.close();
      appenders.removeElement(appender);
  }
 
  /**
   * Clear all appenders
   */
  public static synchronized void clearAppenders() {
      appenders.removeAllElements();
  }
  
  /**
   * Close all appenders
   */
  public static synchronized void shutdown() {
      for (int i = 0; i < appenders.size(); i++) {
          Appender a = (Appender)appenders.elementAt(i);
          a.close();
      }        
  }
  
  /**
   * Set global flag for logging thread names as part of the logger names.
   * 
   * @param logThreadNames true if logging thread names, false otherwise
   */
  public static synchronized void logThreadNames(boolean logThreadNames) {
      Logger.logThreadNames = logThreadNames;
  }
  
  /**
   * Set flag for logging thread names as part of the logger names for this instance
   * of the logger.
   * 
   * @param logThreadName true if logging thread names, false otherwise
   */
  public synchronized void logThreadName(boolean logThreadName) {
      this.logThreadName = logThreadName;
  }
  
  /**
   * Log a message 
   * 
   * @param level     log level
   * @param message   message to log
   * @param t         throwable object
   */
  public synchronized void log(Level level, String message, Throwable t) {
  	if (isEnabledFor(level))
  	{
	        if (useLog4j)
	            log4jLog(level, message, t);
	        else
	            ourLog(level, message, t);
  	}
  }
  
  /**
   * Calls log4j's isEnabledFor method.
   * 
   * @param level logging level to check
   * @return true if logging is enabled
   */
  private boolean log4jIsEnabledFor(Level level)
  {
      if (level.equals(Level.ALL)) // log4j doesn't have an 'ALL' level
          level = Level.DEBUG;

  	try
  	{
  		// convert the level to a Log4j Level object
	    	Object[] toLevelArgs = new Object[] { level.toString() };
	    	Object l = toLevelMethod.invoke(null, toLevelArgs);
	
	    	// call isEnabled
	    	Object[] isEnabledArgs = new Object[] { l };
	    	Object isEnabled = isEnabledForMethod.invoke(logger, isEnabledArgs);
	    	
	    	return ((Boolean)isEnabled).booleanValue();
      } 
      catch (Exception ex) { // there's a few, we don't care what they are
          ourLog(Level.ERROR, "Failed to invoke log4j toLevel/isEnabledFor method", ex);
          useLog4j = false;
          return false;
      }
  }
  
  /**
   * Log a message to log4j
   * 
   * @param level     log level
   * @param message   message to log
   * @param t         throwable object
   */
  private void log4jLog(Level level, String message, Throwable t) {
      
      if (level.equals(Level.ALL)) // log4j doesn't have an 'ALL' level
          level = Level.DEBUG;
      
      // set up arguments
      Object[] args = null;
      int pos = -1;
      if (t == null) {
          args = argsPlain;
          pos = 0;
      }
      else {
          args = argsThrowable;
          args[1] = t;
          pos = 1;
      }
      args[0] = message;
      
      // retrieve the correct method
      Method method = logMethods[level.getLevel()][pos];
      
      // and invoke the method
      try {
          method.invoke(logger, args);
      } 
      catch (Exception ex) { // there's a few, we don't care what they are
          ourLog(Level.ERROR, "Failed to invoke log4j logging method", ex);
          ourLog(level, message, t);
          useLog4j = false;
      }
  }

  /**
   * Log a message to our logging system
   * 
   * @param level     log level
   * @param message   message to log
   * @param t         throwable object
   */
  private void ourLog(Level level, String message, Throwable t) {
      ts.setTime(System.currentTimeMillis());
      String stamp = format.format(ts);
      StringBuffer buf = new StringBuffer(level.toString());
      buf.append(" [");
      if (logThreadNames || logThreadName)
          buf.append(Thread.currentThread().getName()).append("_");
      buf.append(claps).append("] ").append(stamp).
      append(" : ").append(message);
      if (t != null) {
          buf.append(" : ").append(t.getMessage());
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          pw.println();
          t.printStackTrace(pw);
          pw.println();
          buf.append(sw.toString());
      }
      if (appenders.size() == 0) { // by default to stdout
          System.out.println(buf.toString());
          while (t != null) {
              t.printStackTrace(System.out);
              if (t instanceof BaseIOException) {
              	t = ((BaseIOException)t).getInnerThrowable();
              	if (t!=null)
              		System.out.println("CAUSED BY:");
              }
              else
              	t = null;
          }
      }
      else {
          for (int i = 0; i < appenders.size(); i++) {
              Appender a = (Appender)appenders.elementAt(i);
              a.log(buf.toString());
              while (t != null) {
                  a.log(t);
                  if (t instanceof BaseIOException) {
                  	t = ((BaseIOException)t).getInnerThrowable();
                  	if (t!=null)
                  		a.log("CAUSED BY:");
                  }
                  else
                  	t = null;
              }
          }
      }
  }
      
  /**
   * Log an info level message
   * 
   * @param message   message to log
   */
  public void info(String message)  {
      log(Level.INFO, message, null); 
  }
  
  /**
   * Log an info level message
   * 
   * @param message   message to log
   * @param t         throwable object
   */
  public void info(String message, Throwable t)  {
      log(Level.INFO, message, t); 
  }

  /**
   * Log a warning level message
   * 
   * @param message   message to log
   */
  public void warn(String message)  {
      log(Level.WARN, message, null); 
  }
  
  /**
   * Log a warning level message
   * 
   * @param message   message to log
   * @param t         throwable object
   */
  public void warn(String message, Throwable t)  {
      log(Level.WARN, message, t); 
  }
  
  /**
   * Log an error level message
   * 
   * @param message   message to log
   */
  public void error(String message)  {
      log(Level.ERROR, message, null);   
  }
  
  /**
   * Log an error level message
   * 
   * @param message   message to log
   * @param t         throwable object
   */
  public void error(String message, Throwable t)  {
      log(Level.ERROR, message, t);   
  } 
  
  /**
   * Log a fatal level message
   * 
   * @param message   message to log
   */
  public void fatal(String message)  {
      log(Level.FATAL, message, null); 
  }

  /**
   * Log a fatal level message
   * 
   * @param message   message to log
    * @param t         throwable object
  */
  public void fatal(String message, Throwable t)  {
      log(Level.FATAL, message, t); 
  }
  
  /**
   * Log a debug level message
   * 
   * @param message   message to log
   */
  public void debug(String message)  {
      log(Level.DEBUG, message, null); 
  }
  
	private static String hex[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"a", "b", "c", "d", "e", "f" };
  
  /**
   * Log a debug level message
   * 
   * @param message   message to log
   */
  public void debug(String message, byte[] data)  {
      log(Level.DEBUG, message, null); 
		int i;

		StringBuffer hexStr = new StringBuffer();
		StringBuffer charStr = new StringBuffer();
		for (i = 0; i < data.length; i++) {
			byte b = data[i];
			if ((i > 0) && ((i % 12) == 0)) {
				log(Level.DEBUG, hexStr.toString() + "  " + charStr.toString(), null);
				hexStr = new StringBuffer();
				charStr = new StringBuffer();
			}

			hexStr.append(hex[(b >> 4) & 0x0f] + hex[b & 0x0f] + " ");
			charStr.append(b >= ' ' && b <= '~' ? (char)b : '?');
		}

		log(Level.DEBUG, hexStr.toString() + "  " + charStr.toString(), null);
  }
  
  /**
   * Logs by substituting in the argument at the location marked in the message
   * argument by {0}.
   * Additional MessageFormat formatting instructions may be included.  Note that
   * this method saves processing time by not building the complete string unless
   * it is necessary  saves the need for encapsulating
   * many complete logging statements in an "if (log.isDebugEnabled())" block.
   * @param message Message containing "substitution marks"
   * @param arg argument to be substituted at the marked location.
   */
  public void debug(String message, Object arg)
  {
  	if (isDebugEnabled())
  		log(Level.DEBUG, MessageFormat.format(message, new Object[]{arg}), null);
  }
  
  /**
   * Logs by substituting in the arguments at the locations marked in the message
   * argument by {#} (where # is a number).
   * Additional MessageFormat formatting instructions may be included.Note that
   * this method saves processing time by not building the complete string unless
   * it is necessary saves the need for encapsulating
   * many complete logging statements in an "if (log.isDebugEnabled())" block.
   * @param message Message containing "substitution marks"
   * @param arg0 argument to be substituted at the marked location.
   * @param arg1 argument to be substituted at the marked location.
   */
  public void debug(String message, Object arg0, Object arg1)
  {
  	if (isDebugEnabled())
  		log(Level.DEBUG, MessageFormat.format(message, new Object[]{arg0, arg1}), null);
  }
  
  /**
   * Logs by substituting in the arguments at the locations marked in the message
   * argument by {#} (where # is a number).
   * Additional MessageFormat formatting instructions may be included.Note that
   * this method saves processing time by not building the complete string unless
   * it is necessary; this saves the need for encapsulating
   * many complete logging statements in an "if (log.isDebugEnabled())" block.
   * @param message Message containing "substitution marks"
   * @param arg0 argument to be substituted at the marked location.
   * @param arg1 argument to be substituted at the marked location.
   * @param arg2 argument to be substituted at the marked location.
   */
  public void debug(String message, Object arg0, Object arg1, Object arg2)
  {
  	if (isDebugEnabled())
  		log(Level.DEBUG, MessageFormat.format(message, new Object[]{arg0, arg1, arg2}), null);
  }
  
  /**
   * Logs by substituting in the arguments at the locations marked in the message
   * argument by {#} (where # is a number).
   * Additional MessageFormat formatting instructions may be included.Note that
   * this method saves processing time by not building the complete string unless
   * it is necessary saves the need for encapsulating
   * many complete logging statements in an "if (log.isDebugEnabled())" block.
   * @param message Message containing "substitution marks"
   * @param arg0 argument to be substituted at the marked location.
   * @param arg1 argument to be substituted at the marked location.
   * @param arg2 argument to be substituted at the marked location.
   * @param arg3 argument to be substituted at the marked location.
   */
  public void debug(String message, Object arg0, Object arg1, Object arg2, Object arg3)
  {
  	if (isDebugEnabled())
  		log(Level.DEBUG, MessageFormat.format(message, new Object[]{arg0, arg1, arg2, arg3}), null);
  }
  
  /**
   * Logs by substituting in the arguments at the locations marked in the message
   * argument by {#} (where # is a number).
   * Additional MessageFormat formatting instructions may be included.Note that
   * this method saves processing time by not building the complete string unless
   * it is necessary  saves the need for encapsulating
   * many complete logging statements in an "if (log.isDebugEnabled())" block.
   * @param message Message containing "substitution marks"
   * @param arg0 argument to be substituted at the marked location.
   * @param arg1 argument to be substituted at the marked location.
   * @param arg2 argument to be substituted at the marked location.
   * @param arg3 argument to be substituted at the marked location.
   * @param arg4 argument to be substituted at the marked location.
   */
  public void debug(String message, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4)
  {
  	if (isDebugEnabled())
  		log(Level.DEBUG, MessageFormat.format(message, new Object[]{arg0, arg1, arg2, arg3, arg4}), null);
  }

  /**
   * Log a debug level message
   * 
   * @param message   message to log
   * @param t         throwable object
   */
  public void debug(String message, Throwable t)  {
      log(Level.DEBUG, message, t); 
  }
  
  /**
   * Is logging enabled for the supplied level?
   * 
   * @param level   level to test for
   * @return true   if enabled
   */
  public synchronized boolean isEnabledFor(Level level) {
  	if (useLog4j) {
  		return log4jIsEnabledFor(level);
      }
  	else 
  		return globalLevel.isGreaterOrEqual(level);
  }
  
  /**
   * Is logging enabled for the supplied level?
   * 
   * @return true if enabled
   */
  public boolean isDebugEnabled() {
      return isEnabledFor(Level.DEBUG);
  }
  
  /**
   * Is logging enabled for the supplied level?
   * 
   * @return true if enabled
   */
  public boolean isInfoEnabled()  {
      return isEnabledFor(Level.INFO);
  }
}
