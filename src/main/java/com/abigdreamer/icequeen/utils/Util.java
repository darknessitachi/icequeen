package com.abigdreamer.icequeen.utils;

import java.math.BigDecimal;

public class Util {
	
	public static double getDecimal(double num, int scale) {
		if (Double.isNaN(num)) {
			return 0;
		}
		BigDecimal bd = new BigDecimal(num+"");
		num = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}
}
