/*-
 * ========================LICENSE_START=================================
 * TeamApps Protocol Schema
 * ---
 * Copyright (C) 2022 TeamApps.org
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
package org.teamapps.protocol.schema;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MessagePojoBuilder {

	public static void createPojos(ModelCollection modelCollection, File directory) throws IOException {
		File dir = directory;
		String namespace = modelCollection.getNamespace();
		for (String name : namespace.split("\\.")) {
			dir = new File(dir, name);
			dir.mkdir();
		}
		System.out.println("Create source in path: " + dir.getPath());
		//createServiceClasses(modelCollection, dir);
		createSchemaPojo(modelCollection, dir);
		for (MessageModel model : modelCollection.getModels()) {
			createMessagePojoSave(modelCollection, model, dir);
		}
	}

	private static void createSchemaPojo(ModelCollection modelCollection, File directory) throws IOException {
		String tpl = readTemplate("messageCollection.tpl");
		tpl = setValue(tpl, "package", modelCollection.getNamespace());
		tpl = setValue(tpl, "type", firstUpperCase(modelCollection.getName()));
		tpl = setValue(tpl, "version", "" + modelCollection.getVersion());
		tpl = setValue(tpl, "name", modelCollection.getName());

		StringBuilder data = new StringBuilder();
		StringBuilder registry = new StringBuilder();

		for (MessageModel model : modelCollection.getModels()) {
			ObjectPropertyDefinition objDef = model.getObjectPropertyDefinition();
			String objName = objDef.getName();
			data.append(getTabs(2))
					.append("ObjectPropertyDefinition ")
					.append(objName)
					.append(" = MODEL_COLLECTION.createModel(")
					.append(withQuotes(objName)).append(", ")
					.append(withQuotes(objDef.getObjectUuid())).append(", ")
					.append(model.getModelVersion()).append(", ")
					.append(withQuotes(objDef.getTitle())).append(", ")
					.append(withQuotes(objDef.getSpecificType()))
					.append(");\n");
			for (PropertyDefinition propDef : model.getPropertyDefinitions()) {
				if (propDef.isReferenceProperty()) {
					String method = propDef.getType() == PropertyType.OBJECT_SINGLE_REFERENCE ? "addSingleReference" : "addMultiReference";
					data.append(getTabs(2))
							.append(objName)
							.append(".").append(method).append("(")
							.append(withQuotes(propDef.getName())).append(", ")
							.append(propDef.getKey()).append(", ")
							.append(withQuotes(propDef.getSpecificType())).append(", ")
							.append(withQuotes(propDef.getTitle())).append(", ")
							.append(propDef.getReferencedObject().getName())
							.append(");\n");
				} else {
					data.append(getTabs(2))
							.append(objName)
							.append(".addProperty(")
							.append(withQuotes(propDef.getName())).append(", ")
							.append(propDef.getKey()).append(", ")
							.append("PropertyType.").append(propDef.getType()).append(", ")
							.append("PropertyContentType.").append(propDef.getContentType()).append(", ")
							.append(withQuotes(propDef.getSpecificType())).append(", ")
							.append(withQuotes(propDef.getTitle()))
							.append(");\n");
				}

			}
			registry.append(getTabs(2))
					.append("MODEL_COLLECTION.addMessageDecoder(")
					.append(objName).append(".getObjectUuid(), ")
					.append(firstUpperCase(objName))
					.append(".getMessageDecoder());\n");
		}

		tpl = setValue(tpl, "data", data.toString());
		tpl = setValue(tpl, "registry", registry.toString());

		File file = new File(directory, firstUpperCase(modelCollection.getName()) + ".java");
		Files.writeString(file.toPath(), tpl);
	}

	private static void createMessagePojoSave(ModelCollection modelCollection, MessageModel field, File directory) {
		try {
			createMessagePojo(modelCollection, field, directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createMessagePojo(ModelCollection modelCollection, MessageModel model, File directory) throws IOException {
		ObjectPropertyDefinition objDef = model.getObjectPropertyDefinition();
		String tpl = readTemplate("messagePojo.tpl");
		tpl = setValue(tpl, "package", modelCollection.getNamespace());
		tpl = setValue(tpl, "type", firstUpperCase(objDef.getName()));
		tpl = setValue(tpl, "schema", firstUpperCase(modelCollection.getName()));
		tpl = setValue(tpl, "version", "" + model.getModelVersion());
		tpl = setValue(tpl, "uuid", objDef.getObjectUuid());
		StringBuilder data = new StringBuilder();

		for (PropertyDefinition propDef : objDef.getPropertyDefinitions()) {
			String objectReferenceWithType = propDef.isReferenceProperty() ? "AsType" : "";
			data.append(getTabs(1))
					.append("public ")
					.append(getReturnType(propDef))
					.append(" ").append(propDef.getType() == PropertyType.BOOLEAN ? "is" : "get")
					.append(firstUpperCase(propDef.getName())).append("() {\n")
					.append(getTabs(2))
					.append("return get").append(getGetterSetterMethodName(propDef)).append(objectReferenceWithType).append("(")
					.append(withQuotes(propDef.getName())).append(");\n")
					.append(getTabs(1))
					.append("}\n\n");

			data.append(getTabs(1))
					.append("public ")
					.append(firstUpperCase(objDef.getName())).append(" ")
					.append("set")
					.append(firstUpperCase(propDef.getName())).append("(")
					.append(getReturnType(propDef)).append(" value) {\n")
					.append(getTabs(2))
					.append("set").append(getGetterSetterMethodName(propDef)).append(objectReferenceWithType).append("(")
					.append(withQuotes(propDef.getName())).append(", value);\n")
					.append(getTabs(2))
					.append("return this;\n")
					.append(getTabs(1))
					.append("}\n\n");

			if (propDef.getType() == PropertyType.OBJECT_MULTI_REFERENCE) {
				data.append(getTabs(1))
						.append("public ")
						.append(firstUpperCase(objDef.getName())).append(" ")
						.append("add")
						.append(firstUpperCase(propDef.getName())).append("(")
						.append(firstUpperCase(propDef.getReferencedObject().getName())).append(" value) {\n")
						.append(getTabs(2))
						.append("addReference").append("(")
						.append(withQuotes(propDef.getName())).append(", value);\n")
						.append(getTabs(2))
						.append("return this;\n")
						.append(getTabs(1))
						.append("}\n\n");
			}

			if (propDef.getType() == PropertyType.STRING_ARRAY) {
				//todo getter and setter with List<String> --> getXxxAsList
			}
		}

		tpl = setValue(tpl, "methods", data.toString());
		File file = new File(directory, firstUpperCase(objDef.getName()) + ".java");
		Files.writeString(file.toPath(), tpl);
		System.out.println("Write pojo:" + file.getPath());
	}

	private static String readTemplate(String name) throws IOException {
		InputStream inputStream = MessagePojoBuilder.class.getResourceAsStream("/templates/" + name);
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}

	private static String setValue(String template, String name, String value) {
		return template.replace("{" + name + "}", value);
	}

	private static String firstUpperCase(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private static String getTabs(int count) {
		return "\t".repeat(count);
	}

	private static String withQuotes(String value) {
		return value != null ? "\"" + value + "\"" : "null";
	}

	private static String getReturnType(PropertyDefinition propDef) {
		return switch (propDef.getType()) {
			case OBJECT -> firstUpperCase(propDef.getName());
			case OBJECT_SINGLE_REFERENCE ->
					firstUpperCase(propDef.getReferencedObject().getName());
			case OBJECT_MULTI_REFERENCE ->
					"List<" + firstUpperCase(propDef.getReferencedObject().getName()) + ">";
			case BOOLEAN -> "boolean";
			case BYTE -> "byte";
			case INT -> "int";
			case LONG -> "long";
			case FLOAT -> "float";
			case DOUBLE -> "double";
			case STRING -> "String";
			case BITSET -> "BitSet";
			case BYTE_ARRAY -> "byte[]";
			case INT_ARRAY -> "int[]";
			case LONG_ARRAY -> "long[]";
			case FLOAT_ARRAY -> "float[]";
			case DOUBLE_ARRAY -> "double[]";
			case STRING_ARRAY -> "String[]";
			case FILE -> "File";
			case ENUM -> firstUpperCase(propDef.getName());
		};
	}

	private static String getGetterSetterMethodName(PropertyDefinition propDef) {
		return switch (propDef.getType()) {
			case OBJECT -> "MessageObject";
			case OBJECT_SINGLE_REFERENCE -> "ReferencedObject";
			case OBJECT_MULTI_REFERENCE -> "ReferencedObjects";
			case BOOLEAN -> "BooleanProperty";
			case BYTE -> "ByteProperty";
			case INT -> "IntProperty";
			case LONG -> "LongProperty";
			case FLOAT -> "FloatProperty";
			case DOUBLE -> "DoubleProperty";
			case STRING -> "StringProperty";
			case BITSET -> "BitSetProperty";
			case BYTE_ARRAY -> "ByteArrayProperty";
			case INT_ARRAY -> "IntArrayProperty";
			case LONG_ARRAY -> "LongArrayProperty";
			case FLOAT_ARRAY -> "FloatArrayProperty";
			case DOUBLE_ARRAY -> "DoubleArrayProperty";
			case STRING_ARRAY -> "StringArrayProperty";
			case FILE -> "FileProperty";
			case ENUM -> "IntProperty";
		};
	}


}
