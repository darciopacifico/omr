package br.com.dlp.jazz.svnhook;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.tmatesoft.svn.core.SVNException;

import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookLogTimeException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookRTException;
import br.com.dlp.jazz.svnhook.services.JazzSVNHookService;
import br.com.dlp.jazz.svnhook.services.JazzSVNHookServiceImpl;
import br.com.dlp.jazz.svnhook.vos.CitationVO;
import br.com.dlp.jazz.svnhook.vos.CriticVO;

/**
 * Classe executável para ser utilizada no processamento dos eventos de commit do SVN, disparados por pre-commit e post-commit
 * 
 * @author t_dpacifico
 */
@SuppressWarnings("PMD.SystemPrintln")
public final class HookMain {
	private static final String JUST_A_SEPARATOR = "-------------------------------------------------------------------------";
	private static final Logger log = LoggerFactory.getLogger(JazzSVNHookServiceImpl.class);

	/**
	 * Construtor padrao
	 */
	private HookMain() {
		// EXPLICITY EMPTY and private
	}
	
	/**
	 * Comandos possíveis para invocar nos hooks.
	 * CRITICIZE: normalmente pre-commit 
	 * LOGTIME: normalmente post-commit
	 * @author t_dpacifico
	 */
	public enum COMANDO{
		CRITICIZE, LOGTIME;
	}
	
	/**
	 * Enum de argumentos possíveis para chamada do utilitario
	 * 
	 * @author t_dpacifico
	 */
	public enum ARGUMENTS {
		CHANGED_DIRS(0), REPO_SVN_UUID(1), COMMENTS(2), AUTHOR(3),COMANDO(4);
		private int argPos;

		ARGUMENTS(int i) {
			this.argPos = i;
		}
		
		/**
		 * Para logging dos parametros
		 */
		@Override
		public String toString() {
			return this.name()+"["+this.argPos+"]";
		}

		/**
		 * @return the i
		 */
		public int getArgPos() {
			return argPos;
		}

		/**
		 * @param i the i to set
		 */
		public void setArgPos(int i) {
			this.argPos = i;
		}
	}

	/**
	 * Recebe os parâmetros de pre-commit ou post-commit e processa os comentários de commit, como "refs #4 4h" ou "closes #6 8h"
	 * 
	 * @param args
	 * @throws SVNException
	 */
	public static void main(String[] args) throws SVNException {

		/*
		 * jazzxdoclet/paginas/produto/ jazzxdoclet/subprojeto1/ my-project/ novoprojeto/ f8163358-0f88-44f2-8509-3be3471138aa
		 * "refs #1 Corrigindo ticket 21 closes   	 #2  	 12h rEfs #3 2h #4 3h #5 #6 fixEs #70 3h ok Refs#26sdsd Refs #8 7h refs #9 12h" dpacifico
		 */

		List<String> dirsChanged = getListStrings(args, ARGUMENTS.CHANGED_DIRS);
		String repoUUID = getArg(args, ARGUMENTS.REPO_SVN_UUID);
		String comments = getArg(args, ARGUMENTS.COMMENTS);
		String author = getArg(args, ARGUMENTS.AUTHOR);
		COMANDO comando = parseComand(getArg(args, ARGUMENTS.COMANDO).toUpperCase());

		JazzSVNHookService svnHookService = getJazzSVNHookService();
		
		try {
			Collection<CitationVO> citacoes;
			if(comando.equals(COMANDO.LOGTIME)){
				citacoes = svnHookService.criticizeAndLogTimes(dirsChanged, repoUUID, comments, author);
			}else{
				citacoes = svnHookService.criticizeComments(dirsChanged, repoUUID, comments, author);
			}
			
			logCitations(citacoes);

		} catch (JazzSVNHookLogTimeException e) {
			logErrorsAndExit(e);

		} catch (JazzSVNHookException e) {
			logErrorsAndExit(e);
		}

	}




	/**
	 * @param strComando
	 * @return
	 * @throws JazzSVNHookRTException
	 */
	protected static COMANDO parseComand(String strComando) throws JazzSVNHookRTException {
		COMANDO comando;
		try {
			comando = COMANDO.valueOf(strComando);
		} catch (IllegalArgumentException e) {
			StringBuffer sb = new StringBuffer();
			for (COMANDO cmd : COMANDO.values()) {
				sb.append(cmd.name()).append(" ");
			}
			
			log.error("Erro ao tentar identificar o comando solicitado. Por favor, informe uma das opções: "+sb,e);
			throw new JazzSVNHookRTException("Erro ao tentar identificar o comando solicitado. Por favor, informe uma das opções: "+sb,e);
		}
		return comando;
	}

