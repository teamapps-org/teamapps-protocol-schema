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
package org.teamapps.protocol.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileSinkFileProviderLoop implements FileSink, FileProvider{

	private int fileId;
	private final Map<String, File> fileMap = new HashMap<>();

	@Override
	public File getFile(String fileId) {
		return fileMap.get(fileId);
	}

	@Override
	public String handleFile(File file) throws IOException {
		String fileId = createNewFileId();
		fileMap.put(fileId, file);
		return fileId;
	}

	private String createNewFileId() {
		return "" + (++fileId);
	}
}
