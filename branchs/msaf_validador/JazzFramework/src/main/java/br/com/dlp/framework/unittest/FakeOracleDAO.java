/*
 * Created on 25/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FakeOracleDAO implements FakeDAO {

	public Collection findAllXYZ() {

		List list = new ArrayList();

		list.add("Darcio");
		list.add("Suellen");
		list.add("Ariane");
		list.add("NaoLembro");
		list.add("Erika");

		return list;
	}

}
