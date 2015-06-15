package com.msaf.validador.consumer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.framework.utils.ExtratorParamentrosDLL;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;

/**
 * Tela swing monitora do consumidor JMS do Validador
 * 
 * @author dlopes
 *
 */
public class SwingConsumerMonitor extends JFrame {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 13253544364567786L;
	private JButton btnIniciar;
	private JButton btnParar;
	private JTextArea txtConsole;
	private final Component estaTela = this;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private javax.swing.JComboBox comboBoxJMSProxy;
    private javax.swing.JComboBox comboBoxDllConfProxy;
    
    private String proxySelecionado = new String();
    private DllDadosDTO dllConfSelecionado = new DllDadosDTO();
    
    private Map<String, DllDadosDTO> listaDLL = new HashMap<String, DllDadosDTO>();
    
	//Componente de negócio consumidor
	private JMSValidadorConsumidorBO validadorConsumidorBO;
	
	Logger log = Logger.getLogger(SwingConsumerMonitor.class);
	
	/**
	 * Escreve a mensagem enviada na console
	 */
	public void registraEventoNaConsole(String mensagem){
		java.text.MessageFormat messageFormat =new MessageFormat("");
		String strDate = dateFormat.format(new Date());
		txtConsole.setText(txtConsole.getText()+"\n "+strDate+": "+mensagem);
	}
	
	
	/**
	 * Constroi tela do consumidor
	 */
	public SwingConsumerMonitor() {
		super("Consumidor Validador JMS");
		recuperaValidadorConsumidorBO();
		montaGUI();
		layoutTela();
		adicionaEventosAosBotaoes();
	}

