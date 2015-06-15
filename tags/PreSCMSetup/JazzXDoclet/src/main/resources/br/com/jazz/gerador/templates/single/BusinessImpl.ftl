<@gc.comentarioArquivoJava/>
${domain.package};

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * <@gc.comentarioClasse/>
 *
 * Contrado de Business para o componente ${domain.name}
 *
 **/
@Component
@WebService
<@gc.classSignature type="class" implements='${gc.getComponentName("Business")}' /> {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private <@gc.componentName "DAO" /> <@gc.componentVarName "DAO" />;

<@gc.isCriarFind><#--testa se há atributos pesquisáveis-->
	<@gc.comentariosFind/>
	public <@gc.findReturns/> <@gc.metodoFind/>(<@gc.argumentosFind/>){<#--cria metodo com os atributos pesquisaveis do VO-->
		return <@gc.componentVarName "DAO" />.<@gc.metodoFind/>(<@gc.argumentosFind type=false/>);
	}

	<@gc.comentariosFind>
	 * @param QueryOrder Ordenação de pesquisa
	</@gc.comentariosFind>
	public <@gc.findReturns/> <@gc.metodoFind true/>(<@gc.argumentosFind/>, QueryOrder... queryOrders){<#--cria metodo com os atributos pesquisaveis do VO mais ordenação-->
		return <@gc.componentVarName "DAO" />.<@gc.metodoFind/>(<@gc.argumentosFind type=false/>, queryOrders);
	}
	
</@gc.isCriarFind>

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<<@gc.componentName "VO" />> getDao() {
		return <@gc.componentVarName "DAO" />;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public ${domain.name} newVO() {
		return new ${domain.name}();
	}
	
}