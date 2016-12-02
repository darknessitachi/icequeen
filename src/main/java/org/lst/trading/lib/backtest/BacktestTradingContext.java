package org.lst.trading.lib.backtest;

import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;
import org.lst.trading.lib.series.TimeSeries;
import org.lst.trading.lib.series.TimeSeries.Entry;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.lst.trading.lib.util.Util.check;

class BacktestTradingContext implements TradingContext {
	
    private Instant instant;
    private List<Double> prices;
    List<String> instruments;
    private DoubleSeries plSeries = new DoubleSeries("pl");
    private DoubleSeries fundsHistorySeries = new DoubleSeries("funds");
    MultipleDoubleSeries history;
    double initialFunds;
    private double commissions;

    int orderId = 1;

    List<SimpleOrder> orders = new ArrayList<>();

    private double closedPl = 0;
    List<SimpleClosedOrder> closedOrders = new ArrayList<>();
    double leverage;
    
    public DoubleSeries getFundsHistorySeries() {
		return fundsHistorySeries;
	}
    
    public double getCommissions() {
		return commissions;
	}

    public double getClosedPl() {
		return closedPl;
	}
    
	@Override
	public Instant getTime() {
		return instant;
	}
	
	public DoubleSeries getPlSeries() {
		return plSeries;
	}
	
	public void setTime(Instant time) {
		this.instant = time;
	}
	
	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}

	@Override
	public double getLastPrice(String instrument) {
		return prices.get(instruments.indexOf(instrument));
	}

	@Override
	public Stream<TimeSeries.Entry<Double>> getHistory(String instrument) {
		int index = instruments.indexOf(instrument);
		return history.reversedStream().map(t -> new TimeSeries.Entry<>(t.getItem().get(index), t.getInstant()));
	}

	@Override
	public Order order(String instrument, boolean buy, int amount) {
		check(amount > 0);

        double price = getLastPrice(instrument);
        SimpleOrder order = new SimpleOrder(orderId++, instrument, getTime(), price, amount * (buy ? 1 : -1));
        orders.add(order);

        commissions += calculateCommission(order);

        return order;
    }

	@Override
	public ClosedOrder close(Order order) {
        SimpleOrder simpleOrder = (SimpleOrder) order;
        orders.remove(simpleOrder);
        double price = getLastPrice(order.getInstrument());
        SimpleClosedOrder closedOrder = new SimpleClosedOrder(simpleOrder, price, getTime());
        closedOrders.add(closedOrder);
        closedPl += closedOrder.getPl();
        commissions += calculateCommission(order);

        return closedOrder;
    }

	@Override
	public double getPl() {
        return closedPl + orders.stream().mapToDouble(t -> t.calculatePl(getLastPrice(t.getInstrument()))).sum() - commissions;
    }

	@Override
	public List<String> getInstruments() {
        return instruments;
    }

	@Override
	public double getAvailableFunds() {
        return getNetValue() - orders.stream().mapToDouble(t -> Math.abs(t.getAmount()) * t.getOpenPrice() / leverage).sum();
    }

	@Override
	public double getInitialFunds() {
        return initialFunds;
    }

	@Override
	public double getNetValue() {
        return initialFunds + getPl();
    }

	@Override
	public double getLeverage() {
        return leverage;
    }

    double calculateCommission(Order order) {
        return 1 + Math.abs(order.getAmount()) * 0.005;
    }

	public void addPl(double pl, Instant instant) {
		this.plSeries.add(pl, instant);
	}

	public void addFundsHistory(double availableFunds, Instant instant) {
		this.fundsHistorySeries.add(availableFunds, instant);
	}

	public void addHistory(Entry<List<Double>> entry) {
		this.history.add(entry);
	}
}

