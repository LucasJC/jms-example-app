package lucasjc.jmsapp.config;

import lucasjc.jmsapp.messaging.CustomErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.jndi.JndiTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.naming.NamingException;

@Configuration
@EnableJms
public class JmsConfig {

	@Bean
	public ConnectionFactory connectionFactory(
			@Value("${jms.connection-factory.jndi}") String connectionFactoryName,
			JndiLocatorDelegate jndiLocator
	) throws NamingException {
		return jndiLocator.lookup(connectionFactoryName, QueueConnectionFactory.class);
	}

	@Bean
	public DestinationResolver destinationResolver(JndiTemplate jndiTemplate) {
		JndiDestinationResolver resolver = new JndiDestinationResolver();
		resolver.setJndiTemplate(jndiTemplate);
		return resolver;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
			ConnectionFactory connectionFactory,
			DestinationResolver destinationResolver,
			@Value("${jms.listener.concurrency:1}") String concurrency,
			@Value("${jms.listener.recovery-interval:5000}") long recoveryInterval
	) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setDestinationResolver(destinationResolver);
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(JmsProperties.AcknowledgeMode.AUTO.getMode());
		factory.setRecoveryInterval(recoveryInterval);
		factory.setConcurrency(concurrency);
		factory.setErrorHandler(new CustomErrorHandler());
		return factory;
	}
}
