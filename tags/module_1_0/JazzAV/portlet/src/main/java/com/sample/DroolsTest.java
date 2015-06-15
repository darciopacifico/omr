package com.sample;



/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	/*
	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = DroolsTest.readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger log = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			Message message = new Message();
			//message.setMessage("Hello World");
			message.setMessage("Darcio");
			message.setStatus(Message.HELLO);
			
			
			ksession.insert(message);
			
			ksession.fireAllRules();
			
			
			WSHumanTaskHandler wsHumanTaskHandler = new WSHumanTaskHandler();
			ksession.getWorkItemManager().registerWorkItemHandler("Human Task", wsHumanTaskHandler);
			
			
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("nome", "Nome Variável, substituiu a default!");
			values.put("idade", new Integer(17));
			ProcessInstance pInstance = ksession.startProcess("ExibirNome",values);
			
			
			pInstance.signalEvent("Aceitar Termo de Responsabilidade", Boolean.TRUE);
			
			System.out.println(pInstance.getState());
			System.out.println("complete="+ProcessInstance.STATE_COMPLETED);
			System.out.println("pend="+ProcessInstance.STATE_PENDING);
			System.out.println("active="+ProcessInstance.STATE_ACTIVE);
			
			
			log.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	 *//*
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("Renderer.rf"), ResourceType.DRF);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error: errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
	  */
	
	
	
	public static class Message {
		
		public static final int HELLO = 0;
		public static final int GOODBYE = 1;
		
		private String message;
		
		private int status;
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public int getStatus() {
			return status;
		}
		
		public void setStatus(int status) {
			this.status = status;
		}
		
		public void generateFile(String mensagem){
			System.out.println("gerandoArquivo"+mensagem);
		}
		
		
	}
	
}