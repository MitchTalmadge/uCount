package com.AptiTekk.Poll.core.utilities;

import javax.servlet.http.Cookie;

public class BanHelper {

	private final static String PREFIX = "BH_";

	public static void recordFailedAttempt(String banName) {
		int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
		if (failedAttempts == -1)
			SessionHelper.setSessionVariable(PREFIX + banName, 1);
		else
			SessionHelper.setSessionVariable(PREFIX + banName, ++failedAttempts);
	}

	public static int getNumberFailedAttempts(String banName) {
		int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
		return failedAttempts == -1 ? 0 : failedAttempts;
	}

	public static void clearFailedAttempts(String banName) {
		int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
		if (failedAttempts == -1)
			return;
		else
			SessionHelper.setSessionVariable(PREFIX + banName, 0);
	}

	public static boolean isUserBanned(String banName) {
		Cookie cookie = CookieHelper.getCookie(PREFIX + banName);
		if (cookie != null) {
			return cookie.getValue().equals("true");
		}
		return false;
	}

	public static void banUser(String banName, int banLengthHours) {
		CookieHelper.setCookie(PREFIX + banName, "true", banLengthHours * 60 * 60);
	}

	public static void unBanUser(String banName) {
		CookieHelper.deleteCookie(PREFIX + banName);
	}

}
