package br.com.dlp.jazz.svnhook.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.jazz.svnhook.daos.RedmineDAO;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookLogTimeException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookRTException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookUserNotFoundException;
import br.com.dlp.jazz.svnhook.vos.CitationVO;
import br.com.dlp.jazz.svnhook.vos.CriticVO;
import br.com.dlp.jazz.svnhook.vos.CriticVO.SEVERIDADE;
import br.com.dlp.jazz.svnhook.vos.CriticVO.TIPO_CRITICA;
import br.com.dlp.jazz.svnhook.vos.IssueVO;
import br.com.dlp.jazz.svnhook.vos.ProjectVO;
import br.com.dlp.jazz.svnhook.vos.RedmineUserVO;
import br.com.dlp.jazz.svnhook.vos.RepositorioVO;

/**
 * Implementação de serviço para tratamento dos eventos do Subversion. 
 * Basicamente prove serviços que validam o formato do comentário do commit e logam 
 * o tempo despendito em cada ticket.
 * 
 * @author t_dpacifico
 * @since 20100702
 */
@Component
public class JazzSVNHookServiceImpl implements JazzSVNHookService {
	private static final Logger log = LoggerFactory.getLogger(JazzSVNHookServiceImpl.class);

	protected static final int REGEX_GROUP_ESFORCO = 4;
	protected static final int REGEX_GROUP_TICKET = 2;
	protected static final int REGEX_GROUP_COMANDO = 1;
	protected static final int PARAM_TICKET_ID = 0;

	private String regex = "\\b(closes|refs|fixes)\\s+#([0-9]+)(\\s+([0-9]+)h){0,1}\\b";
	private Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

	@Autowired
	private RedmineDAO redmineDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.jazz.svnhook.JazzSVNHookService#analisarComentarios(java.lang.String, java.lang.String)
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public Collection<CitationVO> criticizeComments(List<String> dirsChanged, String repoUUID, String comments, String author) throws JazzSVNHookException {
		// Recupera todas as informacoes sobre os comentários feitos no commit e os referidos tickets do redmine, consultando banco de dados
		Collection<CitationVO> citacoes = retrieveCitations(comments, author);

		// Critica os comentários/tickets encontrados
		citacoes = criticizeCitations(citacoes, dirsChanged, repoUUID);

		// valida criticas levantadas anteriormente
		validateCritics(citacoes);

		return citacoes;
	}

	/**
	 * Processa as citações efetuadas no commit, validando as referências dos tickets e logando o esforço despendido.
	 * 
	 * @param dirsChanged
	 * @param repoUUID
	 * @param comments
	 * @param author
	 * @return
	 * @throws JazzSVNHookException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { JazzSVNHookException.class })
	public Collection<CitationVO> criticizeAndLogTimes(List<String> dirsChanged, String repoUUID, String comments, String author) throws JazzSVNHookException {

		// critica e valida comentários efetuados
		Collection<CitationVO> citacoes = criticizeComments(dirsChanged, repoUUID, comments, author);

		// loga o tempo despendido na mudança
		logTimes(citacoes);

		return citacoes;
	}

	/**
	 * Processa as citações parseadas e criticadas por este componente. Atualmente faz apenas o seguinte: Caso haja alguma citacao apontando esforco despendido, loga este esforço no referido ticket
	 * 
	 * @return
	 * @throws JazzSVNHookLogTimeException
	 */
	@Override
	public void logTimes(Collection<CitationVO> citacoes) {
		for (CitationVO citacaoIssueVO : citacoes) {
			logTime(citacaoIssueVO);
		}
	}

	/**
	 * @param citacoes
	 * @throws JazzSVNImpeditiveCriticException
	 * @throws JazzSVNHookLogTimeException
	 */
	protected void validateCritics(Collection<CitationVO> citacoes) throws JazzSVNHookLogTimeException {
		for (CitationVO citationVO : citacoes) {
			if (hasAnyImpeditiveCritic(citationVO)) {
				log.error(citationVO.getCritics().toString());
				throw new JazzSVNHookLogTimeException(citationVO, "Nao foi registrar o tempo despendido para " + citationVO.getIssueVO()
						+ ". Verifique as criticas impetitivas referentes a citacao " + citationVO + " " + citationVO.getCritics());
			}
		}
	}

	/**
	 * Critica citacoes e issues encontradas
	 * 
	 * @param citacoes
	 * @param dirRepo
	 * @return
	 */
	protected Collection<CitationVO> criticizeCitations(Collection<CitationVO> citacoes, List<String> changedDirs, String repoUUID) {

		for (CitationVO citacaoIssueVO : citacoes) {
			criticizeCitation(citacaoIssueVO, changedDirs, repoUUID);
		}

		return citacoes;
	}

