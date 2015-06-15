/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente DummyVO
 *
 **/
@Component
public class DummyHibernateDAOImpl extends AbstractHibernateDAO<DummyVO> implements DummyDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @returns Coleção de DummyVO
	 */
	public List<DummyVO> findDummyVO(String descricao, String nome, Date dtInclusao){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(descricao, criteria, "descricao");
 	 	eqRestriction(nome, criteria, "nome");
 	 	eqRestriction(dtInclusao, criteria, "dtInclusao");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo DummyVO 
	 * @author t_dpacifico
	 * @param descricao
 	 * @param nome
 	 * @param dtInclusao
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de DummyVO
	 */
	public List<DummyVO> findDummyVO(String descricao, String nome, Date dtInclusao, ExtraArgumentsDTO  metaArgument){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(descricao, criteria, "descricao");
 	 	eqRestriction(nome, criteria, "nome");
 	 	eqRestriction(dtInclusao, criteria, "dtInclusao");
		
		order(criteria, metaArgument);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

