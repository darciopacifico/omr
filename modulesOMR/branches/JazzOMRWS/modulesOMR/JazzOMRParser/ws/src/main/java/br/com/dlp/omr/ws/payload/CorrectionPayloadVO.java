package br.com.dlp.omr.ws.payload;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.aegis.type.java5.XmlElement;

import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.results.JazzImageTypeCreator;

@XmlRootElement
public class CorrectionPayloadVO implements Serializable {

	private static final long serialVersionUID = -572245663009827881L;
	
	private EventVO eventVO;
	
	public List<PageImageVO> pageImages; 
	
	
	public List<PageImageVO> getPageImages() {
		return pageImages;
	}

	public void setPageImages(List<PageImageVO> pageImages) {
		this.pageImages = pageImages;
	}

	public EventVO getEventVO() {
		return eventVO;
	}
	
	public void setEventVO(EventVO eventVO) {
		this.eventVO = eventVO;
	}
	
}
