package com.msaf.validador.integration.jna;

import com.msaf.validador.consultaonline.ValidadorFacade;
import com.sun.jna.win32.StdCallLibrary;


/**
 * Interface JNA para execução de consulta a DLL do Validador. Esta Interface
 * obedece ao mesmo contrato de utilização da DLL do Validador.
 * 
 * Não confunfir com a interface do WebService do Validador (
 * {@link ValidadorFacade}), que é cliente desta interface.
 * 
 * @author dlopes
 * 
 */
public interface IConsultaOnLineJNA extends StdCallLibrary {

	/**
	 * Consulta dados de cliente pela DLL do validador
	 * 
	 * @return id de ponteiro para limpeza de memória: serve de entrada para a
	 *         operação DBI_DLLStrFree
	 * @param servico
	 *            Variável do tipo inteiro, para determinar o tipo da consulta.
	 *            1 – Sintegra
	 * @param parametros
	 *            Variável do tipo Pchar (null terminated string) contendo o XML
	 *            com os parâmetros variáveis de cada consulta e as
	 *            configurações de proxy. Utilizando o formato como descrito no
	 *            exemplo para consulta de um CNPJ no estado de Santa Catarina:
	 *            <?xml version="1.0" encoding="iso-8859-1"?>
	 *            <ParametrosConsulta Versao="1.0">
	 *            <TipoConsulta>0</TipoConsulta> <Estado>SC</Estado>
	 *            <Valor>07613982000136</Valor> <Proxy> <Ativo>True</Ativo>
	 *            <Usuario/> <Senha/> <Host>192.168.10.24</Host>
	 *            <Porta>8002</Porta> </Proxy> </ParametrosConsulta>
	 * @param dllPath
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontram as DLLs e os arquivos
	 *            auxiliares. Ex.: C:\EvServer\DLL_OnLine
	 * 
	 * @param evServer
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontra o instalado o EvServer. Ex.:
	 *            C:\EvServer
	 */

	public String DBI_EfetuaConsultaServico(Integer servico, String parametros,
			String dllPath, String evServer);

	/**
	 * Limpa área de memória utilizada pela operacao DBI_EfetuaConsultaServico.
	 * Deve ser chamada após cada chamada da operação DBI_EfetuaConsultaServico
	 * 
	 * @param memId
	 *            endereço de memória: ver retorno da operação
	 *            DBI_EfetuaConsultaServico
	 * 
	 */
	public void DBI_DLLStrFree(String memId);

}
