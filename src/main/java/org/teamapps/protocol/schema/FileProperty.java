/*-
 * ========================LICENSE_START=================================
 * TeamApps Protocol Schema
 * ---
 * Copyright (C) 2022 TeamApps.org
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
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
