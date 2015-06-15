package com.msaf.validador.lerXls;
//package com.msaf.validador.consumer;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.MessageFormat;
//import java.text.SimpleDateFormat;
//
//import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
//import org.apache.poi.hssf.eventusermodel.HSSFListener;
//import org.apache.poi.hssf.eventusermodel.HSSFRequest;
//import org.apache.poi.hssf.record.BOFRecord;
//import org.apache.poi.hssf.record.BoundSheetRecord;
//import org.apache.poi.hssf.record.IndexRecord;
//import org.apache.poi.hssf.record.InterfaceEndRecord;
//import org.apache.poi.hssf.record.LabelSSTRecord;
//import org.apache.poi.hssf.record.NameRecord;
//import org.apache.poi.hssf.record.NumberRecord;
//import org.apache.poi.hssf.record.PrintGridlinesRecord;
//import org.apache.poi.hssf.record.Record;
//import org.apache.poi.hssf.record.RowRecord;
//import org.apache.poi.hssf.record.SSTRecord;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//
//import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetornoInstituicaoVO;
//
//
//public class Teste3 implements HSSFListener {
//	private SSTRecord sstrec;
//
//	private static String s = "";
//
//	private int totalDeLinhas = 0;
//	private int totalDeColunas = 0;
//	private DadosRetornoInstituicaoVO retornoInstituicaoVO = new DadosRetornoInstituicaoVO();
//	@Override
//	public void processRecord(Record record) {
//		String value;
//		int coluna;
//		
//		switch (record.getSid()) {
//
//		case RowRecord.sid:
//			RowRecord rowrec = (RowRecord) record;
//			if(rowrec.getRowNumber() == 0) totalDeColunas = rowrec.getLastCol();
//			totalDeLinhas++;
//			break;
//
//		case SSTRecord.sid:
//			sstrec = (SSTRecord) record;
//			break;
//			
//		case LabelSSTRecord.sid:
//			LabelSSTRecord lrec = (LabelSSTRecord) record;
//			if(0!=lrec.getRow()){
//				value = sstrec.getString(lrec.getSSTIndex()).toString();
//				coluna = (int)lrec.getColumn();
//				popularDadosRetornoInstituicao(value, coluna);
//				if(finalDeLinha(lrec)){
//					dispararJMS(retornoInstituicaoVO);
//					cleanRetornoInstituicaoVO();
//				}
//				finalDoArquivo(lrec);
//			}
//			break;
//		
//		case NumberRecord.sid:
//			NumberRecord numrec = (NumberRecord) record;
//			Double doublee = numrec.getValue();
//			Long l = doublee.longValue();
//			value = l.toString();
//			coluna = (int)numrec.getColumn();
//			if(0!=numrec.getRow()){
//				
//				popularDadosRetornoInstituicao(value, coluna);
//				if(finalDeLinha(numrec)){
//					dispararJMS(retornoInstituicaoVO);
//					cleanRetornoInstituicaoVO();
//				}	
//				finalDoArquivo(numrec);
//			}
//			break;
//		}
//
//	}
//
//
//	private void dispararJMS(DadosRetornoInstituicaoVO retornoInstituicaoVO2) {
//		if(log.isDebugEnabled()) log.debug("TESTE: " + retornoInstituicaoVO2.getLogradouro());
//		
//	}
//
//
//	private void cleanRetornoInstituicaoVO() {
//		this.retornoInstituicaoVO = new DadosRetornoInstituicaoVO();
//		
//	}
//
//	/**
//	 * Codigo Temporario: Mapeamento fixo fechado com a area de negocios
//	 * 
//	 * 
//	 * @param value
//	 * @param coluna
//	 */
//	private void popularDadosRetornoInstituicao(String value, int coluna){
//		switch (coluna) {
//		case 0:
//			this.retornoInstituicaoVO.setIdDadosRetInst(Long.parseLong(value));
//			break;
//		case 1:
//			this.retornoInstituicaoVO.setLogradouro(value);
//			break;
//		case 2:
//			this.retornoInstituicaoVO.setNumero(value);
//			break;
//		case 3:
//			this.retornoInstituicaoVO.setComplemento(value);
//			break;
//		case 4:
//			this.retornoInstituicaoVO.setBairro(value);
//			break;
//		case 5:
//			this.retornoInstituicaoVO.setCep(value);
//			break;
//		case 6:
//			this.retornoInstituicaoVO.setCidade(value);
//			break;
//		case 7:
//			this.retornoInstituicaoVO.setEstado(value);
//			break;
//		case 8:
//			this.retornoInstituicaoVO.setEstado(value);
//			break;
//		case 9:
//			this.retornoInstituicaoVO.setCnpj(value);
//			break;
//		case 10:
//			this.retornoInstituicaoVO.setIe(value);
//			break;
//		}
//		
//	}
//	private DadosRetornoInstituicaoVO getDadosRetornoInstituicao(){
//		return retornoInstituicaoVO;
//	}
//	private boolean finalDoArquivo(NumberRecord numrec) {
//		int row = numrec.getRow();
//		int column = numrec.getColumn();
//			return (totalDeLinhas == row+1 && totalDeColunas == column+1);
//	}
//	private boolean finalDoArquivo(LabelSSTRecord lrec) {
//		int row = lrec.getRow();
//		int column = lrec.getColumn();
//		return (totalDeLinhas == row+1 && totalDeColunas == column+1); 
//	}
//	private boolean finalDeLinha(LabelSSTRecord lrec) {
//		int column = lrec.getColumn();
//		return (totalDeColunas == column+1); 
//	}
//	private boolean finalDeLinha(NumberRecord numrec) {
//		int column = numrec.getColumn();
//		return (totalDeColunas == column+1); 
//	}
//
//	public static void main(String[] args) throws IOException {
//		String string = "C:/excel/ModeloParaTrabalho03.xls";
//		FileInputStream fin = new FileInputStream(string);
//		POIFSFileSystem poifs = new POIFSFileSystem(fin);
//		InputStream din = poifs.createDocumentInputStream("Workbook");
//		
//		HSSFRequest req = new HSSFRequest();
//		req.addListenerForAllRecords(new Teste3());
//		HSSFEventFactory factory = new HSSFEventFactory();
//		factory.processEvents(req, din);
//		fin.close();
//		din.close();
//		if(log.isDebugEnabled()) log.debug(s);
//	}
//}
