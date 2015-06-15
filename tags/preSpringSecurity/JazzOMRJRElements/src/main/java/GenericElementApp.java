/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.jazzomr.jr.handler.JazzQRCodeGenerator;
import br.com.dlp.jazzomr.jr.util.IQRCodeGenerator;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GenericElementApp.java 3030 2009-08-27 11:12:48Z teodord $
 */
public class GenericElementApp {
	private static final Logger log = LoggerFactory.getLogger(GenericElementApp.class);
	
	/**
	 * @throws JRException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws JRException, ClassNotFoundException, SQLException, IOException {

		JasperReport jr = JasperCompileManager.compileReport("src/main/resources/reports/reportBlank2.jrxml");
		
		BufferedImage bi = loadImage();

		
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();
		
		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("examInstancePK", 1l);
		
		JasperPrint jp = JasperFillManager.fillReport(jr, parameters, getConn());

		JasperExportManager.exportReportToXmlFile(jp, "ellipseJRElement.xml", false);
		//JasperExportManager.exportReportToHtmlFile(jp, "ellipseJRElement.html");
		JasperExportManager.exportReportToPdfFile(jp, "ellipseJRElement.pdf");

	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected static BufferedImage loadImage() throws IOException {
		InputStream is = GenericElementApp.class.getResourceAsStream("reports/logo-unip.jpg");

		BufferedImage bi = ImageIO.read(is);
		return bi;
	}

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected static Connection getConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jazzomr", "root", "CelularCinza");
		return conn;
	}

}
