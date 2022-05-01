package org.teamapps.protocol.schema;

import java.io.IOException;
import java.util.List;

public interface ModelRegistry {

	void mergeRegistry(ModelRegistry registry);

	List<MessageModel> getLatestModels();

	List<MessageModel> getAllModels();

	List<MessageModel> getModelVersions(String uuid);

	MessageModel getModel(String uuid, short modelVersion);

	MessageModel getLatestModel(String uuid);

	MessageModel getModel(byte[] message) throws IOException;

	PropertyDefinition getPropertyDefinition(String qualifiedName);

	ModelRegistry addModel(MessageModel model);

	byte[] toBytes();

}
