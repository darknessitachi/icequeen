import backtests.BackTest;
import datahandlers.HistoricDataHandler;
import datahandlers.IDataHandler;
import events.IEventBus;
import events.QueuedEventBus;
import executionhandlers.IExecutionHandler;
import executionhandlers.SimulatedExecutionHandler;
import marketdata.ComposedMarketData;
import marketdata.CsvDataSource;
import marketdata.IMarketData;
import marketdata.Symbols;
import portfolio.IPortfolio;
import portfolio.NaivePortfolio;
import strategies.BuyAndHoldStrategy;
import strategies.IStrategy;

public class EventDrivenBacktestingTest {
	
	static void main(String[] args) {
		System.out.println("BackTest starting...");

		DoMainBackTest();

		System.out.println("\nBackTest has been finished. Press ENTER to exit.");
	}

	private static void DoMainBackTest() {
		IEventBus eventBus = new QueuedEventBus();
		CsvDataSource dataSource = CsvDataSource.CreateFromFiles("Data\\Min1",
				new String[] { Symbols.Sber, Symbols.Vtbr });
		IMarketData marketData = new ComposedMarketData(dataSource.Bars);
		IDataHandler bars = new HistoricDataHandler(eventBus, marketData);
		IStrategy strategy = new BuyAndHoldStrategy(eventBus, bars);
		IExecutionHandler executionHandler = new SimulatedExecutionHandler(eventBus, bars);
		IPortfolio portfolio = new NaivePortfolio(eventBus, bars, 10000);

		BackTest backTest = new BackTest(eventBus, bars, strategy, portfolio, executionHandler);

		backTest.SimulateTrading();
	}

}
