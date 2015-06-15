package com.msaf.validador.core.tratamentoxls;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.xls.Column;
import com.msaf.validador.core.tratamentoxls.xls.DadosXLSService;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnLink;
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
	
	private String urlPaginaRetorno; //= new String("http://localhost:8080/msaf.validador.web/paginaRetornoServlet?id=");
	
	final String NOME_SERVLET = "/paginaRetornoServlet?dispathMethod=gerarPagina&id=";
	
	HSSFCellStyle cellStyle;
	HSSFCellStyle estiloCabecalho;
	HSSFCellStyle estiloDadoConsultado;
	
	public GeradorXLSRetorno(){
	}
	
	public GeradorXLSRetorno(Repositorio repositorio){
	}
	
	
	


	/**
	 * Estilo de cabecalho.
	 * @param wb 
	 * @return retorna o estilo da celula.
	 * @author Ekler Paulino de Mattos.
	 */
	private HSSFCellStyle estiloCabecalho(HSSFWorkbook wb){
		
		if(this.estiloCabecalho==null){
			this.estiloCabecalho = wb.createCellStyle();
			this.estiloCabecalho.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			this.estiloCabecalho.setBorderRight(HSSFCellStyle.BORDER_THIN);
			this.estiloCabecalho.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			this.estiloCabecalho.setBorderTop(HSSFCellStyle.BORDER_THIN);
			
			this.estiloCabecalho.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		//	cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			this.estiloCabecalho.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			
			this.estiloCabecalho.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			
			HSSFFont font = this.estiloCabecalho.getFont(wb);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFFont.COLOR_NORMAL);
			font.setFontName(HSSFFont.FONT_ARIAL);
			this.estiloCabecalho.setFont(font);
		}
		
		return this.estiloCabecalho;
		
	}
	
	
	/**
	 * Estilo dos campos dos dados.
	 * @param wb 
	 * @return retorna o estilo da celula.
	 * @author Ekler Paulino de Mattos.
	 */
	private HSSFCellStyle estiloDadosRetorno(HSSFWorkbook wb){
		
		if(this.cellStyle==null){
			this.cellStyle = wb.createCellStyle();
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			
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
		}

		return this.cellStyle;
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
	@SuppressWarnings("unchecked")
	public void gerarArquivoXLS(Long idPedido,  OutputStream outputStream) throws LeitorXLSException {
				
		PedValidacaoVO pedido = new PedValidacaoVO();
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenciaProject");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		  
		Query query = entityManager.createQuery(
				"	select ped.pessoaFK " +
				"	from PedValidacaoVO ped " +
				"	left join FETCH ped.pessoaFK " +
				"	where ped.id = :id");
		
		
		query.setParameter("id",idPedido);
		
		List<PessoaVO> pessoas = (List<PessoaVO>) query.getResultList();

		this.ordenarLista(pessoas);
		
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

		int numLinha = 0;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFSheet sheet = wb.createSheet("resultado da pesquisa");
		
		// gera o cabeçalho
		this.criarHeaderXLS(wb, sheet, numLinha);
		
		for (PessoaVO pessoaVO : pessoas) {
			
			Set<DadosRetInstVO> listDadosRetornoInstituicao = pessoaVO.getDadosRetornoFk();
			
			// incremento para a linha de resultados.
			numLinha++;
			
			this.criarBodyXLS(wb, sheet, numLinha, pessoaVO, listDadosRetornoInstituicao);
		}

		wb.write(outputStream);
	}

	
	public String getUrlPaginaRetorno() {
		return urlPaginaRetorno;
	}

	
	public void setUrlPaginaRetorno(String urlPaginaRetorno) {
		this.urlPaginaRetorno = urlPaginaRetorno;
	}

	
	private void criarHeaderXLS(HSSFWorkbook wb, HSSFSheet sheet, int numLinha) {
		
		DadosXLSService service = new DadosXLSService();
		
		List<Column> listHeader = service.getValuesHeader();
		
		HSSFRow row = sheet.createRow(numLinha);
		
		// inicializa as linhas com string vazia para nao gerar erro.
		for (int contador = 0; contador < listHeader.size(); contador++) {
			row.createCell(contador, HSSFCell.CELL_TYPE_STRING).setCellValue(new HSSFRichTextString(""));	
		}
		
		int i = 0;
		
		for (Column headerValue : listHeader) {
			row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(new HSSFRichTextString(headerValue.getValue()));
		}
		
		
		for (int numColuna = 0; numColuna < listHeader.size(); numColuna++) {
			row.getCell(numColuna).setCellStyle(this.estiloCabecalho(wb));
			listTamanhoColunas.add(new Integer(row.getCell(numColuna).getRichStringCellValue().getString().length()));
			autoSizing(sheet, numColuna, row.getCell(numColuna).getRichStringCellValue().getString().length());
		}		
		
	}

	
	private void criarBodyXLS(HSSFWorkbook wb, HSSFSheet sheet, int numLinha, PessoaVO pessoa, Set<DadosRetInstVO> listResultados) {

		DadosXLSService service = new DadosXLSService();
		
		service.prepareBody(pessoa, listResultados);
		
		List<Column> listBody = service.getValuesBody();
		
		
		HSSFRow row = sheet.createRow(numLinha);
		
		// tipo de validacao na coluna 0.
		int i = 0;
		
		
		for(Column value: listBody) {
			
			if(value instanceof ColumnLink) {
				
				// cria o link na planilha
				HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
				
				String url = urlPaginaRetorno + NOME_SERVLET + value.getValue();
				
				link.setAddress(url);
				
				HSSFCell cellLink = row.createCell(i++);
				cellLink.setHyperlink(link);
				
				ColumnLink columnLink = (ColumnLink) value;
				
				cellLink.setCellValue(new HSSFRichTextString(columnLink.getTextoLink()));				
				
			} else {
				row.createCell(i++, HSSFCell.CELL_TYPE_STRING).setCellValue(new HSSFRichTextString(value.getValue()));
			}
			
			
		}
		
			
		// preenchimento da folha de estilo. do mesmo tamanho do cabecalho.
		for (int numColuna = 0; numColuna < listBody.size(); numColuna++) {
			row.getCell(numColuna).setCellStyle(estiloDadosRetorno(wb));
			int tamanhoColuna = row.getCell(numColuna).getRichStringCellValue().getString().length(); 
			autoSizing(sheet, numColuna, tamanhoColuna);
		}
				
	}
	
	
	/**
	 * Ordena a lista através do atributo ordem
	 */
	private void ordenarLista(List<PessoaVO> list){
		
		if(list == null || list.isEmpty()) {
			return;
		}
		
		// Em ordem crescente 
		Collections.sort(list, new Comparator<PessoaVO>() {  
			public int compare(PessoaVO param1, PessoaVO param2) {
				return param1.getOrdem() < param2.getOrdem() ? -1 : (param1.getOrdem() > param2.getOrdem() ? +1 : 0);  
			} 
		}); 
	}
	
}
