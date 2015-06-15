package br.com.dlp.framework.vo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.sun.mail.pop3.POP3Message;

public class MailClient extends Authenticator {
	public static final int SHOW_MESSAGES = 1;
	public static final int CLEAR_MESSAGES = 2;
	public static final int SHOW_AND_CLEAR = MailClient.SHOW_MESSAGES + MailClient.CLEAR_MESSAGES;
	
	protected String from;
	protected Session session;
	protected PasswordAuthentication authentication;
	
	public MailClient(String user, String pass, String host) {
		this(user, pass, host, false);
	}
	
	public MailClient(String user, String pass, String host, boolean debug) {
		from = user + '@' + host;
		authentication = new PasswordAuthentication(user, user);
		Properties props = new Properties();
		props.put("mail.user", user);
		props.put("mail.password", pass);
		props.put("mail.host", host);
		props.put("mail.debug", debug ? "true" : "false");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		session = Session.getInstance(props, this);
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return authentication;
	}
	
	public void sendMessage(String to, String subject, String content) throws MessagingException {
		System.out.println("SENDING message from " + from + " to " + to);
		System.out.println();
		MimeMessage msg = new MimeMessage(session);
		msg.addRecipients(Message.RecipientType.TO, to);
		msg.setSubject(subject);
		msg.setText(content);
		Transport.send(msg);
	}
	
	public void checkInbox(int mode) throws MessagingException, IOException {
		if (mode == 0) {
			return;
		}
		boolean show = (mode & MailClient.SHOW_MESSAGES) > 0;
		boolean clear = (mode & MailClient.CLEAR_MESSAGES) > 0;
		String action = (show ? "Show" : "") + (show && clear ? " and " : "") + (clear ? "Clear" : "");
		System.out.println(action + " INBOX for " + from);
		Store store = session.getStore();
		store.connect();
		Folder root = store.getDefaultFolder();
		Folder inbox = root.getFolder("inbox");
		inbox.open(Folder.READ_WRITE);
		Message[] msgs = inbox.getMessages();
		if (msgs.length == 0 && show) {
			System.out.println("No messages in inbox");
		}
		for (Message msg2 : msgs) {
			MimeMessage msg = (MimeMessage) msg2;
			if (show) {
				System.out.println("    From: " + msg.getFrom()[0]);
				System.out.println(" Subject: " + msg.getSubject());
				System.out.println(" Content: " + msg.getContent());
			}
			if (clear) {
				msg.setFlag(Flags.Flag.DELETED, true);
			}
		}
		inbox.close(true);
		store.close();
		System.out.println();
	}
	
	public void checkInboxSave(int mode) throws MessagingException, IOException {
		
		Store store = session.getStore();
		store.connect();
		Folder root = store.getDefaultFolder();
		Folder inbox = root.getFolder("inbox");
		inbox.open(Folder.READ_WRITE);
		Message[] msgs = inbox.getMessages();
		
		for (Message msg2 : msgs) {
			
			POP3Message msg = (POP3Message) msg2;
			
			Object object = msg.getContent();
			if (object instanceof Multipart) {
				Multipart multipart = (Multipart) object;
				
				for (int i = 0, n = multipart.getCount(); i < n; i++) {
					MailClient.handlePart(multipart.getBodyPart(i));
				}
				
			}
			
			System.out.println("    From: " + msg.getFrom()[0]);
			System.out.println(" Subject: " + msg.getSubject());
			System.out.println(" Content: " + object);
		}
		inbox.close(true);
		store.close();
	}
	
	public static void handlePart(Part part) throws MessagingException, IOException {
		String disposition = part.getDisposition();
		String contentType = part.getContentType();
		if (disposition == null) { // When just body
			System.out.println("Null: " + contentType);
			// Check if plain
			if (contentType.length() >= 10 && contentType.toLowerCase().substring(0, 10).equals("text/plain")) {
				part.writeTo(System.out);
			} else { // Don't think this will happen
				System.out.println("Other body: " + contentType);
				part.writeTo(System.out);
			}
		} else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
			System.out.println("Attachment: " + part.getFileName() + " : " + contentType);
			MailClient.saveFile(part.getFileName(), part.getInputStream());
		} else if (disposition.equalsIgnoreCase(Part.INLINE)) {
			System.out.println("Inline: " + part.getFileName() + " : " + contentType);
			MailClient.saveFile(part.getFileName(), part.getInputStream());
		} else { // Should never happen
			System.out.println("Other: " + disposition);
		}
	}
	
	public static void saveFile(String filename, InputStream input) throws IOException {
		if (filename == null) {
			filename = File.createTempFile("xx", ".out").getName();
		}
		
		System.out.println(" ENDERECO DO ARQUIVO #################### "+filename);
		// Do no overwrite existing file
		File file = new File(filename);
		for (int i = 0; file.exists(); i++) {
			file = new File(filename + i);
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		BufferedInputStream bis = new BufferedInputStream(input);
		int aByte;
		while ((aByte = bis.read()) != -1) {
			bos.write(aByte);
		}
		bos.flush();
		bos.close();
		bis.close();
	}
	
}
