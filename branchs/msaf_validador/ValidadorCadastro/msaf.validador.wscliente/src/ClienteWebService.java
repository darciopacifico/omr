import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.msaf.validador.wsclient.ValidadorDelegateImplService_Impl;
import com.msaf.validador.wsclient.ValidadorDelegateImpl_Stub;
import com.sun.xml.messaging.saaj.soap.dom4j.ElementImpl;

/**
 * @author Darcio
 * 
 */
public class ClienteWebService {

	/**
	 * @constructor
	 */
	public ClienteWebService() {
		super();
	}

	private static boolean ignorePropertyNull = false; 
	
	private static String separator;
	
	private static String fileExtension;
	
	static {
		
		String gerarPropertyString = PropertiesLoadImpl.getValor("config.ignorePropertyNull");
		
		if(gerarPropertyString != null) {
			ignorePropertyNull = Boolean.valueOf(gerarPropertyString).booleanValue();
		}
		
		String propertySeparator = PropertiesLoadImpl.getValor("config.separetor");
		
		if(propertySeparator != null) {
			separator = propertySeparator;
		}else{
			separator = "=";
		}
	
		String propertyExtension = PropertiesLoadImpl.getValor("config.file.extension");
		
		if(propertyExtension != null) {
			fileExtension = propertyExtension;
		}else{
			fileExtension = ".properties";
		}
		
		
	}
	
	
	/**
	 * M�todo main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		ClienteWebService clienteWebService = new ClienteWebService();

		clienteWebService.execute(args);
	}

	/**
	 * 
	 * @param paramtros
	 * @throws RemoteException
	 * @throws SOAPException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static void execute(String[] parametros) {
		try {
			if (validaArgumentos(parametros)) {
				System.out
						.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<< Inicio Pesquisa...");
				String documento = parametros[0];
				int tpConsulta = Integer.parseInt(parametros[1]);
				int tpServico = Integer.parseInt(parametros[2]);
				String uf = parametros[3];

				ValidadorDelegateImpl_Stub stub = (ValidadorDelegateImpl_Stub) (new ValidadorDelegateImplService_Impl()
						.getValidadorDelegateImplPort());
				stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY,
						PropertiesLoadImpl.getValor("web.service.path"));

				SOAPElement[] registros = stub.consultaCliente(documento,
						tpConsulta, tpServico, uf);

				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<< Gravando arquivos...");

				gravarResultado(registros, documento, uf, retornaServico(tpServico), retornaTpConsulta(tpConsulta, tpServico));

				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<< Fim do processo...");
			}

		} catch (Exception e) {

			System.out.println("Problemas ao acessar o webservice... - mensagem: " + e.getMessage());

			System.exit(0);
		}
	}

	/**
	 * Grava os dados retornados da consulta em um arquivo xml.
	 * 
	 * @param registro
	 * @param documento
	 * @param uf
	 * @param servico
	 * @param consulta
	 **/
	private static void gravarResultado(SOAPElement[] registro,
			String documento, String uf, String servico, String consulta) {

		final String nomeArquivo = documento + uf.toUpperCase() + servico + consulta + "_" + System.currentTimeMillis() + fileExtension;
		final String diretorioXML = PropertiesLoadImpl.getValor("dir.nome.file");
		
		try {

			// Gravando no arquivo
			File arquivo;

			arquivo = new File(diretorioXML + nomeArquivo);
			FileOutputStream fos = new FileOutputStream(arquivo);

			ElementImpl element = null;
			ElementImpl child = null;
			String value = null;

			for (int i = 0; i < registro.length; i++) {

				element = (ElementImpl) registro[i];

				for (Iterator iterator = element.getChildElements(); iterator.hasNext();) {

					child = (ElementImpl) iterator.next();

					if(ignorePropertyNull) {
						
						if(child.getValue() != null) {
							
							value = child.getName() + separator + (child.getValue() == null ? "" : child.getValue()) + "\n";
							fos.write(value.getBytes());
							
						}
					}else{
						value = child.getName() + separator + (child.getValue() == null ? "" : child.getValue()) + "\n";

						fos.write(value.getBytes());
					}
					

				}

			}

			fos.close();

		} catch (IOException e) {
			System.out.println("Problemas ao gravar arquivo xml");
		}

	}

	/**
	 * Retorna a descri��o do servi�o pesquisado.
	 * 
	 * @param tpServico
	 * @return
	 **/
	private static String retornaServico(int tpServico) {
		return tpServico == 1 ? "Sintegra" : "ReceitaFederal";
	}

	/**
	 * Retorna a descri��o do tipo de documento consultado.
	 * 
	 * @param tpConsulta
	 * @param tpServico
	 * @return
	 **/
	private static String retornaTpConsulta(int tpConsulta, int tpServico) {

		if (tpServico == 1) {
			return tpConsulta == 0 ? "CNPJ" : "IE";
		}
		if (tpServico == 3) {
			return tpConsulta == 0 ? "CNPJ" : "CPF";
		}
		return new String("Con");
	}

	/**
	 * Faz a valida��o dos argumentos de entrada.
	 * 
	 * @param args
	 **/
	private static boolean validaArgumentos(String[] args) {
		if (args == null || args.length < 4) {
			System.out
					.println("Necess�rio passar todos os atributos corretamente !!!");
			return false;
		}
		return true;

	}

}
