package oz.courses.spring.integration.direct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class DirectChannelDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DirectChannelDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("one").build());
		inputChannel.send(MessageBuilder.withPayload("two").build());
		inputChannel.send(MessageBuilder.withPayload("three").build());
	}

	@Bean
	public IntegrationFlow sampleFlowA() {
		return IntegrationFlows.from("input")
				.handle(System.out::println)
				.get();
	}

	@Bean
	public IntegrationFlow sampleFlowB() {
		return IntegrationFlows.from("input")
				.handle(System.out::println)
				.get();
	}


}
