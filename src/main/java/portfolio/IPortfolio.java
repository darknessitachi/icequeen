package portfolio;

import java.time.LocalDateTime;
import java.util.Map;

import events.FillEvent;
import events.MarketEvent;
import events.SignalEvent;

public interface IPortfolio {
	
	void UpdateSignal(SignalEvent signal);

	void UpdateFill(FillEvent fill);

	void UpdateTimeIndex(MarketEvent market);

	Map<LocalDateTime, Holding> getHoldingHistory();

	Map<LocalDateTime, Double> GetEquityCurve();
}
