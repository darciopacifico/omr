/**
 * 
 */
package com.msaf.validador.core.tratamentoxls;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.msaf.validador.consultaonline.exceptions.ArquivoInvalidoException;
import com.msaf.validador.consultaonline.exceptions.ProcessamentoArquivoException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.exceptions.VersaoExcelIncorreta;
import com.msaf.validador.core.lerXLS.LeitorXLSException;

/**
 * @author dlopes
 *
 */
public class VerificadorXLSCliente implements HSSFListener {

	
	protected static final int PRIMEIRA_LINHA_XLS = 0;
	
	
	//Dependências para execução do trabalho
	protected Logger log = Logger.getLogger(LeitorXLSCliente.class);

	//variáveis de instância para controle do processamento 
	protected Record xlsRecordAtual;
	
	protected SSTRecord sstrec;
	
	//controles da implementação do algoritmo de leitura do XLS
	protected boolean mudouDeLinha;
	protected boolean leuPrimeiraLinha;
	protected int colunaAtualDoProcessRecord;
	protected int linhaAtualDoProcessRecord;
	protected boolean criadoNovoRegistro;

	
	/**
	 * Constroi o leitor xls do cliente. A instância deste objeto deve ser
	 * específica de um pedido de validação e não mais pode ser usada depois do
	 * processamento
	 * 
	 * @param facadeMQImpl
	 * @param tiposValidacao
	 * @param pedValidacao
	 */
	public VerificadorXLSCliente() {
	}

	/**
	 * Lê arquivo enviado e gera mensagens JMS.
	 * 
	 * Instancia request e starta leitura do arquivo com esta instância (this) de
	 * objeto como listener
	 * 
	 * @param inputStream
	 * @throws LeitorXLSException
	 * @throws ArquivoInvalidoException 
	 */
	public Integer contarLinhas(InputStream inputStream) throws LeitorXLSException, ArquivoInvalidoException {
		try {
			processarArquivoIO(inputStream);
			return this.linhaAtualDoProcessRecord;
		} catch (IOException e) {
			throw new LeitorXLSException("Erro ao tentar processar validacao de arquivo de clientes!",e);
		}
	}
	
	
	/**
	 * Lê arquivo enviado e gera mensagens JMS.
	 * 
	 * Instancia request e starta leitura do arquivo com esta instância (this) de
	 * objeto como listener
	 * 
	 * @param inputStream
	 * @throws ArquivoInvalidoException 
	 * @throws IOException 
	 * @throws IOException
	 * 
	 */
	protected void processarArquivoIO(InputStream inputStream) throws ArquivoInvalidoException, IOException {
		POIFSFileSystem poifs;
		try{
			poifs = new POIFSFileSystem(inputStream);
		}catch(OfficeXmlFileException e){
			throw new VersaoExcelIncorreta("A versao do arquivo enviada nao é válida!",e);
			
		} catch (IOException e) {
			throw new ArquivoInvalidoException("A versao do arquivo enviada nao é válida!",e);
			
		}
		
		InputStream din = poifs.createDocumentInputStream("Workbook");
		try {


			HSSFRequest req = new HSSFRequest();
			req.addListenerForAllRecords(this);

			HSSFEventFactory factory = new HSSFEventFactory();
			factory.processEvents(req, din);

		} finally {
			inputStream.close();
			din.close();
		}
	}

