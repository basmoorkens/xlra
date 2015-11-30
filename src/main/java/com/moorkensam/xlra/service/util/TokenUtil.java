package com.moorkensam.xlra.service.util;

import java.util.Calendar;
import java.util.UUID;

import com.moorkensam.xlra.model.security.TokenInfo;

public class TokenUtil {

	public static TokenInfo getNextToken() {
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setVerificationToken(UUID.randomUUID().toString());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tokenInfo.setValidTo(calendar.getTime());
		return tokenInfo;
	}

}
