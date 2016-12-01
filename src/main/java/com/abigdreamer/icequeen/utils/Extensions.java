//namespace BackTesting.Model.Utils
//{
//    using System;
//    using System.Collections.Generic;
//    using BackTesting.Model.MarketData;
package com.abigdreamer.icequeen.utils;

import java.time.LocalDateTime;
import java.util.Map;

import com.abigdreamer.icequeen.marketdata.Bar;

public class Extensions {
	public static void Print(Map<LocalDateTime, Bar> bars) {
		for (LocalDateTime kvp : bars.keySet()) {
			System.out.println(kvp + "=>" + bars.get(kvp));
		}
	}

	public static void Print(Bar bar) {
		System.out.println(bar);
	}
}
