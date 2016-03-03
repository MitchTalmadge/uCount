package org.BinghamTSA.uCount.core.utilities;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionHelper {

	public static void setSessionVariable(String name, Object value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getSessionMap().put(name, value);
	}

	public static String getSessionVariableAsString(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (String) facesContext.getExternalContext().getSessionMap().get(name);
	}

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