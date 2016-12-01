package com.abigdreamer.icequeen.events;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuedEventBus implements IEventBus {
	
	private Queue<Event> queue;
	private static Object sync = new Object();

	public QueuedEventBus() {
		this.queue = new LinkedBlockingQueue<Event>();
	}

	public Event get() {
		synchronized (sync) {
			if(this.queue.isEmpty()) {
				return null;
			}
			return this.queue.poll();
		}
	}

	public void put(Event message) {
		synchronized (sync) {
			this.queue.offer(message);
		}
	}
}
