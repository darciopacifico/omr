package br.com.dlp.framework.vowrapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import br.com.dlp.framework.vo.IBaseVO;

/**
 * Wrapper class para IBaseVO.<br>
 * <b>Atencão!! É extremamente importante que os Wrappers não guardem o estado
 * dos VOs (atributos, referencias a outros VOs)!!</b>
 */
public interface IVOWrapper extends Serializable {

	public ResourceBundle getResourceBundle();

	public void setResourceBundle(ResourceBundle resourceBundle);

	public int getIndexOf(List list, IBaseVO baseVO);

	public IBaseVO getBaseVO(List list, int indexOf);

	public Object convert(String string, Class class1);

	public String convert(Object object);

	public IBaseVO getBaseVO();

	public void setBaseVO(IBaseVO baseVO);

	public List generateVOWrapperList(Collection collection,
			Class voWrapperClass) throws VOWrapperException;

	public IVOWrapper generateVOWrapper(Object object, Class voWrapperClass)
			throws VOWrapperException;

	public List generateBaseVOList(Collection list, Class voWrapperClass)
			throws VOWrapperException;

	public IBaseVO getBaseVO(IVOWrapper wrapper);

	public boolean isSelecionado();

	public void setSelecionado(boolean selecionado);

}