	/**
	 * Recupera a implementação de JazzSVNHookService, para processamento dos hooks de commit do svn
	 * 
	 * @return
	 */
	protected static JazzSVNHookService getJazzSVNHookService() {
		FileSystemXmlApplicationContext applicationContext = getApplicationContext();

		//loga beans registrados em FileSystemXmlApplicationContext
		if (log.isDebugEnabled()) {
			log.debug("BEANS REGISTRADOS EM FileSystemXmlApplicationContext");
			String[] names = applicationContext.getBeanDefinitionNames();
			for (String string : names) {
				log.debug(string);
			}
		}

		return (JazzSVNHookService) applicationContext.getBean("jazzSVNHookServiceImpl");
	}

	/**
	 * Loga as citacoes parseadas e processadas pelo JazzSVNHookService
	 * 
	 * @param citacoes
	 */
	protected static void logCitations(Collection<CitationVO> citacoes) {
		for (CitationVO citationVO : citacoes) {
			log.error(citationVO.getComando() + " #" + citationVO.getIssueVO().getId() + " tempo despendido por " + citationVO.getLogin() + " " + citationVO.getHorasDespendidas() + "h");
		}
	}

	/**
	 * Loga no console de saída os erros enviados por JazzSVNHookService
	 * 
	 * @param e
	 */
	protected static void logErrorsAndExit(JazzSVNHookLogTimeException e) {
		List<CriticVO> criticas = e.getCitacaoIssueVO().getCritics();

		logCriticas(e,criticas);

		System.exit(1);
	}

	/**
	 * 
	 * @param e
	 */
	protected static void logErrorsAndExit(JazzSVNHookException e) {
		System.err.println(JUST_A_SEPARATOR);
		System.err.println(e.getMessage());
		System.err.println(JUST_A_SEPARATOR);
		System.exit(1);
	}

	/**
	 * @param e
	 * @param criticas
	 */
	protected static void logCriticas(Exception e, List<CriticVO> criticas) {
		//propositalmente, deve ser utilizado o System.err ao inves de log.error
		System.err.println(JUST_A_SEPARATOR);
		System.err.println("ERRO AO TENTAR PROCESSAR OS TICKETS REFERENCIADOS NO COMMIT!");
		for (CriticVO criticVO : criticas) {
			System.err.println();
			System.err.println("[" + criticVO.getSeveridade() + ", " + criticVO.getTipoCritica() + "]" + criticVO.getDescricao());
		}
		System.err.println(JUST_A_SEPARATOR);
	}
	
	
	
	/**
	 * Método utiltário. Converte um argumento em lista para uma coleção de Strings
	 * 
	 * @param args
	 * @param argument
	 * @return
	 */
	public static List<String> getListStrings(String[] args, ARGUMENTS argument) {
		String argList = getArg(args, argument);

		List<String> stringList = new ArrayList<String>();
		String[] arrList = argList.split("/n");

		CollectionUtils.addAll(stringList, arrList);

		return stringList;

	}

	/**
	 * Recupera o argumento dentre os parametros informados na chamada da classe Main
	 * 
	 * @param args
	 * @param argIndex
	 * @return
	 */
	protected static String getArg(String[] args, ARGUMENTS argIndex) {

		if (args.length != ARGUMENTS.values().length) {
			log.error("A quantidade de argumentos informados nao é válida!");

			StringBuffer sb = new StringBuffer();
			for (ARGUMENTS arg : ARGUMENTS.values()) {
				sb.append(" ").append(arg);
			}
			log.error("Utilize o formato de argumentos " + sb.toString());

			throw new JazzSVNHookRTException("Argumento informado inválido!");
		}

		
		String value = args[argIndex.argPos];

		if (log.isDebugEnabled()) {
			log.debug("parametro "+argIndex+"="+value);
		}
		
		return value;
	}

	/**
	 * Cria applicationContext
	 * 
	 * @return
	 * @return
	 */
	protected static FileSystemXmlApplicationContext getApplicationContext() {
		String xmlUrlAppContext = "HookApplicationContext.xml";
		
		File appContextXml = new File(xmlUrlAppContext);
		if(appContextXml.exists()){
			return new FileSystemXmlApplicationContext(xmlUrlAppContext);
		}else{
			throw new JazzSVNHookRTException("O arquivo de configuração Spring '"+appContextXml.getAbsolutePath()+"' nao foi encontrado! Este arquivo deve estar no mesmo diretório de onde a aplicacao foi chamada!");
		}
		
	}
}
