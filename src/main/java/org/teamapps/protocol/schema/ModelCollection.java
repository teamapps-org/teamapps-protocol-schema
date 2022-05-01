package org.teamapps.protocol.schema;

import java.util.List;

public interface ModelCollection extends PojoObjectDecoderRegistry{
	String getName();

	short getVersion();

	String getNamespace();

	MessageModel getModel(String uuid);

	List<MessageModel> getModels();

	byte[] toBytes();
}
