package br.com.dlp.jazz.svnhook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.dlp.jazz.svnhook.daos.RedmineDAO;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookException;
import br.com.dlp.jazz.svnhook.services.JazzSVNHookService;
import br.com.dlp.jazz.svnhook.vos.CitationVO;
import br.com.dlp.jazz.svnhook.vos.CriticVO;
import br.com.dlp.jazz.svnhook.vos.IssueVO;
import br.com.dlp.jazz.svnhook.vos.RepositorioVO;

@ContextConfiguration(locations = "HookApplicationContext.xml")
@Test
public class TestSVNHook extends AbstractTestNGSpringContextTests {
	protected static Logger log = LoggerFactory.getLogger(TestSVNHook.class);

	private static final String slashBar = "/";

	@Autowired
	private RedmineDAO redmineDAO;

	@Autowired
	private JazzSVNHookService jazzSVNHookService;

	@Test
	public void testCommentRegex() {
		String comments = "refs #1 Corrigindo ticket 21 closes   	 #2  	 12h rEfs #3 2h #4 3h #5 #6 fixEs #70 3h ok Refs#26sdsd Refs #8 7h refs #9 12h";
		List<String> dirsChanged = new ArrayList<String>();

		//dirsChanged.add(slashBar);
		dirsChanged.add("jazzxdoclet/classes/");
		//dirsChanged.add("jazzxdoclet/paginas/");
		//dirsChanged.add("jazzxdoclet/paginas/produto/");
		dirsChanged.add("jazzxdoclet/subprojeto1/");
		//dirsChanged.add("my-project/");
		//dirsChanged.add("novoprojeto/");
		//dirsChanged.add("projeto4/");

		Collection<CitationVO> citacoes;
		try {
			citacoes = jazzSVNHookService.criticizeAndLogTimes(dirsChanged,"f8163358-0f88-44f2-8509-3be3471138aa", comments, "dpacifico");
		} catch (JazzSVNHookException e) {
			throw new RuntimeException("Erro ao tentar processar os comentarios",e);
		}
		
		for (CitationVO citationVO : citacoes) {
			log.debug(citationVO.toString());
			List<CriticVO> critics = citationVO.getCritics();
			for (CriticVO criticVO : critics) {
				log.debug("\t "+criticVO.toString());
			}
			log.debug("");
		}
		
		

	}

	/**
	 * @param strUrl
	 * @return 
	 */
	protected String padronizaBarras(String strUrl) {
		strUrl = StringUtils.removeEnd(strUrl,slashBar);
		strUrl = StringUtils.removeStart(strUrl,slashBar);
		
		return "/"+strUrl;
	}

	/**
	 * Teste consulta redmine
	 */
	@Test(enabled = false)
	public void testConsultaRedmine() {
		Long[] issueId = new Long[] { 6l, 5l, 4l, 3l };

		List<IssueVO> issues = redmineDAO.pesquisarIssues(issueId);

		Assert.assertTrue(!issues.isEmpty(), "Os issues " + issueId + " realmente não existe?");

		for (IssueVO issueVO : issues) {
			log.info(issueVO.getId() + " " + issueVO.getProjectVO());
		}
	}
	
	
	/**
	 * 
	 */
	@Test(enabled = false)
	public void testReposAfetados(){
		List<RepositorioVO> repos = redmineDAO.affectedRepositories("jazzxdoclet/subprojeto1","f8163358-0f88-44f2-8509-3be3471138aa");
		
		Assert.assertTrue(!repos.isEmpty(),"Era esperado encontrar algo! Verifique o que ocorreu!");
		
		for (RepositorioVO repositorioVO : repos) {
			log.info(repositorioVO.toString());
		}
	}

}
