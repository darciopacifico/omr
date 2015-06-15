package com.msaf.validador.tag;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class PaginacaoTag extends TagSupport {

	private static final long serialVersionUID = 5200459079724642868L;

	private int pagina;

	private int maxResult;
	
	private int quantidadeRegistro;
	
	private String atributeCollectionName; 

	private String styleClassName;

	private String style;
	
	private String url;

	private String contexPath;
	
	private String onclick;
	
	private String variableJavascript;
	
	private int getQuantidadeRegistro() {
		
		if((atributeCollectionName != null) && (atributeCollectionName.trim().length() > 0)) {
			
			Collection<?> collection = (Collection<?>) super.pageContext.getAttribute(this.atributeCollectionName);
			
			return collection != null?collection.size(): 0;
			
		}else {
			return this.quantidadeRegistro;
		}
		
	}
	
	
	public String getVariableJavascript() {
		return variableJavascript;
	}

	public void setVariableJavascript(String variableJavascript) {
		this.variableJavascript = variableJavascript;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAtributeCollectionName() {
		return atributeCollectionName;
	}

	public void setAtributeCollectionName(String atributeCollectionName) {
		this.atributeCollectionName = atributeCollectionName;
	}
	
	public void setQuantidadeRegistro(int quantidadeRegistro) {
		this.quantidadeRegistro = quantidadeRegistro;
	}
	
	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	public String getStyleClassName() {
		return styleClassName;
	}

	public void setStyleClassName(String styleClassName) {
		this.styleClassName = styleClassName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	
	private long getQuantidadePaginas() {
		return (long)Math.ceil((double) this.getQuantidadeRegistro() / this.maxResult);
	}

	public String getOnclick() {
		return onclick;
	}


	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}


	private String moutPageResult() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<spam ");
		
		// coloca o class no spam
		buffer.append(appendAtribute("class", this.styleClassName));
		
		// coloca o style
		buffer.append(appendAtribute("style", this.style));
		
		buffer.append(">");
		
		int start = this.getStartPage();
		
		int finish = this.getFinishPage();
		
		buffer.append(this.appendPages(start, finish));
		
		buffer.append("</spam>");
		
		return buffer.toString();
	}

	protected String appendPages(int start, int finish) {
		
		StringBuffer buffer = new StringBuffer();
		
		String a = "<a href=\" "+ this.url + "&page=2" +" \"></a>";
		
		String url = "";
		
		String link = "";
		
		if((this.url != null) && (this.url.trim() != "")) {
			url = this.contexPath + this.url;
		}
		
		String onclick="";
		
		for(int page=start; page <= finish; page++) {
			
			if(page == pagina) {
				a = " " + page + "&nbsp;";
				
			}else{
				
				if(url != null && url != "") {
					link = url + "&page=" + page;
				}else {
					link = "javascript:";
				}

				onclick = null;
				
				if(this.variableJavascript != null && this.variableJavascript != "") {
					onclick = this.variableJavascript + "(" + page + ");" ;
				}
				
				if(this.onclick != null) {
					onclick += this.onclick;
				}
				
				a = "<a href=\"" + link + "\" " + appendAtribute("onclick",onclick) + ">" + page + "</a> &nbsp;";
			}
			
			buffer.append(a);
		}
		
		return buffer.toString();
	}
	
	private String appendAtribute(String atribute, String valor) {
		
		if(valor != null) {
			return " " + atribute + "=\"" + valor + "\" ";
		}
		
		return "";
	}
	
	private int getStartPage() {
		
		int mostraPagina = (pagina % 10);
		
		mostraPagina = mostraPagina < 1? 1: mostraPagina;

		int start = pagina - (mostraPagina);
		
		start = start < 1? 1: start;

		if(start > this.getQuantidadePaginas()) {
			start = (int) this.getQuantidadePaginas();
		}
		
		if(mostraPagina > 7){
			start = start+3;
		}
		
		return start;
	}
	
	private int getFinishPage() {
		
		int mostraPagina = (pagina % 10);
		
		int finish = pagina + (10 - mostraPagina);
		finish = (int) (finish >= this.getQuantidadePaginas() ? this.getQuantidadePaginas(): finish);
		
		return finish;
	}
	
	@Override
	public int doStartTag() throws JspException {
		
		try {
			
			contexPath = this.pageContext.getServletContext().getContextPath();
			
			this.pageContext.getOut().write(this.moutPageResult());
			
		} catch (IOException e) {
			
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
	public static void main(String[] args) {
		
		PaginacaoTag tag = new PaginacaoTag();
		
		tag.setMaxResult(10);
		tag.setQuantidadeRegistro(191);
		tag.setPagina(19);
		
		tag.moutPageResult();
		
	}
}
