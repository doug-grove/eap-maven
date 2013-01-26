package org.jboss.sample.ibmmq;


// ==================================================================
//
// Program Name
//  MQWrite
//
// Last date of modification
//  1 Oct 2000
//
// Description
//  This java class will read a line of input from the keyboard
//  and send it as a message.  The program will loop until the
//  user presses CTL^Z.
//
// Sample Command Line Parameters
//  -h 127.0.0.1 -p 1414 -c CLIENT.CHANNEL -m MQA1 -q TEST.QUEUE
//
// Copyright(C), Roger Lacroix, Capitalware
//
// ------------------------------------------------------------------

import java.io.DataInputStream;
import java.util.Hashtable;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQWrite {
	private MQQueueManager _queueManager = null;
	@SuppressWarnings("rawtypes")
	private Hashtable params = null;
	public int port = 1414;
	public String hostname = "10.0.0.124";
	public String channel = "SYSTEM.DEF.SVRCONN";
	public String qManager = "redhat.queue.manager";
//	public String outputQName = "GSS.BRIDGE.QUEUE";
	public String outputQName = "GSS.QUEUE";

	public MQWrite() {
		super();
	}

	@SuppressWarnings("unused")
	private boolean allParamsPresent() {
		boolean b = params.containsKey("-h") && params.containsKey("-p")
				&& params.containsKey("-c") && params.containsKey("-m")
				&& params.containsKey("-q");

		if (b) {
			try {
				port = Integer.parseInt((String) params.get("-p"));
			} catch (NumberFormatException e) {
				b = false;
			}
			// Set up MQ environment
			hostname = (String) params.get("-h");
			channel = (String) params.get("-c");
			qManager = (String) params.get("-m");
			outputQName = (String) params.get("-q");

		}

		System.out.println("hostname: " + hostname + "  port: " + port + "  channel: " + channel + 
				"  qManager: " + qManager + "  qname: " + outputQName);
		
		return b;
	}

	private void init(String[] args) throws IllegalArgumentException {
//		params = new Hashtable(5);
//		if (args.length > 0 && (args.length % 2) == 0) {
//			for (int i = 0; i < args.length; i += 2) {
//				params.put(args[i], args[i + 1]);
//			}
//		} else {
//			throw new IllegalArgumentException();
//		}
//
//		if (allParamsPresent()) {
			// Set up MQ environment
			MQEnvironment.hostname = hostname;
			MQEnvironment.channel = channel;
			MQEnvironment.port = port;
//		} else {
//			throw new IllegalArgumentException();
//		}
	}

	public static void main(String[] args) {

		MQWrite write = new MQWrite();

		try {
			write.init(args);
			write.selectQMgr();
			write.write();
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: java MQWrite <-h host> <-p port> <-c channel> <-m QueueManagerName> <-q QueueName>");
			System.exit(1);
		} catch (MQException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	private void selectQMgr() throws MQException {
		_queueManager = new MQQueueManager(qManager);
	}

	@SuppressWarnings("deprecation")
	private void write() throws MQException {
		String line;
		int lineNum = 0;
		int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
		try {
			MQQueue queue = _queueManager.accessQueue(outputQName, openOptions,
					null, // default q manager
					null, // no dynamic q name
					null); // no alternate user id

			DataInputStream input = new DataInputStream(System.in);

			System.out.println("MQWrite v1.0 connected");
			System.out.println("and ready for input, terminate with ^C\n\n");

			// Define a simple MQ message, and write some text in UTF format..
			MQMessage sendmsg = new MQMessage();
			sendmsg.format = MQC.MQFMT_STRING;
			sendmsg.feedback = MQC.MQFB_NONE;
			sendmsg.messageType = MQC.MQMT_DATAGRAM;
			sendmsg.replyToQueueName = "ROGER.QUEUE";
			sendmsg.replyToQueueManagerName = qManager;

			MQPutMessageOptions pmo = new MQPutMessageOptions(); // accept the
																	// defaults,
																	// same
			// as MQPMO_DEFAULT constant
			while ((line = input.readLine()) != null) {
				sendmsg.clearMessage();
				sendmsg.messageId = MQC.MQMI_NONE;
				sendmsg.correlationId = MQC.MQCI_NONE;
				sendmsg.writeString(line);

				// put the message on the queue
				queue.put(sendmsg, pmo);
				System.out.println(++lineNum + ": " + line);
			}

			queue.close();
			_queueManager.disconnect();

		} catch (com.ibm.mq.MQException mqex) {
			System.out.println(mqex);
		} catch (java.io.IOException ioex) {
			System.out.println("An MQ IO error occurred : " + ioex);
		}
	}
}