package lucasjc.jmsapp.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CustomErrorHandler.class);

	@Override
	public void handleError(Throwable t) {
		LOG.error("Unhandled error processing a message", t);
	}
}
