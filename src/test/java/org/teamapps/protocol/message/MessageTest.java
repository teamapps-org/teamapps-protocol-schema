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

import org.junit.Test;
import org.teamapps.protocol.testmodel.Airplane;
import org.teamapps.protocol.testmodel.Airport;
import org.teamapps.protocol.testmodel.Location;
import org.teamapps.protocol.testmodel.TestModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageTest {


	@Test
	public void testMessage() {
		MessageSchema messageSchema = new MessageSchema(1, "testSchema", "org.test");
		MessageField testObject = messageSchema.addObject("testObject");
		MessageField nameField = messageSchema.addTextField(testObject, "name");
		MessageField valueField = messageSchema.addIntField(testObject, "value");
		MessageField peerField = messageSchema.addSingleReference(testObject, testObject, "peer");
		MessageField peersField = messageSchema.addMultiReference(testObject, testObject, "peers");

		assertEquals("testSchema", messageSchema.getName());
		assertEquals("testObject", testObject.getPath(messageSchema));
		assertEquals("testObject/name", nameField.getPath(messageSchema));
		assertEquals("testObject/value", valueField.getPath(messageSchema));
		assertEquals("testObject/peer", peerField.getPath(messageSchema));
		assertEquals("testObject/peers", peersField.getPath(messageSchema));

		List<Message> messages = new ArrayList<>();


		int size = 1_000;
		for (int i = 0; i < size; i++) {
			Message message = new Message(testObject);
			message.setPropertyValue(nameField, "name" + i);
			message.setPropertyValue(valueField, i);
			messages.add(message);
		}

		for (int i = 0; i < size; i++) {
			Message message = messages.get(i);
			assertEquals("name" + i, message.getStringValue(nameField.getName()));
			assertEquals(i, message.getIntValue(valueField.getName()));
		}

		for (int i = 1; i < size; i++) {
			Message parentMessage = messages.get(i - 1);
			Message message = messages.get(i);
			parentMessage.setPropertyValue(peerField.getName(), message);
			parentMessage.addMultiReference(peersField.getName(), message);
		}

		for (int i = 0; i < size - 1; i++) {
			Message message = messages.get(i);
			int intValue = message.getMessageList(peersField.getName()).get(0).getIntValue(valueField.getName());
			assertEquals(i + 1, intValue);
		}

		Message part = messages.get(0);
		for (int i = 1; i < size; i++) {
			List<Message> peers = part.getMessageObjectValue(peersField.getName());
			assertEquals(1, peers.size());
			Message peer = peers.get(0);
			assertEquals(i, peer.getIntValue(valueField.getName()));
			part = peer;
		}
	}

	@Test
	public void testSchema() throws IOException {
		Airplane airplane = new Airplane().setName("Airbus A380");
		Airplane airplane2 = new Airplane().setName("Boing 747");

		Airport airport = new Airport().setName("Frankfurt").setLocation(new Location().setLatitude(5.435f).setLongitude(48.9f));
		Airport airport2 = new Airport().setName("London").setLocation(new Location().setLatitude(12.245f).setLongitude(38.7534f));

		assertEquals(48.9f, airport.getLocation().getLongitude(), 0f);

		Airport airportCopy = new Airport(airport.toBytes());
		assertEquals(48.9f, airportCopy.getLocation().getLongitude(), 0f);

		Message message = new Message(airportCopy.toBytes(), TestModel.SCHEMA);
		assertEquals(48.9f, message.getMessageObject("location").getFloatValue("longitude"), 0.f);

	}

	@Test
	public void testX() throws IOException {
		int size = 10_000;
		for (int i = 0; i < size; i++) {
			Airport airport = new Airport().setName("Frankfurt").setLocation(new Location().setLatitude(5.435f).setLongitude(48.9f));
			byte[] bytes = airport.toBytes();
			Airport airport2 = new Airport(bytes);
			assertEquals(48.9f, airport2.getLocation().getLongitude(), 0f);
		}
	}
}
