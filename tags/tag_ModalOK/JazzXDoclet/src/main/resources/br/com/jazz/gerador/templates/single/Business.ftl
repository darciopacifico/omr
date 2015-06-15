<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.business.IBusiness;


/**
 * <@gc.comentarioClasse/>
 *
 * Contrado de Business para o componente ${domain.name}
 *
 **/
<@gc.classSignature type="interface" /> {

<@gc.isCriarFind><#--testa se há atributos pesquisáveis-->
	<@gc.comentariosFind/>
	public <@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>);<#--cria metodo com os atributos pesquisaveis do VO-->

	<@gc.comentariosFind>
	 * @param QueryOrder Ordenação de pesquisa
	</@gc.comentariosFind>
	public <@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>, QueryOrder... queryOrders);<#--cria metodo com os atributos pesquisaveis do VO mais ordenação-->
	
</@gc.isCriarFind>
}