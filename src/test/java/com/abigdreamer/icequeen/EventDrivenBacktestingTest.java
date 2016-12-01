package com.abigdreamer.icequeen;
import com.abigdreamer.icequeen.backtests.BackTest;
import com.abigdreamer.icequeen.datahandlers.HistoricDataHandler;
import com.abigdreamer.icequeen.datahandlers.IDataHandler;
import com.abigdreamer.icequeen.events.IEventBus;
import com.abigdreamer.icequeen.events.QueuedEventBus;
import com.abigdreamer.icequeen.executionhandlers.IExecutionHandler;
import com.abigdreamer.icequeen.executionhandlers.SimulatedExecutionHandler;
import com.abigdreamer.icequeen.marketdata.ComposedMarketData;
import com.abigdreamer.icequeen.marketdata.CsvDataSource;
import com.abigdreamer.icequeen.marketdata.IMarketData;
import com.abigdreamer.icequeen.marketdata.Symbols;
import com.abigdreamer.icequeen.portfolio.IPortfolio;
import com.abigdreamer.icequeen.portfolio.NaivePortfolio;
import com.abigdreamer.icequeen.strategies.BuyAndHoldStrategy;
import com.abigdreamer.icequeen.strategies.IStrategy;

public class EventDrivenBacktestingTest {
	
	public static void main(String[] args) {
		System.out.println("BackTest starting...");

		doMainBackTest();

		System.out.println("\nBackTest has been finished.");
	}

	private static void doMainBackTest() {
		IEventBus eventBus = new QueuedEventBus();
		CsvDataSource dataSource = CsvDataSource.CreateFromFiles("testData\\Min1", Symbols.Sber, Symbols.Vtbr);
		IMarketData marketData = new ComposedMarketData(dataSource.getBars());
		IDataHandler bars = new HistoricDataHandler(eventBus, marketData);
		IStrategy strategy = new BuyAndHoldStrategy(eventBus, bars);
		IExecutionHandler executionHandler = new SimulatedExecutionHandler(eventBus, bars);
		IPortfolio portfolio = new NaivePortfolio(eventBus, bars, 10000);

		BackTest backTest = new BackTest(eventBus, bars, strategy, portfolio, executionHandler);

		backTest.simulateTrading();
	}

}
