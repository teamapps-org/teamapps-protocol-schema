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

public class EnumPropertyDefinition extends AbstractPropertyDefinition {

	public final String[] enumValues;

	public EnumPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, String[] enumValues, String specificType, String title) {
		super(parent, name, key, PropertyType.ENUM, PropertyContentType.GENERIC, specificType, title);
		this.enumValues = enumValues;
	}

	public String[] getEnumValues() {
		return enumValues;
	}

	@Override
	public EnumPropertyDefinition getAsEnumPropertyDefinition() {
		return this;
	}
}
