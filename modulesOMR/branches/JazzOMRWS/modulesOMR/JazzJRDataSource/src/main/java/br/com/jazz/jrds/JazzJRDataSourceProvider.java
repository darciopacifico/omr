/**
 * 
 */
package br.com.jazz.jrds;


import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jazz.jrds.exceptions.JazzJRDSRuntimeException;

/**
 * Implementacao de JRDataSourceProvider. Permite criar JRDataSources com inteligencia para inferir como um modelo de dados pode
 * ser navegado, através de um produto cartesiano de seus relacionamentos.
 * 
 * Permite recuperar metadados destes modelo para a criação de relatorios
 * 
 * @author darciopa
 * 
 */
public class JazzJRDataSourceProvider<T extends Class<?>> implements JRDataSourceProvider {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected static Logger staticlog = LoggerFactory.getLogger(JazzJRDataSourceProvider.class);
	private JazzJRDataSource<Class<T>> jazzJRDataSource; 
	
	private Collection<T> data; 
	private T class1;
	private Map<String, Object> mapParam;
	
	
	
	/**
	 * Constroi um DSProvider a partir de um modelo de dados.
	 * @param data
	 * @param class1
	 */
	public JazzJRDataSourceProvider(Collection<T> data, T class1, Map<String, Object> mapParam){
		this.mapParam = mapParam;
		this.data = data;
		this.class1 = class1;
		
		this.setJazzJRDataSource(getJazzJRDataSource());
	}

	/**
	 * Determinar que se pode acionar o getFields deste DSProvider. 
	 */
	@Override
	public boolean supportsGetFieldsOperation() {
		return false;
	}

	/**
	 * Recupera os campos inferidos por este DSProvider
	 */
	@Override
	public JRField[] getFields(JasperReport report) throws JRException, UnsupportedOperationException {
		log.error("#####################3 get fields");
		return getJazzJRDataSource().getFields();
	}

	
	/**
	 * Retorna o JazzJRDataSource criado na construcao deste objeto	
	 */
	@Override
	public JRDataSource create(JasperReport report) throws JRException {
		return getJazzJRDataSource();
	}


	/**
	 * Cumprindo o contrato de JRDataSourceProvider. Nada a fazer.
	 */
	@Override
	public void dispose(JRDataSource dataSource) throws JRException {
		this.setJazzJRDataSource(null);
	}

	public JazzJRDataSource<Class<T>> getJazzJRDataSource() {
		
		if(this.jazzJRDataSource==null){

			try{
				this.jazzJRDataSource = new JazzJRDataSource(data, class1, this.mapParam);
				if(log.isDebugEnabled()){
					log.debug("Data Source criado com sucesso!");
				}
			}catch (Exception e) {
				log.error("Erro ao tentar criar o DataSource a partir dos registros "+data+", classe= "+class1,e);
				throw new JazzJRDSRuntimeException("Erro ao tentar criar o DataSource a partir dos registros "+data+", classe= "+class1,e);
			}
			
		}else{
			if(log.isDebugEnabled()){
				log.debug("Reutilizando o dataSource ja existente!");
			}
		}
		
		return jazzJRDataSource;
	}

	public void setJazzJRDataSource(JazzJRDataSource<Class<T>> jazzJRDataSource) {
		this.jazzJRDataSource = jazzJRDataSource;
	}

}
