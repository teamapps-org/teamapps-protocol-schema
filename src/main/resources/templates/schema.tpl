package {package};

import org.teamapps.protocol.message.*;
import org.teamapps.protocol.service.*;
import org.teamapps.protocol.file.*;
import java.util.HashMap;
import java.util.Map;

public class {type} implements MessageDecoderRegistry {

    public static MessageSchema SCHEMA = new MessageSchema({id}, "{name}", "{package}");
    public static MessageDecoderRegistry REGISTRY = new {type}();
	private final static Map<Integer, MessageDecoder<? extends Message>> DECODERS = new HashMap<>();

    static {
{data}
{registry}
    }

	public MessageDecoder<? extends Message> getMessageDecoder(int id) {
		return DECODERS.get(id);
	}

	@Override
	public boolean containsDecoder(int id) {
		return DECODERS.containsKey(id);
	}

}