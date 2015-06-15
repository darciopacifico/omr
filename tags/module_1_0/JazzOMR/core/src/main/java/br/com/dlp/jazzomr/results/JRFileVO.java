package br.com.dlp.jazzomr.results;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.jazzomr.base.AbstractEntityVO;

/**
 * Entidade para armazenar os arquivos de relatórios Jasper. Apenas por uma questão de performance
 * 
 * @author darcio
 */
@Entity
public class JRFileVO extends AbstractEntityVO<Long>{
	
	public static final String RELATORIO_PRINCIPAL="principal";
	
	private static final long serialVersionUID = -8233776083561677227L;

	private String nome;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return pk;
	}
	
	private byte[] jasperReport;

	@Lob
	public byte[] getJasperReport() {
		return jasperReport;
	}

	public void setJasperReport(byte[] jasperReport) {
		this.jasperReport = jasperReport;
	}

	@NotBlank
	@Length(min=1,max=20)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
