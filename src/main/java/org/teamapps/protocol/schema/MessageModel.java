package org.teamapps.protocol.schema;

import java.util.List;

public interface MessageModel {

	short getModelVersion();

	ObjectPropertyDefinition getObjectPropertyDefinition();

	List<PropertyDefinition> getPropertyDefinitions();

	PropertyDefinition getPropertyDefinitionByKey(int key);

	PropertyDefinition getPropertyDefinitionByName(String name);

	byte[] toBytes();
}
