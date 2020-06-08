package oz.courses.spring.integration.pubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@SpringBootApplication(excludeName = "oz.courses.spring.integration.*")
public class PubSubChannelDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PubSubChannelDemo.class, args);
		MessageChannel inputChannel = context.getBean("pubSubChannel", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("one").build());
		inputChannel.send(MessageBuilder.withPayload("two").build());
		inputChannel.send(MessageBuilder.withPayload("three").build());
	}

	@Bean
	public SubscribableChannel pubSubChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public IntegrationFlow sampleFlowA() {
		return IntegrationFlows.from("pubSubChannel")
				.handle(System.out::println)
				.get();
	}

	@Bean
	public IntegrationFlow sampleFlowB() {
		return IntegrationFlows.from("pubSubChannel")
				.handle(System.out::println)
				.get();
	}
}
