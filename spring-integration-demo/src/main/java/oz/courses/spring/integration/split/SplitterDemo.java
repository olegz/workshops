package oz.courses.spring.integration.split;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class SplitterDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SplitterDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload(new int[] {1, 2, 3, 4, 5, 6, 7, 8}).build());
	}

	@Bean
	public IntegrationFlow sampleFlow() {
		return IntegrationFlows.from("input")
				.split() // comment/uncomment and see the difference
				.log()
				.get();
	}

}
