package br.com.mastersaf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.mastersaf.model.Empresa;
import br.com.mastersaf.model.NotaFiscalEletronica;
import br.com.mastersaf.util.GenericBo;

public class NotaFiscalEletronicaController{
	
	private static final long serialVersionUID = 1L;
	
	private final String MSG_SUCESS = "sucessMessage";
	private final String MSG_ERROR = "errorMessage";
	
	private GenericBo genericBo;
	
	private List<NotaFiscalEletronica> all;
	private int scrollerPage;
	private List<Empresa> empresas;
	private String nmEmpresa;
	private List<SelectItem> selectEmpresa;
	private Date dataInicial;
	private Date dataFinal;
	private String nmUsuario;
	private String errorMessage;
	private String styleMessage;
	
	private Map<Long, Boolean> idsNF = new HashMap<Long, Boolean>();
	
	
	
	
	public void setGenericBo(GenericBo genericBo) {
		this.genericBo = genericBo;
	}
	
	
	public void reenviar(){
		
		for(NotaFiscalEletronica nfe : all ){
			if(idsNF != null && nfe != null){
				Long idNF  = nfe.getNotaFiscalEletronicaPK().getIdNf();
				Boolean bol = idsNF.get(idNF);
				if(bol != null){
					if(bol.equals(Boolean.TRUE)){
						nfe.setDmStProc(1L);
						nfe.setDmOper(1L);
						salvarReenvioNFE(nfe);
					}
				}
			}
		}
		
		
		//repesquisa as notas para popular a lista novamente
		pesquisar();
		setMessage("Reenvio das notas selecionadas realizado com sucesso", MSG_SUCESS);
		
		
	}
	
	private void salvarReenvioNFE(NotaFiscalEletronica nfe){
		
		try{
			genericBo.save(nfe);
		}catch(Exception e){
			setMessage("Erro ao reenviar a nota de número: " + nfe.getNumeroNota(), MSG_ERROR);
		}
		
	}
	
	
	public void pesquisar(){
		
		idsNF = new HashMap<Long, Boolean>();
		
		if(validaCamposPesquisa()){
			pesquisarPorDataEmpresa(Long.valueOf(nmEmpresa),dataInicial,dataFinal);
		}
		
	}
	
