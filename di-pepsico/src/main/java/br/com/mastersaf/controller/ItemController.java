package br.com.mastersaf.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import br.com.mastersaf.model.AditivoDIItemNFE;
import br.com.mastersaf.model.DiItemNFE;
import br.com.mastersaf.model.Empresa;
import br.com.mastersaf.util.CriteriaExpression;
import br.com.mastersaf.util.Filter;
import br.com.mastersaf.util.FilterOperator;
import br.com.mastersaf.util.GenericBo;

public class ItemController {

	private final String MSG_SUCESS = "sucessMessage";
	private final String MSG_ERROR = "errorMessage";

	private GenericBo genericBo;
	private Long idNFE;

	private Long idEmpresa;
	private Long nrNota;

	private Empresa empresa;
	private List<DiItemNFE> all;

	private String errorMessage;
	private String styleMessage;

	private String idsItem;
	private String idsAdi;

	// propriedades para di
	private String nrDocumento;
	private String nrItemModal;
	private String nrDocumentoModal;
	private String cdExportadorModal;
	private Date dtRegistroModal;
	private String dsLocalModal;
	private String cdUfModal;
	private Date dtAduaneroModal;
	private DiItemNFE diModal;

	// propriedades para adi
	private List<AditivoDIItemNFE> adiList;
	private Long idAdicaoEditar;
	private AditivoDIItemNFE aditivo;
	private AditivoDIItemNFE aditivoEdit;

	private Long nrAdiModal;
	private Long nrSequencialAdiModal;
	private String vlDescontoAdiModal;
	private String cdEstrangeiroAdiModal;

	private Long adiSessionIdEmpresa;
	private Long adiSessionIdItem;
	private Long adiSessionIdNF;
	private String adiSessionnrDocumento;

	private boolean mostrarModal1;
	private boolean mostrarModal2;
	private boolean mostrarModal3;

	private boolean mostrarListarADI;

	public boolean isMostrarModal1() {
		return mostrarModal1;
	}

	public void setMostrarModal1(boolean mostrarModal1) {
		this.mostrarModal1 = mostrarModal1;
	}

	public boolean isMostrarModal2() {
		return mostrarModal2;
	}

	public void setMostrarModal2(boolean mostrarModal2) {
		this.mostrarModal2 = mostrarModal2;
	}

	public boolean isMostrarModal3() {
		return mostrarModal3;
	}

	public void ativarModal1() {
		this.setMostrarModal1(true);
		this.setMostrarModal2(false);
		this.setMostrarModal3(false);
	}

	public void ativarModal2() {
		this.setMostrarModal1(false);
		this.setMostrarModal2(true);
		this.setMostrarModal3(false);
	}

	public void ativarModal3() {
		this.setMostrarModal1(false);
		this.setMostrarModal2(false);
		this.setMostrarModal3(true);
	}

	public void setMostrarModal3(boolean mostrarModal3) {
		this.mostrarModal3 = mostrarModal3;
	}

	public AditivoDIItemNFE getAditivo() {
		return aditivo;
	}

	public void setAditivo(AditivoDIItemNFE aditivo) {
		this.aditivo = aditivo;
	}

	public DiItemNFE getDiModal() {
		return diModal;
	}

	public void setDiModal(DiItemNFE diModal) {
		this.diModal = diModal;
	}

	public void setGenericBo(GenericBo genericBo) {
		this.genericBo = genericBo;
	}

