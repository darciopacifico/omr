/**
 * 
 */
package br.com.dlp.jazzomr.results;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.web.jsf.FacesContextUtils;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.event.EventBusiness;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * 
 * DTO para implementação de tela de resultados de criterions questoes
 * @author darcio
 * 
 */
public class CriterionDetailDTO implements IBaseVO<String> {

	public static final double COEF_AJUSTE_ALTURA = 1.28;

	private String pk;
	
	public static final int ALTURA_POPUP_CORRECAO = 104;

	private static final long serialVersionUID = -4911161866163360013L;
	
	private Integer correctionBoxHeight = 150;

	private EventVO eventVO;
	private ParticipationVO participationVO;
	private ExamVariantVO examVariantVO;
	private ExamVO examVO;
	private QuestionnaireVO questionnaireVO;
	
	private QuestionVO questionVO;

	private AbstractCriterionVO criterionVO;
	private CriterionCoordinateVO criterionCoordinateVO = null;
	private CriterionResultVO criterionResultVO = null;

	private Map<Integer,PayloadVO> payloadVOs = new HashMap<Integer, PayloadVO>(3);
	
	
	/** Construtor padrao */
	public CriterionDetailDTO() {
		
	}

	public CriterionDetailDTO(EventVO eventVO, ParticipationVO participationVO, ExamVariantVO examVariantVO, ExamVO examVO, QuestionVO questionVO,
			CriterionCoordinateVO criterionCoordinateVO, AbstractCriterionVO criterionVO, CriterionResultVO criterionResultVO) {
		super();
		this.eventVO = eventVO;
		this.participationVO = participationVO;
		this.examVariantVO = examVariantVO;
		this.examVO = examVO;
		this.questionVO = questionVO;
		this.criterionCoordinateVO = criterionCoordinateVO;
		this.criterionVO = criterionVO;
		this.criterionResultVO =criterionResultVO;
	}	

	

	public CriterionDetailDTO(
			EventVO eventVO, ParticipationVO participationVO, ExamVariantVO examVariantVO, ExamVO examVO,	QuestionVO questionVO,
			CriterionCoordinateVO criterionCoordinateVO, AbstractCriterionVO criterionVO, 
			CriterionResultVO criterionResultVO, Map<Integer,PayloadVO> payloadVOs) {
		super();
		this.eventVO = eventVO;
		this.participationVO = participationVO;
		this.examVariantVO = examVariantVO;
		this.examVO = examVO;
		this.questionVO = questionVO;
		this.criterionCoordinateVO = criterionCoordinateVO;
		this.criterionVO = criterionVO;
		this.criterionResultVO = criterionResultVO;
		this.payloadVOs = payloadVOs;
	}	


	public CriterionDetailDTO(ExamVO examVO, ExamVariantVO examVariantVO, QuestionVO questionVO, ParticipationVO participationVO) {
		super();
		this.examVO = examVO;
		this.examVariantVO = examVariantVO;
		this.questionVO = questionVO;
		this.participationVO = participationVO;
	}	


	/**
	 * @param criterionCoordinateVO
	 * @param screenImgHeight
	 * @param coefH
	 * @return
	 */
	protected int getPosicaoY(CriterionCoordinateVO criterionCoordinateVO, double screenImgHeight) {
		double coefH = calcularCoefAjusteAltura(screenImgHeight);
		
		//calcula  o raio da particula de referencia da imagem
		Double radiusImagem = getRadiusImagem(screenImgHeight);
		
		//recupera o raio da particula de referencia do gabarito
		Double radiusExamRef = getRadiusExamRef();
		
		double y = (double)criterionCoordinateVO.getY()-radiusExamRef;
		
		int yPosi = (int)(y/coefH);

		//coeficiente de ajuste, propocional ao zoom da imagem na tela
		
		yPosi = (int) (yPosi + radiusImagem);
		return yPosi;
	}


	/**
	 * @param screenImgHeight
	 * @param radiusImagem
	 * @return
	 */
	protected double calcularCoefAjusteAltura(double screenImgHeight) {
		
		Double radiusImagem = getRadiusImagem(screenImgHeight);
		
		//recupera a distancia y entre os pontos de referencia do gabarito
		double distRefY = getDistRefY();
		
		//recupera a distancia y entre os pontos de referencia da imagem digitalizada
		double distImgY = calculateDistYImg(screenImgHeight, radiusImagem);
		
		
		//calcula a proporcao para ajuste das coordenadas
		double coefH = (distRefY/distImgY);
		return coefH;
	}


