import backtests.BackTest;
import datahandlers.HistoricDataHandler;
import datahandlers.IDataHandler;
import events.IEventBus;
import events.QueuedEventBus;
import executionhandlers.IExecutionHandler;
import executionhandlers.SimulatedExecutionHandler;
import marketdata.ComposedMarketData;
import marketdata.CsvDataSource;
import marketdata.Symbols;
import portfolio.IPortfolio;
import portfolio.NaivePortfolio;
import strategies.BuyAndHoldStrategy;
import strategies.IStrategy;

//namespace ConsoleApp
//{
//    using System;
//    using BackTesting.Model.BackTests;
//    using BackTesting.Model.DataHandlers;
//    using BackTesting.Model.Events;
//    using BackTesting.Model.ExecutionHandlers;
//    using BackTesting.Model.MarketData;
//    using BackTesting.Model.Portfolio;
//    using BackTesting.Model.Strategies;

    class Program
    {
        private  int CONST_ScreenWidth = 150;
        private  int CONST_ScreenHeight = 40;
        private  int CONST_BufferHeight = CONST_ScreenHeight * 10;

        static void main(String[] args)
        {
            System.out.println("BackTest starting...");

            SetupScreen();
            DoMainBackTest();

            System.out.println("\nBackTest has been finished. Press ENTER to exit.");
//            Console.ReadLine();
        }

        private static void DoMainBackTest()
        {
        	IEventBus eventBus = new QueuedEventBus();
            CsvDataSource dataSource = CsvDataSource.CreateFromFiles("Data\\Min1", new String[] { Symbols.Sber, Symbols.Vtbr });
            ComposedMarketData marketData = new ComposedMarketData(dataSource.Bars);
            IDataHandler bars = new HistoricDataHandler(eventBus, marketData);
            IStrategy strategy = new BuyAndHoldStrategy(eventBus, bars);
            IExecutionHandler executionHandler = new SimulatedExecutionHandler(eventBus, bars);
            IPortfolio portfolio = new NaivePortfolio(eventBus, bars, 10000);
            
            BackTest backTest = new BackTest(eventBus, bars, strategy, portfolio, executionHandler);

            backTest.SimulateTrading();
        }

        private static void SetupScreen()
        {
//            Console.WindowWidth = CONST_ScreenWidth;
//            Console.BufferWidth = CONST_ScreenWidth;
//
//            Console.WindowHeight = CONST_ScreenHeight;
//            Console.BufferHeight = CONST_BufferHeight;
        }
    }
//}
