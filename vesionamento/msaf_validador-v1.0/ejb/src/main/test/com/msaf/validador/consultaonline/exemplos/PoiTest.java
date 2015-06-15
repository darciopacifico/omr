package com.msaf.validador.consultaonline.exemplos;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class PoiTest {

	public static void main(String[] args) throws IOException {

           HSSFWorkbook wb = new HSSFWorkbook();
  
           HSSFSheet sheet = wb.createSheet("Planilha");

           HSSFRow row = sheet.createRow((short) 2);
           
           HSSFCell cell = row.createCell((short) (1),HSSFCell.CELL_TYPE_STRING);


           cell.setCellValue("Alinhe isto!fim");
           
           sheet.setColumnWidth( (short) 1, (short) ( ( 10 * cell.getStringCellValue().length() ) / ( (double) 1 / 20 ) ) );
           
           

           HSSFCellStyle estilo = wb.createCellStyle();

          

           //Alinhando ao centro

           estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);

          
           short cor = HSSFColor.BLUE.index;

           // inserindo fontes e cores.
           estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
           estilo.setFillForegroundColor(HSSFColor.GREEN.index);
            //estilo.setFillBackgroundColor(HSSFColor.BLUE.index);
           // estilo.setFillForegroundColor(HSSFColor.BLUE.index2);

           // funcionando
//           estilo.setFillPattern(HSSFCellStyle.FINE_DOTS );
//           estilo.setFillBackgroundColor(new HSSFColor.RED().getIndex()); 

//           estilo.setFillPattern(HSSFCellStyle.FINE_DOTS );
//           estilo.setFillForegroundColor(new HSSFColor.BLUE().getIndex());
//           estilo.setFillBackgroundColor(new HSSFColor.RED().getIndex()); 
          
//           estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
//           estilo.setFillForegroundColor(new HSSFColor.RED().getIndex());

           
           
           //Adicionando bordas
           estilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

           estilo.setBottomBorderColor(cor);

           estilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);

           estilo.setLeftBorderColor(cor);

           estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);

           estilo.setRightBorderColor(cor);

           estilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);

           estilo.setTopBorderColor(cor);


           cell.setCellStyle(estilo);

           FileOutputStream fileOut = new FileOutputStream("C:/planilha.xls");

           wb.write(fileOut);

           fileOut.close();





    }



}