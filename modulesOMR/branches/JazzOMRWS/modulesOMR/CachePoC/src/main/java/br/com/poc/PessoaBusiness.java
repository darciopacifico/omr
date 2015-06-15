package br.com.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PessoaBusiness {

	@Autowired
	private PessoaDAO pessoaDAO;

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}
	
	
	public PessoaVO findByPk(Long pk){
		return getPessoaDAO().findByPk(pk);
	}
	
	
}
