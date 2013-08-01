package org.jboss.sample.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.jboss.logging.Logger;

/**
 * @author grovedc
 * 
 */
@MessageDriven(name="MessageConsumerAMQ", activationConfig = {
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/amq1") })

@ResourceAdapter("activemq-rar-5.8.0.redhat-60024-eap6")
public class MessageConsumerAMQ implements MessageListener {
	private static final Logger logger = Logger.getLogger(MessageConsumerAMQ.class);

//	@Resource(mappedName = "java:/ConnectionFactoryAMQ")
//	private QueueConnectionFactory queueConnectionFactory;
//
//	@Resource(mappedName = "java:/queue/amqA")
//	private Queue queue;
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage)
				logger.info(((TextMessage)message).getText());
			
//			if (queueConnectionFactory != null)
//				logger.debug("CF injected");
//			
//			if (queue != null)
//				logger.debug("queue injected");
			
		} catch (JMSException e) {
			logger.error(e.getMessage());
		}
	}
	
	@PostConstruct  
    public void ejbCreate() throws CreateException {  
		logger.info("Entering/Exiting ejbCreate");
	}  
	
	@PreDestroy
	public void ejbRemove() throws EJBException {  
		logger.info("Entering/Exiting ejbRemove");
	}  
}