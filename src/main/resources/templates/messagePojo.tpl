package {package};

import org.teamapps.protocol.schema.*;
import org.teamapps.protocol.service.*;
import org.teamapps.protocol.file.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;


public class {type} extends MessageObject {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public final static Function<byte[], {type}> DECODER_FUNCTION = bytes -> {
		try {
			return new {type}(bytes);
		} catch (IOException e) {
			LOGGER.error("Error creating {type} instance", e);
		}
		return null;
	};

	private final static PojoObjectDecoder<{type}> decoder = (dis, fileProvider) -> {
		try {
			return new {type}(dis, fileProvider);
		} catch (IOException e) {
			LOGGER.error("Error creating {type} instance", e);
		}
		return null;
	};

	public static PojoObjectDecoder<{type}> getMessageDecoder() {
		return decoder;
	}

    public final static String OBJECT_UUID = "{uuid}";


	public {type}() {
		super({schema}.MODEL_COLLECTION.getModel(OBJECT_UUID));
	}

	public {type}(DataInputStream dis) throws IOException {
		super(dis, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), null, {schema}.MODEL_COLLECTION);
	}

	public {type}(DataInputStream dis, FileProvider fileProvider) throws IOException {
		super(dis, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), fileProvider, {schema}.MODEL_COLLECTION);
	}

	public {type}(byte[] bytes) throws IOException {
		super(bytes, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), null, {schema}.MODEL_COLLECTION);
	}

	public {type}(byte[] bytes, FileProvider fileProvider) throws IOException {
		super(bytes, {schema}.MODEL_COLLECTION.getModel(OBJECT_UUID), fileProvider, {schema}.MODEL_COLLECTION);
	}

{methods}

}