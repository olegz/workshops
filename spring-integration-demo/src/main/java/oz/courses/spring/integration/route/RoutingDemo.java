	package oz.courses.spring.integration.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@SpringBootApplication
public class RoutingDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RoutingDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("1").setHeader("destination", "odd").build());
		inputChannel.send(MessageBuilder.withPayload("2").setHeader("destination", "even").build());
		inputChannel.send(MessageBuilder.withPayload("3").setHeader("destination", "odd").build());

		PollableChannel discardChannel = context.getBean("discardChannel", PollableChannel.class);
		for (int i = 0; i < 3; i++) {
			System.out.println("Discarded Message: " + discardChannel.receive(1000));
		}
	}

	@Bean
	public PollableChannel discardChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public IntegrationFlow sampleFlow() {
		return IntegrationFlows.from("input")
				.route(Integer.class, payload -> {
					return payload % 2 == 0 ? "even" : "odd";
				})
//				.route("headers['destination']")
//				.routeToRecipients(config -> config
//						.recipient("even")
//						.recipient("odd")
//						.recipient("blah"))
//				.filter(Integer.class, payload -> payload % 2 == 0, conf -> conf.discardChannel(discardChannel()))
//				.log()
				.get();
	}

	@Bean
	public IntegrationFlow evenFlow() {
		return IntegrationFlows.from("even")
				.handle(v -> System.out.println("EVEN: " + v))
				.get();
	}

	@Bean
	public IntegrationFlow oddFlow() {
		return IntegrationFlows.from("odd")
				.handle(v -> System.out.println("ODD: " + v))
				.get();
	}

	@Bean
	public IntegrationFlow blahFlow() {
		return IntegrationFlows.from("blah")
				.handle(v -> System.out.println("BLAH: " + v))
				.get();
	}

}
