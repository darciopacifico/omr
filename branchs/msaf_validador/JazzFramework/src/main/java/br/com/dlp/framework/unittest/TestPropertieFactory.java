package br.com.dlp.framework.unittest;

public class TestPropertieFactory {
	public static void main(String[] args) {

		TestPropertieFactory factory = new TestPropertieFactory();

		System.out.println("x:"
				+ factory.getFirstNestedProperty("darcio.lopes.pacifico"));
		System.out.println("x:"
				+ factory.getRestNestedProperty("darcio.lopes.pacifico"));
	}

	protected String getFirstNestedProperty(String nestedProperty) {
		int pointByte = nestedProperty.indexOf(".");

		String returnValue;

		if (pointByte == -1) {
			returnValue = nestedProperty;
		} else {
			returnValue = nestedProperty.substring(0, pointByte);
		}

		return returnValue;
	}

	protected String getRestNestedProperty(String nestedProperty) {
		int pointByte = nestedProperty.indexOf(".");

		String returnValue;

		if (pointByte == -1) {
			returnValue = "";
		} else {
			returnValue = nestedProperty.substring(pointByte + 1);
		}

		return returnValue;
	}

}
