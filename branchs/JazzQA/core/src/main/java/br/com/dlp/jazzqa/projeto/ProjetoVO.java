package br.com.dlp.jazzqa.projeto;
import java.util.List;

import javax.persistence.Entity;

import br.com.dlp.jazzqa.base.AbstractEntityVO;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * Projeto da Companhia
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:20
 */
@Entity
@JazzClass(name="Projeto",description="Projeto de TI da companhia. Controles de históricos, envolvidos, obrigações e métricas!")
public class ProjetoVO extends AbstractEntityVO {
	
	private static final long serialVersionUID = 543991838869180090L;
	public TipoProjetoEnum tipoProjeto;
	public List<HistoricoVO> historicos;
	public List<EnvolvimentoVO> envolvidos;
	public List<ObrigacaoVO> obrigacoes;
	
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public ProjetoVO(){
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public ProjetoVO(final Long pk){
		super(pk);
	}
	
	@JazzProp(name="Tipo do Projeto", tip="Tipo do projeto.", renderer=JazzRenderer.COMBO)
	public TipoProjetoEnum getTipoProjeto() {
		return tipoProjeto;
	}
	
	@JazzProp(tip="Eventos históricos do projeto.", name = "Histórico",searchable=false)
	public List<HistoricoVO> getHistoricos() {
		return historicos;
	}
	
	@JazzProp(name="Envolvidos", tip="Envolvidos no projeto.",searchable=false)
	public List<EnvolvimentoVO> getEnvolvidos() {
		return envolvidos;
	}
	
	@JazzProp(name="Obrigações", tip="Obrigações do projeto.",searchable=false)
	public List<ObrigacaoVO> getObrigacoes() {
		return obrigacoes;
	}
	
	public void setTipoProjeto(TipoProjetoEnum tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}
	
	public void setHistoricos(List<HistoricoVO> historicos) {
		this.historicos = historicos;
	}
	
	public void setEnvolvidos(List<EnvolvimentoVO> envolvidos) {
		this.envolvidos = envolvidos;
	}
	
	public void setObrigacoes(List<ObrigacaoVO> obrigacoes) {
		this.obrigacoes = obrigacoes;
	}
	
}