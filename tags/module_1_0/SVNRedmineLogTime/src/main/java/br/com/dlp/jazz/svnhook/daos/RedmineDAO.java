package br.com.dlp.jazz.svnhook.daos;

import java.io.Serializable;
import java.util.List;

import br.com.dlp.jazz.svnhook.vos.CitationVO;
import br.com.dlp.jazz.svnhook.vos.IssueVO;
import br.com.dlp.jazz.svnhook.vos.RedmineUserVO;
import br.com.dlp.jazz.svnhook.vos.RepositorioVO;

/**
 * Consultas padrão Redmine
 * @author t_dpacifico
 *
 */
public interface RedmineDAO extends Serializable {

	/**
	 * Consulta issues pelo id. Traz detalhes do repositorio e nome do projeto
	 * @return
	 */
	List<IssueVO> pesquisarIssues(Long... issueId);

	/**
	 * Consulta projetos afetados pela mudança realizada na url informada.
	 * 
	 * @return
	 */
	List<RepositorioVO> affectedRepositories(String urlAlteracao, String repoUUID);

	
	
	/**
	 * Pesquisa usuario do redmine pelo login
	 * @param login
	 * @return
	 */
	RedmineUserVO findRedmineUser(String login);

	/**
	 * Registra tempo despendido nos tickets (ambas informações apontadas no commit)
	 * @param citacaoIssueVO
	 * @return
	 */
	int logTime(CitationVO citacaoIssueVO);
	
	/**
	 * @return
	 */
	Integer findDefaultActivityId();
	
}
