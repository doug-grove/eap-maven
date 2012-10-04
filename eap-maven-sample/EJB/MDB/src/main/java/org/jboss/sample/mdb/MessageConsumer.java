/**
 * MessageConsumer.java
 * 
 * Created on Mar 19, 2011, 3:04:11 PM
 *
 * Copyright (C) 2011 Red Hat Inc. - GSS
 * All rights reserved.
 */
package org.jboss.sample.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author grovedc
 * 
 */
@MessageDriven(name="ConsumerMDB", activationConfig = {
//	@ActivationConfigProperty(propertyName = "providerAdapterJNDI", propertyValue="java:/RemoteJMSProvider"),
//  @ActivationConfigProperty(propertyName="reconnectAttempts", propertyValue="60"),
//  @ActivationConfigProperty(propertyName="reconnectInterval", propertyValue="10"),

	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"),
	@ActivationConfigProperty(propertyName = "Destination", propertyValue = "/queue/wmqQueueSsl") })
public class MessageConsumer implements MessageListener {
	/** Logger for the class. */
	private static final Log logger = LogFactory.getLog(MessageConsumer.class);
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage)
				logger.info(((TextMessage)message).getText());
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