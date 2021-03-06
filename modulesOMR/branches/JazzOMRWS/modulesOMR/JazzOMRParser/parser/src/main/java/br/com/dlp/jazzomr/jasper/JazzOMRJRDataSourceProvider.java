/**
 * 
 */
package br.com.dlp.jazzomr.jasper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationHelperWS;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.question.CriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.jrds.JazzJRDataSourceProvider;

/**
 * @author darcio
 *
 */
public class JazzOMRJRDataSourceProvider extends JazzJRDataSourceProvider {

	private static Long questionPk =1l; 
	private static Long alternativePk = 1l;
	
	public JazzOMRJRDataSourceProvider(Map<String, Object> map) {
		super(getEventos(),EventVO.class, map);
	}

	protected static List<EventVO> getEventos(){
		
		List<EventVO> eventos = new ArrayList<EventVO>();
		
		eventos.add(createDemoExam());
		return eventos;
	}
	
	/**
 	 * Cria demo de exame 
 	 * @param pessoaVO
 	 * @param empresaVO
	 * @return 
 	 * @throws JazzBusinessException 
 	 */
	protected static EventVO createDemoExam() {
		
		ExamVO exameDemo = getExame();
		
		List<PessoaVO> pessoasSelecionadas = getPessoas();
				
		ParticipationHelperWS ph = new ParticipationHelperWS();
		EventVO eventVO=null;
		try {
			eventVO = ph.processParticipations("Evento teste",exameDemo, pessoasSelecionadas, 3, getJasperReport());
		} catch (JazzBusinessException e) {
			throw new RuntimeException("Erro ao tentar montar exame",e);
		}
		
		eventVO.setDescription("Prova Bimestral");
		
		return eventVO;
		
	}

	/**
	 * @return
	 * 
	 */
	private static JasperReport getJasperReport() {
		InputStream stream = JazzOMRJRDataSourceProvider.class.getResourceAsStream("/reports/ExamReportDSP2.jrxml");

		JasperReport jr;
		try {
			jr = JasperCompileManager.compileReport(stream);
		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao tentar compilar relatorio!", e);
		}

		return jr;
	}
	
	/**
	 * @return
	 */
	public static List<PessoaVO> getPessoas() {
		List<PessoaVO> pessoasSelecionadas = new ArrayList<PessoaVO>();
		pessoasSelecionadas.add(new PessoaVO(1l,"@@Darcio"));
		pessoasSelecionadas.add(new PessoaVO(2l,"@@Joao"));
		pessoasSelecionadas.add(new PessoaVO(3l,"@@Antonio"));
		pessoasSelecionadas.add(new PessoaVO(4l,"@@Maria"));
		return pessoasSelecionadas;
	}

	/**
	 * @return
	 */
	public static ExamVO getExame() {
		
		QuestionnaireVO enemCiencHumanas = createENEMCienciasHumanas();
		QuestionnaireVO enemCiencNatureza= createENEMCienciasNatureza();
		
		ExamVO exameDemo = new ExamVO();
		exameDemo.setPK(1l);
		//exameDemo.setRelatorioVO(empresaVO.getRelatorioVOs().get(0));
		exameDemo.setDescription("Exame Demo - ENEM 2010");
		exameDemo.getQuestionnaires().add(enemCiencNatureza);
		exameDemo.getQuestionnaires().add(enemCiencHumanas);
		return exameDemo;
	}

	/**
	 * @param empresaVO
	 * @return
	 */
	protected static QuestionnaireVO createENEMCienciasNatureza() {
		QuestionnaireVO questionnaireVO = new QuestionnaireVO();
		questionnaireVO.setPK(2l);
		questionnaireVO.setDescription("CI�NCIAS DA NATUREZA E SUAS TECNOLOGIAS");

		{
			String qDesc = "Em nosso cotidiano";
			
			String[] alt1 = new String[]{
					"A temperatura da �gua",
					"Uma m�e coloca a m�o"};

			createQuestion(questionnaireVO, qDesc, alt1);
			}
			
		{
			String qDesc = 
					"As ondas eletromagn�ticas";
			
			String[] alt1 = new String[]{
					"reflex�o.",
					"refra��o.",
					"difra��o."};

			createQuestion(questionnaireVO, qDesc, alt1);
			}

			

		return questionnaireVO;
	}


