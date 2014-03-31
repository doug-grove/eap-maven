/**
 * 
 */
package org.jboss.sample.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author grovedc
 *
 */
public class AS7Client {
	private static final Logger logger = LoggerFactory.getLogger(AS7Client.class);

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
//		props.put(Context.SECURITY_PRINCIPAL, DEFAULT_USERNAME);
//      props.put(Context.SECURITY_CREDENTIALS, DEFAULT_PASSWORD);

		Connection connection = null;
		InitialContext initialContext = null;
		try {
			// Step 1. Create an initial context to perform the JNDI lookup.
			initialContext = new InitialContext(props);

			// Step 2. Perform a lookup on the queue
			Queue queue = (Queue)initialContext.lookup("/queue/testDistributedQueue");

			// Step 3. Perform a lookup on the Connection Factory
			ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("jms/RemoteConnectionFactory");

			// Step 4.Create a JMS Connection
			connection = cf.createConnection();

			// Step 5. Create a JMS Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Step 6. Create a JMS Message Producer
			MessageProducer producer = session.createProducer(queue);

			// Step 7. Create a Text Message
			TextMessage message = session.createTextMessage("This is a text message");

			logger.info("Sent message: " + message.getText());

			// Step 8. Send the Message
			Long start = System.currentTimeMillis();
			for (int i=0; i<4; i++)
				producer.send(message);
			
			logger.info("elapsed: " + (System.currentTimeMillis() - start));
		} finally {
			// Step 19. Be sure to close our JMS resources!
			if (initialContext != null) {
				initialContext.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
