package br.com.dlp.jazzomr.jasper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import br.com.dlp.jazzomr.results.ImageVO;
import br.com.jazz.jrds.JazzJRDataSource;
import br.com.jazz.jrds.JazzJRDataSourceProvider;

/**
 * DataSource apenas para design do relatorio de imagens
 * @author darciopa
 */
public class JazzOMRImgDSPForDesign extends JazzJRDataSourceProvider{

	public JazzOMRImgDSPForDesign() {
		super(getData(), br.com.dlp.jazzomr.results.ImageVO.class, new java.util.HashMap());
	}

	
	public static void main(String[] args) throws JRException {
		
		
		
		
		JazzJRDataSource ds = new JazzOMRImgDSPForDesign().getJazzJRDataSource();
		
		while(ds.next()){
			
			JRField[] fiedls = ds.getFields();
			
			for (JRField jrField : fiedls) {
				System.out.println(jrField.getName() + " - " + jrField.getValueClassName());
			}
			
			System.out.println("");
			
		}
		
	}
	
	private static Collection getData() {
		
		List<ImageVO> imgs = new ArrayList<ImageVO>();
		
		imgs.add(createImageVO("/reports/logo-cliente.png", "Logo Cliente", 1l));
		imgs.add(createImageVO("/reports/morro.png", null, 2l));
		imgs.add(createImageVO("/reports/morroGrande.png", "Morro Grande Erosao", 3l));
		return imgs;
	}

	private static ImageVO createImageVO(String imgUrl, String imgName, long pk) {
		ImageVO img= new ImageVO();
		img.setPK(pk);
		img.setTitulo(imgName);
		try {
			img.setImage(ImageIO.read(JazzOMRImgDSPForDesign.class.getResourceAsStream(imgUrl)));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar carregar imagem!",e);
		}
		return img;
	}

}
