package br.com.dlp.jazzav.anuncio;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.dlp.jazzav.AbstractLogEntityVO;
import br.com.dlp.jazzav.adesivo.AdesivoVO;
import br.com.dlp.jazzav.adesivo.CampoAdesivoVO;
import br.com.dlp.jazzav.person.PessoaVO;
import br.com.dlp.jazzav.produto.OpcionalVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * Registro de anuncio de veiculo
 * 
 * @author darcio
 * 
 */
@Entity
@JazzClass(name = "Anuncio")
public class AnuncioVO extends AbstractLogEntityVO<Long> {
	private static final long serialVersionUID = -3794244288485058756L;

	public static final Long PESO_PADRAO = 180l;
		
	private AdesivoVO adesivoVO = new AdesivoVO();
	private VeiculoVO veiculoVO = new VeiculoVO();
	private PessoaVO pessoaVO = new PessoaVO();

	private List<FotoVO> fotos;
	
	private Map<String, Boolean> showMap = new HashMap<String, Boolean>();
	private Double valor = 0d;
	private Double valorAnuncio = 0d;

	private String codigoConfirmacao;
	
	private Date dateInc;
	private Date dateAlt;
	private Boolean customOpc = true;
	
	private List<AnuncioStatusVO> historicoStatus = new ArrayList<AnuncioStatusVO>();
	
	/**
	 * Constroi obj anuncio com padroes de exibicao nas linhas do anuncio
	 */
	public AnuncioVO() {
		showMap.put("veiculoVO.combustivel", true);
		showMap.put("veiculoVO.cambio", false);
		showMap.put("descricao", true);
		showMap.put("veiculoVO.porta", true);
		showMap.put("valorF", true);
		showMap.put("veiculoVO.quilometragem", true);
		showMap.put("veiculoVO.strModFab", true);
		showMap.put("pessoaVO.nomeF", true);
		showMap.put("pessoaVO.telefone1", true);
		showMap.put("pessoaVO.telefone2", true);
	}
	
	@Transient
	public Map<String, String> getCampos() {
		
		Map<String, CampoAdesivoVO> mapCampos = adesivoVO.getCampos();
		
		Set<String> keys = mapCampos.keySet();
		
		Map<String,String> mapValores = new HashMap<String, String>();
		
		for (String key: keys) {
			
			String valorCampo = mapCampos.get(key).getValor();
			
			
			String value="";
			
			if(valorCampo!=null && !StringUtils.isBlank(valorCampo)){
				value=valorCampo;
			}else{
				value=adesivoVO.getPlaceholders().get(key);
			}
			
			
			mapValores.put(key, value);
			
		}
		
		String linhaAno = veiculoVO.getStrModFab();
		mapValores.put(adesivoVO.LINHA_ANO, linhaAno);
		
		return mapValores;
	}


	
	@Embedded
	public VeiculoVO getVeiculoVO() {
		return veiculoVO;
	}
	
	@Embedded
	public AdesivoVO getAdesivoVO() {
		return adesivoVO;
	}




	/**
	 * 
	 * @param opcionalVOs
	 * @param bufferLtd
	 * @param bufferLimit
	 * @return
	 */
	protected List<OpcionalVO> appendSiglas(List<OpcionalVO> opcionalVOs, StringBuffer bufferLtd, int bufferLimit) {
		
		String separator = " ";
		int i=0;
		for(; i<opcionalVOs.size() && (bufferLtd.length()+opcionalVOs.get(i).getSigla().length()+separator.length())<bufferLimit; i++){
			
			OpcionalVO opcionalVO = opcionalVOs.get(i);
			
			bufferLtd.append(opcionalVO.getSigla()+separator);
			
		}
		
		List<OpcionalVO> subList = opcionalVOs.subList(i, opcionalVOs.size());
		
		
		return subList;
	}



	@Transient
	public String getValorF() {

		valor = valor != null ? valor : 0;

		String valorF = "";

		if (valor > 0) {

			Locale locale = new Locale("pt", "BR");

			NumberFormat nf = NumberFormat.getInstance(locale);

			String formated = nf.format(valor.intValue());

			valorF = "R$" + (valor == null ? "0" : formated);

		}
		return valorF;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	@ManyToOne(cascade = { CascadeType.ALL })
	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}


	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	public List<FotoVO> getFotos() {
		return fotos;
	}

	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="anuncio_pk")
	@Fetch(FetchMode.SELECT)
	public List<AnuncioStatusVO> getHistoricoStatus() {
		return historicoStatus;
	}

	/**
	 * 
	 * @return
	 */
	@Transient
	public AnuncioStatusVO getAnuncioStatusVO() {

		List<AnuncioStatusVO> historico = getHistoricoStatus();

		AnuncioStatusVO anuncioStatusVO = null;

		if (CollectionUtils.isNotEmpty(historico)) {
			
			Comparator<? super AnuncioStatusVO> comparator = new BeanComparator("dtInc");
			anuncioStatusVO = Collections.max(historico, comparator);
			
		}

		return anuncioStatusVO;

	}

	
	public Double getValor() {
		return valor;
	}
	
	public Date getDateInc() {
		return dateInc;
	}

	public Date getDateAlt() {
		return dateAlt;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	
	public void setFotos(List<FotoVO> fotos) {
		this.fotos = fotos;
	}


	public void setValor(Double valor) {
		this.valor = valor;
	}


	public void setDateInc(Date dateInc) {
		this.dateInc = dateInc;
	}

	public void setDateAlt(Date dateAlt) {
		this.dateAlt = dateAlt;
	}

	@Transient
	public Map<String, Boolean> getShowMap() {
		return showMap;
	}

	public void setShowMap(Map<String, Boolean> showMap) {
		this.showMap = showMap;
	}


	public Boolean getCustomOpc() {
		return customOpc;
	}



	public void setCustomOpc(Boolean customOpc) {
		this.customOpc = customOpc;
	}


	public void setAdesivoVO(AdesivoVO adesivoVO) {
		this.adesivoVO = adesivoVO;
	}
	

	public void setVeiculoVO(VeiculoVO veiculoVO) {
		this.veiculoVO = veiculoVO;
	}

	public Double getValorAnuncio() {
		return valorAnuncio;
	}

	public void setValorAnuncio(Double valorAnuncio) {
		this.valorAnuncio = valorAnuncio;
	}

	public String getCodigoConfirmacao() {
		return codigoConfirmacao;
	}

	public void setCodigoConfirmacao(String codigoConfirmacao) {
		this.codigoConfirmacao = codigoConfirmacao;
	}

	public void setHistoricoStatus(List<AnuncioStatusVO> statusHistorico) {
		this.historicoStatus = statusHistorico;
	}

}
