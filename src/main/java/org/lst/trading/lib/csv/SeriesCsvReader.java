package org.lst.trading.lib.csv;

import static java.util.stream.Collectors.toList;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.lst.trading.lib.csv.CsvReader.Function2;
import org.lst.trading.lib.csv.CsvReader.ParseFunction;
import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;
import org.lst.trading.lib.series.TimeSeries;

/**
 *  
 * @author Darkness
 * @date 2016年12月2日 下午2:14:18
 * @version V1.0
 */
public class SeriesCsvReader {
	
	private static class SeriesConsumer<T> implements Consumer<String[]> {
		
        int i = 0;
        List<String> columns;
        TimeSeries<T> series;
        ParseFunction<Instant> instantParseFunction;
        Function2<String[], List<String>, T> f;

        public SeriesConsumer(TimeSeries<T> series, ParseFunction<Instant> instantParseFunction, Function2<String[], List<String>, T> f) {
            this.series = series;
            this.instantParseFunction = instantParseFunction;
            this.f = f;
        }

		@Override
		public void accept(String[] parts) {
            if (i++ == 0) {
                columns = Stream.of(parts).map(String::trim).collect(toList());
            } else {
                Instant instant = instantParseFunction.parse(parts[columns.indexOf(instantParseFunction.getColumn())]);
                series.add(f.apply(parts, columns), instant);
            }
        }
    }
	
	@SafeVarargs
	public static MultipleDoubleSeries parse(String csv, String sep, ParseFunction<Instant> instantF, ParseFunction<Double>... columns) {
        List<String> columnNames = Stream.of(columns).map(ParseFunction::getColumn).collect(toList());
        MultipleDoubleSeries series = new MultipleDoubleSeries(columnNames);
        SeriesConsumer<List<Double>> consumer = new SeriesConsumer<>(series, instantF, (parts, cn) -> Stream.of(columns).map(t -> t.parse(parts[cn.indexOf(t.getColumn())])).collect(toList()));
        CsvReader.parse(csv, sep, consumer);
        return series;
    }

    public static DoubleSeries parse(String csv, String sep, ParseFunction<Instant> instantF, ParseFunction<Double> column) {
        DoubleSeries series = new DoubleSeries(column.getColumn());
        SeriesConsumer<Double> consumer = new SeriesConsumer<>(series, instantF, (parts, columnNames) -> column.parse(parts[columnNames.indexOf(column.getColumn())]));
        CsvReader.parse(csv, sep, consumer);
        return series;
    }

	public static Stream<Bar> parse(String csv, ParseFunction<Double> open, ParseFunction<Double> high, ParseFunction<Double> low, ParseFunction<Double> close,
			ParseFunction<Long> volume, ParseFunction<Instant> instant) {
		
		Stream<String> lines = Stream.of(csv.split("\n"));
		
		return lines.map(l -> l.split(",")).flatMap(new Function<String[], Stream<? extends Bar>>() {

			public List<Object> columns;
			int i = 0;

			@Override
			public Stream<? extends Bar> apply(String[] parts) {
				if (i++ == 0) {
					columns = Stream.of(parts).map(String::trim).collect(toList());
					return Stream.empty();
				} else {
					Bar bar = new Bar() {
						private double mOpen = open.parse(parts[columns.indexOf(open.getColumn())]);
						private double mHigh = high.parse(parts[columns.indexOf(high.getColumn())]);
						private double mLow = low.parse(parts[columns.indexOf(low.getColumn())]);
						private double mClose = close.parse(parts[columns.indexOf(close.getColumn())]);
						private long mVolume = volume.parse(parts[columns.indexOf(volume.getColumn())]);

						@Override
						public double getOpen() {
							return mOpen;
						}

						@Override
						public double getHigh() {
							return mHigh;
						}

						@Override
						public double getLow() {
							return mLow;
						}

						@Override
						public double getClose() {
							return mClose;
						}

						@Override
						public long getVolume() {
							return mVolume;
						}

						@Override
						public Instant getStart() {
							return instant.parse(parts[columns.indexOf(instant.getColumn())]);
						}

						@Override
						public Duration getDuration() {
							return null;
						}

						@Override
						public double getWAP() {
							return 0;
						}
					};
					return Stream.of(bar);
				}
			}
		});
	}
}
