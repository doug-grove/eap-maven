/**
 * WebSphereMQ.jva
 * 
 * Created on Sep 21, 2012, 9:11:29 AM
 *
 * To the extent possible under law, Red Hat, Inc. has dedicated all copyright to this 
 * software to the public domain worldwide, pursuant to the CC0 Public Domain Dedication. This 
 * software is distributed without any warranty.  
 *
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 *
 */
package org.jboss.sample.mq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.jboss.logging.Logger;

/**
 * @author grovedc
 * 
 */
@MessageDriven(name = "WebSphereMQ", activationConfig = {
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue="30"),
        @ActivationConfigProperty(propertyName = "maxPoolDepth", propertyValue="30"),
        @ActivationConfigProperty(propertyName = "maxMessages", propertyValue="5"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "false"),
		@ActivationConfigProperty(propertyName = "hostName", propertyValue = "10.0.0.150"),
		@ActivationConfigProperty(propertyName = "port", propertyValue = "1414"),
		@ActivationConfigProperty(propertyName = "channel", propertyValue = "SYSTEM.DEF.SVRCONN"),
		@ActivationConfigProperty(propertyName = "queueManager", propertyValue = "REDHAT.QUEUE.MANAGER"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "GSS.QUEUE"),
		@ActivationConfigProperty(propertyName = "transportType", propertyValue = "CLIENT") })
@ResourceAdapter(value="wmq.jmsra.rar")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class WebSphereMQ implements MessageListener {
	private static final Logger logger = Logger.getLogger(WebSphereMQ.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		try {
			logger.debug("Received message: " + message.getJMSMessageID() + " : " + ((TextMessage)message).getText());
			
			// **** test some lookups....
//			lookup();
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
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
    private void lookup() {
		logger.info("Entering lookup");
		
	    try {
			InitialContext iniCtx = new InitialContext();
			
			logger.info("Getting/starting queue manager");
			ConnectionFactory cf = (ConnectionFactory)iniCtx.lookup("java:jboss/mqConnectionFactory");
			Connection conn = cf.createConnection();
			conn.start();
			
			logger.info("Getting queue");
			Queue queue = (Queue)iniCtx.lookup("java:jboss/mqGSS");

			logger.info("Creating session");
			Session session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);

			logger.info("Creating message producer");
			MessageProducer producer = session.createProducer(queue);
			
			session.close();
			conn.close();
		} catch (NamingException e) {
			logger.error(e);
		} catch (JMSException e) {
			logger.error(e);
		}		

		logger.info("Exiting lookup");
	}
}