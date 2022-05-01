package org.teamapps.protocol.schema;

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

	File getFileProperty();

	BitSet getBitSetProperty();

	byte[] getByteArrayProperty();

	int[] getIntArrayProperty();

	long[] getLongArrayProperty();

	float[] getFloatArrayProperty();

	double[] getDoubleArrayProperty();

	String[] getStringArrayProperty();

	void write(DataOutputStream dos, FileSink fileSink) throws IOException;

	byte[] toBytes() throws IOException;

	byte[] toBytes(FileSink fileSink) throws IOException;

	String explain(int level);
}
