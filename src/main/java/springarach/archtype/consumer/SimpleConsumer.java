//package springarach.archtype.consumer;
//
//import reactor.fn.Event;
//import reactor.spring.context.annotation.On;
//import springarach.archtype.event.LogEvent;
//
//import com.alibaba.fastjson.JSON;
//
//public class SimpleConsumer  { //implements Consumer<Event<LogEvent>>
//	/* 
//	 * log 事件异步处理者.
//	 */
//	
//	@On(reactor = "@reactor", selector = "log.event")
//	public void accept(Event<LogEvent> event){
//		System.out.println(Thread.currentThread().getId() + " " + event.getData());
//		System.out.println(JSON.toJSONString(event.getData().getO()));
//	}
//	
//	@On(reactor = "@reactor", selector = "log.event")
//	public String other(Event<LogEvent> event){
//		try {
//			Thread.sleep(2000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("延迟2秒处理.");
//		System.out.println(JSON.toJSONString(event.getData().getO()));
//		
//		return "fdsfd";
//	}
//	
//}
