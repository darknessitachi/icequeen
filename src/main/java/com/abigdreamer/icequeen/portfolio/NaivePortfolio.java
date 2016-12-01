package com.abigdreamer.icequeen.portfolio;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.abigdreamer.icequeen.datahandlers.IDataHandler;
import com.abigdreamer.icequeen.enums.OrderType;
import com.abigdreamer.icequeen.enums.SignalType;
import com.abigdreamer.icequeen.enums.TransactionDirection;
import com.abigdreamer.icequeen.events.FillEvent;
import com.abigdreamer.icequeen.events.IEventBus;
import com.abigdreamer.icequeen.events.MarketEvent;
import com.abigdreamer.icequeen.events.OrderEvent;
import com.abigdreamer.icequeen.events.SignalEvent;

public class NaivePortfolio implements IPortfolio {
	
	private IEventBus eventBus;
	private IDataHandler bars;
	private double initialCapital;

	public Map<LocalDateTime, Holding> HoldingHistory;

	public Map<LocalDateTime, Holding> getHoldingHistory() {
		return HoldingHistory;
	}

	private Map<String, Integer> currentPositions;
	private double currentComission;
	private double currentCash;

	public NaivePortfolio(IEventBus eventBus, IDataHandler bars, double initialCapital) {
		this.eventBus = eventBus;
		this.bars = bars;
		this.initialCapital = initialCapital;
		this.HoldingHistory = new LinkedHashMap<>();
		for (String symbol : this.bars.getSymbols()) {
			this.currentPositions.put(symbol, 0);
		}

		this.currentComission = 0;
		this.currentCash = this.initialCapital;
	}

	public Map<LocalDateTime, Double> GetEquityCurve() {
		double prevTotal = 0;
		double equity = 1;

		for (Map.Entry<LocalDateTime, Holding> kvp : this.getHoldingHistory().entrySet()) {
			double total = kvp.getValue().getTotal();

			double returns = prevTotal != 0 ? ((total - prevTotal) / prevTotal) * 100 : 0;

			prevTotal = total;
			equity *= (1.0 + returns / 100);
			kvp.getValue().setEquityCurve(equity);
			kvp.getValue().setReturns(returns);
		}

		Map<LocalDateTime, Double> result = new LinkedHashMap<>();
		for (Map.Entry<LocalDateTime, Holding> kvp : this.HoldingHistory.entrySet()) {
			result.put(kvp.getKey(), kvp.getValue().getEquityCurve());
		}

		return result;
	}

	public void UpdateSignal(SignalEvent signal) {
		OrderEvent orderEvent = this.GenerateNaiveOrder(signal);
		if (orderEvent != null) {
			this.eventBus.Put(orderEvent);
		}
	}

	public void UpdateFill(FillEvent fill) {
		int fillDir = GetNumericDirection(fill.getDirection());
		double closePrice = this.bars.GetLastClosePrice(fill.getSymbol());

		if (closePrice == 0) {
			throw new RuntimeException("Cannot find last price for {fill.Symbol}");
		}

		double cost = fillDir * closePrice * fill.getQuantity();

		int existValue = this.currentPositions.get(fill.getSymbol());

		this.currentPositions.put(fill.getSymbol(), existValue + fillDir * fill.getQuantity());
		this.currentComission += fill.getComission();
		this.currentCash -= (cost + fill.getComission());
	}

	public void UpdateTimeIndex(MarketEvent market) {
		Map<String, Double> marketHoldings = new LinkedHashMap<>();
		for (String symbol : this.bars.getSymbols()) {
			marketHoldings.put(symbol, 0.0);
		}

		double cash = this.currentCash;
		double commision = this.currentComission;
		double total = this.currentCash;

		for (String symbol : this.bars.getSymbols()) {
			int qty = this.currentPositions.get(symbol);
			double closePrice = this.bars.GetLastClosePrice(symbol);

			if (closePrice == 0) {
				if (qty != 0) {
					throw new RuntimeException("Unknown close price for {symbol} with position qty={qty}.");
				}

				closePrice = 0;
			}

			double marketValue = qty * closePrice;
			marketHoldings.put(symbol, marketValue);
			total += marketValue;
		}

		double change = this.initialCapital != 0 ? ((total - this.initialCapital) / this.initialCapital) * 100 : 0;

		Holding holding = new Holding();

		for (Map.Entry<String, Double> kvp : marketHoldings.entrySet()) {
			holding.getSymbolHoldings().put(kvp.getKey(), kvp.getValue());
		}

		holding.setComission(commision);
		holding.setCash(cash);
		holding.setTotal(total);
		holding.setChange(change);

		LocalDateTime dateTime = market.getCurrentTime();

		if (this.HoldingHistory.containsKey(dateTime)) {
			this.HoldingHistory.put(dateTime, holding);
		} else {
			this.HoldingHistory.put(dateTime, holding);
		}
	}

	private OrderEvent GenerateNaiveOrder(SignalEvent signal) {
		String symbol = signal.getSymbol();
		SignalType direction = signal.getSignalType();
		double strength = signal.getStrength();
		LocalDateTime time = signal.getTimeStamp();

		int marketQuantity = (int) Math.floor(100 * strength);
		int currentQuantity = (int) this.currentPositions.get(symbol);

		if (direction == SignalType.Long && currentQuantity == 0) {
			return new OrderEvent(symbol, OrderType.Market, marketQuantity, TransactionDirection.Buy, time);
		}
		if (direction == SignalType.Short && currentQuantity == 0) {
			return new OrderEvent(symbol, OrderType.Market, marketQuantity, TransactionDirection.Sell, time);
		}
		if (direction == SignalType.Exit && currentQuantity > 0) {
			return new OrderEvent(symbol, OrderType.Market, Math.abs(currentQuantity), TransactionDirection.Sell, time);
		}
		if (direction == SignalType.Exit && currentQuantity < 0) {
			return new OrderEvent(symbol, OrderType.Market, Math.abs(currentQuantity), TransactionDirection.Buy, time);
		}

		return null;
	}

	// TODO: Convert to extension
	private static int GetNumericDirection(TransactionDirection dir) {
		return dir == TransactionDirection.Buy ? 1 : dir == TransactionDirection.Sell ? -1 : 0;
	}
}
