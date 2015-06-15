package br.com.dlp.jazzav.anuncio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

import br.com.dlp.jazzav.produto.OpcionalVO;
import br.com.dlp.jazzav.produto.TipoOpcionalEnum;

@Embeddable
public class VeiculoVO implements Serializable {

	private static final long serialVersionUID = 4558428229888081756L;

	public static final String STR_SEPARA_ANO = "/";
	
	private Map<String, OpcionalVO[]> mapListOpcionais = new HashMap<String, OpcionalVO[]>(TipoOpcionalEnum.values().length);

	private CombustivelEnum combustivel = CombustivelEnum.FLEX;
	private CambioEnum cambio = CambioEnum.MANUAL;
	private CilindradaEnum cilindrada = CilindradaEnum._1_0;
	
	private Integer porta = 2;
	private Long quilometragem;
	private String cor = "";
	private String placa = "";
	private Integer anoFab;
	private Integer anoMod;

	private List<OpcionalVO> opcionais = new ArrayList<OpcionalVO>(40);
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	public List<OpcionalVO> getOpcionais() {
		return opcionais;
	}

	@Enumerated(EnumType.STRING)
	public CilindradaEnum getCilindrada() {
		return cilindrada;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public Map<String, OpcionalVO[]> getMappedList() {
		
		return mapListOpcionais;

	}
	@Enumerated(EnumType.ORDINAL)
	public CombustivelEnum getCombustivel() {
		return combustivel;
	}

	@Range(min = 2, max = 5)
	public Integer getPorta() {
		return porta;
	}


	public CambioEnum getCambio() {
		return cambio;
	}

	/**
	 * Monta String para ano Mod/Fab do veiculo
	 * 
	 * @return
	 */
	@Transient
	public String getStrModFab() {

		String strModFab = "";

		String anoFab = getAnoFab() + "";
		String anoMod = getAnoMod() + "";

		if (StringUtils.isNotBlank(anoMod) && StringUtils.isNotBlank(anoFab)
				&& anoMod.length() == 4 && anoFab.length() == 4) {

			strModFab = anoFab.substring(2) + STR_SEPARA_ANO
					+ anoMod.substring(2);

		}

		return strModFab;
	}

	public Long getQuilometragem() {
		return quilometragem;
	}

	public String getCor() {
		return cor;
	}

	public String getPlaca() {
		return placa;
	}

	public Integer getAnoFab() {

		if (anoFab == null) {
			int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
			anoFab = anoAtual;
		}

		return anoFab;
	}

	public Integer getAnoMod() {
		if (anoMod == null) {
			int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
			anoMod = anoAtual;
		}
		return anoMod;
	}
	
	

	public void setQuilometragem(Long quilometragem) {
		this.quilometragem = quilometragem;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setAnoFab(Integer anoFab) {
		this.anoFab = anoFab;
	}

	public void setAnoMod(Integer anoMod) {
		this.anoMod = anoMod;
	}


	public void setOpcionais(List<OpcionalVO> opcionais) {
		this.opcionais = opcionais;
	}


	/**
	 * Preenche itens do mapa com os elementos de 
	 * @param opcionais TODO
	 * @return
	 */
	protected Map<String, List<OpcionalVO>> fillArrayList(List<OpcionalVO> opcionais) {
		Map<String, List<OpcionalVO>> mapLstOpcionais = new TreeMap<String, List<OpcionalVO>>();

		for (OpcionalVO opcionalVO : opcionais) {
			TipoOpcionalEnum tipoOpcionalEnum = opcionalVO.getTipoOpcional();
			
			List<OpcionalVO> listOpc = mapLstOpcionais.get(tipoOpcionalEnum+"");
			
			//se nao existe lista registrada no mapa cria uma e registra!
			if(listOpc==null){
				listOpc = new ArrayList<OpcionalVO>();
				mapLstOpcionais.put(tipoOpcionalEnum+"", listOpc);
			}
			
			listOpc.add(opcionalVO);
		}
		return mapLstOpcionais;
	}


	/**
	 * Transforma mapa de lista de opcionais para mapa de array de opcionais. Compatibilizar com JSF escroto!
	 * @param mapLstOpcionais
	 * @return
	 */
	protected Map<String, OpcionalVO[]> toMapArray(Map<String, List<OpcionalVO>> mapLstOpcionais) {
		
		Map<String, OpcionalVO[]> mapArrOpcionais = new TreeMap<String, OpcionalVO[]>();
		
		for (String tipoOpcionalEnum : mapLstOpcionais.keySet()) {
			
			OpcionalVO[] opcionalVOs = mapLstOpcionais.get(tipoOpcionalEnum).toArray(new OpcionalVO[0]);
			
			mapArrOpcionais.put(tipoOpcionalEnum, opcionalVOs);
		}
		
		return mapArrOpcionais;
		
	}

	public void setCombustivel(CombustivelEnum combustivel) {
		this.combustivel = combustivel;
	}

	public void setPorta(Integer porta) {
		this.porta = porta;
	}

	public void setCambio(CambioEnum cambio) {
		this.cambio = cambio;
	}


	public void setCilindrada(CilindradaEnum cilindrada) {
		this.cilindrada = cilindrada;
	}

}
