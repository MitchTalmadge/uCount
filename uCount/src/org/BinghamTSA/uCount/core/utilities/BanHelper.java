package org.BinghamTSA.uCount.core.utilities;

import javax.servlet.http.Cookie;

/**
 * The BanHelper is meant to be a convenience class for "banning" users from certain parts of the
 * site. It keeps track of the number of failed attempts, and can set cookies to "ban" the user.
 * 
 * An example of a use case for this class is banning a user after they try to login with failed
 * credentials too many times, or if a student enters an invalid student ID too many times (they are
 * probably guessing random IDs).
 * 
 * Although the bans can easily be cleared by clearing the browser's cookies, this is enough to stop
 * most people.
 * 
 * An IP ban will not work as it would end up banning the entire school.
 */
public class BanHelper {

  private final static String PREFIX = "BH_";

  /**
   * Records a failed attempt to the specified ban name
   * @param banName The ban name used to keep track of failed attempts.
   */
  public static void recordFailedAttempt(String banName) {
    int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
    if (failedAttempts == -1)
      SessionHelper.setSessionVariable(PREFIX + banName, 1);
    else
      SessionHelper.setSessionVariable(PREFIX + banName, ++failedAttempts);
  }

  /**
   * Gets the number of failed attempts for a specified ban name.
   * @param banName The ban name used to keep track of failed attempts.
   * @return The number of failed attempts.
   */
  public static int getNumberFailedAttempts(String banName) {
    int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
    return failedAttempts == -1 ? 0 : failedAttempts;
  }

  /**
   * Clears all failed attempts for the specified ban name.
   * @param banName The ban name used to keep track of failed attempts.
   */
  public static void clearFailedAttempts(String banName) {
    int failedAttempts = SessionHelper.getSessionVariableAsInt(PREFIX + banName);
    if (failedAttempts == -1)
      return;
    else
      SessionHelper.setSessionVariable(PREFIX + banName, 0);
  }

  /**
   * Determines whether or not a user is banned, by checking for a ban cookie.
   * @param banName The ban name used to keep track of failed attempts.
   * @return True if the user is banned, false if they are not.
   */
  public static boolean isUserBanned(String banName) {
    Cookie cookie = CookieHelper.getCookie(PREFIX + banName);
    if (cookie != null) {
      return cookie.getValue().equals("true");
    }
    return false;
  }

  /**
   * Bans a user for a given number of hours.
   * @param banName The ban name used to keep track of failed attempts.
   * @param banLengthHours The length of time in hours to ban the user.
   */
  public static void banUser(String banName, int banLengthHours) {
    CookieHelper.setCookie(PREFIX + banName, "true", banLengthHours * 60 * 60);
  }

  /**
   * Unbans a user.
   * @param banName The ban name used to keep track of failed attempts.
   */
  public static void unBanUser(String banName) {
    CookieHelper.deleteCookie(PREFIX + banName);
  }

}
