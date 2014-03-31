package org.jboss.sample.mq;

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

import org.jboss.logging.Logger;

/**
 * Message-Driven Bean implementation class for: ExternalConfXML
 * 
 */
@MessageDriven(name = "ExternalConfXML", 
    activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })
public class ExternalConfXML implements MessageListener {
    private static final Logger logger = Logger.getLogger(ExternalConfXML.class);

    /**
     * Default constructor.
     */
    public ExternalConfXML() {
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
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        try {
            logger.info("Received message: " + message.getJMSMessageID() + " : " + ((TextMessage)message).getText());
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }
    }
}
