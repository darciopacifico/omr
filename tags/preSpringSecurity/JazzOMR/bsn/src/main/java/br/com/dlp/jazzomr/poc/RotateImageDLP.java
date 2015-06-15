package br.com.dlp.jazzomr.poc;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * RotateImage45Degrees.java - 1. scales an image's dimensions by a factor of two 2. rotates it 45 degrees around the image center 3. displays the processed image
 */
public class RotateImageDLP  {
	private Image inputImage;

	private BufferedImage sourceBI;

	private BufferedImage destinationBI = null;

	private Insets frameInsets;

	private boolean sizeSet = false;

	public RotateImageDLP(String imageFile, double angulo) throws IOException {

		inputImage = readImage(imageFile);
		
		//sourceBI = new BufferedImage(inputImage.getWidth(null), inputImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		sourceBI = (BufferedImage) inputImage;

		Graphics2D g = (Graphics2D) sourceBI.getGraphics();
		g.drawImage(inputImage, 0, 0, null);

		AffineTransform at = new AffineTransform();

		// rotate 45 degrees around image center
		at.rotate(angulo * Math.PI / 180.0, sourceBI.getWidth() / 2.0, sourceBI.getHeight() / 2.0);

		/*
		 * translate to make sure the rotation doesn't cut off any image data
		 */
		AffineTransform translationTransform;
		translationTransform = findTranslation(at, sourceBI);
		at.preConcatenate(translationTransform);

		// instantiate and apply affine transformation filter
		BufferedImageOp bio;
		bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		destinationBI = bio.filter(sourceBI, null);

		ImageIO.write(destinationBI, "gif", new File(angulo+ "_graus_result.gif"));

		//int frameInsetsHorizontal = frameInsets.right + frameInsets.left;
		//int frameInsetsVertical = frameInsets.top + frameInsets.bottom;
	//	setSize(destinationBI.getWidth() + frameInsetsHorizontal, destinationBI.getHeight() + frameInsetsVertical);
		//show();
	}

	/**
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	protected Image readImage(String imageFile) throws IOException {

		byte[] bytes = leByteArray(imageFile);
		ByteArrayInputStream is = new ByteArrayInputStream(bytes); 
		
		Image image = ImageIO.read(is);
		
		
		
		return image;
		
		/*

		return defaultToolkit.createImage(bytes);
*/
		// return defaultToolkit.getImage(imageFile);

	}

	/**
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	protected byte[] leByteArray(String imageFile) throws IOException {
		FileInputStream fis = null;
		byte[] rotacionada;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(new File(imageFile));

			bis = new BufferedInputStream(fis);
			bos = new ByteArrayOutputStream();

			copy(bis, bos, 1024);

		} finally {

			try {
				bis.close();
				bos.flush();
				bos.close();
				fis.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		rotacionada = rotateImage(bos.toByteArray());

		return rotacionada;

	}

	/*
	 * find proper translations to keep rotated image correctly displayed
	 */
	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
		Point2D p2din, p2dout;

		p2din = new Point2D.Double(0.0, 0.0);
		p2dout = at.transform(p2din, null);
		double ytrans = p2dout.getY();

		p2din = new Point2D.Double(0, bi.getHeight());
		p2dout = at.transform(p2din, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate( Math.abs(xtrans), Math.abs(ytrans));
		return tat;
	}


	public static void main(String[] args) throws IOException {
		
		double angulo = 0;
		double incremento = 20;
		
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
		new RotateImageDLP("1.gif", angulo=angulo+incremento);
	}

	/**
	 * 
	 * @param byteArray
	 * @return
	 * @throws IOException
	 */
	private static byte[] rotateImage(byte[] byteArray) throws IOException {

		return byteArray;
	}

	public long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {

		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n;
		while (-1 != (n = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, n);
			count += n;
		}
		return count;

	}

}