	public String chegada() {
		this.idNFE = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("idNF"));
		this.idEmpresa = Long
				.valueOf(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestParameterMap().get("idEmpresa"));
		this.nrNota = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("nrNota"));

		populateEmpresa();
		populateAll();

		return "listaItem";

	}

	public String voltar() {
		return "voltar";
	}

	private void populateEmpresa() {

		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(this.idEmpresa);
		try {
			this.empresa = (Empresa) this.genericBo.get(empresa).get(0);
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unchecked")
	private void populateAll() {
		/*DiItemNFE diItem = new DiItemNFE();
		diItem.getDiItemNFEPK().setIdNf(this.idNFE);
		diItem.getDiItemNFEPK().setIdEmpresa(this.idEmpresa);*/
		//diItem.getDiItemNFEPK().setIdItem(idItem);
		//diItem.getDiItemNFEPK().setNrDocumento(adiSessionnrDocumento);
		
		
		
		try {
			CriteriaExpression criteriaExpression = new CriteriaExpression(DiItemNFE.class);

			Filter filter = new Filter();
			filter.setAttribute("diItemNFEPK.idEmpresa");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(this.idEmpresa);
			criteriaExpression.addFilter(filter);

			filter = new Filter();
			filter.setAttribute("diItemNFEPK.idNf");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(this.idNFE);
			criteriaExpression.addFilter(filter);

			this.all = (List) this.genericBo.get(criteriaExpression);
			
		//	System.out.println(all);
					
			
		} catch (Exception e) {
		}

		if (this.all.size() > 0) {
			DiItemNFE busca = (DiItemNFE) this.all.get(0);
			this.adiSessionIdEmpresa = busca.getDiItemNFEPK().getIdEmpresa();
			this.adiSessionIdItem = busca.getDiItemNFEPK().getIdItem();
			this.adiSessionnrDocumento = busca.getDiItemNFEPK().getNrDocumento();
			this.adiSessionIdNF = busca.getDiItemNFEPK().getIdNf();
		}
	}

	@SuppressWarnings("unchecked")
	public void editarDI() {

		// exibe o modal de edicao
		this.ativarModal1();

		String nrDocumentoDi = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"nrDocumentoDi");
		Long idEmpresaDi = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"idEmpresaDi"));
		Long idNFDi = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("idNFDi"));
		Long idItemDi = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("idItemDi"));

		this.diModal = new DiItemNFE();
		this.diModal.getDiItemNFEPK().setIdEmpresa(idEmpresaDi);
		this.diModal.getDiItemNFEPK().setIdNf(idNFDi);
		this.diModal.getDiItemNFEPK().setNrDocumento(nrDocumentoDi);
		this.diModal.getDiItemNFEPK().setIdItem(idItemDi);
		try {
			this.diModal = (DiItemNFE) this.genericBo.get(this.diModal).get(0);
		} catch (Exception e) {
		}

		this.nrDocumentoModal = this.diModal.getDiItemNFEPK().getNrDocumento();
		this.cdExportadorModal = this.diModal.getExportador();
		this.nrItemModal = this.diModal.getDiItemNFEPK().getIdItem().toString();
		this.dtRegistroModal = this.diModal.getDtRegistro();
		this.dsLocalModal = this.diModal.getLocal();
		this.cdUfModal = this.diModal.getUf();
		this.dtAduaneroModal = this.diModal.getDtSaida();

	}

	public void salvarDi() {

		if (validaCamposDI()) {

			//devido a pepsico necessitar alterar um campo pertencente a chave da tabela
			//sera desenvolvido esse metodo salvar com work around.
			String antigoNrdocumento = this.diModal.getDiItemNFEPK().getNrDocumento();
			Long antigoIdEmpresa = this.diModal.getDiItemNFEPK().getIdEmpresa();
			Long antigoIdItem = this.diModal.getDiItemNFEPK().getIdItem();
			Long antigoIdNf = this.diModal.getDiItemNFEPK().getIdNf();
			
			//efetuar update dos filhos de ADICAO
			try{
				StringBuilder hqlAdi = new StringBuilder();
				hqlAdi.append(" update ");
				hqlAdi.append(AditivoDIItemNFE.class.getName());
				hqlAdi.append(" a set ");
				hqlAdi.append("a.aditivoDIItemNFEPK.nrDocumento = :parADI1 ");
				hqlAdi.append("where ");
				hqlAdi.append("a.aditivoDIItemNFEPK.idEmpresa = :parADI2 and ");
				hqlAdi.append("a.aditivoDIItemNFEPK.idItem = :parADI3 and ");
				hqlAdi.append("a.aditivoDIItemNFEPK.idNf = :parADI4 and ");
				hqlAdi.append("a.aditivoDIItemNFEPK.nrDocumento = :parADI5 ");
				
				Session s = this.genericBo.getSession();
				Query query = s.createQuery(hqlAdi.toString());
				query.setParameter("parADI1", getNrDocumentoModal() );
				query.setParameter("parADI2", antigoIdEmpresa );
				query.setParameter("parADI3", antigoIdItem );
				query.setParameter("parADI4", antigoIdNf );
				query.setParameter("parADI5", antigoNrdocumento );
				query.executeUpdate();
				
				System.out.println("atualizando os filhos ADI--------------------------------------------------------------------");
				
				//atualiza os campos no objeto de dimodal
				this.diModal.getDiItemNFEPK().setNrDocumento(getNrDocumentoModal());
				this.diModal.setExportador(getCdExportadorModal());
				this.diModal.getDiItemNFEPK().setIdItem(Long.valueOf(getNrItemModal()));
				this.diModal.setDtRegistro(getDtRegistroModal());
				this.diModal.setLocal(getDsLocalModal());
				this.diModal.setUf(getCdUfModal());
				this.diModal.setDtSaida(getDtAduaneroModal());

				try {
					
					System.out.println("ids=" + this.diModal.getDiItemNFEPK().toString());
					
					StringBuilder hql = new StringBuilder();
					hql.append("update ");
					hql.append( DiItemNFE.class.getName());
					hql.append( " d set " );
					hql.append("d.diItemNFEPK.nrDocumento = :parUP1, ");
					hql.append("d.exportador = :parUP2, ");
					hql.append("d.diItemNFEPK.idItem = :parUP3, ");
					hql.append("d.dtRegistro = :parUP4, ");
					hql.append("d.local = :parUP5, ");
					hql.append("d.uf = :parUP6, ");
					hql.append("d.dtSaida = :parUP7 ");
					hql.append("where d.diItemNFEPK.idEmpresa = :parUP8 and ");
					hql.append("d.diItemNFEPK.idItem = :parUP9 and ");
					hql.append("d.diItemNFEPK.idNf = :parUP10 and ");
					hql.append("d.diItemNFEPK.nrDocumento = :parUP11 ");
					
					Session ss = this.genericBo.getSession();
					Query querys = s.createQuery(hql.toString());
					querys.setParameter("parUP1", getNrDocumentoModal() );
					querys.setParameter("parUP2", getCdExportadorModal() );
					querys.setParameter("parUP3", Long.valueOf(getNrItemModal()) );
					querys.setParameter("parUP4", getDtRegistroModal());
					querys.setParameter("parUP5", getDsLocalModal() );
					querys.setParameter("parUP6", getCdUfModal() );
					querys.setParameter("parUP7", getDtAduaneroModal() );
					querys.setParameter("parUP8", antigoIdEmpresa);
					querys.setParameter("parUP9", antigoIdItem);
					querys.setParameter("parUP10",antigoIdNf);
					querys.setParameter("parUP11",antigoNrdocumento);

					querys.executeUpdate();
					
					System.out.println("ATUALIZANDO DI ----------------------------");
					
					setMessage("DI salva com sucesso!", MSG_SUCESS);

					// atualizar o all
					populateAll();

				} catch (Exception e) {
					setMessage("Erro ao gravar a DI. O campo n�mero do documento n�o pode ser o mesmo de outra DI do mesmo Item da mesma Nota.", MSG_ERROR);
					e.printStackTrace();
				}

			}catch(Exception e){
				setMessage("N�o � poss�vel modificar o n�mero de documento quando existem ADIs cadastradas para esta DI", MSG_ERROR);
				e.printStackTrace();
			}
			
			
		}

	}

	public void editaAdi() {

		// abilitar o terceito modal
		this.ativarModal3();

		limparMSG();

		Long idAdicaoADI = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"idAdicaoADI"));
		Long idEmpresaADI = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"idEmpresaADI"));
		Long idItemADI = Long
				.valueOf(FacesContext.getCurrentInstance().getExternalContext()
						.getRequestParameterMap().get("idItemADI"));
		Long idNFADI = Long.valueOf(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("idNFADI"));
		String nrDocumentoADI = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get(
						"nrDocumentoADI");

		aditivoEdit = new AditivoDIItemNFE();
		aditivoEdit.getAditivoDIItemNFEPK().setIdAdicao(idAdicaoADI);
		aditivoEdit.getAditivoDIItemNFEPK().setIdEmpresa(idEmpresaADI);
		aditivoEdit.getAditivoDIItemNFEPK().setIdItem(idItemADI);
		aditivoEdit.getAditivoDIItemNFEPK().setIdNf(idNFADI);
		aditivoEdit.getAditivoDIItemNFEPK().setNrDocumento(nrDocumentoADI);

		try {
			aditivoEdit = (AditivoDIItemNFE) this.genericBo.get(aditivoEdit)
					.get(0);
			this.nrAdiModal = aditivoEdit.getAditivoDIItemNFEPK().getIdAdicao();
			this.nrSequencialAdiModal = aditivoEdit.getNrSeq();
			String descontoBR = aditivoEdit.getDesconto().toString();
			descontoBR = descontoBR.replace(".", ",");
			this.vlDescontoAdiModal = descontoBR;
			this.cdEstrangeiroAdiModal = aditivoEdit.getFabricante();
			this.limparMSG();
		} catch (Exception e) {
			setMessage("Erro ao obter a adi", MSG_ERROR);
			e.printStackTrace();
		}
	}

	//
	public void deletaAdi() {

		limparMSG();

		if (!"".equals(this.idsAdi)) {
			AditivoDIItemNFE adToSession = new AditivoDIItemNFE();
			adToSession.getAditivoDIItemNFEPK().setIdAdicao(Long.valueOf(this.idsAdi));
			adToSession.getAditivoDIItemNFEPK().setIdEmpresa(this.adiSessionIdEmpresa);
			adToSession.getAditivoDIItemNFEPK().setIdItem(this.adiSessionIdItem);
			adToSession.getAditivoDIItemNFEPK().setIdNf(this.adiSessionIdNF);

			try {
				String hql = "delete from "+AditivoDIItemNFE.class.getName()+" o where o.aditivoDIItemNFEPK.idAdicao = :idAdicaoQ and o.aditivoDIItemNFEPK.idEmpresa = :idEmpresaQ and o.aditivoDIItemNFEPK.idItem = :idItemQ and o.aditivoDIItemNFEPK.idNf = :idNFQ";
				
				Session s = this.genericBo.getSession();
				Query query = s.createQuery(hql);
				query.setParameter("idAdicaoQ", adToSession.getAditivoDIItemNFEPK().getIdAdicao());
				query.setParameter("idEmpresaQ", adToSession.getAditivoDIItemNFEPK().getIdEmpresa());
				query.setParameter("idItemQ", adToSession.getAditivoDIItemNFEPK().getIdItem());
				query.setParameter("idNFQ", adToSession.getAditivoDIItemNFEPK().getIdNf());
				query.executeUpdate();
				setMessage("Registro removido com sucesso", MSG_SUCESS);
			} catch (Exception e) {
				e.printStackTrace();
				setMessage("Erro ao Excluir", MSG_ERROR);
			}

			refreshAdiList(this.idsItem);
		} else {
			setMessage("selecione uma ADI para deletar", MSG_ERROR);
		}
	}

	private boolean validaCamposDI() {
		boolean validado = true;

		if ("".equals(this.cdExportadorModal)) {
			setMessage("O c�digo exportador n�o pode ficar em branco",
					MSG_ERROR);
			validado = false;
		}

		if (this.dtRegistroModal == null) {
			setMessage("A data de registro n�o pode ficar em branco", MSG_ERROR);
			validado = false;
		}

		if ("".equals(this.dsLocalModal)) {
			setMessage("O local desembara�o n�o pode ficar em branco",
					MSG_ERROR);
			validado = false;
		}

		if ("".equals(this.cdUfModal)) {
			setMessage("A UF n�o pode ficar em branco", MSG_ERROR);
			validado = false;
		}

		if (this.dtAduaneroModal == null) {
			setMessage("A Data des. aduanero n�o pode ficar em branco",
					MSG_ERROR);
			validado = false;
		}
		
		if ("".equals(this.nrDocumentoModal)){
			setMessage("O N�mero do documento n�o pode ficar em branco", MSG_ERROR);
			validado = false;
		}

		Matcher pesquisaUF;

		Pattern padraoUF = Pattern.compile("^([a-zA-Z]){2}$");
		pesquisaUF = padraoUF.matcher(this.cdUfModal);

		if (!pesquisaUF.matches()) {
			setMessage("UF informada incorreta.", MSG_ERROR);
			validado = false;
		}

		return validado;
	}

	private boolean validaCampos() {
		boolean validado = true;

		Matcher pesquisaNrSeq, pesquisaDesconto;

		Pattern padraoNrSeq = Pattern.compile("^([0-9]){1,3}$");
		pesquisaNrSeq = padraoNrSeq.matcher(this.nrSequencialAdiModal
				.toString());

		Pattern padraoDesconto = Pattern
				.compile("^([0-9]){1,6}([,]){1}([0-9]){2}$");
		pesquisaDesconto = padraoDesconto.matcher(this.vlDescontoAdiModal);

		if (!pesquisaNrSeq.matches()) {
			setMessage(
					"N�mero sequencial inv�lido, o campo precisa ser num�rico",
					MSG_ERROR);
			validado = false;
		}

		if (!pesquisaDesconto.matches()) {
			setMessage(
					"Valor de desconto inv�lido, o fomato de ser como: 9,99",
					MSG_ERROR);
			validado = false;
		} else {
			this.vlDescontoAdiModal = this.vlDescontoAdiModal.replace(",", ".");
		}

		if (this.vlDescontoAdiModal == "" || this.vlDescontoAdiModal == null) {
			setMessage("Todos os campos precisam ser preenchidos!", MSG_ERROR);
			validado = false;
		}

		if (this.cdEstrangeiroAdiModal == ""
				|| this.cdEstrangeiroAdiModal == null) {
			setMessage("Todos os campos precisam ser preenchidos!", MSG_ERROR);
			validado = false;
		}

		if (this.nrSequencialAdiModal == 0 || this.nrSequencialAdiModal == null) {
			setMessage("Todos os campos precisam ser preenchidos!", MSG_ERROR);
			validado = false;
		}

		return validado;

	}

	public void novoAdi() {

		// ativar o terceiro modal
		this.ativarModal3();

		limparMSG();

		this.aditivoEdit = new AditivoDIItemNFE();
		this.aditivoEdit.getAditivoDIItemNFEPK().setIdEmpresa(this.adiSessionIdEmpresa);
		this.aditivoEdit.getAditivoDIItemNFEPK().setIdItem(this.adiSessionIdItem);
		this.aditivoEdit.getAditivoDIItemNFEPK().setIdNf(this.adiSessionIdNF);
		this.aditivoEdit.getAditivoDIItemNFEPK().setNrDocumento(this.adiSessionnrDocumento);

		this.nrAdiModal = null;
		this.nrSequencialAdiModal = null;
		this.vlDescontoAdiModal = "";
		this.cdEstrangeiroAdiModal = "";
	}

	public void voltarListaADI() {
		this.limparMSG();
		// ativar o modal de lista de adi
		this.ativarModal2();
	}

	public void salvarADI() {

		limparMSG();

		if (validaCampos()) {
			if (this.nrAdiModal != null) {
				this.aditivoEdit.getAditivoDIItemNFEPK().setIdAdicao(this.nrAdiModal);
			} else {
				this.aditivoEdit.getAditivoDIItemNFEPK().setIdAdicao(obterIdADI());
			}
			this.aditivoEdit.setNrSeq(this.nrSequencialAdiModal);
			Float vlDescontoF = Float.valueOf(this.vlDescontoAdiModal);
			this.aditivoEdit.setDesconto(BigDecimal.valueOf(vlDescontoF));
			this.aditivoEdit.setFabricante(this.cdEstrangeiroAdiModal);
			// this.aditivoEdit.setIdEmpresa(adiSessionIdEmpresa);

			try {
				this.genericBo.save(this.aditivoEdit);
				setMessage("Adi salva com sucesso", MSG_SUCESS);
				refreshAdiList(this.aditivoEdit.getAditivoDIItemNFEPK().getNrDocumento());
			} catch (Exception e) {
				e.printStackTrace();
				setMessage("Erro ao salvar ADI", MSG_ERROR);
			}
		}
	}

	private Long obterIdADI() {
		Long retorno = null;
		try {
			retorno = (Long) this.genericBo.getSession().createCriteria(
					AditivoDIItemNFE.class).setProjection(
					Projections.max("aditivoDIItemNFEPK.idAdicao")).uniqueResult();

		} catch (Exception e) {
			setMessage("Erro ao obter uma nova ADI", MSG_ERROR);
		}

		retorno += 1L;

		return retorno;
	}

	private void refreshAdiList(String idsItem) {
		
		//tokenizer paramentro radio///////////////////////////////////////////
		String[] idsToken = this.idsItem.split("\\|\\:");
		Long idItem = Long.valueOf(idsToken[0]);
		String nrDocumento = idsToken[1];
		Long idEmpresa = Long.valueOf(idsToken[2]);
		Long idNf = Long.valueOf(idsToken[3]);
		
		this.adiSessionIdEmpresa = idEmpresa;
		this.adiSessionIdItem = idItem;
		this.adiSessionIdNF = idNf;
		this.adiSessionnrDocumento = nrDocumento;
		///////////////////////////////////////////////////////////////////////
		
		aditivo = new AditivoDIItemNFE();
		aditivo.getAditivoDIItemNFEPK().setIdItem(idItem);
		aditivo.getAditivoDIItemNFEPK().setIdEmpresa(idEmpresa);
		aditivo.getAditivoDIItemNFEPK().setIdNf(idNf);
		aditivo.getAditivoDIItemNFEPK().setNrDocumento(nrDocumento);
		
		try {
			CriteriaExpression criteriaExpression = new CriteriaExpression(AditivoDIItemNFE.class);

			Filter filter = new Filter();
			filter.setAttribute("aditivoDIItemNFEPK.idItem");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(idItem);
			criteriaExpression.addFilter(filter);

			filter = new Filter();
			filter.setAttribute("aditivoDIItemNFEPK.idNf");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(idNf);
			criteriaExpression.addFilter(filter);
			
			filter = new Filter();
			filter.setAttribute("aditivoDIItemNFEPK.idEmpresa");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(idEmpresa);
			criteriaExpression.addFilter(filter);
			
			filter = new Filter();
			filter.setAttribute("aditivoDIItemNFEPK.nrDocumento");
			filter.setOperation(FilterOperator.EQUAL);
			filter.setValue(nrDocumento);
			criteriaExpression.addFilter(filter);
			
			this.adiList = (List) this.genericBo.get(criteriaExpression);
		} catch (Exception e) {
			setMessage("Erro ao obter lista de ADI", MSG_ERROR);
		}


	}

	@SuppressWarnings("unchecked")
	public void listarAdi() {

		// exibe o modal de adi
		this.ativarModal2();

		limparMSG();

		if (!"".equals(this.idsItem)) {
			
			//tokenizer paramentro radio///////////////////////////////////////////
			String[] idsToken = this.idsItem.split("\\|\\:");
			Long idItem = Long.valueOf(idsToken[0]);
			String nrDocumento = idsToken[1];
			Long idEmpresa = Long.valueOf(idsToken[2]);
			Long idNf = Long.valueOf(idsToken[3]);
			
			this.adiSessionIdEmpresa = idEmpresa;
			this.adiSessionIdItem = idItem;
			this.adiSessionIdNF = idNf;
			this.adiSessionnrDocumento = nrDocumento;
			///////////////////////////////////////////////////////////////////////
			
			aditivo = new AditivoDIItemNFE();
			aditivo.getAditivoDIItemNFEPK().setIdItem(idItem);
			aditivo.getAditivoDIItemNFEPK().setIdEmpresa(idEmpresa);
			aditivo.getAditivoDIItemNFEPK().setIdNf(idNf);
			aditivo.getAditivoDIItemNFEPK().setNrDocumento(nrDocumento);
			try {
				
				CriteriaExpression criteriaExpression = new CriteriaExpression(AditivoDIItemNFE.class);

				Filter filter = new Filter();
				filter.setAttribute("aditivoDIItemNFEPK.idItem");
				filter.setOperation(FilterOperator.EQUAL);
				filter.setValue(idItem);
				criteriaExpression.addFilter(filter);

				filter = new Filter();
				filter.setAttribute("aditivoDIItemNFEPK.idNf");
				filter.setOperation(FilterOperator.EQUAL);
				filter.setValue(idNf);
				criteriaExpression.addFilter(filter);
				
				filter = new Filter();
				filter.setAttribute("aditivoDIItemNFEPK.idEmpresa");
				filter.setOperation(FilterOperator.EQUAL);
				filter.setValue(idEmpresa);
				criteriaExpression.addFilter(filter);
				
				filter = new Filter();
				filter.setAttribute("aditivoDIItemNFEPK.nrDocumento");
				filter.setOperation(FilterOperator.EQUAL);
				filter.setValue(nrDocumento);
				criteriaExpression.addFilter(filter);

				this.adiList = (List) this.genericBo.get(criteriaExpression);
			} catch (Exception e) {
				setMessage("Erro ao obter lista de ADI", MSG_ERROR);
			}
		} else {
			this.adiList = new ArrayList<AditivoDIItemNFE>();
			setMessage(
					"Erro ao localizar ADIS, verifique se pelo menos um, Item foi selecionado",
					MSG_ERROR);
		}

		if (this.adiList.size() > 0) {
			AditivoDIItemNFE adToSession = this.adiList.get(0);
			this.adiSessionIdEmpresa = adToSession.getAditivoDIItemNFEPK().getIdEmpresa();
			this.adiSessionIdItem = adToSession.getAditivoDIItemNFEPK().getIdItem();
			this.adiSessionIdNF = adToSession.getAditivoDIItemNFEPK().getIdNf();
			this.adiSessionnrDocumento = adToSession.getAditivoDIItemNFEPK().getNrDocumento();
		}

	}

	public void setIdNFE(Long idNFE) {
		this.idNFE = idNFE;
	}

	public Long getIdNFE() {
		return idNFE;
	}

	public Long getNrNota() {
		return nrNota;
	}

	public void setNrNota(Long nrNota) {
		this.nrNota = nrNota;
	}

	public void setAll(List<DiItemNFE> all) {
		this.all = all;
	}

	public List<DiItemNFE> getAll() {
		return all;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void limparMSG() {
		this.setMessage("", MSG_SUCESS);
	}

	private void setMessage(String message, String type) {
		this.setErrorMessage(message);
		this.setStyleMessage(type);
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setStyleMessage(String styleMessage) {
		this.styleMessage = styleMessage;
	}

	public String getStyleMessage() {
		return styleMessage;
	}

	public void setIdsItem(String idsItem) {
		this.idsItem = idsItem;
	}

	public String getIdsItem() {
		return idsItem;
	}

	public void setIdsAdi(String idsAdi) {
		this.idsAdi = idsAdi;
	}

	public String getIdsAdi() {
		return idsAdi;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrItemModal(String nrItemModal) {
		this.nrItemModal = nrItemModal;
	}

	public String getNrItemModal() {
		return nrItemModal;
	}

	public void setNrDocumentoModal(String nrDocumentoModal) {
		this.nrDocumentoModal = nrDocumentoModal;
	}

	public String getNrDocumentoModal() {
		return nrDocumentoModal;
	}

	public void setCdExportadorModal(String cdExportadorModal) {
		this.cdExportadorModal = cdExportadorModal;
	}

	public String getCdExportadorModal() {
		return cdExportadorModal;
	}

	public void setDtRegistroModal(Date dtRegistroModal) {
		this.dtRegistroModal = dtRegistroModal;
	}

	public Date getDtRegistroModal() {
		return dtRegistroModal;
	}

	public void setDsLocalModal(String dsLocalModal) {
		this.dsLocalModal = dsLocalModal;
	}

	public String getDsLocalModal() {
		return dsLocalModal;
	}

	public void setCdUfModal(String cdUfModal) {
		this.cdUfModal = cdUfModal;
	}

	public String getCdUfModal() {
		return cdUfModal;
	}

	public void setDtAduaneroModal(Date dtAduaneroModal) {
		this.dtAduaneroModal = dtAduaneroModal;
	}

	public Date getDtAduaneroModal() {
		return dtAduaneroModal;
	}

	public void setAdiList(List<AditivoDIItemNFE> adiList) {
		this.adiList = adiList;
	}

	public List<AditivoDIItemNFE> getAdiList() {
		return adiList;
	}

	public void setIdAdicaoEditar(Long idAdicaoEditar) {
		this.idAdicaoEditar = idAdicaoEditar;
	}

	public Long getIdAdicaoEditar() {
		return idAdicaoEditar;
	}

	public void setNrAdiModal(Long nrAdiModal) {
		this.nrAdiModal = nrAdiModal;
	}

	public Long getNrAdiModal() {
		return nrAdiModal;
	}

	public void setNrSequencialAdiModal(Long nrSequencialAdiModal) {
		this.nrSequencialAdiModal = nrSequencialAdiModal;
	}

	public Long getNrSequencialAdiModal() {
		return nrSequencialAdiModal;
	}

	public void setVlDescontoAdiModal(String vlDescontoAdiModal) {
		this.vlDescontoAdiModal = vlDescontoAdiModal;
	}

	public String getVlDescontoAdiModal() {
		return vlDescontoAdiModal;
	}

	public void setCdEstrangeiroAdiModal(String cdEstrangeiroAdiModal) {
		this.cdEstrangeiroAdiModal = cdEstrangeiroAdiModal;
	}

	public String getCdEstrangeiroAdiModal() {
		return cdEstrangeiroAdiModal;
	}

	public AditivoDIItemNFE getAditivoEdit() {
		return aditivoEdit;
	}

	public void setAditivoEdit(AditivoDIItemNFE aditivoEdit) {
		this.aditivoEdit = aditivoEdit;
	}

	public void setAdiSessionIdEmpresa(Long adiSessionIdEmpresa) {
		this.adiSessionIdEmpresa = adiSessionIdEmpresa;
	}

	public Long getAdiSessionIdEmpresa() {
		return adiSessionIdEmpresa;
	}

	public void setAdiSessionIdItem(Long adiSessionIdItem) {
		this.adiSessionIdItem = adiSessionIdItem;
	}

	public Long getAdiSessionIdItem() {
		return adiSessionIdItem;
	}

	public void setAdiSessionIdNF(Long adiSessionIdNF) {
		this.adiSessionIdNF = adiSessionIdNF;
	}

	public Long getAdiSessionIdNF() {
		return adiSessionIdNF;
	}

	public void setAdiSessionnrDocumento(String adiSessionnrDocumento) {
		this.adiSessionnrDocumento = adiSessionnrDocumento;
	}

	public String getAdiSessionnrDocumento() {
		return adiSessionnrDocumento;
	}

	public void setMostrarListarADI(boolean mostrarListarADI) {
		this.mostrarListarADI = mostrarListarADI;
	}

	public boolean isMostrarListarADI() {
		if (this.all == null) {
			this.mostrarListarADI = false;
		} else if (this.all.size() < 1) {
			this.mostrarListarADI = false;
		} else {
			this.mostrarListarADI = true;
		}
		return mostrarListarADI;
	}

}
