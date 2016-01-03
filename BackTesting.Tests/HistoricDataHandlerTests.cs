﻿namespace BackTesting.Tests
{
    using System;
    using System.Collections.Generic;
    using BackTesting.Model.DataHandlers;
    using BackTesting.Model.Events;
    using BackTesting.Model.MarketData;
    using Deedle;
    using NUnit.Framework;

    [TestFixture]
    public class HistoricDataHandlerTests
    {
        #region CSV data sample
        private static readonly IDictionary<string, string> csvData = new Dictionary<string, string>() {
                {"sber", @"
<TICKER>,<PER>,<DATE>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
SBER,1,20151123,100100,106.8500000,106.8600000,106.1400000,106.2900000,655780
SBER,1,20151123,100200,106.2700000,106.3300000,106.1400000,106.2600000,548880
SBER,1,20151123,100300,106.2600000,106.2600000,105.9300000,106.0500000,1028870
SBER,1,20151123,100400,106.1100000,106.3000000,106.0500000,106.2800000,371940
SBER,1,20151123,100500,106.2600000,106.2800000,106.1300000,106.2200000,478220
SBER,1,20151123,100600,106.2100000,106.2300000,106.0200000,106.1100000,281740
SBER,1,20151123,100700,106.1500000,106.3000000,106.0500000,106.2600000,138440
SBER,1,20151123,100800,106.2900000,106.5200000,106.2400000,106.4000000,384210
SBER,1,20151123,100900,106.3900000,106.4500000,106.3000000,106.4500000,218000
SBER,1,20151123,101000,106.4700000,106.4700000,106.3400000,106.4100000,122320
"},
                {"vtbr", @"
<TICKER>,<PER>,<DATE>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
VTBR,1,20151120,184800,0.0760100,0.0760100,0.0760100,0.0760100,5570000
VTBR,1,20151120,184900,0.0760100,0.0760100,0.0760100,0.0760100,3040000
VTBR,1,20151123,100100,0.0761800,0.0762700,0.0758700,0.0759100,43670000
VTBR,1,20151123,100200,0.0759100,0.0760300,0.0758200,0.0759800,18330000
VTBR,1,20151123,100300,0.0758600,0.0759700,0.0758000,0.0758100,38320000
VTBR,1,20151123,100400,0.0758100,0.0758500,0.0758000,0.0758400,5660000
VTBR,1,20151123,100500,0.0758400,0.0760000,0.0756200,0.0758000,54390000
VTBR,1,20151123,100600,0.0758000,0.0758700,0.0757300,0.0758000,14570000
VTBR,1,20151123,100700,0.0758600,0.0758800,0.0758000,0.0758800,1720000
VTBR,1,20151123,110800,0.0758800,0.0758800,0.0758400,0.0758400,2440000
VTBR,1,20151123,110900,0.0758100,0.0758900,0.0758000,0.0758100,13090000
VTBR,1,20151123,111000,0.0758100,0.0758800,0.0758100,0.0758100,4410000
"}
            };
        #endregion

        [Test]
        public void CanGetLastBarFromCsvMarketData()
        {
            var dataSource = CsvDataSource.CreateFormStrings(csvData);
            var marketData = new ComposedMarketData(dataSource.Frames);
            var eventBus = new QueuedEventBus();

            var handler = new HistoricDataHandler(eventBus, marketData);

            Console.WriteLine("Total rows = {0}", marketData.RowKeys.Count);

            var i = 0;
            while (handler.ContinueBacktest)
            {
                handler.Update();
                i++;

                var sber = handler.GetLast("sber");
                var vtbr = handler.GetLast("vtbr");

                Console.WriteLine($"Iteration {i}");

                if (sber == null)
                {
                    Console.WriteLine($"{handler.CurrentTime} => SBER is NULL");
                }
                else
                {
                    sber.Print();
                }
                if (vtbr == null)
                {
                    Console.WriteLine($"{handler.CurrentTime} => VTBR is NULL");
                }
                else
                {
                    vtbr.Print();
                }
                Console.WriteLine("------------------");
            }
        }
    }
}
