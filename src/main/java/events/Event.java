package events;

import enums.EventType;

/**
 * 该Event是事件驱动的系统的基本处级单位。它包含一个类型（例如，“市场”，“信号”，“订单”或“填充”），它确定如何将事件的循环中进行处理。
 * Event is base class providing an interface for all subsequent (inherited) events, 
 * that will trigger further events in the trading infrastructure.
 * @author darkness
 *
 */
public abstract class Event {
	public abstract EventType getEventType();
}
