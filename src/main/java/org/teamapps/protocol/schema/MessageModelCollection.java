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

	public ObjectPropertyDefinition createModel(String name, String uuid, int getModelVersion, String title, String specificType) {
		ObjectPropertyDefinition definition = new ObjectPropertyDefinition(uuid, name, title, specificType, version);
		addModel(definition);
		return definition;
	}

	public void addModel(MessageModel model) {
		models.add(model);
		modelByKey.put(model.getObjectPropertyDefinition().getObjectUuid(), model);
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
