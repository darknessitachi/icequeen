package org.lst.trading.lib.csv;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReader {
	
    public interface ParseFunction<T> {
    	
        T parse(String value);

        String getColumn();

        default <F> ParseFunction<F> map(Function<T, F> f) {
            return new ParseFunction<F>() {
				@Override
				public F parse(String value) {
					T t = ParseFunction.this.parse(value);
					return f.apply(t);
				}

				@Override
				public String getColumn() {
					return ParseFunction.this.getColumn();
				}
			};
        }

        static Function<String, String> stripQuotes() {
            return s -> {
                s = s.replaceFirst("^\"", "");
                s = s.replaceFirst("\"$", "");
                return s;
            };
        }

        static ParseFunction<String> ofColumn(String columnName) {
            return new ParseFunction<String>() {
				@Override
				public String parse(String value) {
					return value;
				}

				@Override
				public String getColumn() {
					return columnName;
				}
            };
        }

        static ParseFunction<Long> longColumn(String column) {
            return ofColumn(column).map(Long::parseLong);
        }

        static ParseFunction<Double> doubleColumn(String column) {
            return ofColumn(column).map(Double::parseDouble);
        }

        static ParseFunction<LocalDateTime> localDateTimeColumn(String column, DateTimeFormatter formatter) {
            return ofColumn(column).map(x -> LocalDateTime.from(formatter.parse(x)));
        }

        static ParseFunction<Instant> instantColumn(String column, DateTimeFormatter formatter) {
            return ofColumn(column).map(x -> Instant.from(formatter.parse(x)));
        }

        static ParseFunction<LocalDate> localDateColumn(String column, DateTimeFormatter formatter) {
            return ofColumn(column).map(x -> LocalDate.from(formatter.parse(x)));
        }

        static ParseFunction<LocalDate> localTimeColumn(String column, DateTimeFormatter formatter) {
            return ofColumn(column).map(x -> LocalDate.from(formatter.parse(x)));
        }
    }

    public interface Function2<T1, T2, F> {
        F apply(T1 t1, T2 t2);
    }

    private static class ListConsumer<T> implements Consumer<String[]> {
    	
        int i = 0;
        List<T> entries = new ArrayList<>();
        Function<String[], T> function;
        boolean hasHeader;

        public ListConsumer(Function<String[], T> f, boolean hasHeader) {
            this.function = f;
            this.hasHeader = hasHeader;
        }

		@Override
		public void accept(String[] parts) {
			if (i++ > 0 || !hasHeader) {
				entries.add(function.apply(parts));
			}
		}

        public List<T> getEntries() {
            return entries;
        }
    }

    public static <T extends Consumer<String[]>> void parse(String csv, String sep, T consumer) {
    	Stream<String> lines = Stream.of(csv.split("\n"));
    	parse(lines, sep, consumer);
    }
    
    private static <T extends Consumer<String[]>> void parse(Stream<String> lines, String sep, T consumer) {
        lines.map(line -> line.split(sep)).forEach(consumer);
    }

    public static <T> List<T> parse(Stream<String> lines, String sep, boolean hasHeader, Function<String[], T> f) {
        ListConsumer<T> consumer = new ListConsumer<T>(f, hasHeader);
        parse(lines, sep, consumer);
        return consumer.getEntries();
    }

}
