/**
 * 
 */
package br.com.dlp.framework.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação de ExtendedDataModel do RichFaces;
 * <br>
 * Solução de design para paginação real de registros com ordenação, utilizando hibernate ou banco de dados,
 * sem utilizar páginas em sessão.
 * <br>
 * As especializações de JazzDataModel deverão ser providas de uma implementação de IJazzDataProvider,
 * que naturalmente será o próprio JSF ManagedBean. Desta forma este managed bean saberá como realizar a consulta
 * a partir dos filtros informados em tela, além de também receber os argumentos de paginação e ordenação num objeto ExtraArgumentsDTO.
 * <br>
 * O DTO ExtraArgumentsDTO contém o que convencionou-se chamar de Extra Argumentos de pesquisa,
 * que são os argumentos de ordenação e paginação.
 * 
 * @author t_dpacifico
 */
public class JazzDataModel<B extends IBaseVO<? extends Serializable>> extends ExtendedDataModel {
	
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private static final long serialVersionUID = 4277526478596065428L;
	
	private Serializable currentPk;
	private int rowIndex;
	private final LinkedHashMap<Serializable, B> wrappedData = new LinkedHashMap<Serializable, B>();
	
	
	private IJazzDataProvider<B> jazzDataProvider;
	private final List<Serializable> wrappedKeys = new ArrayList<Serializable>();
	
	private Integer firstRow;
	private Integer numberOfRows;
	private ExtraArgumentsDTO extraArgumentsDTO = new ExtraArgumentsDTO();
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public List<B> getResultados(){
		Collection<B> values = wrappedData.values();
		ArrayList<B> list = new ArrayList<B>(values);
		return list;
	}
	
	/**
	 * Constroi JazzDataModel recebendo IJazzDataProvider sua composição;
	 * @param IJazzDataProvider
	 */
	public JazzDataModel(IJazzDataProvider<B> IJazzDataProvider){
		jazzDataProvider = IJazzDataProvider;
	}
	
	/**
	 * Resolve o label de ordenação para o campo
	 * 
	 * @param field
	 * @return
	 */
	public String getOrderLabel(String field) {
		
		Boolean asc = extraArgumentsDTO.getOrderMap().get(field);
		
		if (asc == null) {
			// não há ordenação para este campo no momento
			return "";
		} else if (asc) {
			// seta p/ cima
			return "\u25B2";
		} else {
			// seta p/ baixo
			return "\u25BC";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
	 */
	@Override
	public void setRowKey(Object key) {
		currentPk = (Serializable) key;
	}
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
	@Override
	public Object getRowKey() {
		return currentPk;
	}
	
	/* (non-Javadoc)
	 * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext, org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range, java.lang.Object)
	 */
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
			
		wrappedKeys.clear();
		wrappedData.clear();
		
		ExtraArgumentsDTO extraArgumentsDTO = getExtraArgumentsDTO(range);
		
		List<B> resultadoPaginado = jazzDataProvider.actionPesquisarCached(extraArgumentsDTO);
		
		int indexOf = 0;
		for (B tipoReq : resultadoPaginado) {
			//wrappedKeys.add(tipoReq.getPK());
			//wrappedData.put(tipoReq.getPK(), tipoReq);
			//visitor.process(context, tipoReq.getPK(), argument);
			
			wrappedKeys.add(indexOf);
			wrappedData.put(indexOf, tipoReq);
			visitor.process(context, indexOf, argument);
			
			indexOf++;
		}
	}
	
	/**
	 * Consulta o IJazzDataProvider informado e retorna a quantidade de registros totais
	 * @see javax.faces.model.DataModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		
		Long cachedRowCount=0l;
		
		if(jazzDataProvider.isValidRowCountCache()){
			// o cache é válido OK
			cachedRowCount = jazzDataProvider.getLinhasTotais();
		}else{
		
			// o cache não é valida, então conta de novo
			cachedRowCount = jazzDataProvider.cachedRowCount();
			
			if(cachedRowCount==null){
				log.warn("A contagem de registros retornou null! Este valor nao era esperado e pode ser um problema.");
				cachedRowCount=0l;
			}

			jazzDataProvider.setLinhasTotais(cachedRowCount);
		}
		
		return cachedRowCount.intValue();
		
	}
	
	/**
	 * Consulta o IJazzDataProvider informado para recuperar o registro selecionado
	 * @see javax.faces.model.DataModel#getRowData()
	 */
	@Override
	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			return wrappedData.get(currentPk);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowIndex()
	 */
	@Override
	public int getRowIndex() {
		return rowIndex;
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setRowIndex(int)
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getWrappedData()
	 */
	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Consult o IJazzDataProvider para verificar se o registro referente à PK corrente está disponível
	 * @see javax.faces.model.DataModel#isRowAvailable()
	 */
	@Override
	public boolean isRowAvailable() {
		if (currentPk == null) {
			return false;
		} else {
			return jazzDataProvider.isRowAvailable(currentPk);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
	 */
	@Override
	public void setWrappedData(Object data) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Retorna os metaArgumentos contidos neste DataModel
	 * @return
	 */
	public ExtraArgumentsDTO getExtraArgumentsDTO() {
		if (extraArgumentsDTO == null) {
			extraArgumentsDTO = new ExtraArgumentsDTO();
		}
		return extraArgumentsDTO;
	}
	
	/**
	 * Atribui ao ExtraArgumentsDTO atual o Range de paginação informado no método walk e retorna o mesmo ExtraArgumentsDTO
	 * @param range
	 * @return
	 */
	protected ExtraArgumentsDTO getExtraArgumentsDTO(Range range) {
		firstRow = ((SequenceRange) range).getFirstRow();
		numberOfRows = ((SequenceRange) range).getRows();
		
		ExtraArgumentsDTO extraArgumentsDTO = getExtraArgumentsDTO();
		
		extraArgumentsDTO.setFirstResult(firstRow);
		extraArgumentsDTO.setMaxResults(numberOfRows);
		return extraArgumentsDTO;
	}
	
	/**
	 * Recupera o IJazzDataProvider
	 * @return
	 */
	public IJazzDataProvider<B> getJazzDataProvider() {
		return jazzDataProvider;
	}
	
	/**
	 * Atribui um IJazzDataProvider
	 * @param jazzDataProvider
	 */
	public void setJazzDataProvider(IJazzDataProvider<B> jazzDataProvider) {
		this.jazzDataProvider = jazzDataProvider;
	}
}
