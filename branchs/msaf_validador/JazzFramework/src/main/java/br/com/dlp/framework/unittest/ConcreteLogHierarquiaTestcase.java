/*
 * Created on 06/08/2005
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
public class ConcreteLogHierarquiaTestcase extends
		AbstractLogHierarquiaTestCase {
	public void testarLog() {
		logger.debug("LLLLOOOOGGGGGG");
	}

	public static void main(String[] args) {

		new ConcreteLogHierarquiaTestcase().testarLog();

	}
}
