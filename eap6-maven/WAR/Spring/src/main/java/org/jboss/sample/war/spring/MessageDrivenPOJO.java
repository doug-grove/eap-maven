/**
 * MessageDrivenPOJO.java
 * 
 * Created on Oct 10, 2011, 2:58:06 PM
 *
 */
package org.jboss.sample.war.spring;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author grovedc
 *
 */
public class MessageDrivenPOJO implements MessageListener {
	/** Logger for the class. */
	private static final Logger logger = Logger.getLogger(MessageDrivenPOJO.class);
	
	private JmsTemplate jmsTemplate;
    private Queue queue;
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		logger.info("Entering onMessage");
		
        if (message instanceof TextMessage) {
        	try {
        		final String text = ((TextMessage)message).getText();
        		// print the message
        		logger.info(text);
				
	            // re-send the message
	            this.jmsTemplate.send(this.queue, new MessageCreator() {
	                public Message createMessage(Session session) throws JMSException {
	                  return session.createTextMessage(text);
	                }
	            });				
			} catch (JMSException e) {
				logger.error(e);
			}
        }
   	}
	
	/**
	 * Injected from the Spring Context
	 * 
	 * @param queue
	 */
	public void setQueue(Queue queue) {
		this.queue = queue;
	}
	
    /**
     * Injected from the Spring Context
     * 
     * @param cf
     */
    public void setConnectionFactory(ConnectionFactory cf) {
        this.jmsTemplate = new JmsTemplate(cf);
    }
}