/*
 * Created on 27/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GetInterfacesTestCase {

	public static void main(String[] args) {
		FakeOracleDAO fakeOracleDAO = new FakeOracleDAO();
		fakeOracleDAO.getClass().getInterfaces();
	}
}
