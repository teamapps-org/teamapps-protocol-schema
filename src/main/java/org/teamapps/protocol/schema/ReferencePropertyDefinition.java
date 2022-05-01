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

public class ReferencePropertyDefinition extends AbstractPropertyDefinition {

	private final ObjectPropertyDefinition referencedObject;
	private final boolean multiReference;

	public ReferencePropertyDefinition(ObjectPropertyDefinition parent, String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject, boolean multiReference) {
		super(parent, name, key, multiReference ? PropertyType.OBJECT_MULTI_REFERENCE : PropertyType.OBJECT_SINGLE_REFERENCE, PropertyContentType.GENERIC, specificType, title);
		this.referencedObject = referencedObject;
		this.multiReference = multiReference;
	}

	public ObjectPropertyDefinition getReferencedObject() {
		return referencedObject;
	}

	public boolean isMultiReference() {
		return multiReference;
	}

	@Override
	public ReferencePropertyDefinition getAsReferencePropertyDefinition() {
		return this;
	}
}
