package com.abigdreamer.icequeen.marketdata;

import java.time.LocalDateTime;

public class Bar {
	
	private LocalDateTime dateTime;
	private String symbol;
	private String period;
	private double open;
	private double high;
	private double low;
	private double close;
	private long volume;

	@Override
	public String toString() {
		return String.format("%s S=%s P=%s O=%s H=%s L=%s C=%s V=%s",
				dateTime, symbol, period, open, high, low, close, volume);
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

}
