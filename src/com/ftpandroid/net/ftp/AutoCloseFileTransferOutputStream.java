package com.ftpandroid.net.ftp;

import java.io.IOException;

class AutoCloseFileTransferOutputStream extends FileTransferOutputStream {

	private FileTransferOutputStream outStr;
	private FileTransferClientInterface client;
	
	public AutoCloseFileTransferOutputStream(FileTransferOutputStream outStr, FileTransferClientInterface client) {
		this.outStr = outStr;
		this.client = client;
	}

	public void write(int b) throws IOException {
		outStr.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		outStr.write(b, off, len);
	}

	public String getRemoteFile() {
		return outStr.getRemoteFile();
	}

	public void close() throws IOException {
		outStr.close();
		try {
			client.disconnect(true);
		} catch (Throwable t) {
			throw new IOException(t.getMessage());
		}
	}

	public void flush() throws IOException {
		outStr.flush();
	}

	public void write(byte[] b) throws IOException {
		outStr.write(b);
	}
	
	/**
     * Get the number of bytes transferred
     * 
     * @return long
     */
    public long getBytesTransferred() {
        return outStr.getBytesTransferred();
    }

	public boolean equals(Object obj) {
		return outStr.equals(obj);
	}

	public int hashCode() {
		return outStr.hashCode();
	}

	public String toString() {
		return outStr.toString();
	}
}
