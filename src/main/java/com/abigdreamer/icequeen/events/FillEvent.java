package com.abigdreamer.icequeen.events;

import java.time.LocalDateTime;

import com.abigdreamer.icequeen.enums.EventType;
import com.abigdreamer.icequeen.enums.TransactionDirection;

/**
 * 当ExecutionHandler接收到一个OrderEvent必须办理顺序。一旦订单已经办完了它生成一个FillEvent，这说明购买或出售的成本以及交易成本等费用或延误。
 * @author darkness
 *
 */
public class FillEvent extends Event {
	
	private LocalDateTime timeIndex;
	private String symbol;
	private String exchange;
	private int quantity;
	private TransactionDirection direction;
	private double fillCost;
	private double comission;

	public FillEvent(LocalDateTime timeIndex, String symbol, String exchange, int quantity,
			TransactionDirection direction, double fillCost) {
		this.timeIndex = timeIndex;
		this.symbol = symbol;
		this.exchange = exchange;
		this.quantity = quantity;
		this.direction = direction;
		this.fillCost = fillCost;
		this.comission = this.CalculateIbComission();
	}

	public FillEvent(LocalDateTime timeIndex, String symbol, String exchange, int quantity,
			TransactionDirection direction, double fillCost, double comission) {
		this.timeIndex = timeIndex;
		this.symbol = symbol;
		this.exchange = exchange;
		this.quantity = quantity;
		this.direction = direction;
		this.fillCost = fillCost;
		this.comission = comission == 0 ? this.CalculateIbComission() : comission;
	}
	
	@Override
	public com.abigdreamer.icequeen.enums.EventType getEventType() {
		return EventType.Fill;
	}

	/// Calculates the fees of trading based on an Interactive Brokers fee
	/// structure for API, in USD.
	/// This does not include exchange or ECN fees.
	/// Based on "US API Directed Orders":
	/// https://www.interactivebrokers.com/en/index.php?f=commission&p=stocks2
	/**
	 * 计算交易费
	 * @return
	 */
	private double CalculateIbComission() {
		double fullCost = this.quantity <= 500 ? Math.max(1.3, 0.013 * this.quantity)
				: Math.max(1.3, 0.008 * this.quantity);
		return Math.min(fullCost, (0.5 / 100.0 * this.quantity * this.fillCost));
	}

	@Override
	public String toString() {
		return "Fill: "+this.timeIndex+", "+this.exchange+", "+this.symbol+", "+this.direction+", Qty="+this.quantity+", FillCost="+this.fillCost+", Comission="+this.comission;
	}

	public LocalDateTime getTimeIndex() {
		return timeIndex;
	}

	public void setTimeIndex(LocalDateTime timeIndex) {
		this.timeIndex = timeIndex;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TransactionDirection getDirection() {
		return direction;
	}

	public void setDirection(TransactionDirection direction) {
		this.direction = direction;
	}

	public double getFillCost() {
		return fillCost;
	}

	public void setFillCost(double fillCost) {
		this.fillCost = fillCost;
	}

	public double getComission() {
		return comission;
	}

	public void setComission(double comission) {
		this.comission = comission;
	}
}
