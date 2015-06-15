package com.msaf.validador.core.lote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.core.tratamentoxls.Compactador;
import com.msaf.validador.integration.hibernate.Repositorio;
import com.msaf.validador.integration.hibernate.intf.ITpResultDAO;
import com.msaf.validador.integration.util.Util;

public class GeradorLote {

	private Repositorio repositorio;
	
	private List<CertificadoTO> certificados = new ArrayList<CertificadoTO>();
	
	private String directory;
	
	public GeradorLote(Repositorio repositorio) {
		this.repositorio = repositorio;
	}
	
	public byte[] gerarLotePaginas(Long idPessoa) {
		
		List<PessoaVO> list = repositorio.getPessoaDAO().findByIdPedido(idPessoa);
		
		// declara o zip
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ZipOutputStream zip = new ZipOutputStream(out);
		
		Compactador compactador = new Compactador();
		
		certificados.clear();
		
		if((list != null) && (!list.isEmpty())) {
			
			String nameFileHtml = "";
			String label = "";
			
			String html = "";
			
			Set<DadosRetInstVO> listDados = null;
			
			for (PessoaVO pessoa : list) {

				
				listDados = pessoa.getDadosRetornoFk();
				
				if(listDados != null && !listDados.isEmpty()) {
					
					for (DadosRetInstVO dados : listDados) {

						nameFileHtml = "mastersaf/" + dados.getPk() + "_Certificado.html";
			
						label = "" + dados.getPk();
						
						certificados.add(new CertificadoTO(nameFileHtml, label));
						
						try {
							
							html = montaHtmlDeRetorno(dados);
							
							// adiciona no zip
							compactador.addToZip(zip, nameFileHtml, html.getBytes());
							
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}
				}
			}
		}
		
		
		try {
			
			// cria a página index
			this.criarIndex(zip);
			
			
			
			zip.flush();
			zip.close();

			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return out.toByteArray();
	}

	private String montaHtmlDeRetorno(DadosRetInstVO dadosRetInstVO) {
		
		String html = "";
		
		if(Util.isEmpty(dadosRetInstVO.getPagina())) {
			
			
			ITpResultDAO tpResultDAO = this.repositorio.getTpResultDAO();
			
			String identif = dadosRetInstVO.getIdentif();
			
			if(Util.isEmpty(identif)) {
				return "<html><body><h1>Consulta invalida</h1></body></html>";
			}
			
			TpResultVO tpResultVO = tpResultDAO.findById(Long.parseLong(identif));
			
			if(tpResultVO == null || Util.isEmpty(tpResultVO.getDescricao())) {
				return "<html><body><h1>Consulta invalida</h1></body></html>";
			}
			
			html = MessageFormat.format("<html><body><h1>{0}</h1></body></html>",tpResultVO.getDescricao());
			
		} else {
			html = dadosRetInstVO.getPagina();
		}
		
		
		html = html.replace("elt;","<" );
		html = html.replace( "egt;",">");
		html = html.replace("eamp;nbsp;","&nbsp;");
		
		return html;
	}
	
	private String criarTemplete() throws Exception {
		
		Properties property = new Properties();
	    property.put("input.encoding", "utf-8");
	    
	    if(!Util.isEmpty(directory)) {
	    	property.setProperty("file.resource.loader.path", directory + "/template");   
	    }

	    // inicializando o velocity  
	    VelocityEngine ve = new VelocityEngine();  
	    ve.init(property);  
	       
	    // criando o contexto que liga o java ao template  
	    VelocityContext context = new VelocityContext();  
	       
	    // escolhendo o template  
	    Template t = ve.getTemplate("certificado.vm");  
	           
	       
	    // aqui! damos a variavel list para  
	    // o contexto!  
	    context.put("certificados", this.certificados);
	    
	    StringWriter writer = new StringWriter();  
	       
	    // mistura o contexto com o template  
	    t.merge(context, writer);  

	    return writer.toString();  
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	private void criarIndex(ZipOutputStream zip) throws Exception {
		
		String html = this.criarTemplete();
		
		Compactador compactador = new Compactador();
		
		compactador.addToZip(zip, "index.html", html.getBytes());

		this.addImages(zip);
		
	}
	
	private void addImages(ZipOutputStream zip) throws Exception {
		
		String[] images = new String[] {"bg_title0.gif","certificado_lote1.png", "logo_validador_pagerro.png"};
		
		Compactador compactador = new Compactador();
		
		for (String image : images) {
			compactador.addToZip(zip, "imagens/" + image, directory + "/imagens/" + image);
		}
	}
}
