package com.msaf.validador.integration.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {
	private static Log log = LogFactory.getLog(FileUtil.class);

	private static final String ERROR_MSG = "Argumento {0} não pode ser vazio/nulo.";
	private static String getErrorMessage(String argumentName) {
		return MessageFormat.format(ERROR_MSG, argumentName);
	}

	public static void moverArquivo(File file, String destinoPath) {
		if(Util.isEmpty(destinoPath)) throw new IllegalArgumentException(getErrorMessage("Path"));

		moverArquivo(file, new File(destinoPath));
	}

	/**
	 * Recebe dois files representando o arquivo origem e o arquivo destino.
	 * Até a versão atual, o diretório do arquivo destino precisa existir
	 * @param file
	 * @param fileDestino
	 */
	public static void moverArquivo(File file, File fileDestino) {
		if(file == null) throw new IllegalArgumentException(getErrorMessage("File"));
		if(!file.exists()) throw new IllegalArgumentException("Arquivo origem inexistente");
		if(fileDestino == null) throw new IllegalArgumentException(getErrorMessage("File Destino"));

		byte[] buffer = new byte[1024];
		InputStream in = null;
		OutputStream out = null;
		boolean ok = true;

		try {
			if(!fileDestino.exists()) {
				if(!fileDestino.createNewFile()) throw new IOException("Impossível criar o diretório.");
			}

			in = new FileInputStream(file);
			out = new FileOutputStream(fileDestino);

			while(in.read(buffer) != -1) {
				out.write(buffer);
			}
		} catch (Exception e) {
			ok = false;
			if(log.isDebugEnabled()) log.debug(MessageFormat.format("Falha ao mover arquivo {0} para {1}:{2}", file.getPath(), fileDestino, e.getMessage()));
		} finally {
			closeStreams(in, out);
		}

		if(ok)
			if(!file.delete()) log.warn(MessageFormat.format("Impossível excluir o arquivo {0}", file.getAbsolutePath()));
	}

	private static void closeStreams(InputStream in, OutputStream out) {
		try {
			if(out != null)	out.close();
			closeStreams(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void closeStreams(InputStream... in) {
		if(in != null) {
			for (int i = 0; i < in.length; i++) {
				if(in[i] != null)
					try {
						in[i].close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static String readFileTrim(String path) {
		return Util.trim(readFile(path));
	}

	public static String readFile(String path) {
		if(Util.isEmpty(path)) throw new IllegalArgumentException(getErrorMessage("Path"));
		if(!new File(path).exists()) throw new IllegalArgumentException("Arquivo inexistente!");

		StringBuilder text = new StringBuilder();

		DataInputStream in = null;
		BufferedReader br = null;
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(path);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)
				text.append(strLine);
		} catch (FileNotFoundException e) {
			if(log.isDebugEnabled()) log.debug(MessageFormat.format("Falha ao ler arquivo {0}", path));
		} catch (IOException e) {
			if(log.isDebugEnabled()) log.debug(MessageFormat.format("Falha ao ler arquivo {0}", path));
		} finally {
			closeReaders(br);
			closeStreams(in);
		}

		return text.toString();
	}

	private static void closeReaders(BufferedReader... br) {
		if(br != null) {
			for (int i = 0; i < br.length; i++) {
				if(br[i] != null)
					try {
						br[i].close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static void criaPasta(String basePath, String path) throws IOException {
		if(Util.isEmpty(path)) throw new IllegalArgumentException(getErrorMessage("Path"));
		if(basePath == null) throw new IllegalArgumentException(getErrorMessage("BasePath"));

		String novoPath = basePath;
		File file = new File(MessageFormat.format("{0}{1}", basePath, path));

		if(file.exists()) return;

		String[] pathParts = null;
		String separator = (File.separatorChar == '\\')?"[\\\\]":"/";
		pathParts = path.split(separator);

		for (int indice = 0; indice < pathParts.length; indice++) {
			novoPath = MessageFormat.format("{0}{1}{2}", novoPath, pathParts[indice], File.separator);

			file = new File(novoPath);
			if(!file.mkdir()) log.warn(MessageFormat.format("Não foi possível criar diretório {0}", file.getAbsolutePath()));
		}
	}
	public static void excluirArquivo(File file) {
		if(file == null) throw new IllegalArgumentException(getErrorMessage("File"));
		if(!file.exists()) throw new IllegalArgumentException("Arquivo origem inexistente");

		try {
			file.delete();
		} catch (Exception e) {
			if(log.isDebugEnabled()) log.debug(MessageFormat.format("Falha ao excluir arquivo {0} \n\n Erro:\n{1}", file.getAbsolutePath(), e.getMessage()));
		}


	}

	public static void writeFile(byte[] bytes, File destination) {
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(destination);
			fileOutputStream.write(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeStreams(null, fileOutputStream);
		}
	}

	public static boolean exists(String path) {
		if(Util.isEmpty(path)) return false;

		return new File(path).exists();
	}
	
	 public static String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1).toLowerCase();
        }
        return ext;
    }
	 
	 public static boolean getFileExtensionValidation(String f,String ext) {
        return ext.equals(getFileExtension(f));
    }
}
