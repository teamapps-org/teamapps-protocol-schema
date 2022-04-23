package {package};

import org.teamapps.protocol.message.*;
import org.teamapps.protocol.service.*;
import org.teamapps.protocol.file.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class {type} extends AbstractClusterServiceClient {

    public {type}(ServiceRegistry registry) {
        super(registry, "{serviceName}");
    }

{methods}

}