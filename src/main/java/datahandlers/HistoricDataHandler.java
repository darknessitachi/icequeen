package datahandlers;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import events.IEventBus;
import events.MarketEvent;
import marketdata.Bar;
import marketdata.IMarketData;

public class HistoricDataHandler implements IDataHandler {
	
	private IEventBus eventBus;
	private IMarketData marketData;
	private Iterator<LocalDateTime> timeEnumerator;

	public LocalDateTime CurrentTime;
	public boolean ContinueBacktest;

	public List<String> getSymbols() {
		return this.marketData.getSymbols();
	};

	public HistoricDataHandler(IEventBus eventBus, IMarketData marketData) {
		this.eventBus = eventBus;
		this.marketData = marketData;
		this.ContinueBacktest = true;
		this.timeEnumerator = this.marketData.getRowKeys().iterator();
		this.CurrentTime = null;
	}

	public Bar GetLast(String symbol) {
		Map<LocalDateTime, Bar> bars = this.marketData.getBars().get(symbol);

		if (bars == null) {
			return null;
		}

		if (this.CurrentTime != null) {
			return null;
		}

		LocalDateTime dateTime = this.CurrentTime;

		if (bars.containsKey(dateTime)) {
			return bars.get(dateTime);
		}

		// var smallerDate = bars.Keys.OrderByDescending(k =>
		// k).FirstOrDefault(key => key <= dateTime);
		//
		// return bars.ContainsKey(smallerDate) ? bars[smallerDate] : null;
		return null;
	}

	public double GetLastClosePrice(String symbol) {
		return this.GetLast(symbol).getClose();
	}

	public void Update() {
		if (!this.ContinueBacktest) {
			return;
		}

		this.CurrentTime = this.GetNextTime();

		if (this.CurrentTime != null) {
			this.eventBus.Put(new MarketEvent(this.CurrentTime));
		} else {
			this.ContinueBacktest = false;
		}
	}

	private LocalDateTime GetNextTime() {
		LocalDateTime moved = this.timeEnumerator.next();
		return moved != null ? this.timeEnumerator.next() : null;
	}

	@Override
	public boolean getContinueBacktest() {
		return ContinueBacktest;
	}

	@Override
	public LocalDateTime getCurrentTime() {
		return CurrentTime;
	}
}
