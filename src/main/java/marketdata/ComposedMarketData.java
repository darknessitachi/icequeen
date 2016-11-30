package marketdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComposedMarketData implements IMarketData {
	
	public List<LocalDateTime> RowKeys;

	public List<String> getSymbols() {
		return new ArrayList<>(this.Bars.keySet());
	};

	public Map<String, Map<LocalDateTime, Bar>> Bars;

	public Map<String, Map<LocalDateTime, Bar>> getBars() {
		return Bars;
	}

	public ComposedMarketData(Map<String, Map<LocalDateTime, Bar>> bars) {
		this.Bars = bars;
		this.RowKeys = this.ComposeRowKeys();
	}

	@Override
	public List<LocalDateTime> getRowKeys() {
		return RowKeys;
	}

	private List<LocalDateTime> ComposeRowKeys() {
		List<LocalDateTime> res = new ArrayList<LocalDateTime>();
		return res;
		// return this.Bars.Keys.Aggregate(res, (current, symbol) =>
		// UnionRowKeys(current, this.Bars[symbol].Keys).OrderBy(k =>
		// k).ToList());
	}

	// private static IEnumerable<DateTime> UnionRowKeys(IEnumerable<DateTime>
	// source1, IEnumerable<DateTime> source2)
	// {
	// return source1?.Union(source2) ?? source2;
	// }
}
