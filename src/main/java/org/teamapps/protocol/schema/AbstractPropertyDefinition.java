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
import java.util.HashMap;
import java.util.Map;

public class AbstractPropertyDefinition implements PropertyDefinition {
	private final ObjectPropertyDefinition parent;
	private final String name;
	private final int key;
	private final PropertyType type;
	private final PropertyContentType contentType;
	private final String specificType;
	private final String title;
	private final ObjectPropertyDefinition referencedObject;
	private final boolean multiReference;


	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, PropertyType type, PropertyContentType contentType, String specificType, String title) {
		this.parent = parent;
		this.name = name;
		this.title = title;
		this.key = key;
		this.type = type;
		this.contentType = contentType;
		this.specificType = specificType;
		this.referencedObject = null;
		this.multiReference = false;
	}

	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject, boolean multiReference) {
		this.parent = parent;
		this.name = name;
		this.title = title;
		this.key = key;
		this.type = multiReference ? PropertyType.OBJECT_MULTI_REFERENCE : PropertyType.OBJECT_SINGLE_REFERENCE;
		this.contentType = PropertyContentType.GENERIC;
		this.specificType = specificType;
		this.referencedObject = referencedObject;
		this.multiReference = multiReference;
	}

	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, byte[] bytes, Map<String, MessageModel> readCache) throws IOException {
		this(parent, new DataInputStream(new ByteArrayInputStream(bytes)), readCache);
	}

	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, DataInputStream dis, Map<String, MessageModel> readCache) throws IOException {
		this.parent = parent;
		this.name = MessageUtils.readString(dis);
		this.key = dis.readInt();
		this.type = PropertyType.getById(dis.readInt());
		this.contentType = PropertyContentType.getById(dis.readInt());
		this.specificType = MessageUtils.readString(dis);
		this.title = MessageUtils.readString(dis);

		if (type == PropertyType.OBJECT_SINGLE_REFERENCE || type == PropertyType.OBJECT_MULTI_REFERENCE) {
			this.multiReference = dis.readBoolean();
			if (dis.readBoolean()) {
				String objectUuid = MessageUtils.readString(dis);
				this.referencedObject = readCache.get(objectUuid).getObjectPropertyDefinition();
			} else {
				this.referencedObject = new ObjectPropertyDefinition(dis, readCache);
			}
		} else {
			this.referencedObject = null;
			this.multiReference = false;
		}
	}

	public void write(DataOutputStream dos) throws IOException {
		write(dos, new HashMap<>());
	}

	public void write(DataOutputStream dos, Map<String, MessageModel> writeCache) throws IOException {
		MessageUtils.writeString(dos, name);
		dos.writeInt(key);
		dos.writeInt(type.getId());
		dos.writeInt(contentType.getId());
		MessageUtils.writeString(dos, specificType);
		MessageUtils.writeString(dos, title);
		if (isReferenceProperty()) {
			dos.writeBoolean(multiReference);
			if (writeCache.containsKey(referencedObject.getObjectUuid())) {
				dos.writeBoolean(true);
				MessageUtils.writeString(dos, referencedObject.getObjectUuid());
			} else {
				writeCache.put(referencedObject.getObjectUuid(), referencedObject);
				dos.writeBoolean(false);
				referencedObject.write(dos, writeCache);
			}
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
	public ObjectPropertyDefinition getReferencedObject() {
		return referencedObject;
	}

	@Override
	public boolean isMultiReference() {
		return multiReference;
	}
}
