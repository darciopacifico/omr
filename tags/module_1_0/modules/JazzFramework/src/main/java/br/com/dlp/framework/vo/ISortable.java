package br.com.dlp.framework.vo;

import java.util.Comparator;

import javax.persistence.Column;

/**
 * TODO: REALIZAR TESTES UNITARIOS
 * 
 * Contrato para entidade ordenavel. Serve para utilizacao dos metodos de ordenacao em tela
 * 
 * @author darcio
 *
 */
public interface ISortable {

	public static final ISortableComparator sortableComparator = new ISortableComparator();
	
	public static class ISortableComparator implements Comparator<ISortable>{

		@Override
		public int compare(ISortable sort1, ISortable sort2) {
			if(sort1==null || sort2==null){
				return 0;
			}
			
			Double o1 = sort1.getOrdem();
			Double o2 = sort2.getOrdem();

			if(o1 == null && o2==null){
				return 0;
			}
			
			if(o1==null){
				return 1;
			}else if(o2==null){
				return -1;
			} 
			
			if(o1>o2){
				return 1;
				
			}else if(o1<o2){
				return -1;
				
			}
			
			return 0;
		}
	}
	
	@Column
	public Double getOrdem();

	public void setOrdem(Double ordem);

}