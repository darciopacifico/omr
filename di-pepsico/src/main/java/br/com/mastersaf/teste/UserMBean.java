package br.com.mastersaf.teste;

import br.com.mastersaf.model.Usuario;
import br.com.mastersaf.util.GenericBo;

public class UserMBean {
	
	private GenericBo genericBo;
	
	
	
	public void setGenericBo(GenericBo genericBo) {
		this.genericBo = genericBo;
	}



	public String getNome() {
		try {
			Usuario usuario = new Usuario();
			usuario.setNomeUsuario("adm");
			System.out.println("Like = "+this.genericBo.get(usuario));
			
			System.out.println("Tudo="+genericBo.get(Usuario.class));
			
			System.out.println("Count="+genericBo.getCount(Usuario.class));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	       return "Leandro";  
	 }  
}
