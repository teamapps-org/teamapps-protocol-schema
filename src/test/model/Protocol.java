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
import org.teamapps.protocol.schema.*;

public class Protocol implements ModelCollectionProvider {
	@Override
	public ModelCollection getModelCollection() {
		MessageModelCollection modelCollection = new MessageModelCollection("newTestModel", "org.teamapps.protocol.test", 1);

		ObjectPropertyDefinition employee = modelCollection.createModel("employee", "fowefjweoiewjwo");
		employee.addProperty("firstName", 1, PropertyType.STRING);
		employee.addProperty("lastName", 2, PropertyType.STRING);
		employee.addProperty("pic", 3, PropertyType.BYTE_ARRAY);
		employee.addProperty("vegan", 4, PropertyType.BOOLEAN);

		ObjectPropertyDefinition company = modelCollection.createModel("company", "wvwegjwoie4n");
		company.addProperty("name", 1, PropertyType.STRING);
		company.addProperty("type", 2, PropertyType.STRING);
		company.addSingleReference("ceo", 3, employee);
		company.addMultiReference("employee", 4, employee);
		company.addProperty("picture", 5, PropertyType.FILE);

		ProtocolServiceSchema testService = modelCollection.createProtocolServiceSchema("testService");
		testService.addMethod("method1", company, employee);

		return modelCollection;
	}
}
