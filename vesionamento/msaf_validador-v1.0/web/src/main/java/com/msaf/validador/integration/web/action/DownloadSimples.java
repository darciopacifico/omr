package com.msaf.validador.integration.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadSimples extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		OutputStream out = response.getOutputStream();
		
		

		
        BufferedInputStream in = null;
		try {
			File file = new File("c:/excel/ModeloParaTrabalho03.xls");
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=testeD.xls;");
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}
}
