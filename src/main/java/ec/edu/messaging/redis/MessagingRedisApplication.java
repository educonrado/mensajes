package ec.edu.messaging.redis;

import ec.edu.messaging.redis.model.Receiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import java.util.logging.Logger;

@SpringBootApplication
public class MessagingRedisApplication {

	public static final Logger LOGGER = Logger.getLogger("MessagingRedisApplication");

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
		return container;
	}
	@Bean
	public MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	public Receiver receiver() {
		return new Receiver();
	}

	@Bean
	@Primary
	public RedisTemplate<Long, Receiver> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Long, Receiver> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(MessagingRedisApplication.class, args);
		//Connection connection = ctx.getBean(Connection.class);
		RedisTemplate<Long, Receiver> template = ctx.getBean(RedisTemplate.class);
		Receiver receiver = ctx.getBean(Receiver.class);
		while (receiver.getCount() == 0) {
			LOGGER.info("Enviando mensaje...");
			template.convertAndSend("chat", "Hello from Redis!");
			Thread.sleep(500L);
		}
		System.exit(0);
	}

}
