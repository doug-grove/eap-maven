/**
 * ConfFromAdminObjects.jva
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
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

// **** Activation Configuration 
@MessageDriven(name = "ConfFromAdminObjects", activationConfig = {
// **** standard JMS
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/mqTest")

// **** IBM specific        
//        @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "true"),
//        @ActivationConfigProperty(propertyName = "queueManager", propertyValue = "REDHAT.QUEUE.MANAGER"),
//        @ActivationConfigProperty(propertyName = "hostName", propertyValue = "10.0.0.150"),
//        @ActivationConfigProperty(propertyName = "port", propertyValue = "1414"),
//        @ActivationConfigProperty(propertyName = "channel", propertyValue = "SYSTEM.DEF.SVRCONN"),
//        @ActivationConfigProperty(propertyName = "transportType", propertyValue = "CLIENT") 
        })

// **** RAR specified in jboss-ejb3.xml

// **** TX Attributes
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ConfFromAdminObjects implements MessageListener {
    private static final Logger logger = Logger.getLogger(ConfFromAdminObjects.class);
    
    @PostConstruct  
    public void ejbCreate() throws CreateException {  
        logger.info("Entering/Exiting ejbCreate");
    }  
    
    @PreDestroy
    public void ejbRemove() throws EJBException {  
        logger.info("Entering/Exiting ejbRemove");
    } 
    
    @Override
    public void onMessage(Message msg) {
        try {
            logger.debug("Received message: " + msg.getJMSMessageID() + " : " + ((TextMessage)msg).getText());
            
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }

    }
}
