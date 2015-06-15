/**

 * 
 */
package br.com.dlp.jazzomr.acompanhamento;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.jsf.AbstractJSFBeanImpl;
import br.com.dlp.jazzav.anuncio.AnuncioBusiness;
import br.com.dlp.jazzav.anuncio.AnuncioVO;
import br.com.dlp.jazzav.anuncio.ModeloAdesivoVO;
import br.com.dlp.jazzav.opcionais.OpcionalBusiness;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.produto.TipoOpcionalEnum;

/**
 * Managed bean responsável pelo controle e conversacao da edicao do anuncio
 * @author darcio
 */
@ManagedBean
@SessionScoped
@Scope(value="session")
@Component
public class AcompanhamentopBean extends AbstractJSFBeanImpl<AnuncioVO> {

	//09EE605E38374CB4B23E704CDCD7D8D2
	protected static final Integer PAGE_ADESIVO = new Integer(0);
	private static final long serialVersionUID = 1831552858889190283L;
	protected final int yearListSize = 80;
	protected final int anoIncModelo = 1;

	protected AnuncioVO anuncioVO = new AnuncioVO();

	FacesContext context = FacesContext.getCurrentInstance();

	Map<TipoOpcionalEnum, List<SelectItem>> mapOpcionais;
	protected Map<Long, ModeloAdesivoVO> mapaModelos = new HashMap<Long, ModeloAdesivoVO>();
	
	@Autowired
	protected AnuncioBusiness anuncioBusiness;
	
	@Autowired
	protected OpcionalBusiness opcionalBusiness;
	
	@Autowired
	private PessoaBusiness pessoaBusiness;
	
	@Autowired
	@Qualifier("jazzAuthManager")
	protected AuthenticationManager authenticationManager;
	

	/**
	 * 
	 * @return
	 */
	public AnuncioVO getAnuncioVO(){
		
		if(anuncioVO==null || anuncioVO.getPK()==null){
			//pesquisa o anuncio a partir do pagseguro
			//TODO: configurar cache aqui
						
			try {
				anuncioVO = findAnuncioByPK();
			} catch (Exception e) {
				
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pedido não encontrado! Verifique o código do pedido informado!", ""));
			
			}
		}
				
		return anuncioVO;
	}




	/**
	 * @return
	 * @throws JazzBusinessException 
	 */
	protected AnuncioVO findAnuncioByPK() throws JazzBusinessException {
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		FacesContext context = FacesContext.getCurrentInstance();
		
		String id_pagseguro = req.getParameter("anuncioPK");
		
		if(StringUtils.isBlank(id_pagseguro) || !StringUtils.isNumeric(id_pagseguro)){
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "O códigode pedido informado não é válido!!", ""));
			throw new JazzBusinessException("O codigo de pedido informado nao e valido!");
			
		}
		
		Long anuncioPK = new Long(id_pagseguro);
		
		AnuncioVO anuncioVO=new AnuncioVO();
		anuncioVO = anuncioBusiness.findByPK(anuncioPK);
		
		return anuncioVO;
	}

	
	
		
	@Override
	public List<? extends Enum> autorizeAnyOf() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String actionPesquisar() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String[] getCamposLimparPesquisa() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected IBusiness<AnuncioVO> getBusiness() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
