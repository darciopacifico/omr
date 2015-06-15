package br.com.dlp.jazzqa.base;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.AbstractValueListHandlerFacadeImpl;
import br.com.dlp.jazzqa.util.servicelocator.JazzQAServiceLocator;

public abstract class AbstractJazzQAVLHFacadeImpl extends
		AbstractValueListHandlerFacadeImpl {

	protected Class getServiceLocatorClass() throws BaseTechnicalError {
		return JazzQAServiceLocator.class;
	}
}