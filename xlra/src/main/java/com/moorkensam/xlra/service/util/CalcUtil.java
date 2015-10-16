package com.moorkensam.xlra.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcUtil {

	/**
	 * Converts the percentage to raise ratelines with from a value between 0
	 * and 100 to a double with which we can multiply each rateline value.
	 * 
	 * @param percentage
	 * @return
	 */
	public static BigDecimal convertPercentageToMultiplier(double percentage) {
		double percentagePlus100 = percentage + 100d;
		BigDecimal bd = new BigDecimal((double) percentagePlus100 / 100d);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd;
	}

	public static BigDecimal convertPercentageToBaseMultiplier(double percentage) {
		BigDecimal bd = new BigDecimal((double) percentage / 100d);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd;
	}
	
	public static BigDecimal roundBigDecimal(BigDecimal number) {
		number = number.setScale(2, RoundingMode.HALF_UP);
		return number;
	}
}
