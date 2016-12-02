package org.lst.trading.main.strategy.kalman;

import org.la4j.Matrix;

/**
 * A cointegration model using a Kalman filter.
 *  
 * @author Darkness
 * @date 2016年12月2日 下午12:01:05
 * @version V1.0
 */
public class Cointegration {
	
    double delta;
    double r;
    KalmanFilter filter;
    int nobs = 2;

    public Cointegration(double delta, double r) {
        this.delta = delta;
        this.r = r;

        Matrix vw = Matrix.identity(nobs).multiply(this.delta / (1 - delta));
        Matrix a = Matrix.identity(nobs);

        Matrix x = Matrix.zero(nobs, 1);

        filter = new KalmanFilter(nobs, 1);
        filter.setUpdateMatrix(a);
        filter.setState(x);
        filter.setStateCovariance(Matrix.zero(nobs, nobs));
        filter.setUpdateCovariance(vw);
        filter.setMeasurementCovariance(Matrix.constant(1, 1, r));
    }

    public void step(double x, double y) {
        filter.setExtractionMatrix(Matrix.from1DArray(1, 2, new double[]{1, x}));
        filter.step(Matrix.constant(1, 1, y));
    }

    public double getAlpha() {
        return filter.getState().getRow(0).get(0);
    }

    public double getBeta() {
        return filter.getState().getRow(1).get(0);
    }

    public double getVariance() {
        return filter.getInnovationCovariance().get(0, 0);
    }

    public double getError() {
        return filter.getInnovation().get(0, 0);
    }
}
