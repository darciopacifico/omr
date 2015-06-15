package com.msaf.validador.consumer;

import javax.jms.MessageListener;
import javax.management.j2ee.statistics.MessageDrivenBeanStats;

import com.msaf.framework.utils.DllDadosDTO;

public interface IValidadorJMSListener extends MessageListener{
	
	public void setConfiguracaoDll(DllDadosDTO configuracaoDll);

}
