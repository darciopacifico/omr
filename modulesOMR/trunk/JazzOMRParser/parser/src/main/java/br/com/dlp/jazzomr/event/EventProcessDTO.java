/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.io.Serializable;

import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author darciopa
 *
 */
public class EventProcessDTO implements Serializable{
	private JasperPrint jasperPrint;
	private EventVO eventVO;

	
	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}
	public EventVO getEventVO() {
		return eventVO;
	}

	
	public void setJasperPrint(JasperPrint eventoReturn) {
		this.jasperPrint = eventoReturn;
	}
	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}
}