	/**
	 * Cabeçote de leitura do LeitorXLSCliente. Implementação do método
	 * processRecord da interface HSSFListener do POI {@link HSSFListener}.
	 * <BR><BR>
	 * A implementação de leitura de XLS do POI HSSF faz a leitura linear dos
	 * dados do arquivo, acionando o método processRecord a cada registro
	 * encontrado. Técnica semelhante à leitura de um XML por SAX.
	 * <BR><BR>
	 * @param record elemento do Arquivo excel que está sendo lido no momento.
	 */
	public void processRecord(Record record) {
		this.xlsRecordAtual = record;
				
		
		if(isPermiteLerLinhaColuna()){
			atualizaControlePosicaoAtual();
			
			if (isEnviarMensagemJMS() && criadoNovoRegistro) {
				enviarMensagemJMS();
				criarNovoRegistro();
				this.leuPrimeiraLinha = true;
				this.criadoNovoRegistro = false;
			}
			
			/*
			if (isCriarNovoRegistro() && !criadoNovoRegistro) {
				this.criadoNovoRegistro = true;
			}
			
			if (isLerValorDoRecord()) {
				popularRegistroAtual();
			}
			 */
			
		}		

		if (record.getSid()==SSTRecord.sid){
			sstrec = (SSTRecord) record;
		}
		
		
	}

	/**
	 * Testa se o cabeçote de leitura chegou ao final do arquivo
	 * @param record último registro lido
	 * @return
	 */
	protected boolean isFinalDeArquivo() {
		
		return this.leuPrimeiraLinha && this.xlsRecordAtual.getSid()==EOFRecord.sid;
	}
	


	/**
	 * Recuper último valor lido pelo cabeçote de leitura do XLS
	 * @return
	 */
	protected String getValorLido() {
		String valorLido="";
		
		if(xlsRecordAtual.getSid()== LabelSSTRecord.sid){
			LabelSSTRecord labelSSTRecord = (LabelSSTRecord) xlsRecordAtual;
			valorLido = sstrec.getString(labelSSTRecord.getSSTIndex()).toString();
			
		}else if(xlsRecordAtual.getSid() == NumberRecord.sid){
			NumberRecord numberRecord = (NumberRecord) xlsRecordAtual;
			
			valorLido = ""+numberRecord.getValue();
		}else{
			
			log.warn("Atencao. A colula, linha nao pode ser lida!");
		}
		return valorLido;
	}

	/**
	 * Verifica se o record atual tem valor para ser lido
	 * @return
	 */
	protected boolean isLerValorDoRecord() {
		return (isPermiteLerLinhaColuna() && !isPrimeiraLinha());
	}

	/**
	 * 
	 * 
	 * @throws ControlePosicaoXLSException 
	 * 
	 *  
	 */
	protected void atualizaControlePosicaoAtual()  {
		if(isPermiteLerLinhaColuna() ){
			try {
				colunaAtualDoProcessRecord = getColunaAtual();
				
				int linhaLeitura = getLinhaAtual();
				
				//Checa se houve mudança de linha
				if(linhaLeitura != linhaAtualDoProcessRecord){
					this.mudouDeLinha = true;
				}else{
					this.mudouDeLinha = false;
				}
				
				//atualiza linha atual após checagem de mudança de linha
				linhaAtualDoProcessRecord = getLinhaAtual();
				
				
				//if(log.isDebugEnabled()) log.debug("Linha:"+linhaAtualDoProcessRecord+" coluna:"+colunaAtualDoProcessRecord+" mudou:"+mudouDeLinha+"");
				
			} catch (ControlePosicaoXLSException e) {
				throw new ValidadorRuntimeException("Erro de lógica. Não foi possível determinar posição de linha e coluna!",e);
			}
		}
	}


	/**
	 * Recupera linha atual a partir do recor que acabou de ser lifo
	 * @return
	 * @throws ControlePosicaoXLSException
	 */
	protected int getLinhaAtual() throws ControlePosicaoXLSException{
		
		
		if(this.xlsRecordAtual.getSid()== LabelSSTRecord.sid){
			LabelSSTRecord labelSSTRecord = (LabelSSTRecord) this.xlsRecordAtual;
			return labelSSTRecord.getRow();			
		}else if(this.xlsRecordAtual.getSid() == NumberRecord.sid){
			NumberRecord numberRecord = (NumberRecord) this.xlsRecordAtual;
			return numberRecord.getRow();			
		}
		
		throw new ControlePosicaoXLSException("O record atual não contém informacoes sobre linha e coluna!");
	}
	

