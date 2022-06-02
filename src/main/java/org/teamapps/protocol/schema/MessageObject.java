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
import org.teamapps.protocol.file.FileProvider;
import org.teamapps.protocol.file.FileSink;
import org.teamapps.protocol.message.MessageUtils;

import java.io.*;
import java.util.*;

public class MessageObject {

	private final ObjectPropertyDefinition objectPropertyDefinition;
	private final List<MessageProperty> properties;
	private final Map<String, MessageProperty> propertyByName;

	public static String readMessageObjectUuid(byte[] bytes) throws IOException {
		return MessageUtils.readString(new DataInputStream(new ByteArrayInputStream(bytes)));
	}

	public static String readMessageObjectUuid(ByteBuf buf) {
		return MessageUtils.readString(buf);
	}

	public MessageObject(ObjectPropertyDefinition objectPropertyDefinition) {
		this.objectPropertyDefinition = objectPropertyDefinition;
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
	}

	public MessageObject(MessageModel model) {
		this(model.getObjectPropertyDefinition());
	}

	public MessageObject(MessageObject message, PojoObjectDecoderRegistry decoderRegistry) {
		this.objectPropertyDefinition = message.objectPropertyDefinition;
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
		for (MessageProperty property : message.getProperties()) {
			MessageProperty messageProperty = new AbstractMessageProperty((AbstractMessageProperty) property, decoderRegistry);
			properties.add(messageProperty);
			propertyByName.put(property.getPropertyDefinition().getName(), messageProperty);
		}
	}

	public MessageObject(byte[] bytes, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this(new DataInputStream(new ByteArrayInputStream(bytes)), model, fileProvider, decoderRegistry);
	}

	public MessageObject(byte[] bytes, ModelRegistry modelRegistry, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this(new DataInputStream(new ByteArrayInputStream(bytes)), modelRegistry, fileProvider, decoderRegistry);
	}

	public MessageObject(DataInputStream dis, ModelRegistry modelRegistry, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		String objectUuid = MessageUtils.readString(dis);
		short modelVersion = dis.readShort();
		MessageModel model = modelRegistry.getModel(objectUuid, modelVersion);
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
		int propertyCount = dis.readShort();
		for (int i = 0; i < propertyCount; i++) {
			AbstractMessageProperty messageProperty = new AbstractMessageProperty(dis, objectPropertyDefinition, fileProvider, decoderRegistry);
			properties.add(messageProperty);
			propertyByName.put(messageProperty.getPropertyDefinition().getName(), messageProperty);
		}
	}

	public MessageObject(ByteBuf buf, ModelRegistry modelRegistry, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		String objectUuid = MessageUtils.readString(buf);
		short modelVersion = buf.readShort();
		MessageModel model = modelRegistry.getModel(objectUuid, modelVersion);
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
		int propertyCount = buf.readShort();
		for (int i = 0; i < propertyCount; i++) {
			AbstractMessageProperty messageProperty = new AbstractMessageProperty(buf, objectPropertyDefinition, fileProvider, decoderRegistry);
			properties.add(messageProperty);
			propertyByName.put(messageProperty.getPropertyDefinition().getName(), messageProperty);
		}
	}


	public MessageObject(DataInputStream dis, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
		String objectUuid = MessageUtils.readString(dis);
		if (!model.getObjectPropertyDefinition().getObjectUuid().equals(objectUuid)) {
			throw new RuntimeException("Cannot parse message with wrong model:" + objectUuid + ", expected:" + objectPropertyDefinition.getObjectUuid());
		}
		short modelVersion = dis.readShort();
		if (model.getModelVersion() != modelVersion) {
			System.out.println("Wrong model version " + model + ", expected: " + model.getModelVersion());
		}
		int propertyCount = dis.readShort();
		for (int i = 0; i < propertyCount; i++) {
			AbstractMessageProperty messageProperty = new AbstractMessageProperty(dis, objectPropertyDefinition, fileProvider, decoderRegistry);
			properties.add(messageProperty);
			propertyByName.put(messageProperty.getPropertyDefinition().getName(), messageProperty);
		}
	}

