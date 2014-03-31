/**
 * ActiveMQ.java
 * 
 * Created on Mar 10, 2013, 10:38:03 AM
 *
 * To the extent possible under law, Red Hat, Inc. has dedicated all copyright to this 
 * software to the public domain worldwide, pursuant to the CC0 Public Domain Dedication. This 
 * software is distributed without any warranty.  
 *
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
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

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.jboss.logging.Logger;

/**
 * @author grovedc
 * 
 * see:
 * 
 * http://activemq.apache.org/activation-spec-properties.html
 * 
 */
@MessageDriven(name="ActiveMQ", activationConfig = {
   @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
   @ActivationConfigProperty(propertyName = "destination", propertyValue = " java:/activemq/queue_in") })

@ResourceAdapter("activemq-rar-5.8.0.redhat-60024.rar")
public class ActiveMQ implements MessageListener {
    private static final Logger logger = Logger.getLogger(ActiveMQ.class);
    
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
