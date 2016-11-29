//namespace BackTesting.Model.Events
//{
//    using System;
package events;

import java.time.LocalDateTime;

import enums.EventType;
import enums.OrderType;
import enums.TransactionDirection;

public class OrderEvent extends Event
    {
        @Override
        public EventType getEventType() {
        	return EventType.Order;
        }

        private String Symbol ;
        private OrderType OrderType ;
        private int Quantity ;
        private TransactionDirection OrderDirection ;
        private LocalDateTime OrderTime ;

        public OrderEvent(String symbol, OrderType orderType, int quantity, TransactionDirection orderDirection, LocalDateTime orderTime)
        {
            this.Symbol = symbol;
            this.OrderType = orderType;
            this.Quantity = quantity;
            this.OrderDirection = orderDirection;
            this.OrderTime = orderTime;
        }

        @Override
        public String toString() {
        return "Order: {this.OrderTime} {this.Symbol} {this.OrderDirection} {this.OrderType} Qty={this.Quantity}";
        }

		public String getSymbol() {
			return Symbol;
		}

		public void setSymbol(String symbol) {
			Symbol = symbol;
		}

		public OrderType getOrderType() {
			return OrderType;
		}

		public void setOrderType(OrderType orderType) {
			OrderType = orderType;
		}

		public int getQuantity() {
			return Quantity;
		}

		public void setQuantity(int quantity) {
			Quantity = quantity;
		}

		public TransactionDirection getOrderDirection() {
			return OrderDirection;
		}

		public void setOrderDirection(TransactionDirection orderDirection) {
			OrderDirection = orderDirection;
		}

		public LocalDateTime getOrderTime() {
			return OrderTime;
		}

		public void setOrderTime(LocalDateTime orderTime) {
			OrderTime = orderTime;
		}
    }
