package com.abigdreamer.icequeen.events;

import java.time.LocalDateTime;

import com.abigdreamer.icequeen.enums.EventType;

/**
 * 当它发生DataHandler对象接收的市场数据的新的更新为其当前正在跟踪的任何符号。它被用于触发Strategy对象生成新的交易的信号。事件对象仅仅包含的识别，这是一个市场事件，没有其它结构。
 * Handles the event of receiving a new market update with corresponding bars.
 * 
 * @author darkness
 *
 */
public class MarketEvent extends Event {
	
	@Override
	public EventType getEventType() {
		return EventType.Market;
	}

	private LocalDateTime CurrentTime;

	public MarketEvent(LocalDateTime currentTime) {
		this.CurrentTime = currentTime;
	}

	public LocalDateTime getCurrentTime() {
		return CurrentTime;
	}
}
