package com.ftpandroid.net.ftp;

import com.ftpandroid.connx.debug.Logger;

public class BandwidthThrottler {

    private static Logger log = Logger.getLogger("BandwidthThrottler");
    private long lastTime = 0;
    private long lastBytes = 0;
    private int thresholdBytesPerSec = -1;
    
    public BandwidthThrottler(int thresholdBytesPerSec) {
        this.thresholdBytesPerSec = thresholdBytesPerSec;
    }
    
    public void setThreshold(int thresholdBytesPerSec) {
        this.thresholdBytesPerSec = thresholdBytesPerSec;
    }
    
    public int getThreshold() {
        return thresholdBytesPerSec;
    }
    
    public void throttleTransfer(long bytesSoFar) {
        long time = System.currentTimeMillis();
        long diffBytes = bytesSoFar - lastBytes;
        long diffTime = time - lastTime;
        if (diffTime == 0)
            return;
        
        double rate = ((double)diffBytes/(double)diffTime)*1000.0;
        if (log.isDebugEnabled())
            log.debug("rate= " + rate);
        
        while (rate > thresholdBytesPerSec) {
            try {
                if (log.isDebugEnabled())
                    log.debug("Sleeping to decrease transfer rate (rate = " + rate + " bytes/s");
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {}
            diffTime = System.currentTimeMillis() - lastTime;
            rate = ((double)diffBytes/(double)diffTime)*1000.0;
        }
        lastTime = time;
        lastBytes = bytesSoFar;
    }
    
    
    public void reset() {
        lastTime = System.currentTimeMillis();
        lastBytes = 0;
    }
}
