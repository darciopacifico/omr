package br.com.dlp.framework.vo;

import br.com.dlp.framework.vo.report.IReportParamsVO;

public abstract class AbstractReportParamsVO implements IReportParamsVO,
		ICadastroBaseVO {
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
