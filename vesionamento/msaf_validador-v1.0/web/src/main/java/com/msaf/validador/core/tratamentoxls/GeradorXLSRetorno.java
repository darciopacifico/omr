package com.msaf.validador.core.tratamentoxls;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.msaf.validador.consultaonline.TiposServicoXMLFactory;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.integration.hibernate.Repositorio;


/**
 * Gerador de XLS de retorno
 * 
 * @author dlopes
 * 
 */
public class GeradorXLSRetorno {
	private List<Integer> listTamanhoColunas = new ArrayList<Integer>();

	Logger log = Logger.getLogger(LeitorXLSCliente.class);
	
	private List<String> vetCabecalho = new ArrayList<String>();

	private Repositorio repositorio;
	
	private String urlPaginaRetorno; //= new String("http://localhost:8080/msaf.validador.web/paginaRetornoServlet?id=");
	
	final String NOME_SERVLET = "/paginaRetornoServlet?id=";
	
	private static String STR_PAGINA_INEXISTENTE = new String("(Dados não encontrados!)");
	
	public GeradorXLSRetorno(){
		populaCabecalho();
	}
	
	public GeradorXLSRetorno(Repositorio repositorio){
		populaCabecalho();
		this.repositorio = repositorio;
	}
	
	
	private void populaCabecalho(){
		this.vetCabecalho = new ArrayList<String>();
		this.vetCabecalho.add("ID CLIENTE");
		this.vetCabecalho.add("RAZAO SOCIAL");
		this.vetCabecalho.add("LOGRADOURO");
		this.vetCabecalho.add("NUMERO");
		this.vetCabecalho.add("COMPLEMENTO");
		this.vetCabecalho.add("BAIRRO");
		this.vetCabecalho.add("CEP");
		this.vetCabecalho.add("CIDADE");
		this.vetCabecalho.add("ESTADO");
		this.vetCabecalho.add("CNPJ");
		this.vetCabecalho.add("IE");
		this.vetCabecalho.add("CPF");
		
		this.vetCabecalho.add("TIPO VALIDAÇÃO");
		this.vetCabecalho.add("NOME VALIDAÇÃO");
		this.vetCabecalho.add("ID MASTERSAF");
		this.vetCabecalho.add("TIPO RESULTADO");
		this.vetCabecalho.add("DESCRICAO RESULTADO");
		
		this.vetCabecalho.add("NOME FANTASIA");
		this.vetCabecalho.add("DATA CONSULTA");
		this.vetCabecalho.add("SITUACAO");
		this.vetCabecalho.add("CODIGO IBGE");
		this.vetCabecalho.add("NUMERO CONSULTA");
		this.vetCabecalho.add("HORA CONSULTA");
		this.vetCabecalho.add("TIPO INSCRICAO");
		this.vetCabecalho.add("ESPECIALIDADE");
		this.vetCabecalho.add("DATA BAIXA");
		this.vetCabecalho.add("BANDEIRA POSTO");
		this.vetCabecalho.add("AUTORIZACAO");
		this.vetCabecalho.add("IE2");
		this.vetCabecalho.add("IES ENCONTRADAS");
		
		this.vetCabecalho.add("TIPO POSTO");
		this.vetCabecalho.add("DATA INCLUSAO");
		this.vetCabecalho.add("DATA PUBLICACAO");
		this.vetCabecalho.add("DATA INSCSUFRAMA");
		this.vetCabecalho.add("DATA VALIDADE");
		this.vetCabecalho.add("TIPO INCENTIVO");
		this.vetCabecalho.add("SUMAFRA");
		this.vetCabecalho.add("CRM");
		this.vetCabecalho.add("QTDE ENCONTRADA");
		this.vetCabecalho.add("INCENTIVO");
		this.vetCabecalho.add("REGIME APURACAO");
		
		this.vetCabecalho.add("TSENTATSA");
		this.vetCabecalho.add("TIPO DOCUMENTO");
		this.vetCabecalho.add("ATIVIDADE ECONOMICA");
		this.vetCabecalho.add("ENQUANDRAMENTO EMPRESA");
		
		this.vetCabecalho.add("DATA ABERTURA");
		this.vetCabecalho.add("CODIGO AE");
		this.vetCabecalho.add("DESCRICAO AE");
		this.vetCabecalho.add("CODIGO NJ");
		this.vetCabecalho.add("DESCRICAO NJ");
		this.vetCabecalho.add("DATA SITUACAO");
		this.vetCabecalho.add("SITUACAO ESPECIAL");
		this.vetCabecalho.add("DATA SITUACAO ESPECIAL");
		this.vetCabecalho.add("CERTIFICADO / EVIDÊNCIA CONSULTA");

		

	}
	
