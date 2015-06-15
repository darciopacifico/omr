package br.com.dlp.jazzav.anuncio;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import br.com.dlp.jazzav.person.PessoaVO;

/**
 * Especialização de bean wrapper que checa se a propriedade pode ser exibida
 * @author darcio
 *
 */
public class BeanWrapperAdesivo extends BeanWrapperImpl {

	private AnuncioVO anuncioVO;


	public BeanWrapperAdesivo(AnuncioVO anuncioVO) {
		super(anuncioVO);
		
		this.anuncioVO = anuncioVO;
	}

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		AnuncioVO anuncioVO = new AnuncioVO();
		
		anuncioVO.getShowMap().put("pessoaVO.nome", false);
		
		PessoaVO pessoaVO = new PessoaVO();
		
		anuncioVO.setPessoaVO(pessoaVO);
		
		pessoaVO.setNome("darcio");
		
		BeanWrapper wrapper = new BeanWrapperAdesivo(anuncioVO);
		
		System.out.println(wrapper.getPropertyValue("pessoaVO.nome"));
		
	}
	
	
	/**
	 * Verifica no show mapListOpcionais se a propriedade deve ser acessada ou não
	 */
	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		
		Map<String, Boolean> showMap = anuncioVO.getShowMap();
		
		Boolean toShow = showMap.get(propertyName);
		
		Object propertyValue;
		if(toShow==null || toShow){
			propertyValue = super.getPropertyValue(propertyName);
		}else{
			propertyValue = "";
		}
		
		return propertyValue;
	}

	/**
	 * Apenas uma versão para deixar o svg menos verboso
	 * @param propertyName
	 * @return
	 */
	public Object p(String propertyName){
		return getPropertyValue(propertyName);
	}
	

	public AnuncioVO getAnuncioVO() {
		return anuncioVO;
	}

	public void setAnuncioVO(AnuncioVO anuncioVO) {
		this.anuncioVO = anuncioVO;
	}
	
}