package com.msaf.validador.resulcons;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;



/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

//@XmlRootElement(name="ResultadosConsulta")
@XmlRootElement(name="ResultadoConsulta")
public class ResulConsVO  extends AbstractCadastroBaseVO{
	private String versao;
	private Integer quantidade;
	//private String retornoXML;  // excluir depois, apenas para teste até 23/01/09 FIXME: EXCLUIR ESSA GAMBI AQUI!!!
	
	//private Collection<DadosRetInstVO> dadosRetornoInstituicaoVOS = new ArrayList<DadosRetInstVO>();
	private Set dadosRetornoInstituicaoVOS = new HashSet();

	
	/**
	 * @wj2ee ordem="10" somenteLeitura="true" descricao="Código"
	 *        pesquisavel="true" listavel="true" pk="true"
	 * 
	 * @hibernate.id generator-class="increment"
	 */
	public Integer getId() {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		return abstractJazzQAPK.getId();
	}




	public void setId(Integer id) {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		abstractJazzQAPK.setId(id);
	}
	public Set getRegistrosRetorno() {
		return dadosRetornoInstituicaoVOS;
    }

	
//	public Collection<RegistroPessoaVO> getRegistrosRetorno() {
//		return registrosRetorno;
//	}

	@XmlAttribute(name="Versao")
	public String getVersao() {
		return versao;
	}

	@XmlAttribute(name="Quantidade")
	public Integer getQuantidade() {
		return quantidade;
	}
	
//	public String getRetornoXML() {
//		return retornoXML;
//	}
//
//	public void setRetornoXML(String retornoXML) {
//		this.retornoXML = retornoXML;
//	}
//	
	public void setVersao(String versao) {
		this.versao = versao;
	}
	
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
//	public void setRegistrosRetorno(
//			Collection<RegistroPessoaVO> registrosRetorno) {
//		this.registrosRetorno = registrosRetorno;
//	}

	
	//public void setRegistrosRetorno(Collection<DadosRetInstVO> dadosRetornoInstituicaoVOs) {
	public void setRegistrosRetorno(Set dadosRetornoInstituicaoVOs) {
		this.dadosRetornoInstituicaoVOS = dadosRetornoInstituicaoVOs;
	}

}
