package org.lst.trading.lib.series;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.lst.trading.lib.util.Util.check;

/**
 * A general purpose generic time series data structure implementation and which handles stuff like mapping, merging and filtering.
 *  
 * @author Darkness
 * @date 2016年12月2日 上午11:17:28
 * @version V1.0
 */
public class TimeSeries<T> implements Iterable<TimeSeries.Entry<T>> {
	
    public static class Entry<T> {
    	
        T item;
        Instant instant;

        public Entry(T t, Instant instant) {
            item = t;
            this.instant = instant;
        }

        public T getItem() {
            return item;
        }

        public Instant getInstant() {
            return this.instant;
        }

        @SuppressWarnings("rawtypes")
		@Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

			if (!instant.equals(entry.instant)) {
				return false;
			}
			if (item != null ? !item.equals(entry.item) : entry.item != null) {
				return false;
			}
            return true;
        }

        @Override
        public int hashCode() {
            int result = item != null ? item.hashCode() : 0;
            result = 31 * result + instant.hashCode();
            return result;
        }

		@Override
		public String toString() {
			return "Entry{" + "instant=" + instant + ", item=" + item + '}';
		}
    }

    List<Entry<T>> data;

    public TimeSeries() {
        data = new ArrayList<>();
    }

    protected TimeSeries(List<Entry<T>> data) {
        this.data = data;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean add(Entry<T> tEntry) {
        return data.add(tEntry);
    }

    public void add(T item, Instant instant) {
        add(new Entry<T>(item, instant));
    }

    public Stream<Entry<T>> stream() {
        return data.stream();
    }

    public Stream<Entry<T>> reversedStream() {
        check(!(data instanceof LinkedList));
        return IntStream.range(1, data.size() + 1).mapToObj(i -> data.get(data.size() - i));
    }

	@Override
	public Iterator<Entry<T>> iterator() {
		return data.iterator();
	}

    public List<Entry<T>> getData() {
        return Collections.unmodifiableList(data);
    }

    public Entry<T> get(int index) {
        return data.get(index);
    }

    public interface MergeFunction<T, F> {
        F merge(T t1, T t2);
    }

    public interface MergeFunction2<T1, T2, F> {
        F merge(T1 t1, T2 t2);
    }

    public <F> TimeSeries<F> map(Function<T, F> f) {
        List<Entry<F>> newEntries = new ArrayList<>(size());
        for (Entry<T> entry : data) {
            newEntries.add(new Entry<>(f.apply(entry.item), entry.instant));
        }
        return new TimeSeries<>(newEntries);
    }

    public boolean isAscending() {
        return size() <= 1 || get(0).getInstant().isBefore(get(1).instant);
    }

    public TimeSeries<T> toAscending() {
        if (!isAscending()) {
            return reverse();
        }
        return this;
    }

    public TimeSeries<T> toDescending() {
        if (isAscending()) {
            return reverse();
        }
        return this;
    }

    public TimeSeries<T> reverse() {
        ArrayList<Entry<T>> entries = new ArrayList<>(data);
        Collections.reverse(entries);
        return new TimeSeries<>(entries);
    }

    public TimeSeries<T> lag(int k) {
        return lag(k, false, null);
    }

    public TimeSeries<T> lag(int k, boolean addEmpty, T emptyVal) {
        check(k > 0);
        check(data.size() >= k);

        ArrayList<Entry<T>> entries = new ArrayList<>(addEmpty ? data.size() : data.size() - k);
        if (addEmpty) {
            for (int i = 0; i < k; i++) {
                entries.add(new Entry<>(emptyVal, data.get(i).instant));
            }
        }

        for (int i = k; i < size(); i++) {
            entries.add(new Entry<>(data.get(i - k).getItem(), data.get(i).getInstant()));
        }

        return new TimeSeries<>(entries);
    }

    public static <T1, T2, F> TimeSeries<F> merge(TimeSeries<T1> t1, TimeSeries<T2> t2, MergeFunction2<T1, T2, F> f) {
        check(t1.isAscending());
        check(t2.isAscending());

        Iterator<Entry<T1>> i1 = t1.iterator();
        Iterator<Entry<T2>> i2 = t2.iterator();

        List<Entry<F>> newEntries = new ArrayList<>();

        while (i1.hasNext() && i2.hasNext()) {
            Entry<T1> n1 = i1.next();
            Entry<T2> n2 = i2.next();

            while (!n2.instant.equals(n1.instant)) {
                if (n1.instant.isBefore(n2.instant)) {
                    while (i1.hasNext()) {
                        n1 = i1.next();
                        if (!n1.instant.isBefore(n2.instant)) {
                            break;
                        }
                    }
                } else if (n2.instant.isBefore(n1.instant)) {
                    while (i2.hasNext()) {
                        n2 = i2.next();
                        if (!n2.instant.isBefore(n1.instant)) {
                            break;
                        }
                    }
                }
            }

            if (n2.instant.equals(n1.instant)) {
                newEntries.add(new Entry<F>(f.merge(n1.item, n2.item), n1.instant));
            }
        }

        return new TimeSeries<>(newEntries);
    }

    public static <T, F> TimeSeries<F> merge(TimeSeries<T> t1, TimeSeries<T> t2, MergeFunction<T, F> f) {
        return TimeSeries.<T, T, F>merge(t1, t2, f::merge);
    }

    @Override public String toString() {
        return data.isEmpty() ? "TimeSeries{empty}" :
            "TimeSeries{" +
                "from=" + data.get(0).getInstant() +
                ", to=" + data.get(size() - 1).getInstant() +
                ", size=" + data.size() +
            '}';
    }
}
