package lucasjc.jmsapp.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Basic JMS message sender that marshals payload to a json string
 */
@Service
public class JsonMessageSender {
	private final JmsTemplate jmsTemplate;
	private final String queueName;

	private final ObjectMapper om;

	public JsonMessageSender(JmsTemplate jmsTemplate, @Value("${jms.queue.jndi}") String queueName, ObjectMapper om) {
		this.jmsTemplate = jmsTemplate;
		this.queueName = queueName;
		this.om = om;
	}

	public void sendMessage(String id, Object content) throws JsonProcessingException {
		// message generation logic goes here...
		// this example generates a json string from the content, which will result in a TextMessage
		String json = om.writeValueAsString(content);
		jmsTemplate.convertAndSend(queueName, json, msg -> {
			// custom properties can be set, this will be received as headers
			msg.setStringProperty("customId", id);
			return msg;
		});
	}

	public String getQueueName() {
		return queueName;
	}
}
