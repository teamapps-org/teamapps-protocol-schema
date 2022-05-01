package org.teamapps.protocol.schema;


public class AbstractPropertyDefinition implements PropertyDefinition {
	private final ObjectPropertyDefinition parent;
	private final String name;
	private final int key;
	private final PropertyType type;
	private final PropertyContentType contentType;
	private final String specificType;
	private final String title;

	public AbstractPropertyDefinition(ObjectPropertyDefinition parent, String name, int key, PropertyType type, PropertyContentType contentType, String specificType, String title) {
		this.parent = parent;
		this.name = name;
		this.title = title;
		this.key = key;
		this.type = type;
		this.contentType = contentType;
		this.specificType = specificType;
	}

	@Override
	public ObjectPropertyDefinition getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getKey() {
		return key;
	}

	@Override
	public String getQualifiedName() {
		return parent.getQualifiedName() + "/" + name;
	}

	@Override
	public PropertyType getType() {
		return type;
	}

	@Override
	public PropertyContentType getContentType() {
		return contentType;
	}

	@Override
	public String getSpecificType() {
		return specificType;
	}

	@Override
	public boolean isReferenceProperty() {
		return type == PropertyType.OBJECT_SINGLE_REFERENCE || type == PropertyType.OBJECT_MULTI_REFERENCE;
	}

	@Override
	public boolean isEnumProperty() {
		return type == PropertyType.ENUM;
	}

	@Override
	public ReferencePropertyDefinition getAsReferencePropertyDefinition() {
		return null;
	}

	@Override
	public EnumPropertyDefinition getAsEnumPropertyDefinition() {
		return null;
	}

	public byte[] toBytes() {
		return new byte[0]; //TODO!!!
	}
}
