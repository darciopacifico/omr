package br.com.dlp.jazzomr.selfservice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.jws.WebService;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.empresa.ETipoRelatorio;
import br.com.dlp.jazzomr.empresa.EmpresaBusiness;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.event.EventBusiness;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationHelper;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamBusiness;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.QuestionnaireBusiness;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.person.EAuthority;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.poc.CopiaArquivo;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.ImageLogoVO;
import br.com.dlp.jazzomr.results.JRFileVO;


@Component
@WebService
public class SelfRegBusinessImpl {

	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Autowired
	private EmpresaBusiness empresaBusiness ;

	
	
	
 	/**
 	 * Salva o novo auto registro 
 	 * @param empresaVO
 	 * @param pessoaVO
 	 * @throws JazzBusinessException
 	 */
 	public void saveNewReg(EmpresaVO empresaVO, PessoaVO pessoaVO) throws JazzBusinessException{
 		
 		checkExistUserName(pessoaVO);
 		
 		String pkPessoa = pessoaVO.getLogin();

 		
		ImageLogoVO imageLogoVO = new ImageLogoVO();
		imageLogoVO.setTitulo("Logo padrão do sistema!");
		BufferedImage logoPadrao = loadImage("/reports/logo-cliente.png");
		imageLogoVO.setImage(logoPadrao);
		empresaVO.getLogos().add(imageLogoVO);
		
		
		RelatorioVO relatorioVO = crateNewRelatorio(pkPessoa);
		empresaVO.getRelatorioVOs().add(relatorioVO);
 		
		
 		pessoaVO.getAuthorities().add(EAuthority.MASTER_ADM);
 		pessoaVO.getAuthorities().add(EAuthority.PROFESSOR);
 		pessoaVO.setAlteradoPor(pkPessoa);
 		pessoaVO.setCriadoPor(pkPessoa);

 		empresaVO.setAlteradoPor(pkPessoa);
 		empresaVO.setCriadoPor(pkPessoa);
		
 		
 		empresaBusiness.saveOrUpdate(empresaVO);
 		pessoaBusiness.saveOrUpdate(pessoaVO);

 		pessoaVO.setEmpresaVO(empresaVO);
 		
 		pessoaBusiness.saveOrUpdate(pessoaVO);

 		
 		
 	
 		
 		
 	}



	/**
	 * @param pkPessoa
	 * @return
	 */
	public RelatorioVO crateNewRelatorio(String pkPessoa) {
		RelatorioVO relatorioVO = new RelatorioVO();
 
		relatorioVO.setDescription("Layout padrão de provas.");
		relatorioVO.setTipoRelatorio(ETipoRelatorio.EXAME);
		relatorioVO.setCriadoPor(pkPessoa);
		relatorioVO.setAlteradoPor(pkPessoa);
		
		JRFileVO jrFileVO = new JRFileVO();
		byte[] jasperReportBytes = getJasperReportBytes("/reports/ExamReport.jasper");
		jrFileVO.setJasperReport(jasperReportBytes);
		jrFileVO.setNome("principal");

		JRFileVO jrFileQuestImgVO = new JRFileVO();
		byte[] questionImgRpt = getJasperReportBytes("/reports/questionImgRpt.jasper");
		jrFileQuestImgVO.setJasperReport(questionImgRpt);
		jrFileQuestImgVO.setNome("questionImgRpt");
		
		relatorioVO.getJrFileVOs().add(jrFileVO);
		relatorioVO.getJrFileVOs().add(jrFileQuestImgVO);
		return relatorioVO;
	}

	/**
	 * Checa se o login que está sendo enviado já existe no sistema. Não faz nenhum tratamento, pois espera-se que o jsf bean já tenha tratado isso.
	 * Apenas para garantir que, por exemplo, senhas não sejam alteradas.
	 * 
	 * @param pessoaVO
	 */
	protected void checkExistUserName(PessoaVO pessoaVO) {
		
		if(pessoaVO==null || pessoaVO.getLogin()==null){
			throw new JazzRuntimeException("A pessoa ou o login informado são nulos!", new NullPointerException("npe forçado"));
		}
		
		String login = pessoaVO.getLogin();
		
		if(pessoaBusiness.existsLogin(login)){
			throw new JazzRuntimeException("O login informado ("+login+") já existe!");
		}
		
		
	}



	/**
	 * @param imageFile
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected BufferedImage loadImage(String imageFile) {
		InputStream is = this.getClass().getResourceAsStream(imageFile);

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}
	/**
	 * @param reportName TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws JRException
	 */
	protected byte[] getJasperReportBytes(String reportName)  {
		InputStream jasperCompiled = this.getClass().getResourceAsStream(reportName);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			CopiaArquivo.copy(jasperCompiled, baos, 1024);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar recuperar array de bytes do relatorio padrao!",e);
		}
		
		return baos.toByteArray();
	}
 	
}
