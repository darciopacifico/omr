/**
 * 
 */
package br.com.dlp.jazzomr.event;

import javax.xml.bind.annotation.XmlTransient;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.jazz.jrds.annotations.Navigable;

import com.sun.istack.NotNull;

/**
 * @author darcio
 * 
 */
public class ParticipationVO extends AbstractBaseVO<Long> {

	private static final long serialVersionUID = 2512255172110878569L;

	private PessoaVO pessoaVO; 
	private ExamVO examVO = new ExamVO();
	private Integer examVariant = null;

	public ParticipationVO() {
	}

	@Override
	public void setPK(Long ppk) {
		super.setPK(ppk);
	}

	/**
	 * @return the exam
	 */
	@Navigable(alias = "exa")
	@XmlTransient
	public ExamVO getExamVO() {
		return examVO;
	}

	/**
	 * @param exam
	 *          the exam to set
	 */
	public void setExamVO(ExamVO exam) {
		this.examVO = exam;
	}

	@Override
	public Long getPK() {
		return this.pk;
	}

	@NotNull
	@Navigable(alias = "pes")
	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}

	/**
	 * @param pessoaVO
	 *          the pessoaVO to set
	 */
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public Integer getExamVariant() {
		return examVariant;
	}

	public void setExamVariant(Integer examVar) {
		this.examVariant = examVar;
	}

}
