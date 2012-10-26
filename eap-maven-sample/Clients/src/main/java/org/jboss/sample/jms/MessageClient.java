/**
 * 
 */
package org.jboss.sample.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * This sample is for EAP 4 and 5.  Note the use of the "org.jnp" package.  This
 * does not exist on EAP 6.
 * 
 * @author grovedc
 *
 */
public class MessageClient {
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,	"org.jnp.interfaces.NamingContextFactory");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		props.put(Context.PROVIDER_URL, "10.0.0.150:1100,10.0.0.151:1100");

		Connection connection = null;
		InitialContext initialContext = null;
		try {
			// Step 1. Create an initial context to perform the JNDI lookup.
			initialContext = new InitialContext(props);

			// Step 2. Perfom a lookup on the queue
			Queue queue = (Queue) initialContext.lookup("/queue/inQueue");

			// Step 3. Perform a lookup on the Connection Factory
			ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");

			// Step 4.Create a JMS Connection
			connection = cf.createConnection();

			// Step 5. Create a JMS Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Step 6. Create a JMS Message Producer
			MessageProducer producer = session.createProducer(queue);

			// Step 7. Create a Text Message
			TextMessage message = session.createTextMessage("This is a text message");

			System.out.println("Sent message: " + message.getText());

			// Step 8. Send the Message
			producer.send(message);

			// Step 15. We lookup the reply queue
			queue = (Queue) initialContext.lookup("/queue/outQueue");

			// Step 16. We create a JMS message consumer
			MessageConsumer messageConsumer = session.createConsumer(queue);

			// Step 17. We start the connedction so we can receive messages
			connection.start();

			// Step 18. We receive the message and print it out
			message = (TextMessage) messageConsumer.receive(5000);

			System.out.println("message.getText() = " + message.getText());

			System.out.println("Kill Live Server and press enter");

			System.in.read();

			// Step 7. Create a Text Message
			message = session.createTextMessage("This is another text message");

			System.out.println("Sent message: " + message.getText());

			// Step 8. Send the Message
			producer.send(message);

			// Step 16. We create a JMS message consumer
			messageConsumer = session.createConsumer(queue);

			// Step 18. We receive the message and print it out
			message = (TextMessage) messageConsumer.receive(5000);

			System.out.println("message.getText() = " + message.getText());

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
