/**
 * WebSphereMDB.java
 * 
 * Created on Jun 10, 2011, 10:38:03 AM
 *
 * To the extent possible under law, Red Hat, Inc. has dedicated all copyright to this 
 * software to the public domain worldwide, pursuant to the CC0 Public Domain Dedication. This 
 * software is distributed without any warranty.  
 *
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.jboss.sample.mq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

import org.jboss.logging.Logger;


/**
 * @author dgrove
 *
 */
@MessageDriven(name="WebSphereMDB", activationConfig = 
{ 
	@ActivationConfigProperty(propertyName = "messagingType", propertyValue="javax.jms.MessageListener"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "GSS.SSL.QUEUE"),
	@ActivationConfigProperty(propertyName = "maxSession",propertyValue="30")
}) 
@ResourceAdapter(value = "wmq.jmsra.rar")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class WebSphereMDB implements MessageListener {
	private static final Logger logger = Logger.getLogger(WebSphereMDB.class);
	
	@Resource
	MessageDrivenContext sessionContext;

	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		try {
			logger.debug("Received message: " + message.getJMSMessageID() + " : " + ((TextMessage)message).getText());
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