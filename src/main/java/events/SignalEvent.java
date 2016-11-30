package events;

import java.time.LocalDateTime;

import enums.EventType;
import enums.SignalType;

/**
 * 该Strategy对象利用市场数据来创建新的SignalEvent秒。
 * 该SignalEvent包含一个股票代码，生成时的时间戳和方向（长或短）。
 * 在SignalEventS被利用Portfolio对象的意见如何交易。
 * @author darkness
 *
 */
public class SignalEvent extends Event {
	
	@Override
	public EventType getEventType() {
		return EventType.Signal;
	}

	private String Symbol;
	private LocalDateTime TimeStamp;
	private SignalType SignalType;
	private double Strength;

	public SignalEvent(String symbol, LocalDateTime timeStamp, SignalType signalType, double strength) {
		this.Symbol = symbol;
		this.TimeStamp = timeStamp;
		this.SignalType = signalType;
		this.Strength = strength;// 1
	}

	public SignalEvent(String symbol, LocalDateTime timeStamp, SignalType signalType) {
		this.Symbol = symbol;
		this.TimeStamp = timeStamp;
		this.SignalType = signalType;
		this.Strength = 1;
	}

	@Override
	public String toString() {
		return "Signal: {this.TimeStamp} {this.Symbol} {this.SignalType} Strength={this.Strength}";
	}

	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	public LocalDateTime getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		TimeStamp = timeStamp;
	}

	public SignalType getSignalType() {
		return SignalType;
	}

	public void setSignalType(SignalType signalType) {
		SignalType = signalType;
	}

	public double getStrength() {
		return Strength;
	}

	public void setStrength(double strength) {
		Strength = strength;
	}
}
