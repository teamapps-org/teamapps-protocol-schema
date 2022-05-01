package org.teamapps.protocol.schema;

public class ReferencePropertyDefinition extends AbstractPropertyDefinition {

	private final ObjectPropertyDefinition referencedObject;
	private final boolean multiReference;

	public ReferencePropertyDefinition(ObjectPropertyDefinition parent, String name, int key, String specificType, String title, ObjectPropertyDefinition referencedObject, boolean multiReference) {
		super(parent, name, key, multiReference ? PropertyType.OBJECT_MULTI_REFERENCE : PropertyType.OBJECT_SINGLE_REFERENCE, PropertyContentType.GENERIC, specificType, title);
		this.referencedObject = referencedObject;
		this.multiReference = multiReference;
	}

	public ObjectPropertyDefinition getReferencedObject() {
		return referencedObject;
	}

	public boolean isMultiReference() {
		return multiReference;
	}

	@Override
	public ReferencePropertyDefinition getAsReferencePropertyDefinition() {
		return this;
	}
}
