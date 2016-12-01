package com.abigdreamer.icequeen.datahandlers;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.abigdreamer.icequeen.events.IEventBus;
import com.abigdreamer.icequeen.events.MarketEvent;
import com.abigdreamer.icequeen.marketdata.Bar;
import com.abigdreamer.icequeen.marketdata.IMarketData;

public class HistoricDataHandler implements IDataHandler {
	
	private IEventBus eventBus;
	private IMarketData marketData;
	private Iterator<LocalDateTime> timeEnumerator;

	public LocalDateTime currentTime;
	public boolean isContinueBacktest;

	public HistoricDataHandler(IEventBus eventBus, IMarketData marketData) {
		this.eventBus = eventBus;
		this.marketData = marketData;
		this.isContinueBacktest = true;
		this.timeEnumerator = this.marketData.getTimeLine().iterator();
		this.currentTime = null;
	}

	public List<String> getSymbols() {
		return this.marketData.getSymbols();
	}
	
	public Bar getLast(String symbol) {
		Map<LocalDateTime, Bar> bars = this.marketData.getBars().get(symbol);

		if (this.currentTime == null) {
			return null;
		}

		LocalDateTime dateTime = this.currentTime;

		if (bars.containsKey(dateTime)) {
			return bars.get(dateTime);
		}

		return null;
	}

	public double getLastClosePrice(String symbol) {
		Bar bar = this.getLast(symbol);
		if(bar == null) {
			return 0;
		}
		return bar.getClose();
	}

	public void update() {
		if (!this.isContinueBacktest) {
			return;
		}

		this.currentTime = this.getNextTime();

		if (this.currentTime != null) {
			this.eventBus.put(new MarketEvent(this.currentTime));
		} else {
			this.isContinueBacktest = false;
		}
	}

	private LocalDateTime getNextTime() {
		if(this.timeEnumerator.hasNext()) {
			return this.timeEnumerator.next();
		}
		return null;
	}

	@Override
	public boolean isContinueBacktest() {
		return isContinueBacktest;
	}

	@Override
	public LocalDateTime getCurrentTime() {
		return currentTime;
	}
}
