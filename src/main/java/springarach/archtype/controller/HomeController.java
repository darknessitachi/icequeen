package springarach.archtype.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import reactor.Fn;
import reactor.core.Reactor;
import springarach.archtype.event.LogEvent;

@Controller
public class HomeController {
	
	@Autowired 
	Reactor reactor;

	@RequestMapping(value = "/")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		// lets send some stuff
		LogEvent logEvent = new LogEvent();
		logEvent.setMessage("hello world");
		logEvent.setTimestamp(new Date());
		logEvent.setSource("test");
		logEvent.setO(reactor);
		// send
		reactor.notify("log.event", Fn.event(logEvent));
		
		System.out.println("after notified");
		
		Map model=new HashMap();
		model.put("result", "您的事件已受理.稍后将会通知你.");
		model.put("success", true);
		
		return new ModelAndView("home",model);
	}
}
