package com.abigdreamer.icequeen.events;

public interface IEventBus {
	
	void Put(Event message);

	Event Get();
}
