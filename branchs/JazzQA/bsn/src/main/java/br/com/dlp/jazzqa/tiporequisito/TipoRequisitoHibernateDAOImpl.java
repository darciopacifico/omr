/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente TipoRequisitoVO
 *
 **/
@Component
public class TipoRequisitoHibernateDAOImpl extends AbstractHibernateDAO<TipoRequisitoVO> implements TipoRequisitoDAO {
	
	private static final long serialVersionUID = 4188683927282245182L;
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO,ExtraArgumentsDTO orderMap){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
		ilikeRestriction(descricao, criteria, "descricao");
		ilikeRestriction(nome, criteria, "nome");
		eqRestriction(dummyVO, criteria, "dummyVO");
		
		return findByCriteria(criteria,orderMap);
		
	}
	
	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
	 * @param dtInclusaoTo
	 * @param descricao
	 * @param nome
	 * @param dummyVO
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO){
		
		return this.findTipoRequisitoVO(dtInclusaoFrom, dtInclusaoTo, descricao, nome, dummyVO, null);
		
	}
	
	/**
	 * Contador de resultados de consulta. Utilizado no mecanismo de paginação
	 * 
	 * @see br.com.dlp.jazzqa.tiporequisito.TipoRequisitoDAO#countFindTipoRequisitoVO(java.util.Date, java.util.Date, java.lang.String, java.lang.String, br.com.dlp.jazzqa.tiporequisito.DummyVO)
	 */
	@Override
	public Long countFindTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, DummyVO dummyVO) {
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
		ilikeRestriction(descricao, criteria, "descricao");
		ilikeRestriction(nome, criteria, "nome");
		eqRestriction(dummyVO, criteria, "dummyVO");
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		return (Long) list.get(0);
	}
}

