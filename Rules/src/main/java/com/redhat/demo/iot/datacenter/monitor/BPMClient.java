package com.redhat.demo.iot.datacenter.monitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.remote.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.api.task.TaskService;
import org.kie.api.runtime.process.ProcessInstance;

public class BPMClient {

	RuntimeEngine runtimeEngine;

	public BPMClient() {

	}

	public void doCall(String applicationContext, String deploymentId, String userId, String password, DataSet myData) {
		runtimeEngine = getRuntimeEngine( applicationContext, deploymentId, userId, password );

		KieSession kieSession = runtimeEngine.getKieSession();

		TaskService taskService = runtimeEngine.getTaskService();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("process_variable", myData );

		ProcessInstance processInstance = kieSession.startProcess( "IoT_Human_Task.WorkProblem", params );

		long procId = processInstance.getId();
	}

	private static RuntimeEngine getRuntimeEngine(String applicationContext, String deploymentId, String userId, String password)
	{
		try {
			URL jbpmURL = new URL( applicationContext );
			RemoteRestRuntimeEngineFactory remoteRestSessionFactory = RemoteRestRuntimeEngineFactory.newBuilder()
					.addDeploymentId(deploymentId)
					.addUrl(jbpmURL)
					.addUserName(userId)
					.addPassword(password)
					.buildFactory();

			RuntimeEngine runtimeEngine = remoteRestSessionFactory.newRuntimeEngine();
			return runtimeEngine;
		}
		catch( MalformedURLException e )
		{
			throw new IllegalStateException( "This URL is always expected to be valid!", e );
		}
	}

}
