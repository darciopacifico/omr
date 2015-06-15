package br.com.dlp.omr.ws;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class C {

	public static void main(String[] args) throws NamingException, JMSException {
		
		
		// Step 1. Create an initial context to perform the JNDI lookup.
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces  ");
		Context ctx = new InitialContext(env);

		// Step 2. Lookup the connection factory
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");

		// Step 3. Lookup the JMS queue
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");

		// Step 4. Create the JMS objects to connect to the server and manage a session
		Connection connection = cf.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Step 5. Create a JMS Message Producer to send a message on the queue
		MessageProducer producer = session.createProducer(queue);

		// Step 6. Create a Text Message and send it using the producer
		TextMessage message = session.createTextMessage("Hello, HornetQ!");
		producer.send(message);
		System.out.println("Sent message: " + message.getText());

		// now that the message has been sent, let's receive it

		// Step 7. Create a JMS Message Consumer to receive message from the queue
		MessageConsumer messageConsumer = session.createConsumer(queue);

		// Step 8. Start the Connection so that the server starts to deliver messages
		connection.start();

		// Step 9. Receive the message
		TextMessage messageReceived = (TextMessage)messageConsumer.receive(5000);
		System.out.println("Received message: " + messageReceived.getText());

		// Finally, we clean up all the JMS resources
		connection.close();
		// Step 1. Create an initial context to perform the JNDI lookup.

		
	}
	
}