	/**
	 * @param empresaVO
	 * @param questionnaireVO
	 * @param qDesc
	 * @param alt1
	 */
	protected static void createQuestion(QuestionnaireVO questionnaireVO, String qDesc, String[] alts) {
		QuestionVO questionVO = createQuestion(qDesc, alts);
		
		questionnaireVO.getQuestions().add(questionVO);
	}
	


	/**
	 * @param empresaVO
	 * @param questionnaireVO
	 * @param qDesc
	 * @param alt1
	 */
	protected static void createQuestion(QuestionnaireVO questionnaireVO, String qDesc, String dissertativa) {
		QuestionVO questionVO = createQuestion(qDesc, dissertativa);
		
		questionnaireVO.getQuestions().add(questionVO);
	}


	private static  QuestionVO createQuestion(String qDesc, String dissertativa) {
		QuestionVO questionVO = new QuestionVO();
		questionVO.setDescription(qDesc);

		CriterionVO dissertativeVO = new CriterionVO();
		dissertativeVO.setDescription(dissertativa);
		dissertativeVO.setMaxPonto(5);
		dissertativeVO.setQtdLinhas(40);
		dissertativeVO.setRequired(true);
		dissertativeVO.setCritType("D");

		questionVO.getCriterionVOs().add(dissertativeVO);

		return questionVO;
	}

	/**
	 * @param empresaVO
	 * @param qDesc
	 * @param alt1
	 * @return
	 */
	protected static QuestionVO createQuestion(String qDesc, String[] alt1) {
		QuestionVO questionVO = new QuestionVO();
		questionVO.setPK(questionPk++);
		questionVO.setDescription(qDesc);

		boolean correct = true;
		for (String altDesc : alt1) {
			CriterionVO criterionVO = new CriterionVO();
			criterionVO.setPK(alternativePk++);
			criterionVO.setDescription(altDesc);
			criterionVO.setCorrect(correct);
			criterionVO.setOrdem(new Double(alternativePk));
			criterionVO.setCritType("A");
			questionVO.getCriterionVOs().add(criterionVO);
			correct = false;
		}
		return questionVO;
	}
	
	/**
	 * @param empresaVO
	 * @return
	 */
	protected static QuestionnaireVO createENEMCienciasHumanas() {
		QuestionnaireVO questionnaireVO = new QuestionnaireVO();
		
		questionnaireVO.setPK(1l);
		questionnaireVO.setDescription("CI�NCIAS HUMANAS E SUAS TECNOLOGIAS");

		{
		
			String qDesc = "Muitos processos erosivos";
			
			String[] alt1 = new String[]{
			"a maior ocorr�ncia de enchentes",
			"a contamina��o da popula��o "
			};
			
			QuestionVO questionVO = createQuestion(qDesc, alt1);
			
			CriterionVO diss = new CriterionVO();
			diss.setPK(99l);
			diss.setDescription("DESCREVA SUA RESPOSTA");
			diss.setQtdLinhas(4);
			diss.setCritType("D");
			questionVO.getCriterionVOs().add(diss);
			
			
			BufferedImage img = loadImage("/reports/morro.png");
			ImageVO imageVO = new ImageVO();
			imageVO.setTitulo("Figura 1");
			imageVO.setImage(img);
			
			questionVO.getImageVOs().add(imageVO);
			
			questionnaireVO.getQuestions().add(questionVO);
		}
		
		{
			String qDesc = "Antes, eram apenas as ";
			
			String[] alt1 = new String[]{
					"o aumento do aproveitamento ",
					"a redu��o do processo ",
					"a amplia��o do "};

			createQuestion(questionnaireVO, qDesc, alt1);
			}

	
		return questionnaireVO;
	}
	
	

	/**
	 * @param imageFile
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected static BufferedImage loadImage(String imageFile) {
		InputStream is = JazzOMRJRDataSourceProvider.class.getResourceAsStream(imageFile);
		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}
	
}
