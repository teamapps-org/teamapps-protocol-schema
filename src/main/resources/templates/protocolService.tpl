package {package};

import org.teamapps.protocol.schema.*;
import org.teamapps.protocol.file.*;
import java.io.IOException;

public abstract class {type} extends AbstractClusterService {

    public {type}() {
        super("{serviceName}");
    }

    public {type}(ClusterServiceRegistry registry) {
        super(registry, "{serviceName}");
    }

{methods}
	@Override
	public MessageObject handleMessage(String method, MessageObject request) {
		switch (method) {
{cases}
		}
		return null;
	}

}