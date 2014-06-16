package com.ftpandroid.net.ftp;

import java.io.IOException;

public class ControlChannelIOException extends IOException {
    
    /**
     * Constructs an {@code ControlChannelIOException} with {@code null}
     * as its error detail message.
     */
    public ControlChannelIOException() {
        super();
    }

    /**
     * Constructs an {@code ControlChannelIOException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public ControlChannelIOException(String message) {
        super(message);
    }
}
