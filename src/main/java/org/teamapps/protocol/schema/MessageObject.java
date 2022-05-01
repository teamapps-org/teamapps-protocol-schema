package org.teamapps.protocol.schema;


import org.teamapps.protocol.file.FileProvider;
import org.teamapps.protocol.file.FileSink;
import org.teamapps.protocol.message.MessageUtils;

import java.io.*;
import java.util.*;

public class MessageObject {

	private final ObjectPropertyDefinition objectPropertyDefinition;
	private final List<MessageProperty> properties = new ArrayList<>();
	private final Map<String, MessageProperty> propertyByName = new HashMap<>();

	public MessageObject(ObjectPropertyDefinition objectPropertyDefinition) {
		this.objectPropertyDefinition = objectPropertyDefinition;
	}

	public MessageObject(MessageModel model) {
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
	}

	public MessageObject(byte[] bytes, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this(new DataInputStream(new ByteArrayInputStream(bytes)), model, fileProvider, decoderRegistry);
	}

	public MessageObject(DataInputStream dis, MessageModel model, FileProvider fileProvider, PojoObjectDecoderRegistry decoderRegistry) throws IOException {
		this.objectPropertyDefinition = model.getObjectPropertyDefinition();
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

	public void write(DataOutputStream dos, FileSink fileSink) throws IOException {
		MessageUtils.writeString(dos, objectPropertyDefinition.getObjectUuid());
		dos.writeShort(objectPropertyDefinition.getModelVersion());
		dos.writeShort(properties.size());
		for (MessageProperty field : properties) {
			field.write(dos, fileSink);
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


	public MessageObject setFileProperty(String name, File value) {
		setProperty(name, value);
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


	public File getFileProperty(String propertyName) {
		MessageProperty property = getProperty(propertyName);
		if (property != null) {
			return property.getFileProperty();
		} else {
			return null;
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