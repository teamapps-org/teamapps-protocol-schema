package org.teamapps.protocol.schema;

public interface PojoObjectDecoderRegistry {

	PojoObjectDecoder<? extends MessageObject> getMessageDecoder(String uuid);

	boolean containsDecoder(String uuid);

	void addMessageDecoder(String uuid, PojoObjectDecoder<? extends MessageObject> decoder);


}