	public MessageObject(ByteBuf buf, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
		this.properties = new ArrayList<>();
		this.propertyByName = new HashMap<>();
		String objectUuid = MessageUtils.readString(buf);
		if (!model.getObjectPropertyDefinition().getObjectUuid().equals(objectUuid)) {
			throw new RuntimeException("Cannot parse message with wrong model:" + objectUuid + ", expected:" + objectPropertyDefinition.getObjectUuid());
		}
		short modelVersion = buf.readShort();
		if (model.getModelVersion() != modelVersion) {
			System.out.println("Wrong model version " + model + ", expected: " + model.getModelVersion());
		}
		int propertyCount = buf.readShort();
		for (int i = 0; i < propertyCount; i++) {
			AbstractMessageProperty messageProperty = new AbstractMessageProperty(buf, objectPropertyDefinition, fileProvider, decoderRegistry);
			properties.add(messageProperty);
			propertyByName.put(messageProperty.getPropertyDefinition().getName(), messageProperty);
		}
	}

	public MessageModel getModel() {
		return objectPropertyDefinition;
	}

	public String getName() {
		return objectPropertyDefinition.getName();
	}

	public List<MessageProperty> getProperties() {
		return properties;
	}

	public void write(DataOutputStream dos, FileSink fileSink) throws IOException {
		MessageUtils.writeString(dos, objectPropertyDefinition.getObjectUuid());
		dos.writeShort(objectPropertyDefinition.getModelVersion());
		dos.writeShort(properties.size());
		for (MessageProperty field : properties) {
			field.write(dos, fileSink);
		}
	}

	public void write(ByteBuf buffer, FileSink fileSink) throws IOException {
		MessageUtils.writeString(buffer, objectPropertyDefinition.getObjectUuid());
		buffer.writeShort(objectPropertyDefinition.getModelVersion());
		buffer.writeShort(properties.size());
		for (MessageProperty field : properties) {
			field.write(buffer, fileSink);
		}
	}

	public byte[] toBytes() throws IOException {
		return toBytes(null);
	}

