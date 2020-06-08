package oz.courses.spring.integration.xml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

@SpringBootApplication
@ImportResource(locations = "classpath:*spring-config.xml")
public class SpringIntegrationXmlDemo {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringIntegrationXmlDemo.class, args);
		MessageChannel channel = context.getBean("inputChannel", MessageChannel.class);
		channel.send(MessageBuilder.withPayload("{\"name\":\"bob\"}")
				.setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
				.build());
	}

}
