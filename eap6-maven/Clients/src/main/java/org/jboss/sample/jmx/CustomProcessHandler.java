package org.jboss.sample.jmx;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;


public class CustomProcessHandler implements WorkItemHandler{

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
	  
		System.out.println("CustomProcessHandler:|:executeWorkItem():|:start ...");
		
		HashMap <String, String> inputDataMap  = new HashMap <String, String>();
		inputDataMap.put("hostName", (String)workItem.getParameter("hostName"));
		inputDataMap.put("portNumber", (String)workItem.getParameter("portNumber"));
		inputDataMap.put("containerName", (String)workItem.getParameter("containerName"));
		inputDataMap.put("managementNamePattern", (String)workItem.getParameter("managementNamePattern"));
		inputDataMap.put("camelContextId", (String)workItem.getParameter("camelContextId"));
		inputDataMap.put("userId", (String)workItem.getParameter("userId"));
		inputDataMap.put("password", (String)workItem.getParameter("password"));

	    MBeanServerConnection connection = null;

        try
        {
        	connection = makeConnection(inputDataMap);
        	// "org.apache.camel:context=Visumpoint-PC/jbpm-camel,type=context,name=\"hello-world\"";
	        String camelObjectName="org.apache.camel:context=" + inputDataMap.get("hostName") + "/" + inputDataMap.get("managementNamePattern") + ",type=context,name=\"" + inputDataMap.get("camelContextId") + "\"";
	        startCamelContext(connection, camelObjectName) ;
	        System.out.println("CustomProcessHandler:|:executeWorkItem():|:CamelContext Started Successfully....");
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }

		manager.completeWorkItem(workItem.getId(), null);

		System.out.println("CustomProcessHandler:|:executeWorkItem():|:end ...");
	}

    private MBeanServerConnection makeConnection(HashMap<String, String> inputDataMap) throws IOException
    {
        System.out.println("CustomProcessHandler:|:makeConnection():|:inputDataMap = " + inputDataMap);
        Map<String, String[]> env = new HashMap<String, String[]>();
        String[] credentials = new String[] { inputDataMap.get("userId"), inputDataMap.get("password") };
        env.put(JMXConnector.CREDENTIALS, credentials);
        JMXServiceURL address = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+inputDataMap.get("hostName")+":"+inputDataMap.get("portNumber")+"/"+inputDataMap.get("containerName"));
        JMXConnector connector = JMXConnectorFactory.connect(address,env);
        MBeanServerConnection connection = connector.getMBeanServerConnection();
        System.out.println("CustomProcessHandler:|:makeConnection():|:GOT THE MBeanServerConnection---SUCCESSFULLY");
        return connection;
    }

    private void startCamelContext(MBeanServerConnection connection, String camelObjectName) throws Exception
    {
        ObjectName serviceRef=new ObjectName(camelObjectName);
        Object[] params = null;
        String[] signature = null;
        Object result = connection.invoke(serviceRef, "start" , params, signature);
        System.out.println("CustomProcessHandler:|:startCamelContext():|:result = " + result);
    }

}