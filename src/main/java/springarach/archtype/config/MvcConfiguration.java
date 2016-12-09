//package springarach.archtype.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//import reactor.core.Environment;
//import reactor.core.Reactor;
//import reactor.fn.Consumer;
//import reactor.fn.Event;
//import reactor.spring.context.ConsumerBeanPostProcessor;
//import reactor.spring.dynamic.DynamicReactorFactoryBean;
//import springarach.archtype.consumer.SimpleConsumer;
//import springarach.archtype.event.LogEvent;
//
//@Configuration
//@ComponentScan(basePackages="springarach.archtype")
//@EnableWebMvc
//public class MvcConfiguration extends WebMvcConfigurerAdapter{
//	
//	@Bean
//	public ConsumerBeanPostProcessor consumerBeanPostProcessor() {
//		return new ConsumerBeanPostProcessor();
//	}
//	
//	@Bean
//	public ViewResolver getViewResolver(){
//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/WEB-INF/views/");
//		resolver.setSuffix(".jsp");
//		return resolver;
//	}
//	
//	@Bean(name="reactor")
//	public Reactor getReactor(){
//		return new Reactor();
//	}
//	
////	@Bean
////	DynamicReactorFactoryBean<TestReactor> testReactorFactoryBean() {
////		return new DynamicReactorFactoryBean<TestReactor>(new Environment(), TestReactor)
////	}
//	
//	@Bean
//	public SimpleConsumer consumer() {//Consumer<Event<LogEvent>>
//		return new SimpleConsumer();
//	}
//
//	
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//	}
//	
//}
