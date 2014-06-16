package com.ftpandroid.net.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Represents a remote file (implementation)
 *
 *  @author      Eric
 */
public class FTPFile {
    


    /**
     * Unknown remote server type
     */
    public final static int UNKNOWN = -1;  
    
    /**
     * Windows type
     */
    public final static int WINDOWS = 0;
    
    /**
     * UNIX type
     */
    public final static int UNIX = 1;
    
    /**
     * VMS type
     */
    public final static int VMS = 2;
        
    /**
     * Date formatter type 1
     */
    private final static SimpleDateFormat formatter =
        new SimpleDateFormat("dd-MM-yyyy HH:mm");
    
    /**
     * Type of file
     */
    private int type;
    
    /**
     * Is this file a symbolic link?
     */
    protected boolean isLink = false;
    
    /**
     * Number of links to file
     */
    protected int linkCount = 1;
    
    /**
     * Permission bits string
     */
    protected String permissions;
       
    /**
     * Is this a directory?
     */
    protected boolean isDir = false;
    
    /**
     * Size of file
     */
    protected long size = 0L;
    
    /**
     * File/dir name
     */
    protected String name;
    
    /**
     * Name of file this is linked to
     */
    protected String linkedname;
    
    /**
     * Owner if known
     */
    protected String owner;
    
    /**
     * Group if known
     */
    protected String group;
    
    /**
     * Last modified
     */
    protected Date lastModified;
    
    /**
     * Created time
     */
    protected Date created;

    /**
     * Raw string
     */
    protected String raw;
    
    /**
     * Directory if known
     */
    protected String path;
    
    /**
     * Children if a directory
     */
    private FTPFile[] children;
    
    /**
     * Constructor
     * 
     * @param type          type of file
     * @param raw           raw string returned from server
     * @param name          name of file
     * @param size          size of file
     * @param isDir         true if a directory
     * @param lastModified  last modified timestamp
     * @deprecated 'type' no longer used.
     */
    public FTPFile(int type, String raw, String name, long size, boolean isDir, Date lastModified) {
        this(raw);
        this.type = type;
        this.name = name;
        this.size = size;
        this.isDir = isDir;
        this.lastModified = lastModified;
    }
    
    /**
     * Constructor
     * 
     * @param raw           raw string returned from server
     * @param name          name of file
     * @param size          size of file
     * @param isDir         true if a directory
     * @param lastModified  last modified timestamp
     */
    public FTPFile(String raw, String name, long size, boolean isDir, Date lastModified) {
        this(raw);
        this.type = UNKNOWN;
        this.name = name;
        this.size = size;
        this.isDir = isDir;
        this.lastModified = lastModified;
    }
    
    /**
     * Constructor
     * 
     * @param raw   raw string returned from server
     */
    public FTPFile(String raw) {
        this.raw = raw;
    }
    
    /**
     * Returns an array of FTPFile objects denoting the files and directories in this
     * directory
     * 
     * @return FTPFile array
     */
    public FTPFile[] listFiles() {
        return children;
    }
    
    /**
     * 
     * @param children
     */
    void setChildren(FTPFile[] children) {
        this.children = children;
    }
    
    /**
     * Get the type of file, i.e UNIX
     * 
     * @return the integer type of the file
     * @deprecated No longer necessary.
     */
    public int getType() {
        return type;
    }
    
    /**
     * @return Returns the group.
     */
    public String getGroup() {
        return group;
    }

    /**
     * @return Returns the isDir.
     */
    public boolean isDir() {
        return isDir;
    }
    
    /**
     * Is this a file (and not a directory
     * or a link).
     * 
     * @return true if a file, false if link or directory
     */
    public boolean isFile() {
        return !isDir() && !isLink();
    }

    /**
     * @return Returns the lastModified date.
     */
    public Date lastModified() {
        return lastModified;
    }
    
    /**
     * Set the last modified date
     * 
     * @param date  last modified date
     */
    public void setLastModified(Date date) {
        lastModified = date;
    }
    
    /**
     * Get the created date for the file. This is not
     * supported by many servers, e.g. Unix does not record
     * the created date of a file.
     * 
     * @return Returns the created date.
     */
    public Date created() {
        return created;
    }
    
    /**
     * Set the created date
     * 
     * @param date
     */
    public void setCreated(Date date) {
        created = date;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of the file
     * 
     * @param name  name of file
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return Returns the raw server string.
     */
    public String getRaw() {
        return raw;
    }

    /**
     * @return Returns the size.
     */
    public long size() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    /**
     * @return Returns the permissions.
     */
    public String getPermissions() {
        return permissions;
    }
    
    /**
     * @return Returns true if file is a symlink
     */
    public boolean isLink() {
        return isLink;
    }
    
    /**
     * @return Returns the number of links to the file
     */
    public int getLinkCount() {
        return linkCount;
    }
    
    /**
     * @return Returns the linked name.
     * @deprecated
     */
    public String getLinkedname() {
        return linkedname;
    }
    
    /**
     * @return Returns the linked name.
     */
    public String getLinkedName() {
        return linkedname;
    }

    /**
     * @param group The group to set.
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @param isDir The isDir to set.
     */
    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }

    /**
     * @param isLink The isLink to set.
     */
    public void setLink(boolean isLink) {
        this.isLink = isLink;
    }
    
    /**
     * @param linkedname The linked name to set.
     */
    public void setLinkedName(String linkedname) {
        this.linkedname = linkedname;
    }

    /**
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @param permissions The permissions to set.
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    /**
     * @param linkCount   new link count
     */
    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }
    
    /**
     * @return string representation
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(raw).append("\n");
        buf.append("Name=").append(name).append(",").
            append("Size=").append(size).append(",").
            append("Permissions=").append(permissions).append(",").
            append("Owner=").append(owner).append(",").
            append("Group=").append(group).append(",").
            append("Is link=").append(isLink).append(",").
            append("Link count=").append(linkCount).append(".").
            append("Is dir=").append(isDir).append(",").
            append("Linked name=").append(linkedname).
            append(",").append("Last modified=").
            append(lastModified != null ? formatter.format(lastModified) : "null");
        if (created != null)
            buf.append(",").append("Created=").append(
                    formatter.format(created));
        return buf.toString();
    }

    public String getPath() {
		return path;
	}

    public void setPath(String path) {
		this.path = path;
	}
}
