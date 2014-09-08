package org.jboss.sample.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class SimpleJMX {
//	private static String userid = "juser";
//	private static String password = "password"; 
	private static String userid = "admin";
	private static String password = "admin"; 
	private static String hostname = "[::1]";
	private static String port = "44444";
	private static String container ="karaf-root";
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		Map<String, String[]> env = new HashMap<String, String[]>();
		String[] credentials = new String[] { userid, password };
        env.put(JMXConnector.CREDENTIALS, credentials);
        
		try {
//			String jmxAddress = "service:jmx:rmi://[::1]:44444/jndi/rmi://[::1]:1099/karaf-root";
			String jmxAddress = "service:jmx:rmi://10.0.0.153:44444/jndi/rmi://10.0.0.153:1099/karaf-root";
	   	    JMXServiceURL address = new JMXServiceURL(jmxAddress);
//		JMXServiceURL address = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+hostname+":"+port+"/"+container);
	        JMXConnector connector = JMXConnectorFactory.connect(address,env);
	        MBeanServerConnection connection = connector.getMBeanServerConnection();
	        System.out.println("CustomProcessHandler:|:makeConnection():|:GOT THE MBeanServerConnection---SUCCESSFULLY");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
