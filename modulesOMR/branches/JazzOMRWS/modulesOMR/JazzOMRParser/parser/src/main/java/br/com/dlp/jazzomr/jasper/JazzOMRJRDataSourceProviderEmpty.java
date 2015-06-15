/**
 * 
 */
package br.com.dlp.jazzomr.jasper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.dlp.jazzomr.event.EventVO;
import br.com.jazz.jrds.JazzJRDataSourceProvider;

/**
 * @author darcio
 *
 */
public class JazzOMRJRDataSourceProviderEmpty extends JazzJRDataSourceProvider {

	
	public JazzOMRJRDataSourceProviderEmpty(Map<String, Object> map) {
		super(getEventos(),EventVO.class,map);
	}

	protected static List<EventVO> getEventos(){
		
		List<EventVO> eventos = new ArrayList<EventVO>();
		
		return eventos;
	}
	
	}
