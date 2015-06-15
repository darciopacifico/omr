package br.com.dlp.framework.wsi;

import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

public class MockWSSOutInterceptor extends WSS4JOutInterceptor {
	
	public MockWSSOutInterceptor() {
		super();
		setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, "br.com.dlp.framework.wsi.MockPasswordCallbackClient");
		setProperty(WSHandlerConstants.USER, "usuario");
		
	}
}