	/**
	 * No evento de click do botão iniciar, inicializa o consumo de mensagens em validadorConsumidorBO 
	 * 
	 */
	protected void adicionaEventosAosBotaoes() {
		btnIniciar.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					proxySelecionado = comboBoxJMSProxy.getSelectedItem().toString();
					dllConfSelecionado = listaDLL.get(comboBoxDllConfProxy.getSelectedItem().toString());

					//inicializa o servidor JMS a partir de um proxy selecionado pelo usuario.
					validadorConsumidorBO.inicializaConsumidorJMS(proxySelecionado, dllConfSelecionado);
					txtConsole.setText("Iniciado! Pronto para receber mensagens.");
					comboBoxJMSProxy.setEnabled(false);
					comboBoxDllConfProxy.setEnabled(false);
				} catch (Exception e2) {
					registraEventoNaConsole("Erro ao inicializar consumidor JMS. Verifique se o servidor de mensagens está disponível.");
					log.error("Erro ao tentar consumir fila JMS",e2);
				}
			}
		});
		
		btnParar.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					validadorConsumidorBO.finalizaConsumidorJMS();
					txtConsole.setText("Finalizado!");
					comboBoxJMSProxy.setEnabled(true);
					comboBoxDllConfProxy.setEnabled(true);
				} catch (Exception e2) {
					log.error("Erro ao tentar consumir fila JMS",e2);
					JOptionPane msgErro = new JOptionPane("Teste!");
					msgErro.showConfirmDialog(estaTela, "Erro ao tentar consumir fila JMS! Verifique o log do sistema!");
				}
			}
		});

		comboBoxJMSProxy.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					proxySelecionado = comboBoxJMSProxy.getSelectedItem().toString();
					log.debug("proxy selecionado:"+ proxySelecionado);
					txtConsole.setText("proxy selecionado:"+ proxySelecionado);
				} catch (Exception e2) {
					log.error("Erro ao tentar conectar ao proxy:"+proxySelecionado,e2);
					JOptionPane msgErro = new JOptionPane("Teste!");
					msgErro.showConfirmDialog(estaTela, "Erro: Confirma");
				}
			}
		});
		
		comboBoxDllConfProxy.addActionListener(new ActionListener(){
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					 
					 dllConfSelecionado = listaDLL.get(comboBoxDllConfProxy.getSelectedItem().toString());
					 
					log.debug("dll config selecionado:"+ dllConfSelecionado.getProxyHost());
					txtConsole.setText("config DLL selecionada:"+ dllConfSelecionado);
				} catch (Exception e2) {
					log.error("Erro ao tentar conectar a dll:"+dllConfSelecionado,e2);
					JOptionPane msgErro = new JOptionPane("Teste!");
					msgErro.showConfirmDialog(estaTela, "Erro: Confirma");
				}
			}
		});
		
	}

	/**
	 * Instancia um contexto Spring e recupera o JMSValidadorConsumidorBO configurado.
	 * @throws ValidadorException 
	 * 
	 */
	protected void recuperaValidadorConsumidorBO() {
		try {
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");
			
			validadorConsumidorBO = (JMSValidadorConsumidorBO) applicationContext.getBean("validadorConsumidorBO");
			
			validadorConsumidorBO.getMessageListener().setSwingConsumerMonitor(this);
			
		} catch (Exception e) {
			e.printStackTrace();
			registraEventoNaConsole("Erro ao tentar inicializar consumo de mensagens JMS");
			log.error("Erro ao tentar inicializar consumo de mensagens JMS",e);
			System.exit(0);
		}
	}

	/**
	 * 
	 */
	private void montaGUI() {
		btnIniciar=new JButton("Iniciar");
		btnParar=new JButton("Parar");
		txtConsole = new JTextArea(7,34);
		//txtConsole = new JTextArea(7,10);
		
		txtConsole.setLineWrap(true);
		
		JPanel panel = new JPanel();
		//panel.setLayout(new BorderLayout());
		//panel.setPreferredSize(new Dimension(500, 1000));
		
		JPanel pnlBotoes = new JPanel();
		pnlBotoes.setLayout(new FlowLayout());
		pnlBotoes.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel pnlScroll = new JPanel();
		pnlScroll.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(txtConsole);
		pnlScroll.add(scrollPane,BorderLayout.CENTER);
		pnlScroll.add(new JLabel("Console"),BorderLayout.NORTH);
		//pnlScroll.setPreferredSize(new Dimension(100, 100));
			
		pnlBotoes.add(btnIniciar, BorderLayout.NORTH);
		pnlBotoes.add(btnParar,BorderLayout.NORTH);
		
		panel.add(pnlBotoes,BorderLayout.NORTH);
		panel.add(pnlScroll,BorderLayout.CENTER);
		
		
		JPanel pnlBox = new JPanel(new FlowLayout());
		
		//criaJMSProxyBox(panel);
		criaJMSProxyBox(panel);
		
		// recupera a lista de proxys do bean.
		String[] proxyList = preencherBoxJMSProxy();		 
		comboBoxJMSProxy.setModel(new javax.swing.DefaultComboBoxModel(proxyList));
		
		//criaDLLConfigBox(panel);
		criaDLLConfigBox(panel);
		// recupera a lista de configs.
		String[] dllList = preencherDLLConfigBox();
		comboBoxDllConfProxy.setModel(new javax.swing.DefaultComboBoxModel(dllList));
		
		pnlBox.setPreferredSize(new Dimension(100, 100));
		
		txtConsole.setAutoscrolls(true);
		//panel.add(pnlBox);
		
		getContentPane().add(panel);
	}


	/**
	 * @param panel
	 */
	private void criaJMSProxyBox(JPanel panel) {
		comboBoxJMSProxy = new javax.swing.JComboBox();
		JPanel pnlComboBox = new JPanel();
		pnlComboBox.add(new JLabel("Proxy:"),BorderLayout.NORTH);
		pnlComboBox.add(comboBoxJMSProxy,BorderLayout.NORTH);
		panel.add(pnlComboBox, BorderLayout.PAGE_END);
	}


	/**
	 * @return
	 */
	private String[] preencherBoxJMSProxy() {
		List proxys =  this.validadorConsumidorBO.getJmsProviderURLList();
		
		String[] proxyList = new String[proxys.size()];
		for (int i = 0; i < proxys.size(); i++) {
			proxyList[i] =  proxys.get(i).toString();
		}
		return proxyList;
	}
	

	/**
	 * cria o componente Swing.
	 * @param panel
	 */
	private void criaDLLConfigBox(JPanel panel) {
		comboBoxDllConfProxy = new javax.swing.JComboBox();
		JPanel pnlComboBoxDLL = new JPanel();
		pnlComboBoxDLL.add(new JLabel("Config da DLL:"),BorderLayout.NORTH);
		pnlComboBoxDLL.add(comboBoxDllConfProxy,BorderLayout.NORTH);
		panel.add(pnlComboBoxDLL, BorderLayout.PAGE_END);

	}


	/**
	 * preenche a lista de dll
	 * @return
	 */
	private String[] preencherDLLConfigBox() {
		List<String> proxysDllList =  this.validadorConsumidorBO.getDllConfList();
		String[] proxyList = new String[proxysDllList.size()];
		ExtratorParamentrosDLL extrator = new ExtratorParamentrosDLL(proxysDllList);	
		this.listaDLL = extrator.getDadosDLLMap();
		
		//recupera a lista de chaves.
		Set<String> listaChaves = listaDLL.keySet();

		int i = 0;
		for (Iterator iterator = listaChaves.iterator(); iterator.hasNext();) {
			String chaveDll = (String) iterator.next();
			proxyList[i] = chaveDll;
			i++;
		}
		
		return proxyList;
	}
	
	
	
	
	private String[] formataListaDadosDLL(List<String> proxys){
		
		//ExtratorParamentrosDLL extratorParamentrosDLL = new ExtratorParamentrosDLL();
		
		
		
//		String[] proxyListFormatado = new String[proxys.length];
//	
//		for (int i = 0; i < proxyListFormatado.length; i++) {
//			proxyListFormatado[i] = proxys[i].
//			                         
//		}
//		
		
	//	return proxyListFormatado;
		return null;
		
	
	}
	
	
	/**
	 * 
	 */
	protected void layoutTela() {
		setSize(400, 300);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		 this.setNativeLookAndFeel();
		
	}
	
	
	public static void setNativeLookAndFeel() {
	    try {
	      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    	 UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

	    } catch(Exception e) {
	      System.out.println("Error setting native LAF: " + e);
	    }
	}
	    
	
	public DllDadosDTO getDllConfSelecionado() {
		return dllConfSelecionado;
	}


	public void setDllConfSelecionado(DllDadosDTO dllConfSelecionado) {
		this.dllConfSelecionado = dllConfSelecionado;
	}


	public static void main(String[] args) {
		SwingConsumerMonitor consumerMonitor = new SwingConsumerMonitor();
	}

	public JMSValidadorConsumidorBO getValidadorConsumidorBO() {
		return validadorConsumidorBO;
	}

	public void setValidadorConsumidorBO(
			JMSValidadorConsumidorBO validadorConsumidorBO) {
		this.validadorConsumidorBO = validadorConsumidorBO;
	}
	
	
	
}
