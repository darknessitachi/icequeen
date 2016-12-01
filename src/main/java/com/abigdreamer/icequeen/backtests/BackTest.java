package com.abigdreamer.icequeen.backtests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.abigdreamer.icequeen.datahandlers.IDataHandler;
import com.abigdreamer.icequeen.enums.EventType;
import com.abigdreamer.icequeen.events.Event;
import com.abigdreamer.icequeen.events.FillEvent;
import com.abigdreamer.icequeen.events.IEventBus;
import com.abigdreamer.icequeen.events.MarketEvent;
import com.abigdreamer.icequeen.events.OrderEvent;
import com.abigdreamer.icequeen.events.SignalEvent;
import com.abigdreamer.icequeen.executionhandlers.IExecutionHandler;
import com.abigdreamer.icequeen.portfolio.Holding;
import com.abigdreamer.icequeen.portfolio.IPortfolio;
import com.abigdreamer.icequeen.strategies.IStrategy;
import com.abigdreamer.icequeen.utils.Util;
import com.abigdreamer.infinity.common.util.TimeWatch;

public class BackTest {
	
	private int heartBeatMilliseconds = 0;

	private IEventBus eventBus;
	private IDataHandler bars;
	private IStrategy strategy;
	private IPortfolio portfolio;
	private IExecutionHandler executionHandler;

	private int signals = 0;
	private int orders = 0;
	private int fills = 0;

	private TimeWatch timeWatch;

	public BackTest(IEventBus eventBus, IDataHandler bars, IStrategy strategy, IPortfolio portfolio,
			IExecutionHandler executionHandler) {
		this.eventBus = eventBus;
		this.bars = bars;
		this.strategy = strategy;
		this.portfolio = portfolio;
		this.executionHandler = executionHandler;
		this.timeWatch = TimeWatch.create();
	}

	public void simulateTrading() {
		this.run();
		this.outputPerformance();
	}

	private void run() {
		this.timeWatch.startWithTaskName("simulate trading");
		
		int iteration = 0;
		while (true) {
			iteration++;
			
			if (!this.bars.isContinueBacktest()) {
				break;	
			}
			
			this.bars.update();

			while (true) {
				Event evt = this.eventBus.get();

				if (evt == null) {
					break;
				}

				if (evt.getEventType() == EventType.Market) {
					MarketEvent mEvt = (MarketEvent) evt;
					if (iteration % 100 == 0) {
						System.out.println(iteration+" Market time: " + mEvt.getCurrentTime());
					}
					this.strategy.calculateSignals();
					this.portfolio.updateTimeIndex(mEvt);
				} else if (evt.getEventType() == EventType.Signal) {
					SignalEvent signal = (SignalEvent) evt;
					System.out.println(" => " + signal);
					this.portfolio.updateSignal(signal);
					this.signals++;
				} else if (evt.getEventType() == EventType.Order) {
					OrderEvent order = (OrderEvent) evt;
					System.out.println(" => " + order);
					this.executionHandler.executeOrder(order);
					this.orders++;
				} else if (evt.getEventType() == EventType.Fill) {
					FillEvent fill = (FillEvent) evt;
					System.out.println(" => " + fill);
					this.portfolio.updateFill(fill);
					this.fills++;
				}
			}

			try {
				Thread.sleep(this.heartBeatMilliseconds);
			} catch (Exception e) {
			}
		}

		this.timeWatch.stopAndPrint();
	}

	private void outputPerformance() {
		System.out.println("\nCreating summary stats ...");
		System.out.println("---------------------------");
		System.out.println("Holdings");
		this.printHoldingHistory(this.portfolio.getHoldingHistory());
		System.out.println("---------------------------");
		System.out.println("Equity");
		Map<LocalDateTime, Double> equityCurve = this.portfolio.getEquityCurve();
		this.printEquityCurve(equityCurve);
		this.saveAsCsv(equityCurve, "equtycurve.csv");
		System.out.println("---------------------------");
		System.out.println("Signals="+this.signals+" Orders="+this.orders+" Fills="+this.fills);
	}

	private void printHoldingHistory(Map<LocalDateTime, Holding> holdingHistory) {
		List<LocalDateTime> times = new ArrayList<>(holdingHistory.keySet());
		for (int i = 0; i < 15; i++) {
			LocalDateTime time = times.get(i);
			System.out.println(time + "==>" + holdingHistory.get(time));
		}

		System.out.println("......................................");

		for (int i = 15; i > 0; i--) {
			LocalDateTime time = times.get(times.size() - i);
			System.out.println(time + "==>" + holdingHistory.get(time));
		}
	}

	private void printEquityCurve(Map<LocalDateTime, Double> equityCurve) {
		List<LocalDateTime> times = new ArrayList<>(equityCurve.keySet());
		for (int i = 0; i < 15; i++) {
			LocalDateTime time = times.get(i);
			System.out.println(time + "==>" + Util.getDecimal(equityCurve.get(time), 4));
		}

		System.out.println("......................................");

		for (int i = 15; i > 0; i--) {
			LocalDateTime time = times.get(times.size() - i);
			System.out.println(time + "==>" + Util.getDecimal(equityCurve.get(time), 4));
		}
	}

	private void saveAsCsv(Map<LocalDateTime, Double> equityCurve, String fileName) {
		// using (var tw = new StreamWriter(fileName))
		// {
		// var csvWriter = new CsvWriter(tw);
		//
		// foreach (var kvp in equityCurve)
		// {
		// csvWriter.WriteRecord(new { DateTime = kvp.Key, Equity = kvp.Value
		// });
		// }
		// }
	}
}
