/*
 * Data de Criacao 26/05/2005 12:13:26
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.subtask;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagvalue.AtributosClasseVO;
import xjavadoc.XClass;

/**
 * Ant SubTask para wizard j2ee
 * 
 * @author Darcio L Pacifico - 26/05/2005 12:13:26
 * 
 * @ant.element display-name="Wizard J2EE" name="wizardj2ee"
 *              parent="xdoclet.DocletTask"
 * 
 */
public class Wj2eeSubTask extends TemplateSubTask {
	
	public Wj2eeSubTask(){
		logger.error("Criando instancia para a SubTask");
	}
	
	@Override
	public void setDestDir(File destDir) {
		// TODO Auto-generated method stub
		super.setDestDir(destDir);
	}
	
	@Override
	public File getDestDir() {
		
		// testa se o arquivo que será gerado já existe. Caso já exista,
		// verifica se é permitido sobrescrever caso permita sobrescrever não faz nada
		if (exists() && !permitirSobrescrever()) {
			
			// troca o diretório raiz de 'src' para 'srcTmp' ou de 'web' para 'webTmp'
			if (logger.isDebugEnabled()){
				logger.debug("O arquivo já existe e não pode ser sobrescrito. Será gerado no diretório temporário !");
			}
			
			File destDirTmp = trocaDirRaiz(this.getDestDir());

			return destDirTmp;
			
		}
		
		return super.getDestDir();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2345234523456789789L;

	protected Log logger = LogFactory.getLog(Wj2eeSubTask.class);

	public static final String CAMADA_MODEL = "model";

	public static final String CAMADA_VIEW = "view";

	public static final String CAMADA_CONTROLLER = "controller";

	public static final String CAMADA_PERSISTENCIA = "persistencia";

	public static final String CAMADA_WRAPPER = "wrapper";

	public static final String VO_PREFIX = "VO";

	public static final String CONFIG_FILE = "/xdoclet/jazzwizard/subtask/configparam.properties";

	public static final String DIR_SRC = "src";

	public static final String DIR_WEB = "web";

	public static final String DIR_TMP = "Tmp";

	private String camada;

	private String gerarParaModuloEscravo;

	private String wj2eeTemplateFile;

	public String getWj2eeTemplateFile() {
		return wj2eeTemplateFile;
	}

	public void setWj2eeTemplateFile(String wj2eeTemplateFile) {
		this.wj2eeTemplateFile = wj2eeTemplateFile;
	}

	public URL getTemplateURL() {
		URL url = this.getClass().getResource(getWj2eeTemplateFile());
		
		if(url==null){
			logger.error("Arquivo de template nao encontrado => "+getWj2eeTemplateFile());
			throw new RuntimeException("Arquivo de template nao encontrado => "+getWj2eeTemplateFile());
		}
		
		return url;
	}

	/** Override do metodo generateForClass da superclasse */
	protected void generateForClass(XClass clazz) throws XDocletException {
		AtributosClasseVO atributosClasseVO;

		try {
			atributosClasseVO = new AtributosClasseVO(clazz);
		} catch (Wj2eeException e) {
			/*
			 * Eu não acho correto utilizar printStackTrace(), mas o modelo de
			 * excecoes de XDoclet não é muito robusto, a cadeia de exceções não
			 * aparece completamente
			 */
			e.printStackTrace();
			throw new XDocletException(e,
					"Erro ao tentar recuperar os atributos da classe");
		}
		String camada = getCamada() + "";

		boolean generate;

		generate = filtroArtefatosCamadaGeral(atributosClasseVO, clazz);

		/* Primeiramente avalio o filtro geral, para depois avaliar os outros */
		if (generate) {
			/*
			 * AVALIA PARA QUAL CAMADA O ARTEFATO SERÁ GERADO E APLICA O
			 * RESPECTIVO FILTRO
			 */
			if (camada.equals(CAMADA_MODEL)) {
				generate = filtroArtefatosCamadaModel(atributosClasseVO, clazz);

			} else if (camada.equals(CAMADA_VIEW)) {
				generate = filtroArtefatosCamadaView(atributosClasseVO, clazz);

			} else if (camada.equals(CAMADA_WRAPPER)) {
				generate = filtroArtefatosCamadaWrapper(atributosClasseVO,
						clazz);

			} else if (camada.equals(CAMADA_CONTROLLER)) {
				generate = filtroArtefatosCamadaController(atributosClasseVO,
						clazz);

			} else if (camada.equals(CAMADA_PERSISTENCIA)) {
				generate = filtroArtefatosCamadaPersistencia(atributosClasseVO,
						clazz);

			} else {
				/* NÃO FOI INFORMADA QUAL A CAMADA, NÃO APLICO NENHUM FILTRO */
				generate = true;
			}
		}

		if (generate) {
			/*
			 * File destFile = new File(getDestDir(),
			 * getGeneratedFileName(clazz)); if(destFile.exists()){ String
			 * destDir = getDestDir()+""; setDestDir(new File(destDir+"Tmp")); }
			 * 
			 * System.out.println("DestDir:"+getDestDir()+ "
			 * destFile.exists()="+destFile.exists());
			 * 
			 * logger.info("####### fullPath = " + fullPath);
			 */
			super.generateForClass(clazz);
		}

	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA
	 * "geral"
	 */
	private boolean filtroArtefatosCamadaGeral(
			AtributosClasseVO atributosClasseVO, XClass clazz) {
		String gerarParaModuloEscravo = getGerarParaModuloEscravo();
		String ignorar = atributosClasseVO.getIgnorar();
		String renderer = atributosClasseVO.getRenderer();

		boolean isGerarParaModuloEscravo = (gerarParaModuloEscravo == null || !gerarParaModuloEscravo
				.equals("false"));
		boolean isIgnorar = ignorar != null && ignorar.equals("true");
		boolean isModuloEscravo = renderer != null
				&& renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO);

		boolean returnValue;

		if (isIgnorar) {
			/* se for ignorar retorno false */
			returnValue = false;
		} else {

			if (isModuloEscravo) {
				/*
				 * se for modulo escravo, vou analisar se o template corrente
				 * para modulos escravos tbm
				 */

				if (isGerarParaModuloEscravo) {/* if burro */
					returnValue = true;
				} else {
					returnValue = false;
				}

			} else {
				/* nao é modulo escravo blz, deixo passar! */
				returnValue = true;
			}

		}

		return returnValue;
	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA Model
	 */
	private boolean filtroArtefatosCamadaPersistencia(
			AtributosClasseVO atributosClasseVO, XClass clazz) {
		if (atributosClasseVO.getGerarPersistencia().equals("false")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA Model
	 */
	private boolean filtroArtefatosCamadaModel(
			AtributosClasseVO atributosClasseVO, XClass clazz) {
		if (atributosClasseVO.getGerarModel().equals("false")
				|| !atributosClasseVO.getRenderer().equalsIgnoreCase(
						AtributosClasseVO.RENDERER_MODULO)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA VIEW
	 */
	private boolean filtroArtefatosCamadaView(
			AtributosClasseVO atributosClasseVO, XClass clazz) {
		if (atributosClasseVO.getGerarView().equals("true")
				&& (atributosClasseVO.getRenderer().equalsIgnoreCase(
						AtributosClasseVO.RENDERER_MODULO) || atributosClasseVO
						.getRenderer().equalsIgnoreCase(
								AtributosClasseVO.RENDERER_MODULOESCRAVO))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA VIEW
	 */
	private boolean filtroArtefatosCamadaWrapper(
			AtributosClasseVO atributosClasseVO, XClass clazz) {
		return true;
	}

	/**
	 * IMPLEMENTAR AQUI OS FILTROS PARA GERAÇÃO DE ARTEFATOS PARA A CAMADA
	 * Controller
	 */
	private boolean filtroArtefatosCamadaController(
			AtributosClasseVO atributosClasseVO, XClass clazz) {

		if (atributosClasseVO.getGerarController().equals("false")
				|| !atributosClasseVO.getRenderer().equalsIgnoreCase(
						AtributosClasseVO.RENDERER_MODULO)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Override do metodo getGeneratedFileName da superclasse<br>
	 * Resolve o local de gravação e nome do arquivo a ser gerado
	 * 
	 */
	protected String getGeneratedFileName(XClass clazz) throws XDocletException {
		String generatedFileName = super.getGeneratedFileName(clazz);
		generatedFileName = generatedFileName.replaceFirst(VO_PREFIX, "");



		return generatedFileName;
	}

	/**
	 * Testa se o arquivo já existe no disco. Uso geral!
	 * 
	 * @param generatedFileName
	 * @return
	 * @throws XDocletException 
	 */
	protected boolean exists() {
		return false;
	}


	/**
	 * Troca o diretório raiz onde as classes serão geradas, caso as 
	 * classes já existam. Define o diretório tmp como o padrão para o gerador 
	 * @param destDir
	 * @return
	 */
	protected File trocaDirRaiz(File destDir) {
		String paths[] = org.apache.commons.lang.StringUtils.split(destDir.getAbsolutePath(), destDir.separatorChar);
		String last = paths[paths.length - 1];
		String dirTmp;

		if (last.startsWith(DIR_SRC)) {
			dirTmp = DIR_SRC + DIR_TMP;

		} else if (last.startsWith(DIR_WEB)) {
			dirTmp = DIR_WEB + DIR_TMP;

		} else {
			logger.warn("Nao foi possivel identificar o diretorio de destino "
					+ "para concatenar com o sufixo 'Tmp'. Vou deixar o nome "
					+ "do novo diretorio apenas como 'Tmp'");
			dirTmp = DIR_TMP;
		}
		
		return new File(this.getDestDir().getParent() + "//" + dirTmp);
	}

	/**
	 * Testa se eh permitido sobreescrever o arquivo a ser gerado
	 * 
	 * @param generatedFileName
	 * @param clazz
	 * @return
	 */
	protected boolean permitirSobrescrever() {
		
		// TODO IMPLEMENTAR MEIO DE DEFINIR QUE VOs e templates podem ser
		// sobrescritos
		// TODO PODE SOLICITAR INTERAÇÃO DO USUÁRIO
		return true;
	}

	/**
	 * Metodo Getter do atributo camada.
	 */
	public String getCamada() {
		return camada;
	}

	/** Metodo Setter do atributo camada. */
	public void setCamada(String camada) {
		this.camada = camada;
	}

	/**
	 * @return Returns the gerarParaModuloEscravo.
	 */
	public String getGerarParaModuloEscravo() {
		return gerarParaModuloEscravo;
	}

	/**
	 * @param gerarParaModuloEscravo
	 *            The gerarParaModuloEscravo to set.
	 */
	public void setGerarParaModuloEscravo(String gerarParaModuloEscravo) {
		this.gerarParaModuloEscravo = gerarParaModuloEscravo;
	}

	/**
	 * Called to validate configuration parameters.
	 * 
	 * @exception XDocletException
	 *                Description of Exception
	 */
	public void validateOptions() throws XDocletException {
		// super.validateOptions();
		/* TODO: IMPLEMENTAR VALIDACOES */
	}

}
