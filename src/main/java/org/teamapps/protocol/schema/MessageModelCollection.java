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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageModelCollection implements ModelCollection {

	private final String name;
	private final String namespace;
	private final int version;
	private final List<MessageModel> models = new ArrayList<>();
	private final Map<String, MessageModel> modelByKey = new ConcurrentHashMap<>();
	private Map<String, PojoObjectDecoder<? extends MessageObject>> decoderByUuid = new ConcurrentHashMap<>();
	private List<ProtocolServiceSchema> protocolServiceSchemas = new ArrayList<>();

	public MessageModelCollection(String name, String namespace, int version) {
		this.name = name;
		this.namespace = namespace;
		this.version = version;
	}

	public ObjectPropertyDefinition createModel(String name, String uuid) {
		ObjectPropertyDefinition definition = new ObjectPropertyDefinition(uuid, name, version);
		addModel(definition);
		return definition;
	}

	public ObjectPropertyDefinition createModel(String name, String uuid, String title, String specificType) {
		ObjectPropertyDefinition definition = new ObjectPropertyDefinition(uuid, name, title, specificType, version);
		addModel(definition);
		return definition;
	}

	public ObjectPropertyDefinition createModel(String name, String uuid, int modelVersion, String title, String specificType) {
		ObjectPropertyDefinition definition = new ObjectPropertyDefinition(uuid, name, title, specificType, modelVersion);
		addModel(definition);
		return definition;
	}

	public void addModel(MessageModel model) {
		models.add(model);
		modelByKey.put(model.getObjectPropertyDefinition().getObjectUuid(), model);
	}

	public ProtocolServiceSchema createProtocolServiceSchema(String serviceName) {
		ProtocolServiceSchema serviceSchema = new ProtocolServiceSchema(serviceName);
		protocolServiceSchemas.add(serviceSchema);
		return serviceSchema;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public short getVersion() {
		return (short) version;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public MessageModel getModel(String uuid) {
		return modelByKey.get(uuid);
	}

	@Override
	public List<MessageModel> getModels() {
		return models;
	}

	@Override
	public ModelRegistry createRegistry() {
		return new MessageModelRegistry(this);
	}

	@Override
	public List<ProtocolServiceSchema> getProtocolServiceSchemas() {
		return protocolServiceSchemas;
	}

	@Override
	public void addMessageDecoder(String uuid, PojoObjectDecoder<? extends MessageObject> decoder) {
		decoderByUuid.put(uuid,decoder);
	}

	@Override
	public PojoObjectDecoder<? extends MessageObject> getMessageDecoder(String uuid) {
		return decoderByUuid.get(uuid);
	}

	@Override
	public boolean containsDecoder(String uuid) {
		return decoderByUuid.containsKey(uuid);
	}

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}
}
