package br.com.dlp.jazz.svnhook.services;

import java.util.Collection;
import java.util.List;

import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookException;
import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookLogTimeException;
import br.com.dlp.jazz.svnhook.vos.CitationVO;

/**
 * Serviço para tratamento dos eventos do Subversion. Basicamente prove serviços 
 * que validam o formato do comentário do commit e logam o tempo despendito em cada ticket.
 * 
 * @author t_dpacifico
 *
 */
public interface JazzSVNHookService {

	/**
	 * Critica evento do commit, a partir dos comentario efetuados e dados enviados a seguir.
	 * 
	 * @param dirsChanged
	 * @param repoUUID
	 * @param comments
	 * @param author
	 * @return
	 * @throws JazzSVNHookException Em caso de erros no formato do comentário ou caso haja alguma divergência com os tickets apontados (ex: ticket fechado) 
	 */
	Collection<CitationVO> criticizeComments(List<String> dirsChanged, String repoUUID, String comments, String author) throws JazzSVNHookException;


	/**
	 * Critica e loga o tempo despendido nas alteracoes (executa os outros dos serviços em sequencia)
	 * 
	 * @param dirsChanged
	 * @param repoUUID
	 * @param comments
	 * @param author
	 * @return
	 * @throws JazzSVNHookLogTimeException
	 * @throws JazzSVNHookException
	 */
	Collection<CitationVO> criticizeAndLogTimes(List<String> dirsChanged, String repoUUID, String comments, String author) throws JazzSVNHookException;
	
	/**
	 * Loga o tempo despendido nas alteracoes
	 * @param citacoes
	 * @throws JazzSVNHookLogTimeException 
	 */
	void logTimes(Collection<CitationVO> citacoes) throws JazzSVNHookLogTimeException;
	
	
	
}