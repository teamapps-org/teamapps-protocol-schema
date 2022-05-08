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

import org.teamapps.protocol.message.MessageUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectPropertyDefinition extends AbstractPropertyDefinition implements MessageModel {

	private final String objectUuid;
	private final short modelVersion;
	private final List<PropertyDefinition> definitions = new ArrayList<>();
	private final Map<Integer, PropertyDefinition> definitionByKey = new HashMap<>();
	private final Map<String, PropertyDefinition> definitionByName = new HashMap<>();

	public ObjectPropertyDefinition(String objectUuid, String name, int modelVersion) {
		this(objectUuid, name, null, null, modelVersion);
	}

	public ObjectPropertyDefinition(String objectUuid, String name, String title, String specificType, int modelVersion) {
		super(null, name, 0, PropertyType.OBJECT, PropertyContentType.GENERIC, specificType, title);
		this.objectUuid = objectUuid;
		this.modelVersion = (short) modelVersion;
	}

	public ObjectPropertyDefinition(byte[] bytes) throws IOException {
		this(new DataInputStream(new ByteArrayInputStream(bytes)));
	}

	public ObjectPropertyDefinition(DataInputStream dis) throws IOException {
		this(dis, new HashMap<>());
	}

	public ObjectPropertyDefinition(DataInputStream dis, Map<String, MessageModel> writeCache) throws IOException {
		this(MessageUtils.readString(dis), MessageUtils.readString(dis), MessageUtils.readString(dis), MessageUtils.readString(dis), dis.readShort());
		int size = dis.readInt();
		for (int i = 0; i < size; i++) {
			PropertyDefinition propertyDefinition = new AbstractPropertyDefinition(this, dis, writeCache);
			addProperty(propertyDefinition);
		}
	}

	public void write(DataOutputStream dos) throws IOException {
		write(dos, new HashMap<>());
	}

	public void write(DataOutputStream dos, Map<String, MessageModel> writeCache) throws IOException {
		MessageUtils.writeString(dos, objectUuid);
		MessageUtils.writeString(dos, getName());
		MessageUtils.writeString(dos, getTitle());
		MessageUtils.writeString(dos, getSpecificType());
		dos.writeShort(modelVersion);
		dos.writeInt(definitions.size());
		for (PropertyDefinition propertyDefinition : definitions) {
			propertyDefinition.write(dos, writeCache);
		}
	}

	@Override
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		write(dos);
		dos.close();
		return bos.toByteArray();
	}

	public String getObjectUuid() {
		return objectUuid;
	}

	public void addProperty(String name, int key, PropertyType type) {
		addProperty(name, key, type, PropertyContentType.GENERIC, null, null);
	}

	public void addProperty(String name, int key, PropertyType type, PropertyContentType contentType, String specificType, String title) {
		AbstractPropertyDefinition propertyDefinition = new AbstractPropertyDefinition(this, name, key, type, contentType, specificType, title);
		addProperty(propertyDefinition);
	}

	public void addSingleReference(String name, int key, ObjectPropertyDefinition referencedObject) {
		addSingleReference(name, key, null, null, referencedObject);
	}

	public void addSingleReference(String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject) {
		AbstractPropertyDefinition referencePropertyDefinition = new AbstractPropertyDefinition(this, name, key, specificType, title, referencedObject, false);
		addProperty(referencePropertyDefinition);
	}

	public void addMultiReference(String name, int key, ObjectPropertyDefinition referencedObject) {
		addMultiReference(name, key, null, null, referencedObject);
	}

	public void addMultiReference(String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject) {
		AbstractPropertyDefinition referencePropertyDefinition = new AbstractPropertyDefinition(this, name, key, specificType, title, referencedObject, true);
		addProperty(referencePropertyDefinition);
	}

	public void addProperty(PropertyDefinition field) {
		if (definitionByName.containsKey(field.getName()) || definitionByKey.containsKey(field.getKey())) {
			throw new RuntimeException("Object property already contains field with this name or key:" + field);
		}
		definitions.add(field);
		definitionByKey.put(field.getKey(), field);
		definitionByName.put(field.getName(), field);
	}

	@Override
	public ObjectPropertyDefinition getParent() {
		return null;
	}

	@Override
	public String getQualifiedName() {
		return getName() + "-" + objectUuid;
	}

	@Override
	public String getModelUuid() {
		return objectUuid;
	}

	@Override
	public short getModelVersion() {
		return modelVersion;
	}

	@Override
	public ObjectPropertyDefinition getObjectPropertyDefinition() {
		return this;
	}

	@Override
	public List<PropertyDefinition> getPropertyDefinitions() {
		return definitions;
	}

	@Override
	public PropertyDefinition getPropertyDefinitionByKey(int key) {
		return definitionByKey.get(key);
	}

	@Override
	public PropertyDefinition getPropertyDefinitionByName(String name) {
		return definitionByName.get(name);
	}
}
