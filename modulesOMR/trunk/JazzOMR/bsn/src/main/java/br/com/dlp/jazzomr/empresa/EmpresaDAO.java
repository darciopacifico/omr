/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.empresa;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.IJazzOMRDAO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente EmpresaVO
 *
 **/
public interface EmpresaDAO extends IJazzOMRDAO<EmpresaVO>{



	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	Long countEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de EmpresaVO
	 */
	List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);

	
}

