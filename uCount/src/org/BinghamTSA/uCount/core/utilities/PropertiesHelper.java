package org.BinghamTSA.uCount.core.utilities;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class PropertiesHelper {

	private final static String APPLICATION_PROPERTIES_FILE = "com.AptiTekk.application";

	public static String getApplicationProperty(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(APPLICATION_PROPERTIES_FILE);
		String value = resourceBundle.getString(key);
		return value.trim();
	}

}
