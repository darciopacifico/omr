package br.com.dlp.jazzqa.base;

import br.com.dlp.framework.dao.AbstractJDBCDAO;
import br.com.dlp.jazzqa.util.servicelocator.JazzQAServiceLocator;

/**
 * Implementação base de JDBCDAO para o sistema JazzQA
 */
public abstract class AbstractJazzQAJDBCDAO extends AbstractJDBCDAO {

	/**
	 * DataSourceName
	 */
	protected String getDefaultDataSouceName() {
		return JazzQAServiceLocator.PROPERTIE_JAZZQA_DS;
	}

}