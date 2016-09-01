package com.moorkensam.xlra.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

  private static final Logger logger = LogManager.getLogger();

  /**
   * Generates a hash based on the input.
   * 
   * @param password The string to hash
   * @return The hashed value
   */
  public static String makePasswordHash(final String password) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
      md.update(password.getBytes("UTF-8"));
      byte[] digest = md.digest();

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < digest.length; i++) {
        String hex = Integer.toHexString(0xff & digest[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage());
    } catch (UnsupportedEncodingException e) {
      logger.error(e.getMessage());
    }
    return "";
  }

}
