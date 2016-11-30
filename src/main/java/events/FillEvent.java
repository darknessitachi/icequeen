package events;

import java.time.LocalDateTime;

import enums.EventType;
import enums.TransactionDirection;

/**
 * 当ExecutionHandler接收到一个OrderEvent必须办理顺序。一旦订单已经办完了它生成一个FillEvent，这说明购买或出售的成本以及交易成本等费用或延误。
 * @author darkness
 *
 */
public class FillEvent extends Event {
	
	@Override
	public enums.EventType getEventType() {
		return EventType.Fill;
	}

	private LocalDateTime TimeIndex;
	private String Symbol;
	private String Exchange;
	private int Quantity;
	private TransactionDirection Direction;
	private double FillCost;
	private double Comission;

	public FillEvent(LocalDateTime timeIndex, String symbol, String exchange, int quantity,
			TransactionDirection direction, double fillCost) {
		this.TimeIndex = timeIndex;
		this.Symbol = symbol;
		this.Exchange = exchange;
		this.Quantity = quantity;
		this.Direction = direction;
		this.FillCost = fillCost;
		this.Comission = this.CalculateIbComission();
	}

	public FillEvent(LocalDateTime timeIndex, String symbol, String exchange, int quantity,
			TransactionDirection direction, double fillCost, double comission) {
		this.TimeIndex = timeIndex;
		this.Symbol = symbol;
		this.Exchange = exchange;
		this.Quantity = quantity;
		this.Direction = direction;
		this.FillCost = fillCost;
		this.Comission = comission == 0 ? this.CalculateIbComission() : comission;
	}

	/// <summary>
	/// Calculates the fees of trading based on an Interactive Brokers fee
	/// structure for API, in USD.
	/// This does not include exchange or ECN fees.
	/// Based on "US API Directed Orders":
	/// https://www.interactivebrokers.com/en/index.php?f=commission&p=stocks2
	/// </summary>
	/// <returns></returns>
	/**
	 * 计算交易费
	 * @return
	 */
	private double CalculateIbComission() {
		double fullCost = this.Quantity <= 500 ? Math.max(1.3, 0.013 * this.Quantity)
				: Math.max(1.3, 0.008 * this.Quantity);
		return Math.min(fullCost, (0.5 / 100.0 * this.Quantity * this.FillCost));
	}

	@Override
	public String toString() {
		return "Fill: {this.TimeIndex} {this.Exchange} {this.Symbol} {this.Direction} Qty={this.Quantity} FillCost={this.FillCost} Comission={this.Comission}";
	}

	public LocalDateTime getTimeIndex() {
		return TimeIndex;
	}

	public void setTimeIndex(LocalDateTime timeIndex) {
		TimeIndex = timeIndex;
	}

	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	public String getExchange() {
		return Exchange;
	}

	public void setExchange(String exchange) {
		Exchange = exchange;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public TransactionDirection getDirection() {
		return Direction;
	}

	public void setDirection(TransactionDirection direction) {
		Direction = direction;
	}

	public double getFillCost() {
		return FillCost;
	}

	public void setFillCost(double fillCost) {
		FillCost = fillCost;
	}

	public double getComission() {
		return Comission;
	}

	public void setComission(double comission) {
		Comission = comission;
	}
}
