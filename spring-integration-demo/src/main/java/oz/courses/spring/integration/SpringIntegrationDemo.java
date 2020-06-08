package oz.courses.spring.integration;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;

import oz.courses.spring.integration.ext.Person;

@SpringBootApplication
public class SpringIntegrationDemo {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringIntegrationDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"oleg\"}")
				.setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"oleg1\"}")
				.setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"oleg2\"}")
				.setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
	}

//	@Bean
//	public IntegrationFlow directChannelFlow() {
//		return IntegrationFlows.from("input").handle(new MyHandler(), "handleWithHeaders").get();
//	}
//
//	@Bean
//	public IntegrationFlow pollableChannelFlow() {
//		return IntegrationFlows.from(queueChannel())
//				.handle(new MyHandler(), "handle", e -> e.poller(Pollers.fixedRate(100).maxMessagesPerPoll(1).get())).get();
//	}
//
//	@Bean
//	public IntegrationFlow routingFlow() {
//		return IntegrationFlows.from("input")
//				.route(Message.class, message -> {
//
//					return "pollableChannelFlow";
//				})
//				.handle(c -> {})
//				.get();
//	}
//
//	@Bean
//	public PollableChannel queueChannel() {
//		return MessageChannels.queue(new SimpleMessageStore(), "queueChannel").get();
//	}
//
//
//
//	@Bean(name = PollerMetadata.DEFAULT_POLLER)
//	public PollerMetadata poller() { // 11
//		return Pollers.fixedRate(2000).maxMessagesPerPoll(1).get();
//	}
//
//	public static class MyHandler {
//		public void handle(Person person) {
//			System.out.println("Person: " + person);
//		}
//
//		public void handleWithHeaders(String person, Map<String, Object> headers) {
//			System.out.println("Person: " + person + "; headers: " + headers);
//		}
//	}
}
