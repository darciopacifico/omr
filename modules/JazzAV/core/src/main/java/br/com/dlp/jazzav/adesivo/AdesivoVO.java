package br.com.dlp.jazzav.adesivo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.dlp.jazzav.anuncio.ModeloAdesivoVO;

/**
 * VO para abstração de um adesivo, com o conteúdo e modelo do mesmo
 * 
 * @author dpacifico
 * 
 */
// @Entity
@Embeddable
public class AdesivoVO implements Serializable /* extends AbstractEntityVO<Long> virou embeddable */{

	private static final long serialVersionUID = 7350383558311553100L;

	public static final String LINHA_ANO			 	= "linhaAno";
	public static final String LINHA_SUBTITULO 	= "linhaSubTitulo";
	public static final String LINHA_OPCIONAIS 	= "linhaOpcionais";
	public static final String LINHA_CONTATOS	 	= "linhaContatos";
	public static final String LINHA_LINK 			= "linhaLink";
	public static final String LINHA_PLACA 			= "linhaPlaca";

	protected Map<String, String> placeholders = new HashMap<String, String>(3);
	
	private ModeloAdesivoVO modeloAdesivoVO = null;
	private Map<String, CampoAdesivoVO> campos = new HashMap<String, CampoAdesivoVO>();

	public AdesivoVO() {
		campos.put(LINHA_SUBTITULO	, new CampoAdesivoVO(LINHA_SUBTITULO));
		campos.put(LINHA_OPCIONAIS	, new CampoAdesivoVO(LINHA_OPCIONAIS));
		campos.put(LINHA_CONTATOS		, new CampoAdesivoVO(LINHA_CONTATOS	));

		campos.put(LINHA_ANO				, new CampoAdesivoVO(LINHA_ANO			));
		campos.put(LINHA_LINK				, new CampoAdesivoVO(LINHA_LINK			));
		campos.put(LINHA_PLACA			, new CampoAdesivoVO(LINHA_PLACA		));

		
		placeholders.put(AdesivoVO.LINHA_SUBTITULO, "1.0 Flex R$12.000");
		placeholders.put(AdesivoVO.LINHA_OPCIONAIS, "Ar Direção Trio Único Dono");
		placeholders.put(AdesivoVO.LINHA_CONTATOS , "(11)99999-9999\n(11)98888-8888");

		placeholders.put(AdesivoVO.LINHA_ANO	, "00/00");
		placeholders.put(AdesivoVO.LINHA_LINK	, "");
		placeholders.put(AdesivoVO.LINHA_PLACA, "");

	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="adesivo")
	@MapKey(name = "chave" )
	
	public Map<String, CampoAdesivoVO> getCampos() {
		return campos;
	}

	@ManyToOne
	public ModeloAdesivoVO getModeloAdesivoVO() {
		return modeloAdesivoVO;
	}

	public void setCampos(Map<String, CampoAdesivoVO> campos) {
		this.campos = campos;
	}

	public void setModeloAdesivoVO(ModeloAdesivoVO modeloAdesivoVO) {
		this.modeloAdesivoVO = modeloAdesivoVO;
	}

	@Transient
	public Map<String, String> getPlaceholders() {
		placeholders.put(AdesivoVO.LINHA_SUBTITULO, "1.0 Flex R$12.000");
		placeholders.put(AdesivoVO.LINHA_OPCIONAIS, "Ar Direção Trio Único Dono");
		placeholders.put(AdesivoVO.LINHA_CONTATOS , "(11)99999-9999\n(11)98888-8888");

		placeholders.put(AdesivoVO.LINHA_ANO	, "00/00");
		placeholders.put(AdesivoVO.LINHA_LINK	, "");
		placeholders.put(AdesivoVO.LINHA_PLACA, "");
		return placeholders;
	}

	public void setPlaceholders(Map<String, String> placeholders) {
		this.placeholders = placeholders;
	}
}
