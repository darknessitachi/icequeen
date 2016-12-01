package com.abigdreamer.icequeen.backtests;

import java.time.LocalDateTime;
import java.util.Map;

import org.omg.PortableInterceptor.HOLDING;

import com.abigdreamer.icequeen.datahandlers.IDataHandler;
import com.abigdreamer.icequeen.enums.*;
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

	private Stopwatch stopWatch;

	public BackTest(IEventBus eventBus, IDataHandler bars, IStrategy strategy, IPortfolio portfolio,
			IExecutionHandler executionHandler) {
		this.eventBus = eventBus;
		this.bars = bars;
		this.strategy = strategy;
		this.portfolio = portfolio;
		this.executionHandler = executionHandler;
		this.stopWatch = new Stopwatch();
	}

	public void SimulateTrading() {
		this.Run();
		this.OutputPerformance();
	}

	private void Run() {
		this.stopWatch.Start();
		int iteration = 0;
		while (true) {
			iteration++;
			if (this.bars.getContinueBacktest()) {
				this.bars.Update();
			} else {
				break;
			}

			while (true) {
				Event evt = this.eventBus.Get();

				if (evt == null) {
					break;
				}

				if (evt.getEventType() == EventType.Market) {
					MarketEvent mEvt = (MarketEvent) evt;
					if (iteration % 100 == 0) {
						System.out.println(iteration+" Market time: " + mEvt.getCurrentTime());
					}
					this.strategy.CalculateSignals();
					this.portfolio.UpdateTimeIndex(mEvt);
				} else if (evt.getEventType() == EventType.Signal) {
					SignalEvent signal = (SignalEvent) evt;
					System.out.println(" => " + signal);
					this.portfolio.UpdateSignal(signal);
					this.signals++;
				} else if (evt.getEventType() == EventType.Order) {
					OrderEvent order = (OrderEvent) evt;
					System.out.println(" => " + order);
					this.executionHandler.ExecuteOrder(order);
					this.orders++;
				} else if (evt.getEventType() == EventType.Fill) {
					FillEvent fill = (FillEvent) evt;
					System.out.println(" => " + fill);
					this.portfolio.UpdateFill(fill);
					this.fills++;
				}
			}

			try {
				Thread.sleep(this.heartBeatMilliseconds);
			} catch (Exception e) {
			}
		}

		this.stopWatch.Stop();
	}

	private void OutputPerformance() {
		System.out.println("\nCreating summary stats ...");
		System.out.println("---------------------------");
		System.out.println("Holdings");
		this.PrintHoldingHistory(this.portfolio.getHoldingHistory());
		System.out.println("---------------------------");
		System.out.println("Equity");
		Map<LocalDateTime, Double> equityCurve = this.portfolio.GetEquityCurve();
		this.PrintEquityCurve(equityCurve);
		this.SaveAsCsv(equityCurve, "equtycurve.csv");
		System.out.println("---------------------------");
		System.out.println("Signals={this.signals} Orders={this.orders} Fills={this.fills}");

		// var ts = this.stopWatch.Elapsed;
		System.out.println("Run Time: {ts.Hours:00}:{ts.Minutes:00}:{ts.Seconds:00}.{ts.Map<K, V>liseconds / 10:00}");

	}

	private void PrintHoldingHistory(Map<LocalDateTime, Holding> holdingHistory) {
		for (LocalDateTime key : holdingHistory.keySet()) {
			System.out.println(holdingHistory.get(key));
		}

		System.out.println("......................................");

		// for (var key in holdingHistory.Keys.OrderByDescending(k =>
		// k).Take(15))
		// {
		// System.out.println(holdingHistory[key]);
		// }
	}

	private void PrintEquityCurve(Map<LocalDateTime, Double> equityCurve) {
		// foreach (var key in equityCurve.Keys.Take(15))
		// {
		// System.out.println($"{key} {equityCurve[key]}");
		// }

		System.out.println("......................................");

		// foreach (var key in equityCurve.Keys.OrderByDescending(k =>
		// k).Take(15))
		// {
		// System.out.println("{key} {equityCurve[key]}");
		// }
	}

	private void SaveAsCsv(Map<LocalDateTime, Double> equityCurve, String fileName) {
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
