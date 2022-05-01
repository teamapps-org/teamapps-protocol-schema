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
		ReferencePropertyDefinition referencePropertyDefinition = new ReferencePropertyDefinition(this, name, key, specificType, title, referencedObject, false);
		addProperty(referencePropertyDefinition);
	}

	public void addMultiReference(String name, int key, ObjectPropertyDefinition referencedObject) {
		addMultiReference(name, key, null, null, referencedObject);
	}

	public void addMultiReference(String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject) {
		ReferencePropertyDefinition referencePropertyDefinition = new ReferencePropertyDefinition(this, name, key, specificType, title, referencedObject, true);
		addProperty(referencePropertyDefinition);
	}

	public void addEnumProperty(String name, int key, String[] enumValues, String specificType, String title) {
		EnumPropertyDefinition enumPropertyDefinition = new EnumPropertyDefinition(this, name, key, enumValues, specificType, title);
		addProperty(enumPropertyDefinition);
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
	public byte[] toBytes() {
		return new byte[0];
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
