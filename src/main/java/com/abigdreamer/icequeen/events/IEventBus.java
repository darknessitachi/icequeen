package com.abigdreamer.icequeen.events;

public interface IEventBus {
	
	void put(Event message);

	Event get();
}
