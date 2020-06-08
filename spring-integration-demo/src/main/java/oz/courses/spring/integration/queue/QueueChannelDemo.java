package oz.courses.spring.integration.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@SpringBootApplication
public class QueueChannelDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(QueueChannelDemo.class, args);
		MessageChannel inputChannel = context.getBean("queueChannel", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("one").build());
		inputChannel.send(MessageBuilder.withPayload("two").build());
		inputChannel.send(MessageBuilder.withPayload("three").build());
	}

	@Bean
	public PollableChannel queueChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public IntegrationFlow sampleFlowA() {
		return IntegrationFlows.from("queueChannel")
				.handle(System.out::println, c -> c.poller(Pollers.fixedRate(1000).maxMessagesPerPoll(1).get())).get();
	}

}
