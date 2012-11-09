/**=============================================================================
 * Filename: JmsExternalClient.java
 *==============================================================================
 */

package org.jboss.sample.case_00718847;


/**=============================================================================================================
 * CODE REVIEWS:
 * -------------------------------------------------------------------------------------------------------------
 * Review Date:		9/24/2012
 * Review Type:		Project
 * Developed By:	Mike Miles
 * Reviewed By:		Ken Morgan
 * Status:			New
 * 
 * Recommendations:
 * 	1.) Some comments, preferably JavaDocs, would be helpful on the class and methods.
 * 
 * 		Status/Response: 	 
 *		
 *
 * Action Items:
 * 	1.) [ Action Item 1... ]
 *		
 * 		Status/Response: 	
 *		
 * 	2.) [ Action Item 2... ]
 *		
 * 		Status/Response: 	
 * 
 * Comments:	
 *
 * 
 *==============================================================================================================
 */


import java.io.IOException;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**=============================================================================
 * :: JmsExternalClient
 *
 * Client class for sending JMS messages to a queue. This class is for use in 
 * applications deployed outside the context of the JBoss server. 
 * 
 * For use inside the context of the JBoss server, please see the 
 * JmsClient.
 *
 *------------------------------------------------------------------------------
 * @author iamilemd      @Created Jul 20, 2012 :: 12:20:19 PM
 * 
 *------------------------------------------------------------------------------
 * Changes:
 *
 * Developer        Date                Comments
 * -------------    -------------       ----------------------------------------
 * 
 *
 *==============================================================================
 */
public class JmsExternalClient {
	private static final Logger log = LoggerFactory.getLogger(JmsExternalClient.class);
	private static final String REMOTE_CONNECTION_FACTORY_NAME = "jms/RemoteConnectionFactory";
	
	public static boolean sendMessageFromFile(String hostName, Integer port, String queueName, String fileLocation, String userName, String password)
	  throws IOException, JMSException, NamingException
	{
		// read the file into a string
//		String message = FileUtil.readFile(fileLocation);

	    // DCG
		String message = "test string";
		
		// Call the other message sender
		return sendMessage(hostName, port, queueName, message, userName, password);
	}
	
	public static boolean sendMessage(String hostName, Integer port, String queueName, String message, String userName, String password)
	  throws IOException, JMSException, NamingException {
		
		Session session = null;
		Connection conn = null;
		MessageProducer producer = null;
		
		Context context = null;
		try	{
			ConnectionFactory connFactory = null;
			Queue queue = null;
			
			Properties props = new Properties();					                                            
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
			props.put(Context.PROVIDER_URL, "remote://" + hostName + ":" + port);
			context = new InitialContext(props);
			connFactory = (ConnectionFactory) context.lookup(REMOTE_CONNECTION_FACTORY_NAME);
			
			queue = (Queue)context.lookup(queueName);
			
			
			conn = connFactory.createConnection(userName, password);
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			producer = session.createProducer(queue);
			Message messageToSend = null;
			messageToSend = session.createTextMessage((String)message);

			producer.send(messageToSend);

//		} catch (NamingException nex) {
//			// TODO: Revisit this exception handler when JBoss fixes the javax.naming.NamingException being returned from the Remoting process.
//			// There is a Remoting Naming Exception being returned regardless that the connection is being made to the remote queues 
//			// and the JMS message is being delivered successfully.
//			//
//			// Do nothing for now but log the error.
//			log.warn("JBoss Remoting is returning a NamingException, but process is working. Will need to revisit with later version of JBoss.");
//			
//			// If the remoting problem gets resolved in JBoss, uncomment the next line;
//			// return false;
		} catch (Exception ex) {
			log.error("Error sending the JMS Message to queue: " + queueName + " on Host: " + hostName, ex);
			return false;
		}
		finally	{
			try	{
				if (conn != null) conn.close();
			} catch (Exception e2) {}
			
			try {
			    if (context != null) 
			        context.close();
			} catch (Exception e2) {}
		}

		return true;
	}
	
	// *****
	public static void main(String[] args) throws Exception {
		try {
			for (int i=0; i<10000; i++)
				sendMessage("127.0.0.1", 4447, "/jms/testQueue", "hello world", "", "");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}