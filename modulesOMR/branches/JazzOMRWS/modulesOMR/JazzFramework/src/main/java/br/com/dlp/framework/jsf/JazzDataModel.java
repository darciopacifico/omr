/**
 * 
 */
package br.com.dlp.framework.jsf;

import java.io.IOException;
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
 * Implementa��o de ExtendedDataModel do RichFaces;
 * <br>
 * Solu��o de design para pagina��o real de registros com ordena��o, utilizando hibernate ou banco de dados,
 * sem utilizar p�ginas em sess�o.
 * <br>
 * As especializa��es de JazzDataModel dever�o ser providas de uma implementa��o de IJazzDataProvider,
 * que naturalmente ser� o pr�prio JSF ManagedBean. Desta forma este managed bean saber� como realizar a consulta
 * a partir dos filtros informados em tela, al�m de tamb�m receber os argumentos de pagina��o e ordena��o num objeto ExtraArgumentsDTO.
 * <br>
 * O DTO ExtraArgumentsDTO cont�m o que convencionou-se chamar de Extra Argumentos de pesquisa,
 * que s�o os argumentos de ordena��o e pagina��o.
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
	 * Constroi JazzDataModel recebendo IJazzDataProvider sua composi��o;
	 * @param IJazzDataProvider
	 */
	public JazzDataModel(IJazzDataProvider<B> IJazzDataProvider){
		jazzDataProvider = IJazzDataProvider;
	}
	
	/**
	 * Resolve o label de ordena��o para o campo
	 * 
	 * @param field
	 * @return
	 */
	public String getOrderLabel(String field) {
		
		Boolean asc = extraArgumentsDTO.getOrderMap().get(field);
		
		if (asc == null) {
			// n�o h� ordena��o para este campo no momento
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
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
			
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
			// o cache � v�lido OK
			cachedRowCount = jazzDataProvider.getLinhasTotais();
		}else{
		
			// o cache n�o � valida, ent�o conta de novo
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
	 * Consult o IJazzDataProvider para verificar se o registro referente à PK corrente est� disponível
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
	 * Atribui ao ExtraArgumentsDTO atual o Range de pagina��o informado no m�todo walk e retorna o mesmo ExtraArgumentsDTO
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
