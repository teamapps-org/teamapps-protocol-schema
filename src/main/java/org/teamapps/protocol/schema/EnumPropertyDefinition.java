package org.teamapps.protocol.schema;

public class EnumPropertyDefinition extends AbstractPropertyDefinition {

	public final String[] enumValues;

	public EnumPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, String[] enumValues, String specificType, String title) {
		super(parent, name, key, PropertyType.ENUM, PropertyContentType.GENERIC, specificType, title);
		this.enumValues = enumValues;
	}

	public String[] getEnumValues() {
		return enumValues;
	}

	@Override
	public EnumPropertyDefinition getAsEnumPropertyDefinition() {
		return this;
	}
}
