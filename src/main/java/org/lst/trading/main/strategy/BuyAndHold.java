package org.lst.trading.main.strategy;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

import java.util.HashMap;
import java.util.Map;

public class BuyAndHold implements TradingStrategy {
	
	Map<String, Order> orders;
	TradingContext context;

	@Override
	public void onStart(TradingContext context) {
		this.context = context;
	}

	@Override
	public void onTick() {
		if (orders == null) {
			orders = new HashMap<>();
			context.getInstruments().stream().forEach(instrument -> orders.put(instrument, context.order(instrument, true, 1)));
		}
	}
}
