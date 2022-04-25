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
import org.teamapps.protocol.message.MessageField;
import org.teamapps.protocol.message.MessageModelSchemaProvider;
import org.teamapps.protocol.message.MessageSchema;

public class MessageModel implements MessageModelSchemaProvider {

	@Override
	public MessageSchema getSchema() {
		MessageSchema schema = new MessageSchema(10, "testModel", "org.teamapps.protocol.testmodel");

		MessageField location = schema.addObject("Location");
		schema.addFloatField(location, "latitude");
		schema.addFloatField(location, "longitude");

		MessageField airport = schema.addObject("airport");
		schema.addTextField(airport, "name");
		schema.addSingleReference(airport, location, "location");

		MessageField airplane = schema.addObject("airplane");
		schema.addTextField(airplane, "name");

		MessageField person = schema.addObject("person");
		schema.addTextField(person, "firstName");
		schema.addTextField(person, "lastName");
		schema.addTextField(person, "city");
		schema.addTextField(person, "country");
		schema.addTextField(person, "phone");

		MessageField seat = schema.addObject("seat");
		schema.addTextField(seat, "name");
		schema.addIntField(seat, "row");
		schema.addBooleanField(seat, "businessClass");

		MessageField booking = schema.addObject("booking");
		schema.addSingleReference(booking, person, "person");
		schema.addSingleReference(booking, seat, "seat");


		MessageField flight = schema.addObject("flight");
		schema.addTextField(flight, "name");
		schema.addTextField(flight, "country");
		schema.addSingleReference(flight, airport, "airportStart");
		schema.addSingleReference(flight, airport, "airportEnd");
		schema.addSingleReference(flight, airplane, "airplane");
		schema.addLongField(flight, "dateTime");
		schema.addMultiReference(flight, booking, "bookings");

		return schema;
	}
}
