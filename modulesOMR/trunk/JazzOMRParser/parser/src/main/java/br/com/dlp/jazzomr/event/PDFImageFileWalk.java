/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.springframework.stereotype.Component;

import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;

/**
 * Navega pela estrutura de um arquivo ZIP recursivamente ate encontrar as imagens contidas no ZIP, entao aciona o processamento das mesmas
 * 
 * @author darcio
 * 
 */
@Component("pdfImageFileWalk")
public class PDFImageFileWalk extends AbstractFileWalk {

	/**
	 (java.lang.String) application/vnd.openxmlformats-officedocument.wordprocessingml.document
	 (java.lang.String) application/msword
	 (java.lang.String) application/pdf
	 (java.lang.String) application/x-tar
	 (java.lang.String) application/x-rar
	 (java.lang.String) image/png
	 (java.lang.String) image/gif
	 (java.lang.String) image/jpeg
	 (java.lang.String) zipedExams.zip
	 (java.lang.String) application/zip
	 */
	
	public PDFImageFileWalk() {
	}

	/**
	 * Parseia um arquivo PDF a procura de imagens. Alguns scanners criam arquivos PDF contendo imagens.
	 */
	@Override
	public void processFile(byte[] bytes, IImageParserVisitor visitor) {

		PDDocument document;
		try {
			document = PDDocument.load(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar ler pdf contido no array de bytes!",e);
		}

		List pages = document.getDocumentCatalog().getAllPages();

		for (Object object : pages) {

			PDPage page = (PDPage) object;
			PDResources resources = page.getResources();
			Map images;
			try {
				images = resources.getImages();
			} catch (IOException e) {
				throw new JazzOMRRuntimeException("Erro ao tentar ler imagem contida na pagina de um pdf!",e);
			}
			if (images != null) {
				Set keySet = images.keySet();
				Iterator imageIter = keySet.iterator();
				while (imageIter.hasNext()) {
					String key = (String) imageIter.next();
					PDXObjectImage image = (PDXObjectImage) images.get(key);
					BufferedImage bufferedImage;
					try {
						bufferedImage = image.getRGBImage();
						visitor.visit(bufferedImage);
					} catch (IOException e) {
						throw new JazzOMRRuntimeException("Erro ao tentar carregar imagem para bufferedImage!",e);
					}
				}
			}
		}
	}
}
