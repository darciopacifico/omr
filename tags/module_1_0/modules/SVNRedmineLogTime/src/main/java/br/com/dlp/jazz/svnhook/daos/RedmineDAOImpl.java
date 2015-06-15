package br.com.dlp.jazz.svnhook.daos;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookRTException;
import br.com.dlp.jazz.svnhook.mappers.IssueVORowMapper;
import br.com.dlp.jazz.svnhook.mappers.RedmineUserRowMapper;
import br.com.dlp.jazz.svnhook.mappers.RepositorioRowMapper;
import br.com.dlp.jazz.svnhook.vos.CitationVO;
import br.com.dlp.jazz.svnhook.vos.IssueVO;
import br.com.dlp.jazz.svnhook.vos.RedmineUserVO;
import br.com.dlp.jazz.svnhook.vos.RepositorioVO;

/**
 * Implementacao das consultas ao redmine
 * 
 * @author t_dpacifico
 */
@Component
public class RedmineDAOImpl extends SimpleJdbcDaoSupport implements RedmineDAO {

	private static final long serialVersionUID = -4008243162952921800L;

	/**
	 * Consulta issues pelo id. Traz detalhes do repositorio e nome do projeto
	 * 
	 * @return
	 */
	public List<IssueVO> pesquisarIssues(Long... issueId) {

		final String queryIssue = 
		" select " +
		"		i.id, " +
		"		st.is_closed, " +
		" 	r.url, " +
		"		r.root_url, " +
		"		r.project_id, " +
		"		p.name," +
		"		p.parent_id " + 
		" from issues i " +
		" inner join repositories r on r.project_id = i.project_id " +
		" inner join issue_statuses st on st.id = i.status_id " +
		" inner join projects p on p.id = r.project_id" + 
		" where i.id in (:ids) ";

		SimpleJdbcTemplate simpleJdbcTemplate = getSimpleJdbcTemplate();
		List<IssueVO> issue = simpleJdbcTemplate.query(queryIssue, new IssueVORowMapper(), Collections.singletonMap("ids", Arrays.asList(issueId)));

		return issue;
	}

	
	/**
	 * Pesquisa usuario do redmine user
	 * @param login
	 * @return
	 */
	public RedmineUserVO findRedmineUser(String login){
		
		SimpleJdbcTemplate simpleJdbcTemplate = getSimpleJdbcTemplate();
		
		String redmineUserQuery = "select id,login,status from users where login = ? ";
		
		RedmineUserVO user = simpleJdbcTemplate.queryForObject(redmineUserQuery, new RedmineUserRowMapper(), login);
		
		return user;
	}
	
	
	/**
	 * Consulta projetos afetados pela mudança realizada na url informada.
	 * 
	 * @return
	 */
	public List<RepositorioVO> affectedRepositories(String urlAlteracao, String repoUUID) {

		try {
			return affectedRepoUUID(urlAlteracao, repoUUID);
		} catch (DataAccessException e) {
			logger.error("## ATENCAO! Nao foi possível confirmar o UUID do repositório registrado no Redmine com o UUID do repositório afetado ("+repoUUID+").");
			logger.error("## Caso existam dois ou mais repositórios SVN registrados do Redmine, podem ocorrer erros em relacao ao tratamento dos tickets citados no commit!");
			logger.error("## Para evitar erros, crie o campo 'repouuid' na tabela 'repositories' varchar(50) do Redmine e atualize este campo com os UUID dos respositórios registrados!");
			logger.error("## "+e);
			return affectedRepoNoUUID(urlAlteracao);
		}
	
	}


	/**
	 * Detecta repositorios registrados no redmine e seus respectivos projetos que foram afetados pela mudança, a partir da URL alterada
	 * @param urlAlteracao
	 * @param repoUUID
	 * @return
	 */
	protected List<RepositorioVO> affectedRepoUUID(String urlAlteracao, String repoUUID) {
		final String queryRepositoriosAfetados = " select " + 
		"     p.id, " + 
		"     p.name, " + 
		"		  p.parent_id, " +		
		"     r.id repository_id, " + 
		"     r.url, " + 
		"     r.repouuid, " + 
		"     r.root_url " + 
		" from  " +
		"     repositories r "+ 
		" inner join projects p on " + 
		"     p.id = r.id  " + 
		" where   " + 
		"     r.repouuid = ? and " + 
		"     instr(concat(r.root_url,'/',?),r.url)  " + 
		" order by  " +
		"     length(url) desc";

		SimpleJdbcTemplate jdbcTemplate = getSimpleJdbcTemplate();

		List<RepositorioVO> repositorioVOs = jdbcTemplate.query(queryRepositoriosAfetados, new RepositorioRowMapper(), repoUUID, urlAlteracao);

		determinaProjetosAfetados(repositorioVOs);

		return repositorioVOs;
	}	
	
