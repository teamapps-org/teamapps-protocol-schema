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

public enum PropertyContentType {
	GENERIC(1),
	TIMESTAMP(2),
	DATE_TIME(3),
	DATE(4),
	TIME(5),
	GEO_LATITUDE(6),
	GEO_LONGITUDE(7),
	GEO_ALTITUDE(8),
	GEO_LONG_HASH(9),
	GEO_STRING_HASH(10),

	;

	private final int id;

	PropertyContentType(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}

	public static PropertyContentType getById(int id) {
		return switch (id) {
			case 1 -> GENERIC;
			case 2 -> TIMESTAMP;
			case 3 -> DATE_TIME;
			case 4 -> DATE;
			case 5 -> TIME;
			case 6 -> GEO_LATITUDE;
			case 7 -> GEO_LONGITUDE;
			case 8 -> GEO_ALTITUDE;
			case 9 -> GEO_LONG_HASH;
			case 10 -> GEO_STRING_HASH;
			default -> null;
		};
	}
}
