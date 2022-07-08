package lucasjc.jmsapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.jndi.JndiTemplate;

import javax.naming.Context;
import java.util.Properties;

@Configuration
public class JndiConfig {

	private final Properties jndiContext;

	public JndiConfig(@Value("${jndi.url}") String url,
					  @Value("${jndi.username}") String username,
					  @Value("${jndi.password}") String password,
					  @Value("${jndi.connection-factory.initial-context}") String initialContextFactory) {
		this.jndiContext = new Properties();
		jndiContext.setProperty(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		jndiContext.setProperty(Context.PROVIDER_URL, url);
		if (username != null && !username.isEmpty()) {
			jndiContext.setProperty(Context.SECURITY_PRINCIPAL, username);
		}
		if (password != null && !password.isEmpty()) {
			jndiContext.setProperty(Context.SECURITY_CREDENTIALS, password);
		}
	}

	@Bean
	public JndiTemplate jndiTemplate() {
		JndiTemplate jndiTemplate = new JndiTemplate();
		jndiTemplate.setEnvironment(jndiContext);
		return jndiTemplate;
	}

	@Bean
	public JndiLocatorDelegate jndiLocator() {
		JndiLocatorDelegate delegate = new JndiLocatorDelegate();
		delegate.setJndiTemplate(jndiTemplate());
		return delegate;
	}
}
