package org.BinghamTSA.uCount.core.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Sha256Helper is a convenience class to help us hash, using SHA256, raw text. The reason for
 * placing this in its own class is to standardize all hashes to make them all use the same salt.
 */
public class Sha256Helper {

  /**
   * The salt for the hasher.
   */
  private static final String SALT = "POLLSYS";

  /**
   * Hashes raw text using SHA256 and a salt.
   * 
   * @param raw The raw text to hash.
   * @return The hash, as a byte array.
   */
  public static byte[] rawToSha(String raw) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update((raw + SALT).getBytes("UTF-8"));
      return md.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
