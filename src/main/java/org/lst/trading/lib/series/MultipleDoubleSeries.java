package org.lst.trading.lib.series;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * A time series class which has multiple doubles as values. (corresponds to a pandas.DataFrame or a R Dataframe)
 *  
 * @author Darkness
 * @date 2016年12月2日 上午11:56:47
 * @version V1.0
 */
public class MultipleDoubleSeries extends TimeSeries<List<Double>> {
	
    List<String> names;

    public MultipleDoubleSeries(Collection<String> names) {
        this.names = new ArrayList<>(names);
    }

    public MultipleDoubleSeries(DoubleSeries... series) {
    	this.names = new ArrayList<>();
        for (int i = 0; i < series.length; i++) {
            if (i == 0) {
                _init(series[i]);
            } else {
                addSeries(series[i]);
            }
        }
    }

    void _init(DoubleSeries series) {
        data = new ArrayList<>();
        for (Entry<Double> entry : series) {
            LinkedList<Double> list = new LinkedList<>();
            list.add(entry.item);
            add(new Entry<>(list, entry.instant));
        }
		this.names.add(series.name);
    }

    public void addSeries(DoubleSeries series) {
        data = TimeSeries.merge(this, series, (l, t) -> {
            l.add(t);
            return l;
        }).data;
        this.names.add(series.name);
    }

    public DoubleSeries getColumn(String name) {
        int index = getNames().indexOf(name);
        List<Entry<Double>> entries = data.stream().map(t -> new Entry<Double>(t.getItem().get(index), t.getInstant())).collect(toList());
        return new DoubleSeries(entries, name);
    }

    public int indexOf(String name) {
        return names.indexOf(name);
    }

    public List<String> getNames() {
        return names;
    }

    @Override public String toString() {
        return data.isEmpty() ? "MultipleDoubleSeries{empty}" :
            "MultipleDoubleSeries{" +
                "mNames={" + names.stream().collect(joining(", ")) +
                ", from=" + data.get(0).getInstant() +
                ", to=" + data.get(data.size() - 1).getInstant() +
                ", size=" + data.size() +
                '}';
    }
}
