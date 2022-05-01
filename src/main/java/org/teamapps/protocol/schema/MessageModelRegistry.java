package org.teamapps.protocol.schema;

import org.teamapps.protocol.message.MessageUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageModelRegistry implements ModelRegistry, PojoObjectDecoderRegistry {

	private Map<String, List<MessageModel>> modelsByObjectUuid = new ConcurrentHashMap<>();
	private Map<String, MessageModel> latestModelByObjectUuid = new ConcurrentHashMap<>();
	private Set<String> allModelKeys = new HashSet<>();
	private List<MessageModel> allModels = new ArrayList<>();
	private Map<String, PojoObjectDecoder<? extends MessageObject>> decoderByUuid = new ConcurrentHashMap<>();

	@Override
	public void mergeRegistry(ModelRegistry registry) {
		registry.getAllModels().forEach(this::addModel);
	}

	@Override
	public List<MessageModel> getLatestModels() {
		return new ArrayList<>(latestModelByObjectUuid.values());
	}

	@Override
	public List<MessageModel> getAllModels() {
		return new ArrayList<>(allModels);
	}

	@Override
	public List<MessageModel> getModelVersions(String uuid) {
		return modelsByObjectUuid.get(uuid);
	}

	@Override
	public MessageModel getModel(String uuid, short modelVersion) {
		MessageModel messageModel = latestModelByObjectUuid.get(uuid);
		if (messageModel != null && messageModel.getModelVersion() == modelVersion) {
			return messageModel;
		} else {
			return null;
		}
	}

	@Override
	public MessageModel getLatestModel(String uuid) {
		return latestModelByObjectUuid.get(uuid);
	}

	@Override
	public MessageModel getModel(byte[] message) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(message));
		String objectUuid = MessageUtils.readString(dis);
		short modelVersion = dis.readShort();
		return getModel(objectUuid, modelVersion);
	}

	@Override
	public PropertyDefinition getPropertyDefinition(String qualifiedName) {
		//todo
		return null;
	}

	@Override
	public ModelRegistry addModel(MessageModel model) {
		String objectUuid = model.getObjectPropertyDefinition().getObjectUuid();
		short modelVersion = model.getModelVersion();
		String key = objectUuid + modelVersion;
		if (allModelKeys.contains(key)) {
			return this;
		}
		allModelKeys.add(key);
		List<MessageModel> messageModels = modelsByObjectUuid.get(objectUuid);
		if (messageModels != null) {
			boolean contained = false;
			short lastVersion = 0;
			for (MessageModel messageModel : messageModels) {
				if (messageModel.getModelVersion() == modelVersion) {
					contained = true;
					lastVersion = (short) Math.max(lastVersion, messageModel.getModelVersion());
				}
			}
			if (!contained) {
				messageModels.add(model);
				if (lastVersion < modelVersion) {
					latestModelByObjectUuid.put(key, model);
				}
			}

		} else {
			messageModels = new ArrayList<>();
			messageModels.add(model);
			modelsByObjectUuid.put(objectUuid, messageModels);
			latestModelByObjectUuid.put(objectUuid, model);
			allModels.add(model);
		}
		return this;
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
	public void addMessageDecoder(String uuid, PojoObjectDecoder<? extends MessageObject> decoder) {
		decoderByUuid.put(uuid, decoder);
	}

	@Override
	public byte[] toBytes() {
		return new byte[0];
	}
}
