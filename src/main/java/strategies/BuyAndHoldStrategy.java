//namespace BackTesting.Model.Strategies
//{
//    using System.Collections.Generic;
//    using System.Linq;
//    using BackTesting.Model.DataHandlers;
//    using BackTesting.Model.Events;
package strategies;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import datahandlers.IDataHandler;
import enums.SignalType;
import events.IEventBus;
import events.SignalEvent;
import marketdata.Bar;

public class BuyAndHoldStrategy implements IStrategy
    {
        private  IEventBus eventBus;
        private  IDataHandler bars;

        private  Map<String, Boolean> bought;

        public BuyAndHoldStrategy(IEventBus eventBus, IDataHandler dataHandler)
        {
            this.eventBus = eventBus;
            this.bars = dataHandler;
            this.bought = this.CalculateInitialBought();
        }

        public void CalculateSignals()
        {
            if (this.bars.getCurrentTime()==null)
            {
                // not started yet
                return;
            }

            LocalDateTime currentTime = this.bars.getCurrentTime();

            for (String symbol : this.bars.getSymbols())
            {
                if (this.bought.get(symbol))
                {
                    continue;
                }

                Bar lastBar = this.bars.GetLast(symbol);

                if (lastBar == null)
                {
                    // No market data
                    continue;
                }

                this.bought.put(symbol, true) ;
                this.eventBus.Put(new SignalEvent(symbol, currentTime, SignalType.Long));
            }
        }

        private Map<String, Boolean> CalculateInitialBought()
        {
        	Map<String, Boolean> result = new LinkedHashMap<>();
        	
        	for (String symbol : this.bars.getSymbols()) {
        		result.put(symbol, false);
			}
        	
        	return result;
        }
    }
