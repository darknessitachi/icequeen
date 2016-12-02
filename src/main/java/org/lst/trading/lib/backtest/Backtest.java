package org.lst.trading.lib.backtest;

import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.TradingStrategy;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;
import org.lst.trading.lib.series.TimeSeries;
import org.lst.trading.lib.util.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.lst.trading.lib.util.Util.check;

/**
 * The core class which runs the backtest
 *  
 * @author Darkness
 * @date 2016年12月2日 上午11:16:47
 * @version V1.0
 */
public class Backtest {

    public static class Result {
        DoubleSeries mPlHistory;
        DoubleSeries mMarginHistory;
        double mPl;
        List<ClosedOrder> mOrders;
        double mInitialFund;
        double mFinalValue;
        double mCommissions;

        public Result(double pl, DoubleSeries plHistory, DoubleSeries marginHistory, List<ClosedOrder> orders, double initialFund, double finalValue, double commisions) {
            mPl = pl;
            mPlHistory = plHistory;
            mMarginHistory = marginHistory;
            mOrders = orders;
            mInitialFund = initialFund;
            mFinalValue = finalValue;
            mCommissions = commisions;
        }

        public DoubleSeries getMarginHistory() {
            return mMarginHistory;
        }

        public double getInitialFund() {
            return mInitialFund;
        }

        public DoubleSeries getAccountValueHistory() {
            return getPlHistory().plus(mInitialFund);
        }

        public double getFinalValue() {
            return mFinalValue;
        }

        public double getReturn() {
            return mFinalValue / mInitialFund - 1;
        }

        public double getAnnualizedReturn() {
            return getReturn() * 250 / getDaysCount();
        }

        public double getSharpe() {
            return Statistics.sharpe(Statistics.returns(getAccountValueHistory().toArray()));
        }

        public double getMaxDrawdown() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[0];
        }

        public double getMaxDrawdownPercent() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[1];
        }

        public int getDaysCount() {
            return mPlHistory.size();
        }

        public DoubleSeries getPlHistory() {
            return mPlHistory;
        }

        public double getPl() {
            return mPl;
        }

        public double getCommissions() {
            return mCommissions;
        }

        public List<ClosedOrder> getOrders() {
            return mOrders;
        }
    }

    MultipleDoubleSeries priceSeries;
    double deposit;
    double leverage = 1;

    TradingStrategy strategy;
    BacktestTradingContext context;

    Iterator<TimeSeries.Entry<List<Double>>> priceIterator;
    Result result;

    public Backtest(double deposit, MultipleDoubleSeries priceSeries) {
        check(priceSeries.isAscending());
        this.deposit = deposit;
        this.priceSeries = priceSeries;
    }

    public void setLeverage(double leverage) {
        this.leverage = leverage;
    }

    public double getLeverage() {
        return leverage;
    }

    public Result run(TradingStrategy strategy) {
        initialize(strategy);
        while (nextStep()) ;
        return result;
    }

    public void initialize(TradingStrategy strategy) {
        this.strategy = strategy;
        context = new BacktestTradingContext();

        context.instruments = priceSeries.getNames();
        context.history = new MultipleDoubleSeries(context.instruments);
        context.initialFunds = deposit;
        context.leverage = leverage;
        
        strategy.onStart(context);
        
        priceIterator = priceSeries.iterator();
        
        nextStep();
    }

    public boolean nextStep() {
        if (!priceIterator.hasNext()) {
            finish();
            return false;
        }

        TimeSeries.Entry<List<Double>> entry = priceIterator.next();

        context.setPrices(entry.getItem());
        context.setTime(entry.getInstant());
        context.addPl(context.getPl(), entry.getInstant());
       
        context.addFundsHistory(context.getAvailableFunds(), entry.getInstant());
        
        if (context.getAvailableFunds() < 0) {
            finish();
            return false;
        }

        strategy.onTick();

        context.addHistory(entry);

        return true;
    }

    public Result getResult() {
        return result;
    }

    private void finish() {
        for (SimpleOrder order : new ArrayList<>(context.orders)) {
            context.close(order);
        }

        strategy.onEnd();

        List<ClosedOrder> orders = Collections.unmodifiableList(context.closedOrders);
        result = new Result(context.getClosedPl(), context.getPlSeries(), context.getFundsHistorySeries(), orders, deposit, deposit + context.getClosedPl(), context.getCommissions());
    }
}
