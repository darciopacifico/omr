//package com.msaf.validador.lerXls;
//
//import java.io.*;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.hssf.record.*;
//import org.apache.poi.hssf.model.*;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.*;
//import java.util.ArrayList;
//
///**
// * 
// * @author Gilberto Hiragi
// */
//public class teste2 {
//	private POIFSFileSystem fs;
//	private HSSFWorkbook wb;
//	private HSSFSheet sheet;
//	private short beginCol, endCol;
//	private int beginRow, endRow;
//	private int rowCurrent;
//	private ArrayList linha;
//
//	/** Creates a new instance of ReadExcel */
//	public teste2() {}
//
//	public boolean OpenExcel(String pathFile) {
//		try {
//			this.fs = new POIFSFileSystem(new FileInputStream(pathFile));
//			this.wb = new HSSFWorkbook(fs);
//			this.sheet = wb.getSheetAt(0);
//			HSSFRow row = this.sheet.getRow(this.sheet.getFirstRowNum());
//			this.beginCol = row.getFirstCellNum();
//			this.endCol = row.getLastCellNum();
//			this.beginRow = this.sheet.getFirstRowNum();
//			this.endRow = this.sheet.getLastRowNum();
//			this.linha = new ArrayList();
//			this.rowCurrent = 0;
//		} catch (Exception e) {
//			System.out.println("Erro WriteExcel: " + e.getMessage());
//			return false;
//		}
//		return true;
//	}
//
//	public String nextInsert(String table) {
//		String insert = "INSERT INTO " + table + " VALUES (";
//		this.rowCurrent++;
//		insert += this.rowCurrent + ", ";
//		// Não tem mais linhas para ler
//		if (this.rowCurrent > endRow) {
//			return null;
//		}
//		HSSFRow row;
//		HSSFCell cell;
//		try {
//			String campo = "";
//			row = this.sheet.getRow(rowCurrent);
//			for (short colCount = beginCol; colCount < endCol; colCount++) {
//				cell = row.getCell(colCount);
//				insert += "'";
//				switch (cell.getCellType()) {
//				case 0:
//					insert += "" + cell.getNumericCellValue();
//					break;
//				case 1:
//					insert += cell.getStringCellValue();
//					break;
//				}
//				insert += "', ";
//			}
//			insert = insert.substring(0, insert.length() - 2) + ")";
//		} catch (Exception erro) {
//			insert = null;
//			HSSFRow row2;
//			HSSFCell cell2;
//			try {
//				String campo = "";
//				row2 = this.sheet.getRow(rowCurrent);
//				for (short colCount = beginCol; colCount < endCol; colCount++) {
//					cell2 = row2.getCell(colCount);
//					switch (cell2.getCellType()) {
//					case 0:
//						this.linha.add("" + cell2.getNumericCellValue());
//						break;
//					case 1:
//						this.linha.add(cell2.getStringCellValue());
//						break;
//					}
//				}
//			} catch (Exception erroException) {
//				this.linha = null;
//				System.out.println("Erro ReadExcel: " + erro.getMessage());
//			}
////			return this.linha;
//			
//		}
//		return null;
//	}
//}