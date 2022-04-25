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
package org.teamapps.protocol.message;

import org.teamapps.protocol.service.ServiceSchema;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageSchema implements MessageModel {
	public static final int MAX_SCHEMA_ID = 2_000_000;

	private final int schemaId;
	private final String name;
	private final String namespace;
	private List<MessageField> topLevelFields = new ArrayList<>();
	private List<MessageField> fields = new ArrayList<>();
	private Map<Integer, MessageField> fieldMap = new HashMap<>();
	private Map<String, MessageField> fieldByPath = new HashMap<>();
	private List<ServiceSchema> serviceSchemas = new ArrayList<>();

	public MessageSchema(int schemaId, String name, String namespace) {
		this.schemaId = schemaId;
		this.name = name;
		this.namespace = namespace;
		if (schemaId > MAX_SCHEMA_ID) {
			throw new RuntimeException("Schema id is too high:" + schemaId + ", max schema id:" + MAX_SCHEMA_ID);
		}
	}

	public MessageSchema(DataInputStream dis) throws IOException {
		schemaId = dis.readInt();
		name = MessageUtils.readString(dis);
		namespace = MessageUtils.readString(dis);
		int fieldCount = dis.readInt();
		for (int i = 0; i < fieldCount; i++) {
			fields.add(new MessageField(dis));
		}
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeInt(schemaId);
		dos.writeInt(fields.size());
		MessageUtils.writeString(dos, namespace);
		for (MessageField field : fields) {
			field.write(dos);
		}
	}

	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		write(dos);
		dos.close();
		return bos.toByteArray();
	}

	public ServiceSchema addService(String serviceName) {
		return addService(new ServiceSchema(serviceName));
	}

	public ServiceSchema addService(ServiceSchema serviceSchema) {
		serviceSchemas.add(serviceSchema);
		return serviceSchema;
	}

	public MessageField addObject(String name) {
		return addObject(0, name, null);
	}

	public MessageField addSingleReference(MessageField field, MessageField reference, String name) {
		MessageField f = new MessageField(field.getId(), getNextFieldId(), name, null, MessageFieldType.OBJECT_SINGLE_REFERENCE, MessageFieldContentType.GENERIC, null, reference.getId());
		return addField(field, f);
	}

	public MessageField addSingleReference(MessageField field, MessageField reference, String name, String title) {
		MessageField f = new MessageField(field.getId(), getNextFieldId(), name, title, MessageFieldType.OBJECT_SINGLE_REFERENCE, MessageFieldContentType.GENERIC, null, reference.getId());
		return addField(field, f);
	}

	public MessageField addMultiReference(MessageField field, MessageField reference, String name) {
		MessageField f = new MessageField(field.getId(), getNextFieldId(), name, null, MessageFieldType.OBJECT_MULTI_REFERENCE, MessageFieldContentType.GENERIC, null, reference.getId());
		return addField(field, f);
	}

	public MessageField addMultiReference(MessageField field, MessageField reference, String name, String title) {
		MessageField f = new MessageField(field.getId(), getNextFieldId(), name, title, MessageFieldType.OBJECT_MULTI_REFERENCE, MessageFieldContentType.GENERIC, null, reference.getId());
		return addField(field, f);
	}

	public MessageField addFileField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.FILE);
	}

	public MessageField addTextField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.STRING);
	}

	public MessageField addIntField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.INT);
	}

	public MessageField addLongField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.LONG);
	}

	public MessageField addFloatField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.FLOAT);
	}

	public MessageField addDoubleField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.DOUBLE);
	}
	
	public MessageField addBooleanField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.BOOLEAN);
	}

	public MessageField addByteField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.BYTE);
	}

	public MessageField addBitsetField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.BITSET);
	}

	public MessageField addByteArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.BYTE_ARRAY);
	}

	public MessageField addIntArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.INT_ARRAY);
	}

	public MessageField addLongArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.LONG_ARRAY);
	}

	public MessageField addFloatArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.FLOAT_ARRAY);
	}

	public MessageField addDoubleArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.DOUBLE_ARRAY);
	}

	public MessageField addStringArrayField(MessageField parent, String name) {
		return addField(parent, name, MessageFieldType.STRING_ARRAY);
	}

	public MessageField addObject(int localId, String name) {
		return addField(null, localId, name, null, MessageFieldType.OBJECT, MessageFieldContentType.GENERIC, null);
	}

	public MessageField addObject(int localId, String name, String title) {
		return addField(null, localId, name, title, MessageFieldType.OBJECT, MessageFieldContentType.GENERIC, null);
	}

	public MessageField addField(MessageField parent, String name, MessageFieldType type) {
		return addField(parent, 0, name, null, type);
	}

	public void addField(MessageField parent, String name, String title, MessageFieldType type) {
		addField(parent, 0, name, title, type);
	}

	public MessageField addField(MessageField parent, int localId, String name, String title, MessageFieldType type) {
		return addField(parent, localId, name, title, type, MessageFieldContentType.GENERIC, null);
	}

	protected MessageField addField(MessageField parent, String name, String title, MessageFieldType type, MessageFieldContentType contentType, String specificType) {
		return addField(parent, 0, name, title, type, contentType, specificType);
	}

	public MessageField addField(MessageField parent, int localId, String name, String title, MessageFieldType type, MessageFieldContentType contentType, String specificType) {
		int parentId = parent != null ? parent.getId() : 0;
		int id = localId > 0 ? localId + getSchemaIdPrefix() : getNextFieldId();
		MessageField field = new MessageField(parentId, id, name, title, type, contentType, specificType, 0);
		return addField(parent, field);
	}


	private MessageField addField(MessageField parent, MessageField field) {
		if (fieldMap.containsKey(field.getId())) {
			throw new RuntimeException("Message field with key already exists:" + field.getId());
		}
		if (parent == null) {
			topLevelFields.add(field);
		}
		if (parent != null) {
			parent.addField(field);
		}
		fields.add(field);
		fieldMap.put(field.getId(), field);
		fieldByPath.put(field.getPath(this), field);
		return field;
	}

	public int getSchemaIdPrefix() {
		return schemaId * 1_000;
	}

	private int getNextFieldId() {
		return (fields.size() + 1) + getSchemaIdPrefix();
	}

	public int getSchemaId() {
		return schemaId;
	}

	public String getName() {
		return name;
	}

	public List<MessageField> getFields() {
		return fields;
	}

	public MessageField getFieldByLocalId(int localId) {
		return fieldMap.get(getSchemaIdPrefix() + localId);
	}

	public List<MessageField> getTopLevelFields() {
		return topLevelFields;
	}

	public List<ServiceSchema> getServiceSchemas() {
		return serviceSchemas;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public MessageField getFieldById(int id) {
		return fieldMap.get(id);
	}

	@Override
	public MessageField getFieldByPath(String path) {
		return fieldByPath.get(path);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(", ").append(schemaId).append("\n");
		topLevelFields.forEach(field -> sb.append(field.explain(1)));
		return sb.toString();
	}
}
