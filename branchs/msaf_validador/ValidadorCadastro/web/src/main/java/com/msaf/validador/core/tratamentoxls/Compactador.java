package com.msaf.validador.core.tratamentoxls;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compactador {

	
	
	public byte[] compactar(byte[] bytes, String name) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ZipOutputStream zout = new ZipOutputStream(out);
		
		zout.putNextEntry(new ZipEntry(name));
		
		zout.write(bytes);
		
		zout.closeEntry();
		
		zout.close();

		return out.toByteArray();
	}

	
	public void addToZip(ZipOutputStream zip, String name, byte[] conteudo) throws IOException {
		
		zip.putNextEntry(new ZipEntry(name));
		
		zip.write(conteudo);
		
		zip.closeEntry();
		
	}

	public void addToZip(ZipOutputStream zip, String name, String file) throws IOException {
		
		zip.putNextEntry(new ZipEntry(name));
		
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		
		
		byte [] buffer = new byte[4096];
		
		@SuppressWarnings("unused")
		int b;
		
		while( ( b = reader.read(buffer, 0, buffer.length) )!= -1 ) {
			zip.write(buffer);
		}  
		
		zip.closeEntry();
	}
	
}
