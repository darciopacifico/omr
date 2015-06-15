package br.com.dlp.jazzomr.poc;

import br.com.dlp.jazzomr.omr.ImageDocVO;

public class QRCodeReaderParam {
	/**
	 * @return the imageDocVO
	 */
	public ImageDocVO getImageDocVO() {
		return imageDocVO;
	}

	/**
	 * @return the particleAnalyzerParamDTO
	 */
	public ParticleAnalyzerParamDTO getParticleAnalyzerParams() {
		return particleAnalyzerParamDTO;
	}

	/**
	 * @param imageDocVO the imageDocVO to set
	 */
	public void setImageDocVO(ImageDocVO imageDocVO) {
		this.imageDocVO = imageDocVO;
	}

	/**
	 * @param particleAnalyzerParamDTO the particleAnalyzerParamDTO to set
	 */
	public void setParticleAnalyzerParams(ParticleAnalyzerParamDTO particleAnalyzerParamDTO) {
		this.particleAnalyzerParamDTO = particleAnalyzerParamDTO;
	}

	public ImageDocVO imageDocVO;
	public ParticleAnalyzerParamDTO particleAnalyzerParamDTO;

	public QRCodeReaderParam(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) {
		this.imageDocVO = imageDocVO;
		this.particleAnalyzerParamDTO = particleAnalyzerParamDTO;
	}
	
	
}