//namespace BackTesting.Model.Events
//{
//    using System.Collections.Generic;
package events;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuedEventBus implements IEventBus
    {
        private  Queue<Event> queue;
        private static  Object sync = new Object();

        public QueuedEventBus()
        {
            this.queue = new LinkedBlockingQueue<Event>();
        }

        public Event Get()
        {
            synchronized (sync) {
                return this.queue.size() <= 0 ? null : this.queue.poll();
            }
        }

        public void Put(Event message)
        {
        	synchronized (sync)
            {
                this.queue.offer(message);
            }
        }
    }
