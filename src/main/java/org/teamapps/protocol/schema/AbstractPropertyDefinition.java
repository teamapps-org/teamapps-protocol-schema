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


public class AbstractPropertyDefinition implements PropertyDefinition {
	private final ObjectPropertyDefinition parent;
	private final String name;
	private final int key;
	private final PropertyType type;
	private final PropertyContentType contentType;
	private final String specificType;
	private final String title;

	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, PropertyType type, PropertyContentType contentType, String specificType, String title) {
		this.parent = parent;
		this.name = name;
		this.title = title;
		this.key = key;
		this.type = type;
		this.contentType = contentType;
		this.specificType = specificType;
	}

	@Override
	public ObjectPropertyDefinition getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public String getQualifiedName() {
		return parent.getQualifiedName() + "/" + name;
	}

	@Override
	public PropertyType getType() {
		return type;
	}

	@Override
	public PropertyContentType getContentType() {
		return contentType;
	}

	@Override
	public String getSpecificType() {
		return specificType;
	}

	@Override
	public boolean isReferenceProperty() {
		return type == PropertyType.OBJECT_SINGLE_REFERENCE || type == PropertyType.OBJECT_MULTI_REFERENCE;
	}

	@Override
	public boolean isEnumProperty() {
		return type == PropertyType.ENUM;
	}

	@Override
	public ReferencePropertyDefinition getAsReferencePropertyDefinition() {
		return null;
	}

	@Override
	public EnumPropertyDefinition getAsEnumPropertyDefinition() {
		return null;
	}

	public byte[] toBytes() {
		return new byte[0]; //TODO!!!
	}
}
