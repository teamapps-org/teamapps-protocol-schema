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
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class AbstractMessageProperty implements MessageProperty {

	private final PropertyDefinition propertyDefinition;
	private final Object value;

	public AbstractMessageProperty(PropertyDefinition propertyDefinition, Object value) {
		this.propertyDefinition = propertyDefinition;
		this.value = value;
	}

	public AbstractMessageProperty(AbstractMessageProperty property, PojoObjectDecoderRegistry decoderRegistry) {
		this.propertyDefinition = property.propertyDefinition;
		if (propertyDefinition.isReferenceProperty()) {
			ObjectPropertyDefinition referencedObjectDefinition = propertyDefinition.getReferencedObject();
			PojoObjectDecoder<? extends MessageObject> messageDecoder = decoderRegistry.getMessageDecoder(referencedObjectDefinition.getObjectUuid());
			if (propertyDefinition.isMultiReference()) {
				List<MessageObject> messageObjects = (List<MessageObject>) property.value;
				List<MessageObject> remappedMessages = new ArrayList<>();
				for (MessageObject messageObject : messageObjects) {
					remappedMessages.add(messageDecoder.remap(messageObject));
				}
				this.value = remappedMessages;
			} else {
				MessageObject messageObject = (MessageObject) property.value;
				this.value = messageDecoder.remap(messageObject);
			}
		} else {
			this.value = property.value;
		}
	}

	public AbstractMessageProperty(DataInputStream dis, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		PropertyType type = PropertyType.getById(dis.readByte());
		int key = dis.readShort();
		this.propertyDefinition = model.getPropertyDefinitionByKey(key);
		if (type != propertyDefinition.getType()) {
			throw new RuntimeException("Message parsing error - property type mismatch: " + type + " <-> " + propertyDefinition.getType());
		}
		switch (propertyDefinition.getType()) {
			case OBJECT_SINGLE_REFERENCE -> {
				ObjectPropertyDefinition referencedObjectDefinition = propertyDefinition.getReferencedObject();
				if (decoderRegistry != null && decoderRegistry.containsDecoder(referencedObjectDefinition.getObjectUuid())) {
					PojoObjectDecoder<? extends MessageObject> messageDecoder = decoderRegistry.getMessageDecoder(referencedObjectDefinition.getObjectUuid());
					value = messageDecoder.decode(dis, fileProvider);
				} else {
					value = new MessageObject(dis, referencedObjectDefinition, fileProvider, decoderRegistry);
				}
			}
			case OBJECT_MULTI_REFERENCE -> {
				ObjectPropertyDefinition referencedObjectDefinition = propertyDefinition.getReferencedObject();
				List<MessageObject> messageObjects = new ArrayList<>();
				int messageCount = dis.readInt();
				if (decoderRegistry != null && decoderRegistry.containsDecoder(referencedObjectDefinition.getObjectUuid())) {
					PojoObjectDecoder<? extends MessageObject> messageDecoder = decoderRegistry.getMessageDecoder(referencedObjectDefinition.getObjectUuid());
					for (int i = 0; i < messageCount; i++) {
						messageObjects.add(messageDecoder.decode(dis, fileProvider));
					}
				} else {
					for (int i = 0; i < messageCount; i++) {
						messageObjects.add(new MessageObject(dis, referencedObjectDefinition, fileProvider, decoderRegistry));
					}
				}
				value = messageObjects;
			}
			case BOOLEAN -> value = dis.readBoolean();
			case BYTE -> value = dis.readByte();
			case INT -> value = dis.readInt();
			case LONG -> value = dis.readLong();
			case FLOAT -> value = dis.readFloat();
			case DOUBLE -> value = dis.readDouble();
			case STRING -> value = MessageUtils.readString(dis);
			case BITSET -> value = MessageUtils.readBitSet(dis);
			case BYTE_ARRAY -> value = MessageUtils.readByteArray(dis);
			case INT_ARRAY -> value = MessageUtils.readIntArray(dis);
			case LONG_ARRAY -> value = MessageUtils.readLongArray(dis);
			case FLOAT_ARRAY -> value = MessageUtils.readFloatArray(dis);
			case DOUBLE_ARRAY -> value = MessageUtils.readDoubleArray(dis);
			case STRING_ARRAY -> value = MessageUtils.readStringArray(dis);
			case FILE -> value = MessageUtils.readFileProperty(dis, fileProvider);
			case ENUM -> {
				value = null;
			}
			default ->
					throw new RuntimeException("Message parsing error - property type unknown:" + propertyDefinition.getType());
		}
	}

	public AbstractMessageProperty(ByteBuf buf, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		PropertyType type = PropertyType.getById(buf.readByte());
		int key = buf.readShort();
		this.propertyDefinition = model.getPropertyDefinitionByKey(key);
		if (type != propertyDefinition.getType()) {
			throw new RuntimeException("Message parsing error - property type mismatch: " + type + " <-> " + propertyDefinition.getType());
		}
		switch (propertyDefinition.getType()) {
			case OBJECT_SINGLE_REFERENCE -> {
				ObjectPropertyDefinition referencedObjectDefinition = propertyDefinition.getReferencedObject();
				if (decoderRegistry != null && decoderRegistry.containsDecoder(referencedObjectDefinition.getObjectUuid())) {
					PojoObjectDecoder<? extends MessageObject> messageDecoder = decoderRegistry.getMessageDecoder(referencedObjectDefinition.getObjectUuid());
					value = messageDecoder.decode(buf, fileProvider);
				} else {
					value = new MessageObject(buf, referencedObjectDefinition, fileProvider, decoderRegistry);
				}
			}
			case OBJECT_MULTI_REFERENCE -> {
				ObjectPropertyDefinition referencedObjectDefinition = propertyDefinition.getReferencedObject();
				List<MessageObject> messageObjects = new ArrayList<>();
				int messageCount = buf.readInt();
				if (decoderRegistry != null && decoderRegistry.containsDecoder(referencedObjectDefinition.getObjectUuid())) {
					PojoObjectDecoder<? extends MessageObject> messageDecoder = decoderRegistry.getMessageDecoder(referencedObjectDefinition.getObjectUuid());
					for (int i = 0; i < messageCount; i++) {
						messageObjects.add(messageDecoder.decode(buf, fileProvider));
					}
				} else {
					for (int i = 0; i < messageCount; i++) {
						messageObjects.add(new MessageObject(buf, referencedObjectDefinition, fileProvider, decoderRegistry));
					}
				}
				value = messageObjects;
			}
			case BOOLEAN -> value = buf.readBoolean();
			case BYTE -> value = buf.readByte();
			case INT -> value = buf.readInt();
			case LONG -> value = buf.readLong();
			case FLOAT -> value = buf.readFloat();
			case DOUBLE -> value = buf.readDouble();
			case STRING -> value = MessageUtils.readString(buf);
//			case BITSET -> value = MessageUtils.readBitSet(buf);
			case BYTE_ARRAY -> value = MessageUtils.readByteArray(buf);
//			case INT_ARRAY -> value = MessageUtils.readIntArray(buf);
//			case LONG_ARRAY -> value = MessageUtils.readLongArray(buf);
//			case FLOAT_ARRAY -> value = MessageUtils.readFloatArray(buf);
//			case DOUBLE_ARRAY -> value = MessageUtils.readDoubleArray(buf);
//			case STRING_ARRAY -> value = MessageUtils.readStringArray(buf);
//			case FILE -> value = MessageUtils.readFile(buf, fileProvider);
			case ENUM -> {
				value = null;
			}
			default ->
					throw new RuntimeException("Message parsing error - property type unknown:" + propertyDefinition.getType());
		}
	}

	@Override
	public void write(DataOutputStream dos, FileSink fileSink) throws IOException {
		dos.writeByte(propertyDefinition.getType().getId());
		dos.writeShort(propertyDefinition.getKey());
		switch (propertyDefinition.getType()) {
			case OBJECT_SINGLE_REFERENCE -> {
				MessageObject referencedObject = getReferencedObject();
				referencedObject.write(dos, fileSink);
			}
			case OBJECT_MULTI_REFERENCE -> {
				List<MessageObject> referencedObjects = getReferencedObjects();
				if (referencedObjects == null || referencedObjects.isEmpty()) {
					dos.writeInt(0);
				} else {
					dos.writeInt(referencedObjects.size());
					for (MessageObject referencedObject : referencedObjects) {
						referencedObject.write(dos, fileSink);
					}
				}
			}
			case BOOLEAN -> dos.writeBoolean(getBooleanProperty());
			case BYTE -> dos.writeByte(getByteProperty());
			case INT -> dos.writeInt(getIntProperty());
			case LONG -> dos.writeLong(getLongProperty());
			case FLOAT -> dos.writeFloat(getFloatProperty());
			case DOUBLE -> dos.writeDouble(getDoubleProperty());
			case STRING -> MessageUtils.writeString(dos, getStringProperty());
			case BITSET -> MessageUtils.writeBitSet(dos, getBitSetProperty());
			case BYTE_ARRAY -> MessageUtils.writeByteArray(dos, getByteArrayProperty());
			case INT_ARRAY -> MessageUtils.writeIntArray(dos, getIntArrayProperty());
			case LONG_ARRAY -> MessageUtils.writeLongArray(dos, getLongArrayProperty());
			case FLOAT_ARRAY -> MessageUtils.writeFloatArray(dos, getFloatArrayProperty());
			case DOUBLE_ARRAY -> MessageUtils.writeDoubleArray(dos, getDoubleArrayProperty());
			case STRING_ARRAY -> MessageUtils.writeStringArray(dos, getStringArrayProperty());
			case FILE -> MessageUtils.writeFileProperty(dos, getFileProperty(), fileSink);
			case ENUM -> {
				//
			}
		}
	}

	@Override
	public void write(ByteBuf buf, FileSink fileSink) throws IOException {
		buf.writeByte(propertyDefinition.getType().getId());
		buf.writeShort(propertyDefinition.getKey());
		switch (propertyDefinition.getType()) {
			case OBJECT_SINGLE_REFERENCE -> {
				MessageObject referencedObject = getReferencedObject();
				referencedObject.write(buf, fileSink);
			}
			case OBJECT_MULTI_REFERENCE -> {
				List<MessageObject> referencedObjects = getReferencedObjects();
				if (referencedObjects == null || referencedObjects.isEmpty()) {
					buf.writeInt(0);
				} else {
					buf.writeInt(referencedObjects.size());
					for (MessageObject referencedObject : referencedObjects) {
						referencedObject.write(buf, fileSink);
					}
				}
			}
			case BOOLEAN -> buf.writeBoolean(getBooleanProperty());
			case BYTE -> buf.writeByte(getByteProperty());
			case INT -> buf.writeInt(getIntProperty());
			case LONG -> buf.writeLong(getLongProperty());
			case FLOAT -> buf.writeFloat(getFloatProperty());
			case DOUBLE -> buf.writeDouble(getDoubleProperty());
			case STRING -> MessageUtils.writeString(buf, getStringProperty());
//			case BITSET -> MessageUtils.writeBitSet(buf, getBitSetProperty());
			case BYTE_ARRAY -> MessageUtils.writeByteArray(buf, getByteArrayProperty());
//			case INT_ARRAY -> MessageUtils.writeIntArray(buf, getIntArrayProperty());
//			case LONG_ARRAY -> MessageUtils.writeLongArray(buf, getLongArrayProperty());
//			case FLOAT_ARRAY -> MessageUtils.writeFloatArray(buf, getFloatArrayProperty());
//			case DOUBLE_ARRAY -> MessageUtils.writeDoubleArray(buf, getDoubleArrayProperty());
//			case STRING_ARRAY -> MessageUtils.writeStringArray(buf, getStringArrayProperty());
			case FILE -> MessageUtils.writeFileProperty(buf, getFileProperty(), fileSink);
			case ENUM -> {
				//
			}
		}
	}

	@Override
	public byte[] toBytes() throws IOException {
		return toBytes(null);
	}

	@Override
	public byte[] toBytes(FileSink fileSink) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		write(dos, fileSink);
		dos.close();
		return bos.toByteArray();
	}

	@Override
	public PropertyDefinition getPropertyDefinition() {
		return propertyDefinition;
	}

	@Override
	public MessageObject getReferencedObject() {
		if (value == null) return null;
		return (MessageObject) value;
	}

	@Override
	public List<MessageObject> getReferencedObjects() {
		if (value == null) return null;
		return (List<MessageObject>) value;
	}

	@Override
	public <TYPE extends MessageObject> TYPE getReferencedObjectAsType() {
		if (value == null) return null;
		return (TYPE) value;
	}

	@Override
	public <TYPE extends MessageObject> List<TYPE> getReferencedObjectsAsType() {
		if (value == null) return null;
		return (List<TYPE>) value;
	}

	@Override
	public boolean getBooleanProperty() {
		if (value == null) return false;
		return (boolean) value;
	}

	@Override
	public byte getByteProperty() {
		if (value == null) return 0;
		return (byte) value;
	}

	@Override
	public int getIntProperty() {
		if (value == null) return 0;
		return (int) value;
	}

	@Override
	public long getLongProperty() {
		if (value == null) return 0;
		return (long) value;
	}

	@Override
	public float getFloatProperty() {
		if (value == null) return 0;
		return (Float) value;
	}

	@Override
	public double getDoubleProperty() {
		if (value == null) return 0;
		return (Double) value;
	}

	@Override
	public String getStringProperty() {
		if (value == null) return null;
		return (String) value;
	}

	@Override
	public FileProperty getFileProperty() {
		if (value == null) return null;
		return (FileProperty) value;
	}

	@Override
	public File getFilePropertyAsFile() {
		if (value == null) return null;
		return getFileProperty().getFile();
	}

	@Override
	public String getFilePropertyAsFileName() {
		if (value == null) return null;
		return getFileProperty().getFileName();
	}

	@Override
	public long getFilePropertyAsFileLength() {
		if (value == null) return 0;
		return getFileProperty().getLength();
	}

	@Override
	public BitSet getBitSetProperty() {
		if (value == null) return null;
		return (BitSet) value;
	}

	@Override
	public byte[] getByteArrayProperty() {
		if (value == null) return null;
		return (byte[]) value;
	}

	@Override
	public int[] getIntArrayProperty() {
		if (value == null) return null;
		return (int[]) value;
	}

	@Override
	public long[] getLongArrayProperty() {
		if (value == null) return null;
		return (long[]) value;
	}

	@Override
	public float[] getFloatArrayProperty() {
		if (value == null) return null;
		return (float[]) value;
	}

	@Override
	public double[] getDoubleArrayProperty() {
		if (value == null) return null;
		return (double[]) value;
	}

	@Override
	public String[] getStringArrayProperty() {
		if (value == null) return null;
		return (String[]) value;
	}

	@Override
	public String getAsString() {
		return "" + value;
	}

	@Override
	public String explain(int level) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t".repeat(level)).append(propertyDefinition.getName()).append(", ");
		if (propertyDefinition.getTitle() != null) {
			sb.append(propertyDefinition.getTitle()).append(", ");
		}
		sb.append(propertyDefinition.getType());
		sb.append(propertyDefinition.getContentType() != PropertyContentType.GENERIC ? ", " + propertyDefinition.getContentType() : "");
		if (propertyDefinition.isReferenceProperty()) {
			ObjectPropertyDefinition referenceDefinition = propertyDefinition.getReferencedObject();
			if (propertyDefinition.isMultiReference()) {
				sb.append("\n");
				for (MessageObject referencedObject : getReferencedObjects()) {
					sb.append(referencedObject.explain(level + 1)).append("\n");
				}
			} else {
				sb.append("\n");
				MessageObject referencedObject = getReferencedObject();
				sb.append(referencedObject.explain(level + 1));
			}
		} else {
			sb.append(": ").append(value);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return explain(0);
	}

}
