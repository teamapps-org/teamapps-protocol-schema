package {package};

import org.teamapps.protocol.message.*;
import org.teamapps.protocol.service.*;
import org.teamapps.protocol.file.*;
import java.io.IOException;

public abstract class {type} extends AbstractClusterService {

    public {type}(ServiceRegistry registry) {
        super(registry, "{serviceName}");
    }

{methods}

	@Override
	public byte[] handleMessage(String method, byte[] bytes, FileProvider fileProvider, FileSink fileSink) throws IOException {
		switch (method) {
{cases}
		}
		return null;
	}

}