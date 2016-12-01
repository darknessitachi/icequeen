package com.abigdreamer.icequeen.marketdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComposedMarketData implements IMarketData {

	public List<LocalDateTime> timeLine;
	public Map<String, Map<LocalDateTime, Bar>> bars;

	public ComposedMarketData(Map<String, Map<LocalDateTime, Bar>> bars) {
		this.bars = bars;
		this.timeLine = this.composeTimeLine();
	}

	public List<String> getSymbols() {
		return new ArrayList<>(this.bars.keySet());
	}

	public Map<String, Map<LocalDateTime, Bar>> getBars() {
		return bars;
	}

	@Override
	public List<LocalDateTime> getTimeLine() {
		return timeLine;
	}

	private List<LocalDateTime> composeTimeLine() {
		Set<LocalDateTime> res = new HashSet<>();
		
		for (Map<LocalDateTime,Bar> singelBars : this.bars.values()) {
			res.addAll(singelBars.keySet());	
		}
		
		List<LocalDateTime> result = new ArrayList<>(res);
		Collections.sort(result);
		return result;
	}

}
