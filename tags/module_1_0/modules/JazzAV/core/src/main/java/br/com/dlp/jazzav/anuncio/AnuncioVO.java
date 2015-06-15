package br.com.dlp.jazzav.anuncio;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;

import br.com.dlp.jazzav.produto.OpcionalVO;
import br.com.dlp.jazzav.produto.ProdutoVO;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * Registro de anuncio de veiculo
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Anuncio")
public class AnuncioVO {

	private PessoaVO pessoaVO;
	private ProdutoVO produtoVO;
	private List<OpcionalVO> opcionais;
	private List<FotoVO> fotos;
	
	private String descricao; 
	
	private CombustivelEnum combustivel;
	private Integer porta;
	private CambioEnum cambio;
	
	private Double valor;
	private Long quilometragem;
	private String cor;
	private String placa;
	private Integer anoFab;
	private Integer anoMod;
	private Date dateInc;
	private Date dateAlt;
	

	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}
	
	public ProdutoVO getProdutoVO() {
		return produtoVO;
	}
	
	public List<OpcionalVO> getOpcionais() {
		return opcionais;
	}
	
	public List<FotoVO> getFotos() {
		return fotos;
	}

	
	public CombustivelEnum getCombustivel() {
		return combustivel;
	}
	
	@Length(min=2,max=5)
	public Integer getPorta() {
		return porta;
	}
	
	public CambioEnum getCambio() {
		return cambio;
	}
	
	
	
	
	public String getDescricao() {
		return descricao;
	}
	public Double getValor() {
		return valor;
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
		return anoFab;
	}
	public Integer getAnoMod() {
		return anoMod;
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
	public void setProdutoVO(ProdutoVO produtoVO) {
		this.produtoVO = produtoVO;
	}
	public void setOpcionais(List<OpcionalVO> opcionais) {
		this.opcionais = opcionais;
	}
	public void setFotos(List<FotoVO> fotos) {
		this.fotos = fotos;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	public void setValor(Double valor) {
		this.valor = valor;
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
	public void setDateInc(Date dateInc) {
		this.dateInc = dateInc;
	}
	public void setDateAlt(Date dateAlt) {
		this.dateAlt = dateAlt;
	}
	

	
	
	
}