	/**
	 * Realiza critica à citacao e ao ticket referente encontrado
	 * 
	 * @param citacaoIssueVO
	 * @param changedDirs
	 * @param criticas
	 * @return
	 */
	protected void criticizeCitation(CitationVO citacaoIssueVO, List<String> changedDirs, String repoUUID) {

		IssueVO issueVO = citacaoIssueVO.getIssueVO();

		// testa se foi encontrado issue
		if (issueVO == null) {
			criticizeIssueNotFound(citacaoIssueVO);
			return;
		}

		// testa se o issue citado faz parte de um projeto afetado pelas mudanças
		if (!isIssueImpactingProject(issueVO, changedDirs, repoUUID)) {
			criticizeIssueNotRelatedWithArtifacts(citacaoIssueVO, changedDirs, issueVO);
		}

		// testa se este issue é deste projeto
		if (issueVO.getClosed()) {
			criticizeClosedIssue(citacaoIssueVO, issueVO);
		}
	}

	/**
	 * Monta um objeto de critica para o caso de um ticket fechado
	 * @param citacaoIssueVO
	 * @param criticas
	 * @param issueVO
	 */
	protected void criticizeClosedIssue(CitationVO citacaoIssueVO, IssueVO issueVO) {
		CriticVO criticaVO = new CriticVO();
		criticaVO.setDescricao("O ticket #" + issueVO.getId() + " esta fechado e nao pode ser referenciado! Crie ou escolha um ticket aberto.");
		criticaVO.setSeveridade(CriticVO.SEVERIDADE.IMPEDITIVO);
		criticaVO.setTipoCritica(TIPO_CRITICA.ISSUE_FECHADA);
		citacaoIssueVO.getCritics().add(criticaVO);
	}

	/**
	 * Monta um objeto de critica para o caso de ticket nao relacionado as mudancas
	 * @param citacaoIssueVO
	 * @param changedDirs
	 * @param criticas
	 * @param issueVO
	 */
	protected void criticizeIssueNotRelatedWithArtifacts(CitationVO citacaoIssueVO, List<String> changedDirs, IssueVO issueVO) {
		CriticVO criticaVO = new CriticVO();
		criticaVO.setDescricao("Nenhum artefato afetado pela mudança " + "está relacionado ao ticket " + issueVO + " do projeto '" + issueVO.getProjectVO() + "'!"
				+ " Escolha um ticket relacionado aos artefatos afetados!" + " Os diretorios alterados foram " + changedDirs);

		criticaVO.setSeveridade(CriticVO.SEVERIDADE.IMPEDITIVO);
		criticaVO.setTipoCritica(TIPO_CRITICA.MUDANCAS_NAO_RELACIONADAS_AO_TICKET);

		citacaoIssueVO.getCritics().add(criticaVO);
	}

	/**
	 * Monta um objeto de critica para o caso de um ticket nao encontrado
	 * @param citacaoIssueVO
	 * @param criticas
	 */
	protected void criticizeIssueNotFound(CitationVO citacaoIssueVO) {

		CriticVO criticaVO = new CriticVO();
		criticaVO.setDescricao("A issue nao foi encontrado (null)!");
		criticaVO.setSeveridade(CriticVO.SEVERIDADE.RISCO);
		criticaVO.setTipoCritica(TIPO_CRITICA.ISSUE_NAO_ENCONTRADO);

		citacaoIssueVO.getCritics().add(criticaVO);

	}

