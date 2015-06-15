/**
 * 
 */
package com.msaf.validador.consultaonline;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * @author abborges
 *
 */
public class RuntimeTest {
	
	private Logger log = Logger.getLogger(RuntimeTest.class);

	@Test
	public void testCommand() throws IOException{
		Process p = Runtime.getRuntime().exec("java -version");
		if(log.isDebugEnabled()) log.debug(p);
	}

}