	/**
	 * Recupera coluna atual a partir do record que acabou de ser lido
	 * @return
	 * @throws ControlePosicaoXLSException caso o record atual não permita leitura de coluna e linha
	 */
	protected short getColunaAtual() throws ControlePosicaoXLSException {
		if(xlsRecordAtual.getSid()== LabelSSTRecord.sid){
			LabelSSTRecord labelSSTRecord = (LabelSSTRecord) xlsRecordAtual;
			return labelSSTRecord.getColumn();
			
		}else if(xlsRecordAtual.getSid() == NumberRecord.sid){
			NumberRecord numberRecord = (NumberRecord) xlsRecordAtual;
			return numberRecord.getColumn();
		}
		
		throw new ControlePosicaoXLSException("O record atual não contém informacoes sobre linha e coluna!");
	}

	
	/**
	 * Verifica se o Record atual permite ler informações sobre linha e coluna
	 * @return
	 */
	protected boolean isPermiteLerLinhaColuna() {
		// Apenas os tipos LabelSSTRecord e NumberRecord permitem ler linha e coluna 
		return 
		this.xlsRecordAtual.getSid()==LabelSSTRecord.sid || 
		this.xlsRecordAtual.getSid() ==NumberRecord.sid;
	}

	
	/**
	 * Em funcão do estado da leitura determina se uma mensagem JMS deve ser
	 * enviada
	 * 
	 * @return
	 */
	protected boolean isEnviarMensagemJMS() {
		
		/**
		 * - Não processar a primeira linha
		 * - Garantir que a leitura da linha foi completa
		 */
		boolean primeiraLinha = isPrimeiraLinha();
		boolean leituraLinhaCompletada = isLeituraLinhaCompletada();
		
		return (!primeiraLinha && leituraLinhaCompletada );
	}
	
	

	/**
	 * Em funcão do estado da leitura do arquivo determina se a linha foi lida completamente
	 * @return
	 */
	protected boolean isLeituraLinhaCompletada() {
		return(isMudancaLinha() || colunaAtualDoProcessRecord >= LeitorXLSCliente.XLS_MAXIMO_DE_COLUNAS );
	}

	/**
	 * retorna valor do atributo de controle mudouDeLinha
	 * @return
	 */
	protected boolean isMudancaLinha() {
		return this.mudouDeLinha;
	}

	
	/**
	 * Testa se a linha atual é a primeira linha 
	 * @return
	 */
	protected boolean isPrimeiraLinha() {
		return this.linhaAtualDoProcessRecord==PRIMEIRA_LINHA_XLS;
	}

	/**
	 * Em funcão do estado da leitura do arquivo determina se um novo registro
	 * deve ser criado
	 * 
	 * @return
	 */
	protected boolean isCriarNovoRegistro() {
	
		boolean mudancaLinha = isMudancaLinha();
		boolean primeiraLinha = isPrimeiraLinha();
		boolean leituraLinhaCompletada = isLeituraLinhaCompletada();
		
		
		if(mudancaLinha && !primeiraLinha && leituraLinhaCompletada){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Envia mensagem JMS com os dados da linha do Excel que acabou de ser lida. É
	 * chamado várias vezes ao longo da leitura do arquivo vindo por inputStream
	 * @throws ProcessamentoArquivoException 
	 */
	protected void enviarMensagemJMS() {
		
		
	}

	/**
	 * Cria um novo registro de pessoa no atributo de classe this.pessoaVO. É
	 * chamado várias vezes ao longo da leitura do arquivo vindo por inputStream
	 */
	protected void criarNovoRegistro() {
	}

	/**
	 * Codigo Temporario: Mapeamento fixo fechado com a area de negocios
	 * 
	 * @param value
	 * @param coluna
	 */
	protected void popularRegistroAtual() {
		this.leuPrimeiraLinha = true;
		String value = getValorLido();

	}


	
	
}
