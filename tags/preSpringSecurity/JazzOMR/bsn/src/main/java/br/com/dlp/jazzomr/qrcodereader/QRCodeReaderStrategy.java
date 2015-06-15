/**
 * 
 */
package br.com.dlp.jazzomr.qrcodereader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.gof.strategy.Strategy;
import br.com.dlp.framework.gof.strategy.StrategyExecutionException;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.poc.IdentityRegion;
import br.com.dlp.jazzomr.poc.ParticleAnalyzerParamDTO;
import br.com.dlp.jazzomr.poc.QRCodeReaderParam;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * @author darcio
 *
 */
@Component("qrCodeReaderDirect")
public class QRCodeReaderStrategy implements Strategy<QRCodeReaderParam, String> {
	protected Logger log = LoggerFactory.getLogger(QRCodeReaderStrategy.class);
	
	public QRCodeReaderStrategy() {
	}
	
	public String execute(QRCodeReaderParam payload) throws StrategyExecutionException {
		String text;
		BufferedImage imgId=null;
		try {
			IdentityRegion idRegionRel = getIdentityRegion(payload);
			BufferedImage fullImage = getFullImage(payload);
			imgId = getIdentityImage(idRegionRel, fullImage);
			text = readQRCode(imgId);
			
			logImgFailed(imgId,null,text);
			
		} catch (Exception e) {
			
			logImgFailed(imgId,e,"");
			throw new StrategyExecutionException("Erro ao tentar ler qrcode!",e);
			
		}
		 
		return text;
	}

	/**
	 * 
	 * @param imgId
	 * @param e
	 * @param text 
	 */
	protected void logImgFailed(BufferedImage imgId, Exception e, String text) {
		
		if(!log.isDebugEnabled()){
			return;
		}
		
		if(imgId==null){
			log.error("imagem do qrcode = null");
		}
		
		File output = new File("result/qrCodeErrors/");
		output.mkdirs();
		
		File fileImg = new File(output,"QRCodeError_"+System.currentTimeMillis()+"_"+text+".gif");
		
		try {
			ImageIO.write(imgId, "gif", fileImg);
		} catch (IOException e1) {
			log.error("erro ao tentar logar qrCode com defeito!",e1);
		}
		
	}

	/**
	 * @param payload
	 * @return
	 */
	protected IdentityRegion getIdentityRegion(QRCodeReaderParam payload) {
		ParticleAnalyzerParamDTO particleAnalyzerParamDTO = payload.particleAnalyzerParamDTO;
		IdentityRegion idRegionRel = getIdRegion(particleAnalyzerParamDTO);
		return idRegionRel;
	}

	/**
	 * @param payload
	 * @return
	 */
	protected BufferedImage getFullImage(QRCodeReaderParam payload) {
		ImageDocVO imageDocVO = payload.imageDocVO;
		
		
		BufferedImage fullImage = imageDocVO.getBufferedImage();
		return fullImage;
	}

	/**
	 * @param particleAnalyzerParamDTO
	 * @return
	 */
	protected IdentityRegion getIdRegion(ParticleAnalyzerParamDTO particleAnalyzerParamDTO) {
		// cria uma binary map a partir da imagem informada
		IdentityRegion idRegionRel = particleAnalyzerParamDTO.getIdentityRegion();
		
		if (idRegionRel == null) {
			throw new NullPointerException("Nao foi informada a regiao da imagem que contem a identificacao do documento!");
		}
		return idRegionRel;
	}

	/**
	 * @param imgId
	 * @return
	 * @throws StrategyExecutionException
	 */
	protected String readQRCode(BufferedImage imgId) throws StrategyExecutionException {
		LuminanceSource source = new BufferedImageLuminanceSource(imgId);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		BinaryBitmap bitmap = bitmap1;

		// recupera os hints para leituda
		Hashtable hints = getHints();

		Reader reader = getReader();

		String text=null;
		try {
			Result result = reader.decode(bitmap, hints);
			text = result.getText();
				
		} catch (NotFoundException e) {
			log.error(this.getClass().getSimpleName()+" Erro ao tentar ler trecho da imagem que contem a identificacao do documento!", e);
		} catch (ChecksumException e) {
			throw new StrategyExecutionException("Erro ao tentar ler trecho da imagem que contem a identificacao do documento!", e);
		} catch (FormatException e) {
			throw new StrategyExecutionException("Erro ao tentar ler trecho da imagem que contem a identificacao do documento!", e);
		}
		return text;
	}

	/**
	 * @param idRegionRel
	 * @param fullImage
	 * @return
	 */
	protected BufferedImage getIdentityImage(IdentityRegion idRegionRel, BufferedImage fullImage) {
		int imageWidth = fullImage.getWidth();
		int imageHeight = fullImage.getHeight();
		
		int x = (int) Math.floor(imageWidth * idRegionRel.getXrel());
		int y = (int) Math.floor(imageHeight * idRegionRel.getYrel());
		int w = (int) Math.floor(imageWidth * idRegionRel.getWrel());
		int h = (int) Math.floor(imageHeight * idRegionRel.getHrel());
		
		BufferedImage imgId = fullImage.getSubimage(x, y, w, h);
		return imgId;
	}

	/**
	 * @return
	 */
	protected Reader getReader() {
		return new QRCodeReader();
	}
	
	
	/**
	 * Hints padrao de processamento de imagem
	 * 
	 * @return the hints
	 */
	public Hashtable getHints() {
		return new Hashtable(0);
	}


	@Override
	public int getOrder() {
		return 10;
	}
	


}
