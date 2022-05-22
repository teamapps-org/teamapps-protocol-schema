package org.teamapps.protocol.schema;

import java.io.File;

public class FileProperty {

	private String fileName;
	private File file;
	private final long length;

	public FileProperty(String fileName, File file) {
		this.fileName = fileName;
		this.file = file;
		this.length = file.length();
	}

	public FileProperty(String fileName, File file, long length) {
		this.fileName = fileName;
		this.file = file;
		this.length = length;
	}

	public FileProperty(File file) {
		this.file = file;
		this.fileName = file.getName();
		this.length = file.length();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getLength() {
		return length;
	}

	public boolean exists() {
		return file.exists();
	}

	@Override
	public String toString() {
		return fileName + " (" + length + ")";
	}
}
