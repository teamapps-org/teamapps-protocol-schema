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


public interface ClusterServiceRegistry {

	void registerService(AbstractClusterService clusterService);

	void registerModelCollection(ModelCollection modelCollection);

	boolean isServiceAvailable(String serviceName);

	<REQUEST extends MessageObject, RESPONSE extends MessageObject> RESPONSE executeServiceMethod(String serviceName, String method, REQUEST request, PojoObjectDecoder<RESPONSE> responseDecoder);
}
