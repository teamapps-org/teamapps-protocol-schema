/*-
 * ========================LICENSE_START=================================
 * TeamApps Cluster
 * ---
 * Copyright (C) 2021 - 2022 TeamApps.org
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.teamapps.protocol.service;

import org.teamapps.protocol.file.FileProvider;
import org.teamapps.protocol.file.FileSink;

import java.io.IOException;

public abstract class AbstractClusterService {

	private final ServiceRegistry serviceRegistry;
	private final String serviceName;

	public AbstractClusterService(ServiceRegistry serviceRegistry, String serviceName) {
		this.serviceRegistry = serviceRegistry;
		this.serviceName = serviceName;
		serviceRegistry.registerService(this);
	}

	public String getServiceName() {
		return serviceName;
	}

	public abstract byte[] handleMessage(String method, byte[] bytes, FileProvider fileProvider, FileSink fileSink) throws IOException;
}
