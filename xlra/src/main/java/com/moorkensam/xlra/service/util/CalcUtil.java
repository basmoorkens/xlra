package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcUtil {

	private static CalcUtil instance;

	private CalcUtil() {
	}

	public static CalcUtil getInstance() {
		if (instance == null) {
			instance = new CalcUtil();
		}
		return instance;
	}


	public BigDecimal convertPercentageToBaseMultiplier(double percentage) {
		BigDecimal bd = new BigDecimal((double) percentage / 100d);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd;
	}

	public BigDecimal roundBigDecimal(BigDecimal number) {
		number = number.setScale(2, RoundingMode.HALF_UP);
		return number;
	}
}
