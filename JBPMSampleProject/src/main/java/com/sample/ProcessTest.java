package com.sample;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
//import org.jbpm.process.workitem.wsht.WSHumanTaskHandler;
import org.jbpm.test.JBPMHelper;
//import org.apache.lucene.store.DataOutput;
//import org.drools.core.marshalling.impl.ProtobufMessages.KnowledgeBase;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.cdi.KBase;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
//import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
//import org.kie.internal.builder.KnowledgeBuilder;
//import org.kie.internal.builder.KnowledgeBuilderFactory;
//import org.kie.internal.io.ResourceFactory;
//import org.kie.internal.logger.KnowledgeRuntimeLogger;
//import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
//import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.api.task.TaskService;

public class ProcessTest extends JbpmJUnitBaseTestCase {

	
	@Test
	public void testEvaluationProcess() {
	
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieBase kbase = kContainer.getKieBase("kbase");
		RuntimeManager manager = createRuntimeManager(kbase);
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
	    KieRuntimeLogger log = KieServices.Factory.get().getLoggers().newThreadedFileLogger(ksession, "test", 1000);
	    System.out.println("Process started ...");
	    // start a new process instance
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("income", 500);
	    ProcessInstance processInstance = ksession.startProcess("happiest.ruleflow", params);
	    assertProcessInstanceCompleted(processInstance.getId(), ksession);
		System.out.println("User Process Ended");
	    manager.disposeRuntimeEngine(engine);
	    System.exit(0);
}

private static RuntimeManager createRuntimeManager(KieBase kbase) {
	JBPMHelper.startH2Server();
	JBPMHelper.setupDataSource();
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
	RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
		.newDefaultBuilder().entityManagerFactory(emf)
		.knowledgeBase(kbase);
	return RuntimeManagerFactory.Factory.get()
		.newSingletonRuntimeManager(builder.get(), "com.sample:example:1.0");
}

}
