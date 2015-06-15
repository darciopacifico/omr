/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.settings;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.ETipoRelatorio;
import br.com.dlp.jazzomr.empresa.EmpresaBusiness;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.person.EAuthority;
import br.com.dlp.jazzomr.person.GrupoBusiness;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.poc.CopiaArquivo;
import br.com.dlp.jazzomr.results.ImageLogoVO;
import br.com.dlp.jazzomr.results.JRFileVO;
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
	
	private RelatorioVO selectedRelatorio;
	private JRFileVO jrUpload;

	
	/**
	 * Action para selecionar um registro de relatório. Após acionar este método um modal é aberto para que seja feito upload do arquivo.
	 * @param relatorioVO
	 * @return
	 */
	public String addArquivoJasper(RelatorioVO relatorioVO){
		
		if(!empresaBusiness.hasExams(relatorioVO)){
			selectedRelatorio = relatorioVO;
			this.jrUpload = new JRFileVO();
		}else{
			selectedRelatorio = null;
			FacesContext context = FacesContext.getCurrentInstance();					
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este relatório já foi utilizado num exame! Seus arquivos não podem ser alterados!", "Este relatório já foi utilizado num exame! Seus arquivos não podem ser alterados"));
			
		}
		
		return "";
	}	
	
	/**
	 * Retorna lista de SelectItem contendo enums para exibição em componentes JSF
	 * @return
	 */
	public List<SelectItem> getReportTypes(){
		return getEnums(ETipoRelatorio.class);
	}

	/**
	 * Cria novo registro de relatorio na empresa atual. 
	 * @return
	 */
	public String addNewRelatorio(){
		EmpresaVO empresaVO = getEmpresaVO();
		String pkPessoa = SecurityHelper.getInstance().getLoginName();
		RelatorioVO relatorioVO = selfRegBusinessImpl.crateNewRelatorio(pkPessoa);
		empresaVO.getRelatorioVOs().add(0,relatorioVO);
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
	 */
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

	
	/**
	 * Processa download do arquivo jasper selecionado
	 * @param jrFileVO
	 * @return
	 */
	public String downloadLayoputFile(JRFileVO jrFileVO){
		
		byte[] zipBytes = jrFileVO.getJasperReport();

		HttpServletResponse response = prepareHttpResponse(zipBytes, jrFileVO.getNome()+".jasper", "application/jasper");

		ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
		
		writeFileDownloadResponse(bais, response);
		
		return "";
		
	}
	

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
	 * Exclui o relatório de exame dos registros da empresa. Checa se o relatório já foi utilizado em algum exame. Se sim, não permite excluir o relatório
	 * @param relatorioVO
	 * @return
	 */
	public String deleteRelatorio(RelatorioVO relatorioVO){
	
		FacesContext context = FacesContext.getCurrentInstance();					
		
		if(!empresaBusiness.hasExams(relatorioVO)){
			
			EmpresaVO empresaVO2 = getEmpresaVO();
			empresaVO2.getRelatorioVOs().remove(relatorioVO);

			if(relatorioVO.getPK()!=null){
				empresaBusiness.deleteRelatorio(empresaVO2, relatorioVO);
				setEmpresaVO(null);
			}
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Relatório excluído com sucesso!", "Relatório excluído com sucesso!"));
		}else{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este relatório não pode ser excluído pois foi utilizado num exame!", "Este relatório não pode ser excluído pois foi utilizado num exame!"));
		}
		
		return "";
	}
	
	
	/**
	 * processa exclusão do arquivo jasper selecionado
	 * @return
	 */
	public String excluirJrFile(RelatorioVO relatorioVO, JRFileVO jrFileVO){
		FacesContext context = FacesContext.getCurrentInstance();					
		
		if(!empresaBusiness.hasExams(relatorioVO)){
			
			relatorioVO.getJrFileVOs().remove(jrFileVO);
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo excluído com sucesso!", "Arquivo excluído com sucesso!"));

		}else{
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este relatório já foi utilizado num exame! Seus arquivos não podem ser alterados!", "Este relatório já foi utilizado num exame! Seus arquivos não podem ser alterados"));
			
		}
		
		return "";
	}
	
	
	/**
	 * Processa upload do arquivo jasper inserindo-o no relatório selecionado
	 * @param event
  public void handleUploadRelatorio(UploadEvent event) {  
  	
  	List<UploadItem> files = event.getUploadItems();
  	
  	for (UploadItem uploadItem : files) {
			
  		byte[] data = getData(uploadItem);//espera um jasper. Já filtrado no componentes rich:fileupload.
			
			JRFileVO jrFileVO = getJrUpload();
			jrFileVO.setJasperReport(data);
			
			String datePart = LABEL_DTFORMAT.format(new Date());

			RelatorioVO relatorioVO = getSelectedRelatorio();
			relatorioVO.getJrFileVOs().add(jrFileVO);
		}
  	
		FacesContext context = FacesContext.getCurrentInstance();					
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivos carregados com sucesso!", "Arquivos carregados com sucesso!"));
  }
	 */


  
	/**
	 * 
	 * @param uploadItem
	 * @return
	protected RelatorioVO getrelatorioVO(UploadItem uploadItem) {
		byte[] data = getData(uploadItem);
		
		
		JRFileVO jrFileVO = new JRFileVO();
		jrFileVO.setJasperReport(data);
		
		RelatorioVO relatorioVO = new RelatorioVO();
		relatorioVO.getJrFileVOs().add(jrFileVO);

		String datePart = LABEL_DTFORMAT.format(new Date());
		relatorioVO.setDescription("Modelo de Prova ("+datePart+")");
		return relatorioVO;
	}

	 */

	

	/**
	 * Processa upload de imagem contendo novo logotipo da empresa
	 * @param event
  public void actionLogoImgUpload(UploadEvent event) {  
  	List<UploadItem> files = event.getUploadItems();
  	for (UploadItem uploadItem : files) {
			//transforma qualquer arquivo que vier (img, zip, gzip, pdf, word) numa colecao de imagens para processamento
			atualizaLogotipoEmpresa(uploadItem);
		}
		FacesContext context = FacesContext.getCurrentInstance();					
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagens carregadas com sucesso!", "Imagens carregadas com sucesso!"));
  }

	 */

  /**
	 * Transforma qualquer arquivo que vier (img, zip, gzip, pdf, word) numa colecao de imagens para processamento
	 * @param uploadedFile
	 * @return
	protected void atualizaLogotipoEmpresa(UploadItem uploadItem) {
		String contentType = uploadItem.getContentType();
		byte[] data = getData(uploadItem);

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(bais);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar ler imagem contida no array de bytes",e);
		}
		
		getEmpresaVO().getLogos().clear();
		
		ImageLogoVO imageLogoVO = new ImageLogoVO();
		imageLogoVO.setImage(bufferedImage);
		
		getEmpresaVO().getLogos().add(imageLogoVO);

	}
   */

	/**
	 * Recupera bytes do arquivo enviado
	 * @param uploadItem
	 * @return
	protected byte[] getData(UploadItem uploadItem) {
		
		if(!uploadItem.isTempFile()){
			return uploadItem.getData();
		}else{
			
			File file = uploadItem.getFile();
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				CopiaArquivo.copy(fis, baos, 2048);

				baos.flush();
				baos.close();
				fis.close();
				
				return baos.toByteArray();
				
			} catch (FileNotFoundException e) {
				throw new JazzRuntimeException("Erro ao tentar processar arquivo vindo por upload",e);
			} catch (IOException e) {
				throw new JazzRuntimeException("Erro ao tentar processar arquivo vindo por upload",e);
			}finally{
				
				file.delete();
			}
		}
	}
	 */

	
	
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
	/**
	 * Replica das propriedades pesquisaveis. Atributos para composicao de formulario de pesquisa
	 */
	public List<? extends Enum> autorizeAnyOf(){
		ArrayList<EAuthority> authorities = new ArrayList<EAuthority>();
		authorities.add(EAuthority.MASTER_ADM);
		return authorities;
	}



	public RelatorioVO getSelectedRelatorio() {
		return selectedRelatorio;
	}



	public void setSelectedRelatorio(RelatorioVO relatorioVO) {
		this.selectedRelatorio = relatorioVO;
	}



	public JRFileVO getJrUpload() {
		return jrUpload;
	}



	public void setJrUpload(JRFileVO jrUpload) {
		this.jrUpload = jrUpload;
	}
	
	
}