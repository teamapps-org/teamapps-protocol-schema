package org.teamapps.protocol.schema;


public interface PropertyDefinition {

	ObjectPropertyDefinition getParent();

	String getName();

	String getTitle();

	int getKey();

	String getQualifiedName();

	PropertyType getType();

	PropertyContentType getContentType();

	String getSpecificType();

	boolean isReferenceProperty();

	boolean isEnumProperty();

	ReferencePropertyDefinition getAsReferencePropertyDefinition();

	EnumPropertyDefinition getAsEnumPropertyDefinition();

	byte[] toBytes();

}
