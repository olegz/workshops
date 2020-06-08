package oz.courses.spring.integration.aggregate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class AggregatorDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AggregatorDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("one").build());
		inputChannel.send(MessageBuilder.withPayload("two").build());
		inputChannel.send(MessageBuilder.withPayload("three").build());
		inputChannel.send(MessageBuilder.withPayload("four").build());
	}

	@Bean
	public IntegrationFlow sampleFlow() {
		return IntegrationFlows.from("input")
				.aggregate(aggregator -> {
					aggregator
						.correlationStrategy(c -> true) 	// basically any message
						.expireGroupsUponCompletion(true) 	// will expire the group so a new group can accumulate
						.releaseStrategy(g -> g.size() == 2);
				})
				.log()
				.get();
	}

}
