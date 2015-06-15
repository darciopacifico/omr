/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.settings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaBusiness;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.person.GrupoBusiness;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.selfservice.SelfRegBusinessImpl;

/**
 * Bean controller para manutenção das configurações de empresa
 **/
@Scope(value = "session")
@Component
public class ConfiguracoesJSFBean  {


	protected static final DateFormat LABEL_DTFORMAT = new SimpleDateFormat("ddMMyy_HHmmss");
	
	private static final long serialVersionUID = 2195241915100521959L;

	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Autowired
	private EmpresaBusiness empresaBusiness;

	@Autowired
	private GrupoBusiness grupoBusiness;

	@Autowired
	private SelfRegBusinessImpl selfRegBusinessImpl;
	
	private EmpresaVO empresaVO;

	
	/**
	 * Cria novo registro de relatorio na empresa atual. 
	 * @return
	 */
	public String addNewRelatorio(){
		EmpresaVO empresaVO = getEmpresaVO();
		String pkPessoa = SecurityHelper.getInstance().getLoginName();
		return "";
	}
	
	

	/**
	 * Salva os dados da empresa atual
	 * @return
	 */
	public String actionSalvarEmpresa(){
		
		try {
			empresaBusiness.saveOrUpdate(empresaVO);
			setEmpresaVO(null);
		} catch (JazzBusinessException e) {
			FacesContext context = FacesContext.getCurrentInstance();					
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar salvar dados!", "Erro ao tentar salvar dados!"));
		}
		successSaveMsg();
		
		return "";
	}


	/**
	 * Emite mensagem de sucesso
	 */
	protected void successSaveMsg() {
		FacesContext context = FacesContext.getCurrentInstance();					
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados salvos com sucesso!", "Dados salvos com sucesso!"));
	}
	

	/**
	 * Recupera VO referente a empresa do usuário logado.
	 * @return
	 */
	public EmpresaVO getEmpresaVO() {
		if(this.empresaVO==null){
			this.empresaVO = empresaBusiness.empresaUsuarioLogado();
		}
		return this.empresaVO;
	}

	
	public void setEmpresaVO(EmpresaVO empresaVO) {
		this.empresaVO = empresaVO;
	}


	/**
	 * Printa imagem do logotipo da empresa em componente p:graphicImage do Primefaces
	 * @return
	public StreamedContent streamEmpresaLogo() {
		DefaultStreamedContent content = new DefaultStreamedContent();
		BufferedImage bi = getEmpresaVO().getLogo().getImage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", baos);
		} catch (IOException e1) {
			throw new JazzRuntimeException("Erro ao tentar escrever imagem de saida!", e1);
		}
		byte[] imgBytes = baos.toByteArray();
		
		StreamedContent streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(imgBytes), "image/png", "logo.png");
		return streamedContent;
	}
	 */

	
	/**
	 * Joga array de bytes do arquivo em httpServletResponde para download
	 * 
	 * @param bais
	 * @param response
	 */
	protected void writeFileDownloadResponse(ByteArrayInputStream bais, HttpServletResponse response) {
		try {
			OutputStream out = response.getOutputStream();

			byte[] buf = new byte[1024];
			int count;
			while ((count = bais.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			bais.close();
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar enviar stream de arquivo para download!", e);
		}
	}
	
	/**
	 * Prepara httpResponse para a saida de bytes do arquivo pdf (1 relatorio) ou zip (>1 relatorio)
	 * 
	 * @param eventVO
	 * @param bytesFile
	 * @param contentType TODO
	 * @param fileNamePattern TODO
	 * @return
	 */
	protected HttpServletResponse prepareHttpResponse(byte[] bytesFile, String fileName, String contentType) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		
		HttpServletResponse httpResp = (HttpServletResponse) context.getResponse();

		httpResp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		
		httpResp.setContentLength((int) bytesFile.length );
		
		httpResp.setContentType(contentType);
		
		return httpResp;
	}


	
	/**
	 * Recupera lista do enum status para composicao de combobox, ou outro componente similar...
	 * 
	 * @return
	 */
	public List<SelectItem> getStatusList() {
		return getEnums(StatusEnum.class);
	}

	/**
	 * Monta lista de selectItem para qualquer enum informado
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
 	public <E extends Enum<E>> List<SelectItem> getEnums(Class<E> enumClass){
 		
 		List<SelectItem> itens = new ArrayList<SelectItem>();
 		
		EnumSet<E> enumSet = EnumSet.allOf(enumClass); 
 		
 		for (E enumItem : enumSet) {
			
 			SelectItem selectItem = new SelectItem();
 			
 			selectItem.setValue(enumItem);
 			selectItem.setLabel(enumItem+"");
 			
 			itens.add(selectItem);
 			
		}
 		
 		return itens;
 	}
	
	
}