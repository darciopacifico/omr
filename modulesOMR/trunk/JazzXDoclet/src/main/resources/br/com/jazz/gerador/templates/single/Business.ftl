<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.business.IBusiness;


/**
 * <@gc.comentarioClasse/>
 *
 * Contrado de Business para o componente ${domain.name}
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
<@gc.classSignature type="interface" /> {




<@gc.isCriarFind><#--testa se há atributos pesquisáveis-->


	<@gc.comentariosFind/>
	@WebMethod
	<@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>);<#--cria metodo com os atributos pesquisaveis do VO-->


	<@gc.comentariosFind/>
	@WebMethod
	Long <@gc.metodoCount/>(<@gc.argumentosFind/>);<#--cria metodo com os atributos pesquisaveis do VO-->


	<@gc.comentariosFind>
	 * @param QueryOrder Ordenação de pesquisa
	</@gc.comentariosFind>
	@WebMethod
	<@gc.findReturns/> <@gc.metodoFind true/>(<@gc.argumentosFind/>, ExtraArgumentsDTO extraArgumentsDTO);<#--cria metodo com os atributos pesquisaveis do VO mais ordenação-->
	
</@gc.isCriarFind>
}