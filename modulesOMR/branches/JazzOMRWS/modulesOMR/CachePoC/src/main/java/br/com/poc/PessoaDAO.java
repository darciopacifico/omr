package br.com.poc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class PessoaDAO {

	
	
	Map<Long, PessoaVO> mapPessoas = new HashMap<Long, PessoaVO>();
	
	public PessoaDAO(){
		mapPessoas.put(1l, new PessoaVO(1l,"pessoa1"));
		mapPessoas.put(2l, new PessoaVO(2l,"pessoa2"));
		mapPessoas.put(3l, new PessoaVO(3l,"pessoa3"));
		mapPessoas.put(4l, new PessoaVO(4l,"pessoa4"));
		mapPessoas.put(5l, new PessoaVO(5l,"pessoa5"));
		mapPessoas.put(6l, new PessoaVO(6l,"pessoa6"));
		mapPessoas.put(7l, new PessoaVO(7l,"pessoa7"));
		mapPessoas.put(8l, new PessoaVO(8l,"pessoa8"));
		mapPessoas.put(9l, new PessoaVO(9l,"pessoa9"));
	}

	public List<PessoaVO> findAll(){
		List<PessoaVO> pessoas = new ArrayList<PessoaVO>();
		pessoas.addAll(mapPessoas.values());
		return pessoas;
	}
	
	@Cacheable(value="pessoas")
	public PessoaVO findByPk(Long pk){
		return mapPessoas.get(pk);
	}
	
	
	@Cacheable(value="pessoas")
	public void updatePessoa(PessoaVO pessoaVO){
		
		
	}
	
	
}
