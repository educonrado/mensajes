package ec.edu.messaging.redis.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Receiver {
	private static final Logger LOGGER = Logger.getLogger("Receiver");
	private static final String TEMPLATE_LOG = "Received <%s>";

	private AtomicInteger counter = new AtomicInteger();

	public void receiveMessage(String message) {
		LOGGER.info(String.format(TEMPLATE_LOG, message));
		counter.incrementAndGet();
	}

	public int getCount() {
		return counter.get();
	}
}
