package com.abigdreamer.icequeen.strategies;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.abigdreamer.icequeen.datahandlers.IDataHandler;
import com.abigdreamer.icequeen.enums.SignalType;
import com.abigdreamer.icequeen.events.IEventBus;
import com.abigdreamer.icequeen.events.SignalEvent;
import com.abigdreamer.icequeen.marketdata.Bar;

public class BuyAndHoldStrategy implements IStrategy {
	
	private IEventBus eventBus;
	private IDataHandler bars;

	private Map<String, Boolean> bought;

	public BuyAndHoldStrategy(IEventBus eventBus, IDataHandler dataHandler) {
		this.eventBus = eventBus;
		this.bars = dataHandler;
		this.bought = this.CalculateInitialBought();
	}

	public void CalculateSignals() {
		if (this.bars.getCurrentTime() == null) {
			// not started yet
			return;
		}

		LocalDateTime currentTime = this.bars.getCurrentTime();

		for (String symbol : this.bars.getSymbols()) {
			if (this.bought.get(symbol)) {
				continue;
			}

			Bar lastBar = this.bars.GetLast(symbol);

			if (lastBar == null) {
				// No market data
				continue;
			}

			this.bought.put(symbol, true);
			this.eventBus.Put(new SignalEvent(symbol, currentTime, SignalType.Long));
		}
	}

	private Map<String, Boolean> CalculateInitialBought() {
		Map<String, Boolean> result = new LinkedHashMap<>();

		for (String symbol : this.bars.getSymbols()) {
			result.put(symbol, false);
		}

		return result;
	}
}
