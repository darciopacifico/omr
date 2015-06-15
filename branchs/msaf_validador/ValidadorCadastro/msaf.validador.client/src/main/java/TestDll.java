import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.sun.jna.Library;
import com.sun.jna.Native;


public class TestDll {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws IOException {
		
		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		final IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) library;
		
		String filepath = JOptionPane.showInputDialog("Digite o caminho do XML a ser enviado a DLL:");

		File file = new File(filepath);
		System.out.println("Buscando XML " + file.getAbsolutePath() + " ...");
		if(!file.exists()){
			System.out.println("Arquivo XML " + file.getAbsolutePath() + " não encontrado!");
			System.exit(1);
		}
		System.out.println("XML " + file.getAbsolutePath() + " encontrado!");
		
		System.out.println("Convertendo XML para String " + file.getAbsolutePath() + " ...");
		final String xml = obterXML(new BufferedReader(new FileReader(file)));
		System.out.println("XML convertido para String " + xml + " com sucesso!");
		
		int qtdeAcessos = Integer.parseInt(JOptionPane.showInputDialog("Digite o número de threads a serem executadas:"));
		System.out.println("Quantidade de threads: " + qtdeAcessos);
		
		final String caminhoDLL = JOptionPane.showInputDialog("Digite o caminho do diretório EVServer:");
		System.out.println("Caminho real das DLLs: " + caminhoDLL);
		
		final String tipoServico = JOptionPane.showInputDialog("Digite o tipo de serviço a ser consultado:");
		System.out.println();
		
		final JFrame frame = new JFrame("Resultado EV Server");
		final JTextArea textarea = new JTextArea();
		textarea.setColumns(100);
		textarea.setRows(40);
		final JScrollPane scroll = new JScrollPane(textarea);
		scroll.setAutoscrolls(true);
		frame.add(scroll);
		frame.setSize(200, 150);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final ThreadGroup group = new ThreadGroup("Group");

		for(int i = 0; i < qtdeAcessos; i++){
			Thread thread = new Thread(group, new Runnable() {
				@Override
				public void run() {
					textarea.append("Iniciando thread " + Thread.currentThread().getName() + " ...\n");
					long time = System.currentTimeMillis();
					String retorno = consultaOnLine.DBI_EfetuaConsultaServico(Integer.parseInt(tipoServico), xml, caminhoDLL, caminhoDLL);
					textarea.append(Thread.currentThread().getName() + ": Retorno da DLL: \n" + retorno + "\n");
					textarea.append(Thread.currentThread().getName() + ": Tempo de resposta: " + ((System.currentTimeMillis() - time) / 1000) + "\n");
					textarea.setCaretPosition(textarea.getText().length());				
				}
			});
			thread.start();
		}
		
		while(group.activeCount() > 0);
		textarea.append("Fim da execução!");
	}

	private static String obterXML(BufferedReader bufferedReader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String temp = "";
		while((temp = bufferedReader.readLine()) != null)
			sb.append(temp.trim());
		bufferedReader.close();
		return sb.toString().trim();
	}

}
