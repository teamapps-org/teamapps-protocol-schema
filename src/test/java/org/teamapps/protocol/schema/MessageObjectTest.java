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

import org.junit.Test;
import org.teamapps.protocol.test.Company;
import org.teamapps.protocol.test.Employee;

import java.io.IOException;

import static org.junit.Assert.*;

public class MessageObjectTest {

	@Test
	public void testMessage() throws IOException {
		ObjectPropertyDefinition user = new ObjectPropertyDefinition("first-model", "user", 1);
		user.addProperty("first", 1, PropertyType.STRING);
		user.addProperty("last", 2, PropertyType.STRING);
		user.addProperty("age", 3, PropertyType.INT);

		ObjectPropertyDefinition address = new ObjectPropertyDefinition("adr.model", "address", 1);
		address.addProperty("street", 1, PropertyType.STRING);
		address.addProperty("city", 2, PropertyType.STRING);
		address.addProperty("zip", 3, PropertyType.INT);

		user.addSingleReference("address", 4, null, "Address of user", address);
		user.addMultiReference("addresses", 5, null, "All addresses of the user", address);

		MessageObject userObject = new MessageObject(user);
		userObject.setStringProperty("first", "Tom");
		userObject.setStringProperty("last", "Miller");
		userObject.setIntProperty("age", 89);

		MessageObject value = new MessageObject(address).setStringProperty("street", "Main Street 4").setStringProperty("city", "New York");
		userObject.setReferencedObject("address", value);

		for (int i = 0; i < 10; i++) {
			value = new MessageObject(address).setStringProperty("street", "Street-" + i).setStringProperty("city", "City-" + i).setIntProperty("zip", i);
			userObject.addReference("addresses", value);
		}

		assertEquals("Tom", userObject.getStringProperty("first"));
		assertEquals("Miller", userObject.getStringProperty("last"));
		assertEquals("Main Street 4", userObject.getReferencedObject("address").getStringProperty("street"));
		assertEquals(10, userObject.getReferencedObjects("addresses").size());

		byte[] bytes = userObject.toBytes();
		MessageObject messageObject = new MessageObject(bytes, user, null, null);

		assertEquals("Tom", messageObject.getStringProperty("first"));
		assertEquals("Miller", messageObject.getStringProperty("last"));
		assertEquals("Main Street 4", messageObject.getReferencedObject("address").getStringProperty("street"));
		assertEquals(10, messageObject.getReferencedObjects("addresses").size());

	}

	@Test
	public void testGenerated() throws IOException {
		int size = 10_000;
		for (int i = 0; i < size; i++) {
			Company company = new Company();
			company.setName("Test-Company");
			for (int n = 0; n < 10; n++) {
				company.addEmployee(new Employee().setFirstName("First-"+ n).setLastName("Last-" + n));
			}
			byte[] bytes = company.toBytes();
			Company p = new Company(bytes);
			assertEquals("Test-Company", p.getName());
			assertEquals(10, p.getEmployee().size());
		}
	}
}