	/**
	 * @param screenImgHeight
	 * @param radiusImagem
	 * @return
	 */
	protected double calculateDistYImg(double screenImgHeight, Double radiusImagem) {
		return screenImgHeight - (2*radiusImagem);
	}


	/**
	 * @param screenImgHeight
	 * @return
	 */
	protected double getRadiusImagem(double screenImgHeight) {
		return screenImgHeight * (84d/2490d);
	}


	/**
	 * @return
	 */
	protected double getRadiusExamRef() {
		return getExamVO().getRelatorioVO().determineTopLeftCorner().getX();
	}


	/**
	 * @return
	 */
	protected double getDistRefY() {
		java.awt.geom.Point2D.Double tlPoint = getExamVO().getRelatorioVO().determineTopLeftCorner();
		java.awt.geom.Point2D.Double brPoint = getExamVO().getRelatorioVO().determineBottomRightCorner();
		double distRefY = (brPoint.getY()-tlPoint.getY());
		return distRefY;
	}
	
	

	/**
	 * Retorna as coordenadas para criação do mapeamento da imagem do exame. Normalmente invocado diretamente da tela
	 * @param criterionCoordinateVO
	 * @return
	 */
	public String correctionPopupYPosition(double screenImgHeight){
		
		if(!(criterionCoordinateVO.getAbstractCriterionVO() instanceof DissertativeVO)){
			return "";
		}
		
		//posicao y da regiao do link
		int y1Link = getPosicaoY(criterionCoordinateVO, screenImgHeight);
		
		
		//posicao y do popup, logo acima do link 
		String yPos = ""+((int)(y1Link-screenImgHeight-(correctionBoxHeight*CriterionDetailDTO.COEF_AJUSTE_ALTURA)));
		return yPos;
	}
	
	
	public Boolean isLoaded(){
		return getPayloadVO()!=null;
	}

	public Boolean isPayloadFetched(){
		return getPayloadVO()!=null && getPayloadVO().getImageVO()!=null;
	}


	private PayloadVO getPayloadVO() {
		
		Map<Integer, PayloadVO> mapPayload = getPayloadVOs();
		
		PayloadVO payloadVO = mapPayload.get(criterionCoordinateVO.getPagina());
		
		return payloadVO;
	}
	

	/**
	 * @param criterionCoordinateVO
	 * @param payloadVO
	 * @return
	 */
	protected StreamedContent streamDissertativeImg(CriterionCoordinateVO criterionCoordinateVO, PayloadVO payloadVO) {
		StreamedContent streamedContent;
		
		AbstractImageVO imageVO = payloadVO.getImageVO();
		
		streamedContent = toStreamedContent(imageVO.getImage());
		
		return streamedContent;
		
	}
	

	/**
	 * @param criterionCoordinateVO
	 * @param payloadVO
	 * @return
	 */
	protected StreamedContent streamAlternativeImg(CriterionCoordinateVO criterionCoordinateVO, PayloadVO payloadVO) {
		StreamedContent streamedContent;
		
		AbstractImageVO imageVO = payloadVO.getImageVO();
		streamedContent = toStreamedContent(imageVO.getImage());
		return streamedContent;
	}

	/**
	 * @param criterionCoordinateVO
	 * @return
	protected BufferedImage getBufferedImage(CriterionCoordinateVO criterionCoordinateVO) {
		
		
		AbstractImageVO imageVO = getPayloadVO().getImageVOs().get(0);

		BufferedImage buffImg = imageVO.getImage();
		return buffImg;
	}
	 */

	/**
	public StreamedContent dumbStreamedContent(StreamedContent streamedContent) {

		BufferedImage bi;
		try {
			bi = ImageIO.read(new File("/home/darcio/250H.jpg"));
		} catch (IOException e1) {
			return null;
		}

		bi = bi.getSubimage(0, 0, bi.getWidth() / 2, bi.getHeight() / 2);

		streamedContent = toStreamedContent(bi);

		return streamedContent;
	}
	 */

