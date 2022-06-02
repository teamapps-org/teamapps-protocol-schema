package {package};

import org.teamapps.protocol.schema.*;
import org.teamapps.protocol.service.*;
import org.teamapps.protocol.file.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.io.*;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import java.util.*;


public class {type} extends MessageObject {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final static PojoObjectDecoder<{type}> decoder = new PojoObjectDecoder<{type}>() {
		@Override
		public {type} decode(DataInputStream dis, FileProvider fileProvider) {
			try {
				return new {type}(dis, fileProvider);
			} catch (IOException e) {
				LOGGER.error("Error creating {type} instance", e);
			}
			return null;
		}

		@Override
		public {type} decode(ByteBuf buf, FileProvider fileProvider) {
			try {
				return new {type}(buf, fileProvider);
			} catch (IOException e) {
				LOGGER.error("Error creating {type} instance", e);
			}
			return null;
		}

		@Override
		public {type} remap(MessageObject message) {
			return new {type}(message, {schema}.MODEL_COLLECTION);
		}

        @Override
        public String getMessageObjectUuid() {
            return OBJECT_UUID;
        }
	};

	public static PojoObjectDecoder<{type}> getMessageDecoder() {
		return decoder;
	}

	public static MessageModel getMessageModel() {
        return {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID);
    }

	public static ModelCollection getModelCollection() {
		return {schema}.MODEL_COLLECTION;
	}

    public static {type} remap(MessageObject message) {
        return new {type}(message, {schema}.MODEL_COLLECTION);
    }

    public final static String OBJECT_UUID = "{uuid}";


	public {type}() {
		super({schema}.MODEL_COLLECTION.getModel(OBJECT_UUID));
	}

	public {type}(MessageObject message, PojoObjectDecoderRegistry pojoObjectDecoderRegistry) {
		super(message, pojoObjectDecoderRegistry);
	}

	public {type}(DataInputStream dis) throws IOException {
		super(dis, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), null, {schema}.MODEL_COLLECTION);
	}

	public {type}(DataInputStream dis, FileProvider fileProvider) throws IOException {
		super(dis, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), fileProvider, {schema}.MODEL_COLLECTION);
	}

	public {type}(ByteBuf buf, FileProvider fileProvider) throws IOException {
		super(buf, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), fileProvider, {schema}.MODEL_COLLECTION);
	}

	public {type}(byte[] bytes) throws IOException {
		super(bytes, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), null, {schema}.MODEL_COLLECTION);
	}

	public {type}(byte[] bytes, FileProvider fileProvider) throws IOException {
		super(bytes, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), fileProvider, {schema}.MODEL_COLLECTION);
	}

{methods}

}