/**
 * 
 */
package br.com.dlp.jazzomr.exam.coordinate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * @author darcio
 *
 */
public class OMRMark implements Serializable {

	private Integer pagina; 
	private String chave;
	private String criterion;
	private String critType;
	private Long pergunta;
	
	private Integer x; 
	private Integer y;
	private Integer h;
	private Integer w;

	private Set<Integer> paginas = new HashSet<Integer>();

	
	public OMRMark() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer getPagina() {
		return pagina;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public Integer getH() {
		return h;
	}

	public Integer getW() {
		return w;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Set<Integer> getPaginas() {
		return paginas;
	}

	public void setPaginas(Set<Integer> paginas) {
		this.paginas = paginas;
	}

	@Override
	public String toString() {
		
		String val = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		
		return val;
	}



	public Long getPergunta() {
		return pergunta;
	}

	public void setPergunta(Long pergunta) {
		this.pergunta = pergunta;
	}

	public String getCritType() {
		return critType;
	}

	public void setCritType(String critType) {
		this.critType = critType;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.chave).toHashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(object==this){
			return true;
		}

		return (object!=null && object.hashCode()==this.hashCode());
		
	}

	public String getCriterion() {
		return criterion;
	}

	public void setCriterion(String criterion) {
		this.criterion = criterion;
	}
	
}
