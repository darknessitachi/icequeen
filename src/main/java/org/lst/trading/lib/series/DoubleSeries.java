package org.lst.trading.lib.series;

import java.util.List;
import java.util.function.Function;

/**
 * A time series class which has doubles as values. (corresponds to a pandas.Series (python))
 *  
 * @author Darkness
 * @date 2016年12月2日 上午11:29:03
 * @version V1.0
 */
public class DoubleSeries extends TimeSeries<Double> {
	
    String name;

    DoubleSeries(List<Entry<Double>> data, String name) {
        super(data);
        this.name = name;
    }

    public DoubleSeries(String name) {
        super();
		this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public DoubleSeries merge(DoubleSeries other, MergeFunction<Double, Double> f) {
        return new DoubleSeries(DoubleSeries.merge(this, other, f).data, name);
    }

    public DoubleSeries mapToDouble(Function<Double, Double> f) {
        return new DoubleSeries(map(f).data, name);
    }

    public DoubleSeries plus(DoubleSeries other) {
        return merge(other, (x, y) -> x + y);
    }

    public DoubleSeries plus(double other) {
        return mapToDouble(x -> x + other);
    }

    public DoubleSeries mul(DoubleSeries other) {
        return merge(other, (x, y) -> x * y);
    }

    public DoubleSeries mul(double factor) {
        return mapToDouble(x -> x * factor);
    }

    public DoubleSeries div(DoubleSeries other) {
        return merge(other, (x, y) -> x / y);
    }

    public DoubleSeries returns() {
        return this.div(lag(1)).plus(-1);
    }

    public double getLast() {
        return getData().get(size() - 1).getItem();
    }

    public DoubleSeries tail(int n) {
        return new DoubleSeries(getData().subList(size() - n, size()), getName());
    }

    public DoubleSeries returns(int days) {
        return this.div(lag(days)).plus(-1);
    }

    public double[] toArray() {
        return stream().mapToDouble(Entry::getItem).toArray();
    }

	@Override
	public DoubleSeries toAscending() {
		return new DoubleSeries(super.toAscending().data, getName());
	}

	@Override
	public DoubleSeries toDescending() {
		return new DoubleSeries(super.toDescending().data, getName());
	}

	@Override
	public DoubleSeries lag(int k) {
		return new DoubleSeries(super.lag(k).data, getName());
	}

	@Override
	public String toString() {
        return data.isEmpty() ? "DoubleSeries{empty}" :
            "DoubleSeries{" +
                "mName=" + name +
                ", from=" + data.get(0).getInstant() +
                ", to=" + data.get(data.size() - 1).getInstant() +
                ", size=" + data.size() +
                '}';
    }
}
