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


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public interface PropertyDefinition {

	ObjectPropertyDefinition getParent();

	String getName();

	String getTitle();

	int getKey();

	String getQualifiedName();

	PropertyType getType();

	PropertyContentType getContentType();

	String getSpecificType();

	boolean isReferenceProperty();

	ObjectPropertyDefinition getReferencedObject();

	boolean isMultiReference();

	boolean isEnumProperty();

	void write(DataOutputStream dos) throws IOException;

	void write(DataOutputStream dos, Map<String, MessageModel> writeCache) throws IOException;

	byte[] toBytes() throws IOException;

}
