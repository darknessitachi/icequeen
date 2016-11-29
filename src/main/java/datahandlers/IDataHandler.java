//namespace BackTesting.Model.DataHandlers
//{
//    using System;
//    using System.Collections.Generic;
//    using BackTesting.Model.MarketData;
package datahandlers;

import java.time.LocalDateTime;
import java.util.List;

import marketdata.Bar;

public interface IDataHandler
    {
        List<String> getSymbols() ;
        boolean getContinueBacktest() ;
        LocalDateTime getCurrentTime ();
        Bar GetLast(String symbol);
        double GetLastClosePrice(String symbol);
        void Update();
    }
