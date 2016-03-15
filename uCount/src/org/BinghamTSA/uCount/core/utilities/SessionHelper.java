package org.BinghamTSA.uCount.core.utilities;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The SessionHelper is a convenience class for setting and clearing session variables.
 */
public class SessionHelper {

  /**
   * Sets a session variable with a given name and value.
   * @param name The name of the variable.
   * @param value The value of the variable.
   */
  public static void setSessionVariable(String name, Object value) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getExternalContext().getSessionMap().put(name, value);
  }

  /**
   * Gets a session variable as a String object.
   * @param name The name of the variable.
   * @return The value of the variable, as a String.
   */
  public static String getSessionVariableAsString(String name) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return (String) facesContext.getExternalContext().getSessionMap().get(name);
  }

  /**
   * Gets a session variable as a primitive int.
   * @param name The name of the variable.
   * @return The value of the variable, as an int.
   */
  public static int getSessionVariableAsInt(String name) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Object value = facesContext.getExternalContext().getSessionMap().get(name);
    if (value == null)
      return -1;
    if (value instanceof String) {
      try {
        return Integer.parseInt((String) value);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        return -1;
      }
    }
    return (int) value;
  }
}
