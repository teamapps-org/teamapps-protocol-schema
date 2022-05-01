import org.teamapps.protocol.schema.*;

public class Protocol implements ModelCollectionProvider {
	@Override
	public ModelCollection getModelCollection() {
		MessageModelCollection modelCollection = new MessageModelCollection("newTestModel", "org.teamapps.protocol.test", 1);

		ObjectPropertyDefinition employee = modelCollection.createModel("employee", "fowefjweoiewjwo");
		employee.addProperty("firstName", 1, PropertyType.STRING);
		employee.addProperty("lastName", 2, PropertyType.STRING);
		employee.addProperty("pic", 3, PropertyType.BYTE_ARRAY);
		employee.addProperty("vegan", 4, PropertyType.BOOLEAN);

		ObjectPropertyDefinition company = modelCollection.createModel("company", "wvwegjwoie4n");
		company.addProperty("name", 1, PropertyType.STRING);
		company.addProperty("type", 2, PropertyType.STRING);
		company.addSingleReference("ceo", 3, employee);
		company.addMultiReference("employee", 4, employee);

		return modelCollection;
	}
}
