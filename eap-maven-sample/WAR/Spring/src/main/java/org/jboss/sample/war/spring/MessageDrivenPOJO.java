/**
 * MessageDrivenPOJO.java
 * 
 * Created on Oct 10, 2011, 2:58:06 PM
 *
 */
package org.jboss.sample.war.spring;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

/**
 * @author grovedc
 *
 */
public class MessageDrivenPOJO implements MessageListener {
	/** Logger for the class. */
	private static final Logger logger = Logger.getLogger(MessageDrivenPOJO.class);
	
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		logger.info("Entering onMessage");
		
        if (message instanceof TextMessage) {
        	Connection conn = null;
        	try {
                logger.debug(((TextMessage)message).getText());
                
//              InitialContext iniCtx = new InitialContext();
//        	    Object tmp = iniCtx.lookup("ConnectionFactory");
//        		QueueConnectionFactory tcf = (QueueConnectionFactory)tmp;
//        		conn = tcf.createQueueConnection();
//        		conn.start();
//
//        		Object ref = iniCtx.lookup("queue/wmqTarget");
//        		Queue topic = (Queue)ref;
//        		
//        		QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
//        		MessageProducer producer = session.createProducer(topic);
//        		
//        		TextMessage mess = session.createTextMessage();
//        		mess.setText("Hello World - 1");
//        		producer.send(mess);
//        		
//        		logger.debug("sent queue message");
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
//            } catch (NamingException e) {
//                throw new RuntimeException(e);
			} finally {
				try {
					if (null != conn) conn.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
        }
	}
}