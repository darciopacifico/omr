package br.com.dlp.framework.vo;

public class JamesConfigTest
{
	public static void main(String[] args)
	throws Exception
	{
		new MailClient("teste","teste", "127.0.0.1");
		new MailClient("teste", "teste", "127.0.0.1");
		MailClient teste2Client = new MailClient("teste2", "teste2", "127.0.0.1");
		new MailClient("teste3", "teste3", "127.0.0.1");
		new MailClient("teste3", "teste3", "pbr001001-46068.net.mapfre.com.br");
		
		// CLEAR EVERYBODY'S INBOX
		//testeClient.checkInbox(MailClient.CLEAR_MESSAGES);
		//teste2Client.checkInbox(MailClient.CLEAR_MESSAGES);
		//teste3Client.checkInbox(MailClient.CLEAR_MESSAGES);
		//Thread.sleep(500); // Let the server catch up
		
		// SEND A COUPLE OF MESSAGES TO BLUE (FROM RED AND GREEN)
		//testeClient.sendMessage("teste2@127.0.0.1",	"Testing blue from red", "This is a test message");
		
		//teste2Client.sendMessage(	"teste3@darcio.com","mensagem paconta teste2","This is a test message");
		
		Thread.sleep(500); // Let the server catch up
		
		// LIST MESSAGES FOR BLUE (EXPECT MESSAGES FROM RED AND GREEN)
		teste2Client.checkInboxSave(MailClient.SHOW_MESSAGES);
		//testeClient.checkInbox(MailClient.SHOW_AND_CLEAR);
	}
}
