package com.msaf.validador.lerXls;
//package com.msaf.validador.consumer;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.MessageFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//import org.apache.poi.hssf.model.Sheet;
//import org.apache.poi.hssf.model.Workbook;
//import org.apache.poi.hssf.record.formula.functions.Row;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//
//import sun.misc.MessageUtils;
//
//import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
//
//public class teste {
//	public static void main(String[] args) throws IOException {
//		
//		matodoPOI();
//		
////	    typeCell();
//		
//
//	}
//
//
//
//	private static void typeCell() throws FileNotFoundException, IOException {
//		HSSFWorkbook wb = new HSSFWorkbook();
//	    HSSFSheet sheet = wb.createSheet("new sheet");
//	    HSSFRow row = sheet.createRow((short)2);
//	    row.createCell(0).setCellValue(1.1);
//	    row.createCell(1).setCellValue(new Date());
//	    row.createCell(2).setCellValue(Calendar.getInstance());
//	    row.createCell(3).setCellValue("a string");
//	    row.createCell(4).setCellValue(true);
//	    row.createCell(5).setCellType(HSSFCell.CELL_TYPE_ERROR);
//
//	    // Write the output to a file
//	    FileOutputStream fileOut;
//			fileOut = new FileOutputStream("workbook.xls");
//			wb.write(fileOut);
//			if(log.isDebugEnabled()) log.debug(fileOut);
//			fileOut.close();
//	}
//
//	
//	
//	private static void matodoPOI() {
//		try {
//		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("C:/excel/ModeloParaTrabalho02.xls"));
//		    HSSFWorkbook wb = new HSSFWorkbook(fs);
//		    HSSFSheet sheet = wb.getSheetAt(0);
//		    HSSFRow row;
//		    HSSFCell cell;
//
//		    int rows; // No of rows
//		    rows = sheet.getPhysicalNumberOfRows();
//
//		    int cols = 0; // No of columns
//		    int tmp = 0;
//
//		    // This trick ensures that we get the data properly even if it doesn't start from first few rows
//		    for(int i = 0; i < 10 || i < rows; i++) {
//		        row = sheet.getRow(i);
//		        if(row != null) {
//		            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
//		            if(tmp > cols) cols = tmp;
//		        }
//		    }
//
//		    for(int r = 0; r < rows; r++) {
//		        row = sheet.getRow(r);
//		        if(row != null) {
//		            for(int c = 0; c < cols; c++) {
//		                cell = row.getCell((short)c);
//		                if(cell != null) {
//		                	switch (cell.getCellType()) {
//		    				case 0:
//		    					if(log.isDebugEnabled()) log.debug(MessageFormat.format("DEBUG - row: {0} | col: {1} | Cell found with value: {2}",r, c ,cell.getNumericCellValue()));
//		    					break;
//		    				case 1:
//		    					if(log.isDebugEnabled()) log.debug(MessageFormat.format("DEBUG - row: {0} | col: {1} String Cell found with value: {2}",r, c ,cell.getStringCellValue()));
//		    					break;
//		    				}
//		                   
//		                }
//		            }
//		        }
//		    }
//		} catch(Exception ioe) {
//		    ioe.printStackTrace();
//		}
//	}
//}
