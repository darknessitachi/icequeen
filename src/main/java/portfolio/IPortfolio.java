//namespace BackTesting.Model.Portfolio
//{
//    using System;
//    using System.Collections.Generic;
//    using BackTesting.Model.Events;
package portfolio;

import java.time.LocalDateTime;
import java.util.Map;

import events.FillEvent;
import events.MarketEvent;
import events.SignalEvent;

public interface IPortfolio
    {
        void UpdateSignal(SignalEvent signal);
        void UpdateFill(FillEvent fill);
        void UpdateTimeIndex(MarketEvent market);
        Map<LocalDateTime, Holding> getHoldingHistory ();
        Map<LocalDateTime, Double> GetEquityCurve();
    }
