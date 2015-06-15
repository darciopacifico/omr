package br.com.dlp.jazzomr.poc;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleneseTestNgHelper;

public class GoogleTest extends SeleneseTestNgHelper {
	
	@BeforeTest
	public void configDLP() throws Exception {
		// We instantiate and start the browser
		
		setUp("http://localhost:8080/JazzOMR.portlet/","*googlechrome");
		/*
		setUp("http://localhost:8080/JazzOMR.portlet/");
		 */
		
		selenium.setSpeed("200");
		
		
	}
	
	@Test
	public void testNew() throws Exception {
		
		String text = System.currentTimeMillis()+"";
		
		selenium.open("http://localhost:8080/JazzOMR.portlet/");
		SeleneseTestNgHelper.assertEquals(selenium.getTitle(), "Menu Desenvolvedor");
		selenium.click("link=TipoRequisitoVO");
		selenium.waitForPageToLoad("30000");
		SeleneseTestNgHelper.assertEquals(selenium.getTitle(), "Tipo de Requisito");
		selenium.click("frmMain:j_id21");
		selenium.type("frmMain:j_id34", "");
		selenium.click("frmMain:j_id14:anchor");
		selenium.click("frmMain:j_id11:anchor");
		selenium.click("frmManutencao:j_id112InputDate");
		selenium.click("frmManutencao:j_id112DayCell2");
		selenium.type("frmManutencao:j_id115", "testedani");
		selenium.type("frmManutencao:j_id118", "testedani");
		selenium.click("frmManutencao:dummyVOPoUpControl");
		selenium.click("frmMain:j_id95:0:j_id96");
		selenium.click("frmManutencao:j_id103");
		selenium.type("frmMain:j_id36", "testedani");
		selenium.click("frmMain:j_id14:anchor");
		verifyTrue(selenium.isTextPresent("testedani"));
		
		
	}
}
