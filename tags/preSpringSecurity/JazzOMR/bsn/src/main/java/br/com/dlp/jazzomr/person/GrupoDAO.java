/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.IJazzOMRDAO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente GrupoVO
 *
 **/
public interface GrupoDAO extends IJazzOMRDAO<GrupoVO>{



	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	List<GrupoVO> findGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	Long countGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de GrupoVO
	 */
	List<GrupoVO> findGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);

	
}

