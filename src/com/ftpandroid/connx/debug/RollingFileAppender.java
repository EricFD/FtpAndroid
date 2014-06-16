package com.ftpandroid.connx.debug;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.ftpandroid.connx.debug.Logger;

public class RollingFileAppender extends FileAppender {

	private static long DEFAULT_MAXSIZE = 10 * 1024 * 1024;
	private static int CHECK_THRESHOLD_BYTES = 1024  *5;
	
	private static String LINE_SEP = System.getProperty("line.separator");
	
	private long maxFileSize = DEFAULT_MAXSIZE;
	
	private int thresholdBytesWritten = 0;
	
	private int maxSizeRollBackups = 1;
	
	public RollingFileAppender(String file, long maxFileSize) throws IOException{
		super(file);
		this.maxFileSize = maxFileSize;
		checkSizeForRollover();
		
	}
	
	public RollingFileAppender(String file) throws IOException{
		super(file);
		checkSizeForRollover();
	}
	
	public int getMaxSizeRollBackups(){ return maxSizeRollBackups;}
	
	public long getMaxFileSize(){ return maxFileSize;}
	
	public synchronized void log(String msg){
		if(!closed){
			checkForRollover();
			log.println(msg);
			log.flush();
			thresholdBytesWritten += msg.length();
		}
	}
		
		public synchronized void log(Throwable t){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			String msg = sw.toString();
			if(!closed){
				checkForRollover();
				log.println(msg);
				log.flush();
				thresholdBytesWritten += msg.length();
			}
		}
		
		private void checkForRollover(){
			if(thresholdBytesWritten < CHECK_THRESHOLD_BYTES)
				return;
			thresholdBytesWritten = 0;
			checkSizeForRollover();
		}
		
		private void checkSizeForRollover(){
			try{
				File f = new File(getFile());
				if(f.length()>maxFileSize){
					rollover();
				}
			}catch (Exception ex){
				String msg = "Failed to rollover log file :" + ex.getMessage();
				System.err.println(msg);
			}
		}
		
		private void rollover() throws IOException{
			close();
			StringBuffer msg = new StringBuffer();
			Exception ex = null;
			try{
				File master = new File(getFile());
				if(maxSizeRollBackups == 0){
					if(!master.delete())
						msg.append("Failed to delete file:"+ master.getAbsolutePath()+LINE_SEP);
				}else{
					File f = new File(getFile()+"."+maxSizeRollBackups);
					if(f.exists()){
						if(!f.delete())
							msg.append("Failed to delte file :"+f.getAbsolutePath()+ LINE_SEP);
					}
				
			 for (int i = maxSizeRollBackups - 1; i > 0; i--) {
                 f = new File(getFile() + "." + i);
                 if (f.exists()) {
                     File renamed = new File(getFile() + "." + (i + 1));
                     if (!f.renameTo(renamed))
                         msg.append("Failed to rename file: " + f.getAbsolutePath() + " to " + renamed.getAbsolutePath() + LINE_SEP);
                 }
             }
             f = new File(getFile() + ".1");
             if (!master.renameTo(f))
                 msg.append("Failed to rename file: " + master.getAbsolutePath() + " to " + f.getAbsolutePath() + LINE_SEP);
         }
     }
     catch (Exception e) {
         ex = e;
     }
     finally {
         open();
         if (ex != null) {
             msg.append("Failed to rollover log files: " + ex.getMessage() + LINE_SEP);                
         }
         if (msg.length() > 0) {
             log.println(msg.toString());
             log.flush();
             thresholdBytesWritten += msg.length();
         }
     }
 }
		
}
	

