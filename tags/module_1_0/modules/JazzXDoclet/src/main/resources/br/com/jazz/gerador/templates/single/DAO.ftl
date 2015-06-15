<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * <@gc.comentarioClasse/>
 *
 * Contrado de DAO para o componente ${domain.name}
 *
 **/
<@gc.classSignature type="interface"/>{

<@gc.isCriarFind><#--testa se há atributos pesquisáveis-->


	<@gc.comentariosFind/>
	<@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>);<#--cria metodo com os atributos pesquisaveis do VO-->

	<@gc.comentariosFind/>
	Long <@gc.metodoCount/>(<@gc.argumentosFind/>);<#--cria metodo com os atributos pesquisaveis do VO-->

	<@gc.comentariosFind>
	 * @param QueryOrder Ordenação de pesquisa
	</@gc.comentariosFind>
	<@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>, ExtraArgumentsDTO extraArgumentsDTO);<#--cria metodo com os atributos pesquisaveis do VO mais ordenação-->

	
</@gc.isCriarFind>
}