	/**
	 * Transforma imagens contidas em BufferedImagens em StreamedContent, para uso de p:GraphicImage do primefaces.
	 * @param bi
	 * @return
	 */
	protected StreamedContent toStreamedContent(BufferedImage bi) {
		StreamedContent streamedContent;
		DefaultStreamedContent content = new DefaultStreamedContent();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "gif", baos);
		} catch (IOException e1) {
			throw new JazzRuntimeException("Erro ao tentar escrever imagem de saida!", e1);
		}

		byte[] imgBytes = baos.toByteArray();

		streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imgBytes), "image/jpg", "xxx.jpg");
		return streamedContent;
	}


	public ExamVO getExamVO() {
		return examVO;
	}

	public ExamVariantVO getExamVariantVO() {
		return examVariantVO;
	}

	public QuestionnaireVO getQuestionnaireVO() {
		return questionnaireVO;
	}

	public QuestionVO getQuestionVO() {
		return questionVO;
	}

	public void setExamVO(ExamVO examVO) {
		this.examVO = examVO;
	}

	public void setExamVariantVO(ExamVariantVO examVariantVO) {
		this.examVariantVO = examVariantVO;
	}

	public void setQuestionnaireVO(QuestionnaireVO questionnaireVO) {
		this.questionnaireVO = questionnaireVO;
	}

	public void setQuestionVO(QuestionVO questionVO) {
		this.questionVO = questionVO;
	}


	public ParticipationVO getParticipationVO() {
		return participationVO;
	}

	public void setParticipationVO(ParticipationVO participationVO) {
		this.participationVO = participationVO;
	}


	/**
	 * Posiciona na primeira questao dissertativa
	 * 
	 * @return
	 */
	public CriterionCoordinateVO getCriterionCoordinateVO() {
		return this.criterionCoordinateVO;
	}


	public void setCriterionCoordinateVO(CriterionCoordinateVO selectedCriterion) {
		this.criterionCoordinateVO = selectedCriterion;
	}


	public CriterionResultVO getCriterionResultVO() {
		if(this.criterionResultVO==null){
			this.criterionResultVO = new CriterionResultVO();
			this.criterionResultVO.setCriterionCoordinateVO(getCriterionCoordinateVO());
			this.criterionResultVO.setParticipationVO(participationVO);
		}
		return this.criterionResultVO;
	}


	public void setCriterionResultVO(CriterionResultVO criterionResultVO) {
		this.criterionResultVO = criterionResultVO;
	}



	public AbstractCriterionVO getCriterionVO() {
		return criterionVO;
	}



	public void setCriterionVO(AbstractCriterionVO criterionVO) {
		this.criterionVO = criterionVO;
	}

	public EventVO getEventVO() {
		return eventVO;
	}

	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}

	
	@Override
	public String getPK() {
		return getParticipationVO().getPK()+"."+this.getQuestionVO().getPK()+"."+getCriterionVO().getPK();
	}
	@Override
	public void setPK(String pk) {
		this.pk = pk;
	}
	

	@Override
	public int hashCode() {
		int hashCode = new HashCodeBuilder().append(getParticipationVO()).append(getQuestionVO()).append(getCriterionVO()).toHashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this==obj){
			return true;
		}
		
		if(obj==null){
			return false;
		}
		
		if(!(obj instanceof CriterionDetailDTO)){
			return false;
		}

		CriterionDetailDTO criterionDetailDTO = (CriterionDetailDTO) obj;
		
		
		return new EqualsBuilder().
			append(this.getParticipationVO(), 	criterionDetailDTO.getParticipationVO()).
			append(this.getQuestionVO(), 				criterionDetailDTO.getQuestionVO()).
			append(this.getCriterionVO(), 			criterionDetailDTO.getCriterionVO()).
			isEquals();
		
	}

	public Integer getCorrectionBoxHeight() {
		return correctionBoxHeight;
	}

	public void setCorrectionBoxHeight(Integer correctionBoxHeight) {
		this.correctionBoxHeight = correctionBoxHeight;
	}

	@Transient
	@Override
	public boolean isNew() {
		return false;
	}

	public Map<Integer, PayloadVO> getPayloadVOs() {
		return payloadVOs;
	}

	public void setPayloadVOs(Map<Integer, PayloadVO> payloadVOs) {
		this.payloadVOs = payloadVOs;
	}
	
}