	/**
	 * Consulta projetos afetados pela mudança realizada na url informada.
	 * 
	 * @return
	 */
	protected List<RepositorioVO> affectedRepoNoUUID(String urlAlteracao) {

		final String queryRepositoriosAfetados = " select " + 
		"     p.id, " + 
		"     p.name, " + 
		"		  p.parent_id, " +		
		"     r.id repository_id, " + 
		"     r.url, " + 
		"     r.root_url " + 
		" from  " +
		"     repositories r "+ 
		" inner join projects p on " + 
		"     p.id = r.id  " + 
		" where   " + 
		"     instr(concat(r.root_url,'/',?),r.url)  " + 
		" order by  " +
		"     length(url) desc";

		SimpleJdbcTemplate jdbcTemplate = getSimpleJdbcTemplate();

		List<RepositorioVO> repositorioVOs = jdbcTemplate.query(queryRepositoriosAfetados, new RepositorioRowMapper(false), urlAlteracao);

		determinaProjetosAfetados(repositorioVOs);

		return repositorioVOs;
	}

	/**
	 * Caso existam subprojetos afetados, significa que o projeto pai também 
	 * apareceu na listagem, mas na verdade o afetado é o projeto filho.
	 *
	 * Exemplo: se a listagem retornada foi a seguinte, e o endereço do artefato 
	 * afetado pela mudança é http://localhost/svn/dlpacifico/jazzxdoclet/subprojeto1/classes, 
	 * então o diretório http://localhost/svn/dlpacifico/jazzxdoclet não deve ser incluido na listagem. 
	 * 
	 * http://localhost/svn/dlpacifico/jazzxdoclet/subprojeto1/
	 * http://localhost/svn/dlpacifico/jazzxdoclet
	 * 
	 * Este método substitui a clausula having length(ur)=max(length(ur)), que não funcionou no mysql.
	 * Esta deveria ser uma regra contida na query, por isso deve ser mantida no DAO. Nao expor esta regra
	 * aos componentes de negócio.
	 * 
	 * @param repositorioVOs
	 */
	protected void determinaProjetosAfetados(List<RepositorioVO> repositorioVOs) {
		Iterator<RepositorioVO> itRepos = repositorioVOs.iterator();
		int len = 0;
		while (itRepos.hasNext()) {

			RepositorioVO repositorioVO = itRepos.next();

			if (len > repositorioVO.getUrl().length()) {
				/*
				Removo as menores urls, mantendo apenas as urls maiores, dos subprojetos. 
				Idealmente apenas uma URL será retornada, a maior. Caso mais de uma url 
				seja retornada, é porque existem dois ou mais repositórios 
				apontando para o mesmo endereço.
				*/
				itRepos.remove();//remove da coleção enviada como argumento
			} else {
				//Passará por aqui na primeira iteração, para atualizar o tamanho da primeira URL, que é a maior
				len = repositorioVO.getUrl().length();
			}

		}
	}

	/**
	 * Atribui datasource configurado no Spring
	 * 
	 * @param dataSource
	 */
	@SuppressWarnings({"unused","squid.UnusedPrivateMethod"})
	@Autowired
	private void setDataSourceJazz(DataSource dataSource) {
		super.setDataSource(dataSource);
	}


	/* (non-Javadoc)
	 * @see br.com.dlp.jazz.svnhook.daos.RedmineDAO#logTime(br.com.dlp.jazz.svnhook.vos.CitationVO)
	 */
	@Override
	@SuppressWarnings("pmd.AvoidDuplicateLiterals")
	public int logTime(CitationVO citacaoIssueVO) {
		Integer activityId = this.findDefaultActivityId();

		SimpleJdbcTemplate simpleJdbcTemplate = getSimpleJdbcTemplate();
		
		@SuppressWarnings("pmd.AvoidDuplicateLiterals")
		String logTimeQuery = " INSERT INTO time_entries( " +
			" project_id,  " +
			" user_id,  " +
			" issue_id,  " +
			" hours,  " +
			" comments,  " +
			" activity_id,  " +
			" spent_on,  " +
			" tyear,  " +
			" tmonth,  " +
			" tweek,  " +
			" created_on, " + 
			" updated_on)  " +
			" VALUES(" +
			" ?, ?, ?, ?, ?, ?, " +
			"curdate(), year(curdate()), month(curdate()), week(curdate()), curdate(), curdate())";	
		
		Integer affected = simpleJdbcTemplate.update(logTimeQuery, 
				citacaoIssueVO.getIssueVO().getProjectVO().getId(),
				citacaoIssueVO.getRedmineUserVO().getId(),
				citacaoIssueVO.getIssueVO().getId(),
				citacaoIssueVO.getHorasDespendidas(),
				"Lançamento automático de esforço a partir da mensagem de commit!",
				activityId);
		
		return affected;
		
	}
	
	/**
	 * Recupera o id padrao de TimeEntryActivity para que seja possível efetuar o log time com este numero
	 */
	@Override
	public Integer findDefaultActivityId() {
		SimpleJdbcTemplate jdbcTemplate = getSimpleJdbcTemplate();
		
		Integer activityId;
		try {
			activityId = jdbcTemplate.queryForInt("select id from enumerations where type='TimeEntryActivity' and is_default=1");
		} catch (EmptyResultDataAccessException e) {
			throw new JazzSVNHookRTException("Nao foi possivel identificar o codigo de atividade padrao para registrar o LogTime! Defina um código de atividade padrão no Redmine.", e);
		}
		
		return activityId;
	}

	
	
}
