package org.BinghamTSA.uCount.core.utilities;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Used to get properties from the properties files in main.resources.org.BinghamTSA
 */
public class PropertiesHelper {

  private final static String APPLICATION_PROPERTIES_FILE = "org.BinghamTSA.application";
  private final static String STYLE_PROPERTIES_FILE = "org.BinghamTSA.style";

  /**
   * Gets a property from the Application properties file (application.properties)
   * 
   * @param key The key of the property
   * @return The value of the property, as a String
   */
  public static String getApplicationProperty(String key) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(APPLICATION_PROPERTIES_FILE);
    String value = resourceBundle.getString(key);
    return value.trim();
  }

  /**
   * Gets a property from the Style properties file (style.properties)
   * 
   * @param key The key of the property
   * @return The value of the property, as a String
   */
  public static String getStyleProperty(String key) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(STYLE_PROPERTIES_FILE);
    String value = resourceBundle.getString(key);
    return value.trim();
  }

}