	private boolean validaCamposPesquisa(){
		boolean resultado = true;
		
		//valida as datas
		if(dataInicial == null && dataFinal == null){
			resultado = true;
		}else if(dataInicial == null || dataFinal == null){
			setMessage("Para pesquisa por data as duas datas devem ser preenchidas", MSG_ERROR);
			resultado = false;
		}else{
			if(dataFinal.after(dataInicial)){
				resultado = true;
			}else{
				setMessage("A data final não pode ser menor que a data inicial na pesquisa", MSG_ERROR);
				resultado = false;
			}
		}
		
		return resultado;
	}
	
	
	public int getScrollerPage() {
		return scrollerPage;
	}
	
	
	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	
	public void setAll(List<NotaFiscalEletronica> all) {
		this.all = all;
	}
	
	
	public List<NotaFiscalEletronica> getAll() {
		
		//pesquisa no banco apenas se o objeto estiver nulo
		if(all == null){
			pesquisarTodos();
		}
		return all;
	}
	
	
	public String reenviarPorItem(){
		
		
		Long idNFE = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("idNFPorItem"));
		Long idEmpresa = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("idEmpresaPorItem"));
		
		NotaFiscalEletronica nfe = new NotaFiscalEletronica();
		nfe.getNotaFiscalEletronicaPK().setIdNf(idNFE);
		nfe.getNotaFiscalEletronicaPK().setIdEmpresa(idEmpresa);
		nfe.setDmStProc(1L);
		nfe.setDmOper(1L);
		
		try{
			genericBo.save(nfe);
			
			//atualizar a lista da pesquisa
			pesquisarTodos();
			
		}catch(Exception e){
			setMessage("Erro ao reenviar a nota de número: " + nfe.getNumeroNota(), MSG_ERROR);
		}
		
		
		
		
		return "reenvioRealizado";
		
	}
	
	
	private void pesquisarTodos(){
		
		idsNF = new HashMap<Long, Boolean>();
		
		try{
			List<Long> cL = new ArrayList<Long>();
			cL.add(99L);
			cL.add(11L);
			Session s = genericBo.getSession();
			Criteria q = s.createCriteria(NotaFiscalEletronica.class);
			q.add(Restrictions.in("dmStProc", cL));
			all =  q.list();
		}catch(Exception e){
			setMessage("Erro ao Pesquisar", MSG_ERROR);
			e.printStackTrace();
		}
	}
	
	private void pesquisarPorDataEmpresa(Long idEmpresa, Date dataInicial, Date dataFinal){
		try{
			List<Long> cL = new ArrayList<Long>();
			cL.add(99L);
			cL.add(11L);
			Session s = genericBo.getSession();
			Criteria q = s.createCriteria(NotaFiscalEletronica.class);
			//q.add(Restrictions.in("dmStProc", cL));
			
			//adicioca filtro criteria caso a empresa selecionada nao seja todos
			if(idEmpresa != 0){
				q.add(Restrictions.eq("notaFiscalEletronicaPK.idEmpresa", idEmpresa));
			}
			
			if(dataInicial != null && dataFinal != null){
				q.add(Restrictions.between("emissao", dataInicial, dataFinal));
			}
			
			all =  q.list();
			setMessage("Pesquisa realizada com sucesso", MSG_SUCESS);
		}catch(Exception e){
			setMessage("Erro ao Pesquisar", MSG_ERROR);
			e.printStackTrace();
		}
	}
	
	
	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Empresa> getEmpresas() {
		try{
			empresas = (List)genericBo.get(Empresa.class);
		}catch(Exception e){}
		return empresas;
	}
	
	
	public void setNmEmpresa(String nmEmpresa) {
		this.nmEmpresa = nmEmpresa;
	}
	
	
	
	public String getNmEmpresa() {
		return nmEmpresa;
	}
	
	
	
	public void setSelectEmpresa(List<SelectItem> selectEmpresa) {
		this.selectEmpresa = selectEmpresa;
	}
	
	
	
	public List<SelectItem> getSelectEmpresa() {
		
		
		List<Empresa> listaEmpresa = getEmpresas();
		selectEmpresa = new ArrayList<SelectItem>();
		SelectItem todos = new SelectItem();
		todos.setLabel("todos");
		todos.setValue("0");
		selectEmpresa.add(todos);
		
		for(Empresa empresa : listaEmpresa){
			SelectItem item = new SelectItem();
			item.setLabel(empresa.getNome());
			item.setValue(empresa.getIdEmpresa());
			selectEmpresa.add(item);
		}
		
		return selectEmpresa;
	}
	
	
	
	private String getSqlNf(){
		StringBuffer query = new StringBuffer();
		query.append(" SELECT nf.ID_NF, nf.ID_EMPRESA, nf.NR_SERIE, nf.NR_DOCUMENTO_FISCAL, ");
		query.append(" nf.DT_EMISSAO, nf.VL_NF, count(i.id_nf) as maxItem ");
		query.append("FROM NFE_NF nf, nfe_nf_item i ");
		query.append(" WHERE i.id_nf = nf.id_nf ");
		query.append(" AND i.id_empresa = nf.id_empresa ");
		query.append(" AND nf.dm_st_proc in(99,11) ");
		query.append(" GROUP BY nf.ID_NF,nf.ID_EMPRESA,nf.NR_SERIE,nf.NR_DOCUMENTO_FISCAL,nf.DT_EMISSAO,nf.VL_NF");
		query.append(" ORDER BY nf.ID_NF");
		return query.toString();
	}
	
	
	public void setIdsNF(Map<Long, Boolean> idsNF) {
		this.idsNF = idsNF;
	}
	
	
	public Map<Long, Boolean> getIdsNF() {
		return idsNF;
	}
	
	
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	
	public Date getDataFinal() {
		return dataFinal;
	}
	
	
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	
	public Date getDataInicial() {
		return dataInicial;
	}
	
	
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	
	
	public String getNmUsuario() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		nmUsuario = (String)session.getAttribute("userName");
		
		return nmUsuario;
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
	
	private void setMessage(String message, String type){
		errorMessage = message;
		styleMessage = type;
	}
	
}
