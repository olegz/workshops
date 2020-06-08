package oz.courses.spring.integration.transform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import oz.courses.spring.integration.ext.Person;

@SpringBootApplication
public class SrviceTransformerDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SrviceTransformerDemo.class, args);
		MessageChannel inputChannel = context.getBean("input", MessageChannel.class);
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"jim\"}").setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"john\"}").setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
		inputChannel.send(MessageBuilder.withPayload("{\"name\":\"jane\"}").setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build());
	}

	@Bean
	public IntegrationFlow sampleFlowA() {
		return IntegrationFlows.from("input")
//				.handle(v -> v)
				.transform(Transformers.fromJson(Person.class))
				.transform(Person.class, v -> v)
				.log()
				.get();
	}

}
