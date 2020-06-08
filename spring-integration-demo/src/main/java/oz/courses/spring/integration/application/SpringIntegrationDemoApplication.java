package oz.courses.spring.integration.application;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpMethod;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.dsl.HttpRequestHandlerEndpointSpec;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

import oz.courses.spring.integration.ext.UppercasePersonService;

@SpringBootApplication
public class SpringIntegrationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationDemoApplication.class, args);
	}

	@Bean
	public MessageChannel httpErrorChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel httpProcessingChannel() {
		return new DirectChannel();
	}

	public HttpRequestHandlerEndpointSpec httpSource() {
		 return Http.inboundChannelAdapter("/foo")
				 .requestMapping(m -> m.methods(HttpMethod.POST))
				 .errorChannel(httpErrorChannel());
	}

	@Bean
	public IntegrationFlow mainFlow(AmqpTemplate amqpTemplate) {
		return IntegrationFlows.from(httpSource())
				.channel(httpProcessingChannel())
				.handle(new UppercasePersonService())
				.transform(Transformers.toJson())
				.handle(Amqp.outboundAdapter(amqpTemplate).routingKey("OUT"))
				.get();
	}

	@Bean
	public IntegrationFlow errorFlow() {
		return IntegrationFlows.from(httpErrorChannel())
				.log()
				.transform(mhe -> {
					String s = (String) ((MessagingException) mhe).getFailedMessage().getPayload();
					return MessageBuilder.withPayload("{\"name\":\"" + s + "\"}").setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build();
				})
				.log()
				.channel(httpProcessingChannel())
				.get();
	}



}
