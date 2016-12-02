package org.lst.trading.main.strategy.kalman;

import org.apache.commons.math3.stat.StatUtils;
import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;
import org.lst.trading.lib.series.TimeSeries;
import org.lst.trading.lib.util.Util;
import org.lst.trading.main.strategy.AbstractTradingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The cointegration strategy implementation.
 *  
 * @author Darkness
 * @date 2016年12月2日 下午12:02:37
 * @version V1.0
 */
public class CointegrationTradingStrategy extends AbstractTradingStrategy {
	
    private static Logger log = LoggerFactory.getLogger(CointegrationTradingStrategy.class);

    boolean reinvest = false;

    String x, y;
    TradingContext context;
    Cointegration cointegration;

    DoubleSeries alpha;
    DoubleSeries beta;
    DoubleSeries xSeries;
    DoubleSeries ySeries;
    DoubleSeries errorSeries;
    DoubleSeries variance;
    DoubleSeries model;

    Order xOrder;
    Order yOrder;

    public CointegrationTradingStrategy(String x, String y) {
        this(1, x, y);
    }

    public CointegrationTradingStrategy(double weight, String x, String y) {
        setWeight(weight);
        this.x = x;
        this.y = y;
    }

	@Override
	public void onStart(TradingContext context) {
		this.context = context;
		cointegration = new Cointegration(1e-10, 1e-7);
		alpha = new DoubleSeries("alpha");
		beta = new DoubleSeries("beta");
		xSeries = new DoubleSeries("x");
		ySeries = new DoubleSeries("y");
		errorSeries = new DoubleSeries("error");
		variance = new DoubleSeries("variance");
		model = new DoubleSeries("model");
	}

	@Override
	public void onTick() {
        double x = context.getLastPrice(this.x);
        double y = context.getLastPrice(this.y);
        double alpha = cointegration.getAlpha();
        double beta = cointegration.getBeta();

        this.cointegration.step(x, y);
        this.alpha.add(alpha, context.getTime());
        this.beta.add(beta, context.getTime());
        this.xSeries.add(x, context.getTime());
        this.ySeries.add(y, context.getTime());
        this.errorSeries.add(cointegration.getError(), context.getTime());
        this.variance.add(cointegration.getVariance(), context.getTime());

        double error = cointegration.getError();

        model.add(beta * x + alpha, context.getTime());

        if (errorSeries.size() > 30) {
            double[] lastValues = errorSeries.reversedStream().mapToDouble(TimeSeries.Entry::getItem).limit(15).toArray();
            double sd = Math.sqrt(StatUtils.variance(lastValues));

            if (yOrder == null && Math.abs(error) > sd) {
                double value = reinvest ? context.getNetValue() : context.getInitialFunds();
                double baseAmount = (value * getWeight() * 0.5 * Math.min(4, context.getLeverage())) / (y + beta * x);

                if (beta > 0 && baseAmount * beta >= 1) {
                    yOrder = context.order(this.y, error < 0, (int) baseAmount);
                    xOrder = context.order(this.x, error > 0, (int) (baseAmount * beta));
                }
                //log.debug("Order: baseAmount={}, residual={}, sd={}, beta={}", baseAmount, residual, sd, beta);
            } else if (yOrder != null) {
                if (yOrder.isLong() && error > 0 || !yOrder.isLong() && error < 0) {
                    context.close(yOrder);
                    context.close(xOrder);

                    yOrder = null;
                    xOrder = null;
                }
            }
        }
    }

	@Override
	public void onEnd() {
		MultipleDoubleSeries series = new MultipleDoubleSeries(xSeries, ySeries, alpha, beta, errorSeries, variance, model);
		log.debug("Kalman filter statistics: " + Util.writeCsv(series));
	}

	@Override
	public String toString() {
        return "CointegrationStrategy{" +
            "y='" + y + '\'' +
            ", x='" + x + '\'' +
            '}';
    }

    public DoubleSeries getAlpha() {
        return alpha;
    }

    public DoubleSeries getBeta() {
        return beta;
    }

    public DoubleSeries getXs() {
        return xSeries;
    }

    public DoubleSeries getYs() {
        return ySeries;
    }

    public DoubleSeries getError() {
        return errorSeries;
    }

    public DoubleSeries getVariance() {
        return variance;
    }

    public DoubleSeries getModel() {
        return model;
    }
}
