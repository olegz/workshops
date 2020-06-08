package oz.courses.spring.integration.executor;

import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class ExecutorChannelDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ExecutorChannelDemo.class, args);
		MessageChannel inputChannel = context.getBean("executorChannel", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("one").build());
		inputChannel.send(MessageBuilder.withPayload("two").build());
		inputChannel.send(MessageBuilder.withPayload("three").build());
	}

	@Bean
	public ExecutorChannel executorChannel() {
		return MessageChannels.executor(Executors.newCachedThreadPool()).get();
	}

	@Bean
	public IntegrationFlow sampleFlowA() {
		return IntegrationFlows.from("executorChannel")
				.handle(v -> System.out.println(Thread.currentThread().getId() + "-Processingg value: " + v))
				.get();
	}

	@Bean
	public IntegrationFlow sampleFlowB() {
		return IntegrationFlows.from("executorChannel")
				.handle(v -> System.out.println(Thread.currentThread().getId() + "-Processingg value: " + v))
				.get();
	}


}
