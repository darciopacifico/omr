/**
 * 
 */
package br.com.dlp.jazzomr.poc;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author darcio
 * 
 */
@ContextConfiguration(locations = "/ApplicationContext_testng.xml")
@Test
public class JazzPDFReader extends AbstractTestNGSpringContextTests implements ImageObserver {
	private static final String PDF_TESTE1 = "/home/darcio/Desktop/ExamePDFTeste/testeScannerRicoh.pdf";
	private static final String PDF_TESTE2 = "/home/darcio/Desktop/ExamePDFTeste/ExamsEvt_17_280611_1141.pdf";
	private static final String PDF_TESTE3 = "/home/darcio/Desktop/ExamePDFTeste/fromApresentacaoTudo.pdf";
	private static final String PDF_TESTE4 = "/home/darcio/Desktop/ExamePDFTeste/tudo.pdf";
	private static final String PDF_TESTE5 = "/home/darcio/Desktop/ExamePDFTeste/tudoJpg.pdf";
	private static final String PDF_TESTE6 = "/home/darcio/Desktop/ExamePDFTeste/tudoJpg.pdf";
	private static final String PDF_TESTE7 = "/home/darcio/Desktop/ExamePDFTeste/tudoJpgScanner.pdf";
	private static final String PDF_TESTE = "/home/darcio/Desktop/ExamePDFTeste/tudoPngJpgScanner.pdf";
	private static final String PDF_TESTE9 = "/home/darcio/Desktop/ExamePDFTeste/tudoPngScanner.pdf";

	public static Logger log = LoggerFactory.getLogger(JazzPDFReader.class);

	@Test
	public void testPDFBox() throws IOException {

		PDDocument document = null;

		File file = new File(JazzPDFReader.PDF_TESTE);

		document = PDDocument.load(file);

		List pages = document.getDocumentCatalog().getAllPages();

		for (Object object : pages) {

			PDPage page = (PDPage) object;
			PDResources resources = page.getResources();
			Map images = resources.getImages();
			if (images != null) {
				Set keySet = images.keySet();
				Iterator imageIter = keySet.iterator();
				while (imageIter.hasNext()) {
					String key = (String) imageIter.next();
					PDXObjectImage image = (PDXObjectImage) images.get(key);
					BufferedImage bufferedImage = image.getRGBImage();

					ImageIO.write(bufferedImage, "jpg", new File("/home/darcio/Desktop/ExamePDFTeste/pdfbox/imgFromPDFBOX" + System.currentTimeMillis() + ".jpg"));
				}
			}
		}
	}
/*
	@Test
	public void testeIcePDF() throws IOException, PDFException, PDFSecurityException {
		Document document = new Document();

		document.setFile(JazzPDFReader.PDF_TESTE);

		// Get the images for a single page
		int pages = document.getNumberOfPages();

		for (int i = 0; i < pages; i++) {

			Enumeration tmpImages = document.getPageImages(i).elements();
			
			// Save the images as JPEGs
			int count = 0;
			while (tmpImages.hasMoreElements()) {
				Image image = (Image) tmpImages.nextElement();

				// create new buffered image to paint to.
				double width = image.getWidth(this);
				double height = image.getHeight(this);

				
				 * width = width * 1.5; height = height * 1.5;
				 

				BufferedImage bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);

				Graphics2D g2d = bufferedImage.createGraphics();

				g2d.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);

				try {
					// Save as JPEG
					File file = new File("/home/darcio/Desktop/ExamePDFTeste/icepdf/newimage_" + i + "_" + count + "_" + System.currentTimeMillis() + ".jpg");
					count++;
					ImageIO.write(bufferedImage, "jpg", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				g2d.dispose();
				bufferedImage.flush();
			}
		}

		// Clean up document resources
		document.dispose();
	}

	@Test
	public void testeIcePDFPageImage() throws IOException, PDFException, PDFSecurityException {
		Document document = new Document();

		document.setFile(JazzPDFReader.PDF_TESTE);

		// Get the images for a single page
		int pages = document.getNumberOfPages();
		int count = 0;

		for (int i = 0; i < 1; i++) {

			Image image = (Image) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_TRIMBOX, 0,2);

			// create new buffered image to paint to.
			double width = image.getWidth(this);
			double height = image.getHeight(this);

			
			 * width = width * 1.5; height = height * 1.5;
			 

			BufferedImage bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = bufferedImage.createGraphics();

			g2d.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);

			try {

				// Save as JPEG
				File file = new File("/home/darcio/Desktop/ExamePDFTeste/icepdfPg/newimage_" + i + "_" + count + "_" + System.currentTimeMillis() + ".jpg");
				count++;
				ImageIO.write(bufferedImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2d.dispose();
			bufferedImage.flush();
		}

		// Clean up document resources
		document.dispose();
	}
*/
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		return true;
	}

}
