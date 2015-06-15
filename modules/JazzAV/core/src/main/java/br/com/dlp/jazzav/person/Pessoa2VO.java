package br.com.dlp.jazzav.person;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.jazzav.AbstractLogEntityVO;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.PropertyStereotype;


public class Pessoa2VO extends AbstractLogEntityVO<Long>  {
	
	
	private String email;
	/**
	 * 
	 * @return
	 */
	//@NotBlank(message="Email não pode ser branco",groups=VGroupUsuario.class)
	//@Email(message="E-mail inválido",groups=VGroupUsuario.class)
	@JazzProp(name="E-mail", stereotype=PropertyStereotype.EMAIL)
	@Email(message="Email inválido!")
	@NotBlank(message="Email não pode ficar em branco")
	public String getEmail() { 
		return email;
	}
	
	
	
	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}



	@Override
	public Long getPK() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	
}