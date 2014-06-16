package com.ftpandroid.net.ftp;

import java.io.IOException;


class AutoCloseFileTransferInputStream extends FileTransferInputStream {

	private FileTransferInputStream inStr;
	private FileTransferClientInterface client;
	
	public AutoCloseFileTransferInputStream(FileTransferInputStream inStr, FileTransferClientInterface client) {
		this.inStr = inStr;
		this.client = client;
	}
	
	public int read() throws IOException {
		return inStr.read();
	}

	public String getRemoteFile() {
		return inStr.getRemoteFile();
	}

	public int available() throws IOException {
		return inStr.available();
	}

	public void close() throws IOException {
		inStr.close();
		try {
			client.disconnect(true);
		} catch (Throwable t) {
			throw new IOException(t.getMessage());
		}
	}

	public synchronized void mark(int readlimit) {
		inStr.mark(readlimit);
	}

	public boolean markSupported() {
		return inStr.markSupported();
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return inStr.read(b, off, len);
	}

	public int read(byte[] b) throws IOException {
		return inStr.read(b);
	}

	public synchronized void reset() throws IOException {
		inStr.reset();
	}

	public long skip(long n) throws IOException {
		return inStr.skip(n);
	}

	public boolean equals(Object obj) {
		return inStr.equals(obj);
	}

	public int hashCode() {
		return inStr.hashCode();
	}

	public String toString() {
		return inStr.toString();
	}
}
