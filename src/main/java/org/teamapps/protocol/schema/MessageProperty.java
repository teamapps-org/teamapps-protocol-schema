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

import io.netty.buffer.ByteBuf;
import org.teamapps.protocol.file.FileSink;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;

public interface MessageProperty {

	PropertyDefinition getPropertyDefinition();
	MessageObject getReferencedObject();

	List<MessageObject> getReferencedObjects();

	<TYPE extends MessageObject> TYPE getReferencedObjectAsType();

	<TYPE extends MessageObject> List<TYPE> getReferencedObjectsAsType();

	boolean getBooleanProperty();

	byte getByteProperty();

	int getIntProperty();

	long getLongProperty();

	float getFloatProperty();

	double getDoubleProperty();

	String getStringProperty();

	FileProperty getFileProperty();

	File getFilePropertyAsFile();

	String getFilePropertyAsFileName();

	long getFilePropertyAsFileLength();

	BitSet getBitSetProperty();

	byte[] getByteArrayProperty();

	int[] getIntArrayProperty();

	long[] getLongArrayProperty();

	float[] getFloatArrayProperty();

	double[] getDoubleArrayProperty();

	String[] getStringArrayProperty();

	String getAsString();

	void write(DataOutputStream dos, FileSink fileSink) throws IOException;

	void write(ByteBuf buffer, FileSink fileSink) throws IOException;

	byte[] toBytes() throws IOException;

	byte[] toBytes(FileSink fileSink) throws IOException;

	String explain(int level);
}
