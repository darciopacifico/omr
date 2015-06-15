package com.msaf.validador.consumer;

import javax.jms.MessageListener;

import com.msaf.framework.utils.DllDadosDTO;

public interface IValidadorJMSListener extends MessageListener{
	
	public void setConfiguracaoDll(DllDadosDTO configuracaoDll);

	public SwingConsumerMonitor getSwingConsumerMonitor();

	public void setSwingConsumerMonitor(SwingConsumerMonitor swingConsumerMonitor); 

}
