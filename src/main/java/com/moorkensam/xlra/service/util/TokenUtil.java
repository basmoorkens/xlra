package com.moorkensam.xlra.service.util;

import com.moorkensam.xlra.model.security.TokenInfo;

import java.util.Calendar;
import java.util.UUID;

public class TokenUtil {

  /**
   * Gt the next token to use.
   * 
   * @return The generated token info.
   */
  public static TokenInfo getNextToken() {
    TokenInfo tokenInfo = new TokenInfo();
    tokenInfo.setVerificationToken(UUID.randomUUID().toString());
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    tokenInfo.setValidTo(calendar.getTime());
    return tokenInfo;
  }

}
