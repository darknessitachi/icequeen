package com.abigdreamer.icequeen.events;

import java.time.LocalDateTime;

import com.abigdreamer.icequeen.enums.EventType;
import com.abigdreamer.icequeen.enums.OrderType;
import com.abigdreamer.icequeen.enums.TransactionDirection;

/**
 * 当一个Portfolio对象收到SignalEvent的IT评估他们投资组合的更广泛的背景下，风险和仓位大小的条款。
 * 这最终导致OrderEventS中的将被发送到一个ExecutionHandler。
 * @author darkness
 *
 */
public class OrderEvent extends Event {
	
	private String symbol;
	private OrderType orderType;
	private int quantity;
	private TransactionDirection orderDirection;
	private LocalDateTime orderTime;

	public OrderEvent(String symbol, OrderType orderType, int quantity, TransactionDirection orderDirection,
			LocalDateTime orderTime) {
		this.symbol = symbol;
		this.orderType = orderType;
		this.quantity = quantity;
		this.orderDirection = orderDirection;
		this.orderTime = orderTime;
	}

	@Override
	public String toString() {
		return "Order: "+this.orderTime+", "+this.symbol+", "+this.orderDirection+", "+this.orderType+", Qty="+ this.quantity;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TransactionDirection getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(TransactionDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}
	
	@Override
	public EventType getEventType() {
		return EventType.Order;
	}
}