	private HSSFRow criarCorpoArquivoPessoa(HSSFWorkbook wb, HSSFSheet sheet, int linha,
			PessoaVO pessoaVO) {
		final String REGISTRO = new String("Reg. Consultado");
		final String VAZIO = new String("");
		
		HSSFRow row = sheet.createRow(linha);
		
		// inicializa as linhas com string vazia para nao gerar erro.
		for (int contador = 0; contador < this.vetCabecalho.size(); contador++) {
			row.createCell(contador, HSSFCell.CELL_TYPE_STRING ).setCellValue(VAZIO);	
		}
		
		int i = 0;
		
		HSSFCell idInicial = row.createCell(i++, HSSFCell.CELL_TYPE_STRING ); 
		idInicial.setCellValue(pessoaVO.getIdentif());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(pessoaVO.getRazaoSocial());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getLogradouro());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getNumero());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getComplemento());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getBairro());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getCep());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getCidade());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getEstado());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(pessoaVO.getCnpj());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getIe());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getCpf());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue("" + pessoaVO.getId());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getDataConsulta());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getSituacao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getNumeroConsulta());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getDataBaixa());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getIesEncontradas());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getDataInclusao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getQtdeEncontrada());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getRegimeApuracao());		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(pessoaVO.getEnquandramentoEmpresa());
		
		
		
		
		
		// preenchimento da folha de estilo.
		for (int numColuna = 0; numColuna < this.vetCabecalho.size(); numColuna++) {
			row.getCell( numColuna).setCellStyle(estiloDadosConsultado(wb));
			
			int tamanhoColuna = row.getCell( numColuna).getStringCellValue().length(); 
			autoSizing(sheet, numColuna, tamanhoColuna);
		}
			
		return row;
		
	}


	/**
	 * Cria o corpo do arquivo.
	 * @param wb
	 * @param sheet
	 * @param numLinha
	 * @param dadosRetornoInstituicaoVO
	 * @return
	 */
	private HSSFRow criarCorpoArquivo(HSSFWorkbook wb, HSSFSheet sheet, int numLinha, DadosRetInstVO dadosRetornoInstituicaoVO) {
		
		HSSFRow row = sheet.createRow(numLinha);
		// tipo de validacao na coluna 0.
		int i = 0;
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getIdentif());
		
		if(isReceitaPessoaFisica(dadosRetornoInstituicaoVO)){
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getNome());
			
		}else{
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getRazaoSocial());
			
		}
		
		
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getLogradouro());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getNumero());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getComplemento());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getBairro());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCep());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCidade());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getEstado());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getCnpj());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getIe());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCpf());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue("" + dadosRetornoInstituicaoVO.getTipoValidacaoFk().getId());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getTipoValidacaoFk().getDescricao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue("" + dadosRetornoInstituicaoVO.getId());
		
		TpResultVO tpResultVO = dadosRetornoInstituicaoVO.getTipoResultadoFk();
		
		if(tpResultVO==null){
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue("");
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue("");
			
		}else{
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue("" + dadosRetornoInstituicaoVO.getTipoResultadoFk().getId());
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTipoResultadoFk().getDescricao());

		}
		

		row.createCell(i++, HSSFCell.CELL_TYPE_STRING ).setCellValue(dadosRetornoInstituicaoVO.getNomeFantasia());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataConsulta());
		
		
		if(isReceitaPessoaFisica(dadosRetornoInstituicaoVO)){
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getSituacaoCadastral());
			
		}else{ 
			
			String situacaoRetorno = dadosRetornoInstituicaoVO.getSituacao();
			
			if(dadosRetornoInstituicaoVO.getTipoValidacaoFk().getId().intValue() == TiposServicoXMLFactory.SINTEGRA
				&&	dadosRetornoInstituicaoVO.getTipoResultadoFk().getId() == 10000){
				
				if(situacaoRetorno== null || situacaoRetorno.length()==0){
					row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue("NAO HABILITADO");
				}else{
					row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(situacaoRetorno);
				}
				
			}else{
				row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(situacaoRetorno);
			}
			
		}
		
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCodigoIBGE());
		
	
		
		if(isReceitaPessoaFisica(dadosRetornoInstituicaoVO)){
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCodigoConsulta());
			
		}else{
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getNumeroConsulta());

		}
		
		
		
		
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getHoraConsulta());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTipoInscricao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getEspecialidade());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataBaixa());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getBandeiraPosto());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getAutorizacao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getIe2());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getIesEncontradas());

		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTipoPosto());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataInclusao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataPublicacao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataInscSuframa());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataValidade());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTipoIncentivo());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getSumafra());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCRM());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getQtdeEncontrada());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getIncentivo());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getRegimeApuracao());
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTsentaTSA());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getTipoDocumento());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getAtividadeEconomica());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getEnquandramentoEmpresa());
		
		
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataAbertura());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCodigoAE());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDescricaoAE());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getCodigoNJ());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDescricaoNJ());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataSituacao());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getSituacaoEspecial());
		row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(dadosRetornoInstituicaoVO.getDataSituacaoEspecial());
		
		// tratamento para pagina inexistente.
		if((dadosRetornoInstituicaoVO.getPagina() == null)) {
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(STR_PAGINA_INEXISTENTE);
		} else row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(urlPaginaRetorno + NOME_SERVLET + dadosRetornoInstituicaoVO.getId());
			
		// preenchimento da folha de estilo. do mesmo tamanho do cabecalho.
		for (int numColuna = 0; numColuna < vetCabecalho.size(); numColuna++) {
			row.getCell( numColuna).setCellStyle(estiloDadosRetorno(wb));
			
			int tamanhoColuna = row.getCell( numColuna).getStringCellValue().length(); 
			autoSizing(sheet, numColuna, tamanhoColuna);
		}
			
		return row;
		
	}

	
	/**
	 * Determina se o dado de retorno é de uma pessoa física e consulta na receita federal
	 * @param dadosRetornoInstituicaoVO
	 * @return
	 */
	protected boolean isReceitaPessoaFisica(DadosRetInstVO dadosRetornoInstituicaoVO) {
		
		String cpf = dadosRetornoInstituicaoVO.getCpf();
		TpValidVO tpValidVO = dadosRetornoInstituicaoVO.getTipoValidacaoFk();
		
		boolean pfReceita = 
			cpf!=null &&
			(tpValidVO.getId().intValue() ==  TiposServicoXMLFactory.RECEITA_FEDERAL);
		
		return pfReceita;
	}

	/**
	 * Estilo de cabecalho.
	 * @param wb 
	 * @return retorna o estilo da celula.
	 * @author Ekler Paulino de Mattos.
	 */
	private HSSFCellStyle estiloCabecalho(HSSFWorkbook wb){
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);
		
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
	//	cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		HSSFFont font = cellStyle.getFont(wb);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle.setFont(font);
		
		return cellStyle;
		
	}
	
	
	/**
	 * Estilo dos campos dos dados.
	 * @param wb 
	 * @return retorna o estilo da celula.
	 * @author Ekler Paulino de Mattos.
	 */
	private HSSFCellStyle estiloDadosConsultado(HSSFWorkbook wb){
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		// plano de fundo - para que funcione deve se  
		// definido antes um padrao de preenchimento.
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		//cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		HSSFFont font = cellStyle.getFont(wb);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle.setFont(font);
		
		return cellStyle;
		
	}
	
	
	/**
	 * Estilo dos campos dos dados.
	 * @param wb 
	 * @return retorna o estilo da celula.
	 * @author Ekler Paulino de Mattos.
	 */
	private HSSFCellStyle estiloDadosRetorno(HSSFWorkbook wb){
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		// plano de fundo - para que funcione deve se  
		// definido antes um padrao de preenchimento.
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		//cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		
		HSSFFont font = cellStyle.getFont(wb);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle.setFont(font);
		
		return cellStyle;
		
	}
	
	/**
	 * Criacao do cabecalho do arquivo.
	 * @param sheet 
	 * @param wb
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	private HSSFRow criarMetadata(HSSFSheet sheet, HSSFWorkbook wb) {
		HSSFRow row = sheet.createRow(0);
				
		for (int i = 0; i< this.vetCabecalho.size(); i++) {
			row.createCell(i).setCellValue(vetCabecalho.get(i));
		}
		
		
		for (int i = 0; i < this.vetCabecalho.size(); i++) {
			row.getCell( i).setCellStyle(this.estiloCabecalho(wb));
			
			// alimenta o vetor tomando como tamannho 
			// inicial o tamanho das colunas do cabecalho.
			listTamanhoColunas.add(new Integer(row.getCell( i).getStringCellValue().length()));
			autoSizing(sheet, i, row.getCell(i).getStringCellValue().length());
			
		}
		
		return row;
	}
	
    /**
     * Define o maior tamanho para as colunas e armazena no vetor. 
     * para a criacao de colunas maiores.
     * @param tamanhoColuna
     * @param posicaoColuna
     */
	private int defineMaiorTamanhoColunas(int tamanhoColuna, int numColuna) {
	 int maiorTamanho = listTamanhoColunas.get(numColuna);
		// troca o tamanho da coluna por uma maior.
		if (maiorTamanho < tamanhoColuna) {
			listTamanhoColunas.set(numColuna, new Integer(tamanhoColuna));
			maiorTamanho = tamanhoColuna;
		}

		return maiorTamanho;
	}
	
	/**
	 * define um tamanho padrao para cada celula.
	 * @param sheet
	 * @param numColuna - coluna a se aplicado o tamanho.
	 * @param tamanhoCelula - o tamanho da celula
	 * @return
	 */
	private void autoSizing( HSSFSheet sheet, int numColuna, int tamanhoCelula){
		// define qual eh o maior tamanho da celula.
		int maiorColuna = defineMaiorTamanhoColunas(tamanhoCelula, numColuna);
		sheet.setColumnWidth( numColuna, (short) ( ( 17 * maiorColuna ) / ( (double) 1 / 20 ) ) );
	}


	/**
	 * Gera os arquivos xls das pessoas a partir dos pedidos armazenados na base
	 * de dados.
	 * 
	 * @param idPessoa
	 *          - identificador do pedido.
	 * @author Ekler Paulino de Mattos
	 * @return
	 * @throws LeitorXLSException
	 */
	public void gerarArquivoXLS(Long idPedido,  OutputStream outputStream) throws LeitorXLSException {
				
		PedValidacaoVO pedido = new PedValidacaoVO();
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenciaProject");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		  
		Query query = entityManager.createQuery(
				"	select ped.pessoaFK " +
				"	from PedValidacaoVO ped " +
				"	left join FETCH ped.pessoaFK " +
				"	where ped.id = :id ");
		
		
		query.setParameter("id",idPedido);
		List<PessoaVO> pessoas = (List<PessoaVO>) query.getResultList();
		
		try {
			gerarXLS(pessoas, pedido, outputStream);
		} catch (IOException e) {
			throw new LeitorXLSException("Erro ao tentar escrever arquivo de resultado em outputstream!", e);
		}
	}

	/**
	 * 
	 * @param pessoas
	 *          - objeto de pessoa
	 * @return
	 * @throws FileNotFoundException
	 *           - excecao lancada caso o arquivo haja problemas na geracao do
	 *           arquivo.
	 * @throws IOException
	 *           - excecao de
	 */
	private void gerarXLS(List<PessoaVO> pessoas, PedValidacaoVO pedValidacaoVO, OutputStream outputStream) throws FileNotFoundException, IOException {

		int numLinha = 1;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("resultado da pesquisa");
		for (PessoaVO pessoaVO : pessoas) {
			Set<DadosRetInstVO> listDadosRetornoInstituicao = pessoaVO.getDadosRetornoFk();

			@SuppressWarnings("unused")
			HSSFRow row = criarMetadata(sheet, wb);

			row = criarCorpoArquivoPessoa(wb, sheet, numLinha, pessoaVO);
			// incremento para a linha de resultados.

			numLinha++;
			for (DadosRetInstVO dadosRetornoInstituicaoVO : listDadosRetornoInstituicao) {
				// HSSFRow row = sheet.getRow(0);
				// HSSFRow row = sheet.getRow(0);
				row = criarCorpoArquivo(wb, sheet, numLinha, dadosRetornoInstituicaoVO);
				numLinha++;
			}
		}

		wb.write(outputStream);
	}

	
	public String getUrlPaginaRetorno() {
		return urlPaginaRetorno;
	}

	
	public void setUrlPaginaRetorno(String urlPaginaRetorno) {
		this.urlPaginaRetorno = urlPaginaRetorno;
	}

	
	
	
}
