/**
 * 
 */
package br.com.tsuite.tiporelacionamento;

import java.util.ArrayList;
import java.util.List;

/**
 * @author darcio
 * 
 */
public class ModuloBean {

	public ModuloVO moduloVO;

	public List<ModuloVO> resultadoConsulta;

	public ModuloBean() {}

	
	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionNovo() {
		System.out.println("teste123");
		
		return CRUDActionEnum.NOVO;
	}

	
	/**
	 * @return
	 */
	public CRUDActionEnum actionSalvar() {
		return CRUDActionEnum.SALVAR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionAlterar() {
		return CRUDActionEnum.ALTERAR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionExcluir() {
		return CRUDActionEnum.EXCLUIR;
	}

	/**
	 * 
	 * @return
	 */
	public CRUDActionEnum actionPesquisar() {
		List<ModuloVO> modulos = new ArrayList<ModuloVO>();
		
		ModuloVO moduloVO1 = new ModuloVO();
		ModuloVO moduloVO2 = new ModuloVO();
		ModuloVO moduloVO3 = new ModuloVO();
		ModuloVO moduloVO4 = new ModuloVO();
		
		moduloVO1.setId(1l);
		moduloVO2.setId(2l);
		moduloVO3.setId(3l);
		moduloVO4.setId(4l);
		
		
		moduloVO1.setDescricao("mod 1");
		moduloVO2.setDescricao("mod 2");
		moduloVO3.setDescricao("mod 3");
		moduloVO4.setDescricao("mod 4");
		
		ProjetoVO projetoVO1 = new ProjetoVO();
		projetoVO1.setId(1l);
		projetoVO1.setDescricao("Projeto 1");
		
		ProjetoVO projetoVO2 = new ProjetoVO();
		projetoVO2.setId(2l);
		projetoVO2.setDescricao("Projeto 2");
		
		
		moduloVO1.setProjetoVO(projetoVO1);
		moduloVO2.setProjetoVO(projetoVO1);
		moduloVO3.setProjetoVO(projetoVO2);
		moduloVO4.setProjetoVO(projetoVO2);
		
		modulos.add(moduloVO1);
		modulos.add(moduloVO2);
		modulos.add(moduloVO3); 
		modulos.add(moduloVO4); 
		
		this.resultadoConsulta = modulos;
		
		return CRUDActionEnum.PESQUISAR;
	}


	public ModuloVO getModuloVO() {
		return moduloVO;
	}


	public void setModuloVO(ModuloVO moduloVO) {
		this.moduloVO = moduloVO;
	}


	public List<ModuloVO> getResultadoConsulta() {
		return resultadoConsulta;
	}


	public void setResultadoConsulta(List<ModuloVO> resultadoConsulta) {
		this.resultadoConsulta = resultadoConsulta;
	}

}
