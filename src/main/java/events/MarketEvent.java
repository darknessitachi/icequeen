//namespace BackTesting.Model.Events
//{
//    using System;
package events;

import java.time.LocalDateTime;

import enums.EventType;

/// <summary>
    /// Handles the event of receiving a new market update with corresponding bars.
    /// </summary>
    public class MarketEvent extends Event
    {
        @Override
        public EventType getEventType() {
        	return EventType.Market;
        }

        private LocalDateTime CurrentTime;

        public MarketEvent(LocalDateTime currentTime)
        {
            this.CurrentTime = currentTime;
        }
        
        public LocalDateTime getCurrentTime() {
			return CurrentTime;
		}
    }
