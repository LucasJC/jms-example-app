package lucasjc.jmsapp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import lucasjc.jmsapp.messaging.JsonMessageListener;
import lucasjc.jmsapp.messaging.JsonMessageSender;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JmsExampleAppTests {

	private static final Logger LOG = LoggerFactory.getLogger(JmsExampleAppTests.class);

	private final JsonMessageSender sender;

	private final JsonMessageListener listener;

	@Autowired
	public JmsExampleAppTests(JsonMessageSender sender, JsonMessageListener listener) {
		this.sender = sender;
		this.listener = listener;
	}

	@DirtiesContext
	@Test
	void testIntegration() throws JsonProcessingException {
		LOG.info("10 test messages will be sent to {}", sender.getQueueName());
		final int messagesToSend = 10;
		for (int i = 0; i < messagesToSend; i++) {
			String id = UUID.randomUUID().toString();
			LOG.info("Sending test message with ID {}", id);
			sender.sendMessage(id, testMessage());
		}
		assertTrue(listener.getProcessingCount() >= messagesToSend);
	}


	private Map<String, Object> testMessage() {
		Map<String, Object> map = new HashMap<>();
		map.put("a", "a");
		map.put("b", true);
		map.put("c", 1.0);
		map.put("d", 500);
		return map;
	}
}
