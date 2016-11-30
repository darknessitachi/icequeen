package events;

public interface IEventBus {
	
	void Put(Event message);

	Event Get();
}
