package lucasjc.jmsapp.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JMS message listener that processes received messages
 */
@Component
public class JsonMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(JsonMessageListener.class);
	private final ObjectMapper om;

	private AtomicInteger processingCount = new AtomicInteger();

	public JsonMessageListener(ObjectMapper om) {
		this.om = om;
	}

	@JmsListener(destination = "${jms.queue.jndi}")
	public void onMessage(Message<?> message) throws JsonProcessingException {
		MessageHeaders headers = message.getHeaders();
		Object payload = message.getPayload();
		String messageId = (String) headers.get("customId");
		if (payload instanceof String) {
			LOG.info("Message [{}] read: {}", messageId, payload);
			Object content = om.readValue((String) payload, Object.class);
			// do something with message content...
		} else {
			LOG.warn("Message [{}] has an unexpected type: {}", messageId, payload.getClass().getName());
		}
		this.processingCount.incrementAndGet();
	}

	/**
	 * @return how many messages were processed by this listener
	 */
	public int getProcessingCount() {
		return processingCount.get();
	}
}
