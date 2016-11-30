//namespace BackTesting.Model.MarketData
//{
//    using System;
package marketdata;

import java.time.LocalDateTime;

public class Bar {
	
	private LocalDateTime DateTime;
	private String Symbol;
	private String Period;
	private double Open;
	private double High;
	private double Low;
	private double Close;
	private long Volume;

	@Override
	public String toString() {
		return "{this.DateTime} S={this.Symbol} P={this.Period} O={this.Open} H={this.High} L={this.Low} C={this.Close} V={this.Volume}";
	}

	public LocalDateTime getDateTime() {
		return DateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		DateTime = dateTime;
	}

	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	public String getPeriod() {
		return Period;
	}

	public void setPeriod(String period) {
		Period = period;
	}

	public double getOpen() {
		return Open;
	}

	public void setOpen(double open) {
		Open = open;
	}

	public double getHigh() {
		return High;
	}

	public void setHigh(double high) {
		High = high;
	}

	public double getLow() {
		return Low;
	}

	public void setLow(double low) {
		Low = low;
	}

	public double getClose() {
		return Close;
	}

	public void setClose(double close) {
		Close = close;
	}

	public long getVolume() {
		return Volume;
	}

	public void setVolume(long volume) {
		Volume = volume;
	}
}