	/**
	 * Testa se issue está relacionado aos artefatos afetados pela mudança, analisando o endereço do repositório do projeto e os diretórios alterados
	 * 
	 * @param issueVO
	 * @param changedDirs
	 * @param repoUUID
	 * @return
	 */
	protected boolean isIssueImpactingProject(IssueVO issueVO, List<String> changedDirs, String repoUUID) {

		for (String changedDir : changedDirs) {
			// recupera repositorios afetados pela mudança
			List<RepositorioVO> repositoriosAfetados = affectedRepositories(repoUUID, changedDir);
			for (RepositorioVO repositorioVO : repositoriosAfetados) {
				if (belongSameProject(issueVO, repositorioVO)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Retorna os repositorios afetados pela mudança
	 * @param repoUUID
	 * @param changedDir
	 * @return
	 */
	protected List<RepositorioVO> affectedRepositories(String repoUUID, String changedDir) {
		List<RepositorioVO> affectedRepositories = redmineDAO.affectedRepositories(changedDir, repoUUID);

		if (log.isDebugEnabled()) {
			log.debug("Repositorios afetados:" + affectedRepositories);
		}

		return affectedRepositories;
	}

	/**
	 * Apenas checa se os ids de projeto sao os mesmos
	 * 
	 * @param issueVO
	 * @param repositorioVO
	 * @return
	 */
	protected boolean belongSameProject(IssueVO issueVO, RepositorioVO repositorioVO) {
		Long idProjetoRepo = repositorioVO.getProjetoVO().getId();
		long idProjetoIssue = issueVO.getProjectVO().getId();

		boolean mesmoRepo = idProjetoRepo.equals(idProjetoIssue) || isParentProject(issueVO.getProjectVO(), repositorioVO.getProjetoVO());

		if (log.isDebugEnabled()) {
			log.debug(" ProjetoPai " + repositorioVO.getProjetoVO().getIdParentProject() + " ProjetoPai " + issueVO.getProjectVO());

			log.debug("idProjetoRepo=" + idProjetoRepo + " idProjetoIssue=" + idProjetoIssue);
			log.debug("Pertencem ao mesmo projeto = " + mesmoRepo);
		}

		return mesmoRepo;
	}

	/**
	 * Testa se os projetos são relacionados
	 * 
	 * @param projectFilhoVO
	 * @param projetoPaiVO
	 * @return
	 */
	protected boolean isParentProject(ProjectVO projectFilhoVO, ProjectVO projetoPaiVO) {

		// IMPLEMENTAR CONSULTA RECURSIVA DE PROJETOS: PROJETOS FILHOS, NETOS, BISNETOS :-) ETC...
		boolean isParent = projetoPaiVO.getId().equals(projectFilhoVO.getIdParentProject());

		if (log.isDebugEnabled() && isParent) {
			log.debug("O projeto " + projectFilhoVO + " é filho de " + projetoPaiVO);
		}

		return isParent;
	}

	/**
	 * Parseia comentários, identificando tickets citados, e recupera-os da base de dados do redmine
	 * 
	 * @param comentarios
	 * @return
	 * @throws JazzSVNHookException
	 */
	protected Collection<CitationVO> retrieveCitations(String comentarios, String login) throws JazzSVNHookException {
		// A partir da String de comentarios de commit cria mapa de citacoes de issues
		Map<Long, CitationVO> citationsMap = parseComments(comentarios, login);

		// consulta a base de dados do redmine sobre os tickets encontrados
		Collection<CitationVO> citacoes = complementCitationVOs(citationsMap);

		// valida se o usuário que efetuou o commit existe no redmine e atualiza o CitationVO com esta informacao
		validateAndComplementRedmineUser(login, citacoes);

		if (log.isDebugEnabled()) {
			log.debug("Foram encontradas " + citacoes.size() + " citacoes!");
		}

		return citacoes;
	}

	/**
	 * valida citacoes e complementa com o usuario do redmine identificado
	 * @param login
	 * @param citacoes
	 * @throws JazzSVNHookUserNotFoundException
	 */
	protected void validateAndComplementRedmineUser(String login, Collection<CitationVO> citacoes) throws JazzSVNHookUserNotFoundException {
		RedmineUserVO redmineUserVO = findRedmineUser(login);

		for (CitationVO citationVO : citacoes) {
			citationVO.setRedmineUserVO(redmineUserVO);
		}
	}

	/**
	 * Complementa citacoes parseadas em comentários com os dados do issue tracking (Redmine)
	 * 
	 * @param mapCitations
	 *          mara contendo as citacoes por numero do issue
	 * @return
	 */
	protected Collection<CitationVO> complementCitationVOs(Map<Long, CitationVO> mapCitations) {

		Collection<Long> issueIds = mapCitations.keySet();
		List<IssueVO> issueVOs = redmineDAO.pesquisarIssues(issueIds.toArray(new Long[issueIds.size()]));

		for (IssueVO issueVO : issueVOs) {
			CitationVO citacaoVO = mapCitations.get(issueVO.getId());
			citacaoVO.setIssueVO(issueVO);
		}

		return mapCitations.values();

	}

	/**
	 * A partir da string do comentário, extrai os tickets referenciados
	 * 
	 * @param comentarios
	 * @param dirRepo
	 * 
	 * @param comentarios
	 * @return
	 * @throws JazzSVNInvalidCommentException
	 */
	protected Map<Long, CitationVO> parseComments(String comentarios, String login) throws JazzSVNInvalidCommentException {

		boolean esforcoFoiCitado = false;

		Map<Long, CitationVO> citationsMap = new HashMap<Long, CitationVO>(2);

		Matcher matcher = pattern.matcher(comentarios);

		// itera sobre citacoes à tickets encontradas nos comentários
		while (matcher.find()) {

			CitationVO citacaoTicket = createCitationVO(matcher, login);

			// Checa se houve citacao de esforco em horas
			// OK, ao menos uma citacao de ticket referenciava o esforco!
			// ATENCAO!!! NUNCA VOLTA A SER FALSE SE FOI TRUE UMA VEZ!!! DESCULPE-ME A COMPLICACAO! :-|
			esforcoFoiCitado = esforcoFoiCitado || hasLogTimeComment(citacaoTicket);

			// dá erro se já existe o ticket no mapa
			putCitationMap(citationsMap, citacaoTicket);
		}

		if (!esforcoFoiCitado) {
			throw new JazzSVNInvalidCommentException("Ao menos um ticket deve citar o esforco despendido em horas, ex: 'Correcao do item xxx refs #34 4h'");
		}

		return citationsMap;
	}

	/**
	 * Checa se foi comentado o esforço despendido no ticket citado
	 * 
	 * @param citacaoTicket
	 * @return
	 */
	protected boolean hasLogTimeComment(CitationVO citacaoTicket) {
		return citacaoTicket.getHorasDespendidas() != null && citacaoTicket.getHorasDespendidas() > 0;
	}

	/**
	 * Checa se o ticket ja não citado e, caso ainda não, o inclui no mpara de tickets
	 * 
	 * @param citationsMap
	 * @param citacaoTicket
	 */
	protected void putCitationMap(Map<Long, CitationVO> citationsMap, CitationVO citacaoTicket) {
		if (!citationsMap.containsKey(citacaoTicket.getIssue())) {
			// adiciona no mapa
			citationsMap.put(citacaoTicket.getIssue(), citacaoTicket);
		} else {
			throw new JazzSVNHookRTException("O ticket #" + citacaoTicket.getIssue()
					+ " foi citado mais de uma vez. Por favor, faça apenas uma referencia por ticket no comentario de commit!");
		}
	}

	/**
	 * Cria uma citacao issueVO a partir de um matcher posicionado num determinado trecho do texto compatível com a regex
	 * 
	 * @param matched
	 * @return
	 */
	protected CitationVO createCitationVO(Matcher matched, String login) {
		String strComando = matched.group(REGEX_GROUP_COMANDO);
		String strTicket = matched.group(REGEX_GROUP_TICKET);
		String strEsforco = matched.group(REGEX_GROUP_ESFORCO);

		return new CitationVO(strComando, strTicket, strEsforco, login);
	}

	/**
	 * Processa dados enviados numa citacao de commit.
	 * 
	 * @param citacaoIssueVO
	 * @throws JazzSVNHookLogTimeException
	 */
	protected void logTime(CitationVO citacaoIssueVO) {

		if (citacaoIssueVO.getHorasDespendidas() == null) {
			log.warn("O ticket " + citacaoIssueVO.getIssueVO() + " foi citado, mas nao ha referencia de esforco de trabalho!");
			return;
		}

		redmineDAO.logTime(citacaoIssueVO);
	}

	/**
	 * Procura pelo usuario que efetuou a alteracao no redmine
	 * @param citacaoIssueVO
	 * @return
	 * @throws JazzSVNHookUserNotFoundException
	 */
	protected RedmineUserVO findRedmineUser(CitationVO citacaoIssueVO) throws JazzSVNHookUserNotFoundException {
		String login = citacaoIssueVO.getLogin();
		return findRedmineUser(login);
	}

	/**
	 * Procura pelo usuario que efetuou a alteracao no redmine
	 * @param login
	 * @return
	 * @throws JazzSVNHookUserNotFoundException
	 */
	public RedmineUserVO findRedmineUser(String login) throws JazzSVNHookUserNotFoundException {
		RedmineUserVO redmineUserVO;
		try {
			redmineUserVO = redmineDAO.findRedmineUser(login);

		} catch (EmptyResultDataAccessException e) {
			throw new JazzSVNHookUserNotFoundException("O usuario " + login + " nao foi " + "encontrado no Redmine! Por favor, cadastre-o ou comite as "
					+ "alteracoes com um usuario registrado no redmine!", e);
		}

		return redmineUserVO;
	}

	/**
	 * Verifica se alguma das criticas é impetitiva
	 * @param critics
	 */
	protected boolean hasAnyImpeditiveCritic(CitationVO citationVO) {
		List<CriticVO> critics = citationVO.getCritics();
		for (CriticVO criticVO : critics) {
			if (!criticVO.getSeveridade().equals(SEVERIDADE.NENHUMA)) {
				return true;
			}
		}
		return false;
	}

}
