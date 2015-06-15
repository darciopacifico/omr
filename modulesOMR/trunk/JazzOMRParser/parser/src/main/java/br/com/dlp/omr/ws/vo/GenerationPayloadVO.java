package br.com.dlp.omr.ws.vo;

import java.io.Serializable;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.sf.jasperreports.engine.JasperReport;

import org.apache.cxf.aegis.type.java5.XmlElement;

import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.results.JazzJasperTypeCreator;

/**
 * 
 * @author darciopa
 */
@XmlType
@XmlRootElement
public class GenerationPayloadVO implements Serializable{
	private static final long serialVersionUID = -4629491756754317219L;

	private String eventName;
	
	private ExamVO examVO;
	private List<PessoaVO> pessoas;
	
	private JasperReport modeloProva;
	
	private Integer qtdVariacoes;
	
	/*
	private DataHandler modeloProvaDH;
	
	@XmlMimeType("application/octet-stream")
	public DataHandler getModeloProvaDH() {
		return modeloProvaDH;
	}*/
	
	@XmlElement(type=JazzJasperTypeCreator.class)
	public JasperReport getModeloProva() {
		return modeloProva;
	}
	
	
	
	
	public Integer getQtdVariacoes() {
		return qtdVariacoes;
	}
	
	
	public void setQtdVariacoes(Integer qtdVariacoes) {
		this.qtdVariacoes = qtdVariacoes;
	}
	public ExamVO getExamVO() {
		return examVO;
	}
	public List<PessoaVO> getPessoas() {
		return pessoas;
	}
	public String getEventName() {
		return eventName;
	}
	public void setExamVO(ExamVO examVO) {
		this.examVO = examVO;
	}
	public void setPessoas(List<PessoaVO> pessoas) {
		this.pessoas = pessoas;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	/*
	public void setModeloProvaDH(DataHandler modeloProvaDH) {
		this.modeloProvaDH = modeloProvaDH;
	}*/
	public void setModeloProva(JasperReport modeloProva) {
		this.modeloProva = modeloProva;
	}	
	
	
}
