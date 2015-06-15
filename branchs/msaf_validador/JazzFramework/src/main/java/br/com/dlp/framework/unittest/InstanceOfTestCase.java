/*
 * Created on 25/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

import br.com.dlp.framework.dao.AbstractJDBCDAO;
import br.com.dlp.framework.dao.IDAO;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InstanceOfTestCase {

	public static void main(String[] args) {
		/* O pai reconhece o filho, mas nao o contrario */
		System.out.println(IDAO.class.isAssignableFrom(AbstractJDBCDAO.class));

		/* O pai reconhece o filho, mas nao o contrario */
		System.out.println(AbstractJDBCDAO.class.isAssignableFrom(IDAO.class));

	}
}
