/**
 * 
 */
package br.com.dlp.jazzav.anuncio;

import java.awt.image.BufferedImage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import br.com.dlp.jazzav.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Modelo de adesivo
 * @author darcio
 */
@Entity
public class ModeloAdesivoVO extends AbstractEntityVO<Long> {

	private static final long serialVersionUID = 6616284337346989541L;

	@Expose(deserialize=false,serialize=false)
	@Transient
	public BufferedImage imgSample;
	
	public String nome;
	public String descricao;
	private Object modeloAdesivoCompilado;
	
	
	@Id
	@JazzProp(name="PK" ,tip="Chave de Modelo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	@Transient
	public Object getModeloAdesivoCompilado() {
		return modeloAdesivoCompilado;
	}
	
	@Transient
	public ModeloAdesivoVO getEuMesmo(){
		return this;
	}


	public void setModeloAdesivoCompilado(Object modeloAdesivoCompilado) {
		this.modeloAdesivoCompilado = modeloAdesivoCompilado;
	}

	@Expose(deserialize=false,serialize=false)
	public FileVO file;

	@ManyToOne
	public FileVO getFile() {
		return file;
	}

	
	@Transient
	public BufferedImage getImgSample() {
		return imgSample;
	}
	
	
	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	
	public void setImgSample(BufferedImage imgSample) {
		this.imgSample = imgSample;
	}
	
	
	

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setFile(FileVO file) {
		this.file = file;
	}

	
	
	
	
}

