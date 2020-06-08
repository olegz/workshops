package oz.courses.spring.integration.application;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.dsl.HttpRequestHandlerEndpointSpec;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import oz.courses.spring.integration.ext.UppercasePersonService;

@SpringBootApplication
public class SpringIntegrationDemoApplicationRouter {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationDemoApplicationRouter.class, args);
	}

	@Bean
	public MessageChannel httpFixingChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel httpProcessingChannel() {
		return new DirectChannel();
	}

	public HttpRequestHandlerEndpointSpec httpSource() {
		 return Http.inboundChannelAdapter("/si")
				 .requestMapping(m -> m.methods(HttpMethod.POST));
	}

	@Bean
	public IntegrationFlow httpFlow() {
		return IntegrationFlows.from(httpSource())
				.transform(Transformers.objectToString())
				.route(String.class, payload -> {
					return payload.startsWith("{") ? "httpProcessingChannel" : "httpFixingChannel";
				})
				.get();

	}

	@Bean
	public IntegrationFlow mainFlow(AmqpTemplate amqpTemplate) {
		return IntegrationFlows.from(httpProcessingChannel())
				.transform(new UppercasePersonService())
				.aggregate(aggregator -> {
					aggregator
					.correlationStrategy(c -> true)
					.expireGroupsUponCompletion(true)
					.releaseStrategy(g -> g.size() == 2);
				})
				.transform(Transformers.toJson())
				.handle(Amqp.outboundAdapter(amqpTemplate).routingKey("OUT"))
				.get();
	}

	@Bean
	public IntegrationFlow fixingFlow() {
		return IntegrationFlows.from(httpFixingChannel())
				.log()
				.transform(payload -> {
					return MessageBuilder.withPayload("{\"name\":\"" + payload + "\"}")
							.setHeader(MessageHeaders.CONTENT_TYPE, "application/json").build();
				})
				.log()
				.channel(httpProcessingChannel())
				.get();
	}

}
