package com.ftpandroid.net.ftp;
public class AdvancedFTPSettings {

    private ConnectionContext context;
    
    /**
     * Constructor
     * 
     * @param context  context that settings are kept in
     */
    AdvancedFTPSettings(ConnectionContext context) {
        this.context = context;
    }
    
    /**
     * Get the data transfer mode. This only applies to the FTP and FTPS
     * protocols.
     * 
     * @return the data transfer mode of the master context
     */
    public FTPConnectMode getConnectMode() {
        return context.getConnectMode();
    }
    
    /**
     * Set the data transfer mode to either active (PORT) or passive (PASV).
     * Only applicable to FTP/S. Setting applies to all subsequent transfers 
     * that are initiated.
     * 
     * @param mode              data transfer mode to set
     * @throws FTPException
     */
    public void setConnectMode(FTPConnectMode mode) throws FTPException {
        context.setConnectMode(mode);
    }
        
    /**
     * Set strict checking of FTP return codes. If strict 
     * checking is on (the default) code must exactly match the expected 
     * code. If strict checking is off, only the first digit must match.
     * 
     * @param strict    true for strict checking, false for loose checking
     */
    public void setStrictReturnCodes(boolean strict) {
        context.setStrictReturnCodes(strict);
    }
    
    /**
     * Determine if strict checking of return codes is switched on. If it is 
     * (the default), all return codes must exactly match the expected code.  
     * If strict checking is off, only the first digit must match.
     * 
     * @return  true if strict return code checking, false if non-strict.
     */
    public boolean isStrictReturnCodes() {
        return context.isStrictReturnCodes();
    }
    
    /**
     * Set the list of locales to be tried for date parsing of directory listings
     * 
     * @param locales    locales to use
     */
    public void setParserLocales(Locale[] locales) {
        context.setParserLocales(locales);
    }
    
    /**
     * Get the current parser locales used for directory listing parsing
     * 
     * @return  Locale[]
     */
    public Locale[] getParserLocales() {
        return context.getParserLocales();
    }
    
    /**
     * Get the account details to be sent at login time for use
     * in the ACCT command
     * 
     * @return account details string
     */
    public String getAccountDetails() {
        return context.getAccountDetails();
    }
    
    /**
     * Set the account details to be sent at login time for use
     * in the ACCT command. 
     * 
     * @param accountDetails  account details to set
     */
    public void setAccountDetails(String accountDetails) {
        context.setAccountDetails(accountDetails);
    }
    
    /**
     * Is automatic substitution of the remote host IP set to
     * be on for passive mode connections?
     * 
     * @return true if set on, false otherwise
     */
    public boolean isAutoPassiveIPSubstitution() {
        return context.isAutoPassiveIPSubstitution();
    }

    /**
     * Set automatic substitution of the remote host IP on if
     * in passive mode
     * 
     * @param autoPassiveIPSubstitution true if set to on, false otherwise
     */
    public void setAutoPassiveIPSubstitution(boolean autoPassiveIPSubstitution) {
        context.setAutoPassiveIPSubstitution(autoPassiveIPSubstitution);
    }
   
    
    /**
     * Force a certain range of ports to be used in active mode. This is
     * generally so that a port range can be configured in a firewall. Note
     * that if lowest == highest, a single port will be used. This works well
     * for uploads, but downloads generally require multiple ports, as most
     * servers fail to create a connection repeatedly for the same port.
     * 
     * @param lowest     Lower limit of range (should be >= 1024).
     * @param highest    Upper limit of range.(should be <= 65535)
     */
    public void setActivePortRange(int lowest, int highest) {
        context.setActivePortRange(lowest, highest);
    }
    
    /**
     * Get the lower limit of the port range for active mode.
     * 
     * @return lower limit, or -1 if not set
     */
    public int getActiveLowPort() {
        return context.getActiveLowPort();
    }

    /**
     * Get the upper limit of the port range for active mode.
     * 
     * @return upper limit, or -1 if not set
     */
    public int getActiveHighPort() {
        return context.getActiveHighPort();
    }
    
    /**
     * We can force PORT to send a fixed IP address, which can be useful with certain
     * NAT configurations. 
     * 
     * @param activeIP     IP address to force, in 192.168.1.0 form or in IPV6 form, e.g.
     *                            1080::8:800:200C:417A
     */
    public void setActiveIPAddress(String activeIP) {
        
        context.setActiveIPAddress(activeIP);
    }
    
    /**
     * The active IP address being used, or null if not used
     * @return IP address as a string or null
     */
    public String getActiveIPAddress() {
        return context.getActiveIPAddress();
    }
    
    
    /**
     * Get the retry count for retrying file transfers. Default
     * is 3 attempts.
     * 
     * @return number of times a transfer is retried
     */
    public synchronized int getRetryCount() {
        return context.getRetryCount();
    }

    /**
     * Set the retry count for retrying file transfers.
     * 
     * @param retryCount    new retry count
     */
    public void setRetryCount(int retryCount) {
        context.setRetryCount(retryCount);
    }

    /**
     * Get the retry delay between retry attempts, in milliseconds.
     * Default is 5000.
     * 
     * @return  retry delay in milliseconds
     */
    public int getRetryDelay() {
        return context.getRetryDelay();
    }

    /**
     * Set the retry delay between retry attempts, in milliseconds
     * 
     * @param  new retry delay in milliseconds
     */
    public void setRetryDelay(int retryDelay) {
        context.setRetryDelay(retryDelay);
    }

    
    /**
     * Get class that holds fragments of server messages that indicate a file was 
     * not found. New messages can be added.
     * <p>
     * The fragments are used when it is necessary to examine the message
     * returned by a server to see if it is saying a file was not found. 
     * If an FTP server is returning a different message that still clearly 
     * indicates a file was not found, use this property to add a new server 
     * fragment to the repository via the add method. It would be helpful to
     * email support at enterprisedt dot com to inform us of the message so
     * it can be added to the next build.
     * 
     * @return  messages class
     */
    public FileNotFoundStrings getFileNotFoundMessages() {
        return context.getFileNotFoundMessages();
    }
    
    /**
     * Get class that holds fragments of server messages that indicate a transfer completed. 
     * New messages can be added.
     * <p>
     * The fragments are used when it is necessary to examine the message
     * returned by a server to see if it is saying a transfer completed. 
     * If an FTP server is returning a different message that still clearly 
     * indicates a transfer failed, use this property to add a new server 
     * fragment to the repository via the add method. It would be helpful to
     * email support at enterprisedt dot com to inform us of the message so
     * it can be added to the next build.
     * 
     * @return  messages class
     */
    public TransferCompleteStrings getTransferCompleteMessages() {
        return context.getTransferCompleteMessages();
    }
    
    /**
     * Get class that holds fragments of server messages that indicate a  
     * directory is empty. New messages can be added.
     * <p>
     * The fragments are used when it is necessary to examine the message
     * returned by a server to see if it is saying a directory is empty. 
     * If an FTP server is returning a different message that still clearly 
     * indicates a directory is empty, use this property to add a new server 
     * fragment to the repository via the add method. It would be helpful to
     * email support at enterprisedt dot com to inform us of the message so
     * it can be added to the next build.
     * 
     * @return  messages class
     */
    public DirectoryEmptyStrings getDirectoryEmptyMessages() {
        return context.getDirectoryEmptyMessages();
    }
}