	public byte[] toBytes(FileSink fileSink) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		write(dos, fileSink);
		dos.close();
		return bos.toByteArray();
	}


	public MessageObject setReferencedObject(String name, MessageObject value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setReferencedObjects(String name, List<MessageObject> value) {
		setProperty(name, value);
		return this;
	}

	public <TYPE extends MessageObject> MessageObject setReferencedObjectAsType(String name, TYPE value) {
		setProperty(name, value);
		return this;
	}

	public <TYPE extends MessageObject> MessageObject setReferencedObjectsAsType(String name, List<TYPE> value) {
		setProperty(name, value);
		return this;
	}

	public MessageObject setBooleanProperty(String name, boolean value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setByteProperty(String name, byte value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setIntProperty(String name, int value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setLongProperty(String name, long value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setFloatProperty(String name, float value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setDoubleProperty(String name, double value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setStringProperty(String name, String value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setFileProperty(String name, FileProperty value) {
		setProperty(name, value);
		return this;
	}

	public MessageObject setFileProperty(String name, File file) {
		setProperty(name, file != null ? new FileProperty(file) : null);
		return this;
	}

	public MessageObject setFileProperty(String name, File file, String fileName) {
		setProperty(name, file != null ? new FileProperty(fileName, file) : null);
		return this;
	}

	public MessageObject setBitSetProperty(String name, BitSet value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setByteArrayProperty(String name, byte[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setIntArrayProperty(String name, int[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setLongArrayProperty(String name, long[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setFloatArrayProperty(String name, float[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setDoubleArrayProperty(String name, double[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject setStringArrayProperty(String name, String[] value) {
		setProperty(name, value);
		return this;
	}


	public MessageObject getReferencedObject(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getReferencedObject();
		} else {
			return null;
		}
	}


	public List<MessageObject> getReferencedObjects(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getReferencedObjects();
		} else {
			return null;
		}
	}


	public <TYPE extends MessageObject> TYPE getReferencedObjectAsType(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getReferencedObjectAsType();
		} else {
			return null;
		}
	}


	public <TYPE extends MessageObject> List<TYPE> getReferencedObjectsAsType(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getReferencedObjectsAsType();
		} else {
			return null;
		}
	}


	public boolean getBooleanProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getBooleanProperty();
		} else {
			return false;
		}
	}


	public byte getByteProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getByteProperty();
		} else {
			return 0;
		}
	}


	public int getIntProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getIntProperty();
		} else {
			return 0;
		}
	}


	public long getLongProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getLongProperty();
		} else {
			return 0;
		}
	}


	public float getFloatProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFloatProperty();
		} else {
			return 0;
		}
	}


	public double getDoubleProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getDoubleProperty();
		} else {
			return 0;
		}
	}


	public String getStringProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getStringProperty();
		} else {
			return null;
		}
	}


	public FileProperty getFileProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFileProperty();
		} else {
			return null;
		}
	}

	public File getFilePropertyAsFile(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFilePropertyAsFile();
		} else {
			return null;
		}
	}

	public String getFilePropertyAsFileName(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFilePropertyAsFileName();
		} else {
			return null;
		}
	}

	public long getFilePropertyAsFileLength(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFilePropertyAsFileLength();
		} else {
			return 0;
		}
	}


	public BitSet getBitSetProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getBitSetProperty();
		} else {
			return null;
		}
	}


	public byte[] getByteArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getByteArrayProperty();
		} else {
			return null;
		}
	}


	public int[] getIntArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getIntArrayProperty();
		} else {
			return null;
		}
	}


	public long[] getLongArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getLongArrayProperty();
		} else {
			return null;
		}
	}


	public float[] getFloatArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFloatArrayProperty();
		} else {
			return null;
		}
	}


	public double[] getDoubleArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getDoubleArrayProperty();
		} else {
			return null;
		}
	}


	public String[] getStringArrayProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getStringArrayProperty();
		} else {
			return null;
		}
	}


	public void addReference(String name, MessageObject messageObject) {
		PropertyDefinition propertyDefinition = objectPropertyDefinition.getPropertyDefinitionByName(name);
		if (propertyDefinition == null) {
			throw new RuntimeException("Message model does not contain a field with name:" + name);
		}
		if (propertyDefinition.getType() == PropertyType.OBJECT_SINGLE_REFERENCE) {
			setProperty(name, messageObject);
		} else if (propertyDefinition.getType() == PropertyType.OBJECT_MULTI_REFERENCE) {
			MessageProperty messageProperty = getProperty(name);
			if (messageProperty == null) {
				List<MessageObject> messageObjects = new ArrayList<>();
				messageObjects.add(messageObject);
				setProperty(name, messageObjects);
			} else {
				List<MessageObject> referencedObjects = messageProperty.getReferencedObjects();
				referencedObjects.add(messageObject);
			}
		}
	}

	public void setProperty(String name, Object value) {
		PropertyDefinition propertyDefinition = objectPropertyDefinition.getPropertyDefinitionByName(name);
		if (propertyDefinition == null) {
			throw new RuntimeException("Message model does not contain a field with name:" + name);
		}
		MessageProperty existingField = propertyByName.get(name);
		if (existingField != null) {
			properties.remove(existingField);
			if (value != null) {
				MessageProperty messageProperty = new AbstractMessageProperty(propertyDefinition, value);
				properties.add(messageProperty);
				propertyByName.put(name, messageProperty);
			} else {
				propertyByName.remove(name);
			}
		} else if (value != null) {
			MessageProperty messageProperty = new AbstractMessageProperty(propertyDefinition, value);
			properties.add(messageProperty);
			propertyByName.put(name, messageProperty);
		}
	}

	public void removeField(PropertyDefinition propertyDefinition) {
		MessageProperty existingField = propertyByName.get(propertyDefinition);
		if (existingField != null) {
			properties.remove(existingField);
			propertyByName.remove(propertyDefinition);
		}
	}

	public MessageProperty getProperty(String name) {
		return propertyByName.get(name);
	}

	protected String explain(int level) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t".repeat(level)).append(objectPropertyDefinition.getName()).append(", ");
		if (objectPropertyDefinition.getTitle() != null) {
			sb.append(objectPropertyDefinition.getTitle()).append(", ");
		}
		sb.append("[").append(objectPropertyDefinition.getObjectUuid()).append("], ").append(objectPropertyDefinition.getType());
		sb.append(objectPropertyDefinition.getContentType() != PropertyContentType.GENERIC ? ", " + objectPropertyDefinition.getContentType() : "");
		for (MessageProperty property : properties) {
			sb.append("\n");
			sb.append(property.explain(level + 1));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return explain(0);
	}

}
