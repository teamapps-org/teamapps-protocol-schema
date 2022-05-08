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
