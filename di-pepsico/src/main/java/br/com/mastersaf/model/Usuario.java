package br.com.mastersaf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.mastersaf.util.Bean;
import br.com.mastersaf.util.Util;

/**
 * Entity class Usuario
 *
 * @author Fabio
 */
@Entity
@Table(name = "NFE_USUARIO")
public class Usuario implements Bean {
    
   	private static final long serialVersionUID = -4265941383930974250L;
	
    @Id
    @Column(name = "ID_USUARIO", nullable = false)
    private Long id;
    
    @Column(name = "NM_USUARIO", nullable = false)
    private String nomeUsuario;
    
    @Column(name = "DM_ATIVO", nullable = false)
    private short ativo;
    
    @Column(name = "DS_EMAIL")
    private String email;
    
    @Column(name = "ID_LOGIN", nullable = false)
    private String login;
    
    @Column(name = "DS_SENHA", nullable = false)
    private String senha;
    

    /** Creates a new instance of Usuario */
    public Usuario() {
    }
    
    /**
     * Creates a new instance of Usuario with the specified values.
     * @param id the id of the Usuario
     */
    public Usuario(Long id) {
        this.id = id;
    }
    
    /**
     * Creates a new instance of Usuario with the specified values.
     * @param id the id of the Usuario
     * @param nmUsuario the nmUsuario of the Usuario
     * @param dmAtivo the dmAtivo of the Usuario
     * @param dsSenha the dsSenha of the Usuario
     */
    public Usuario(Long id, String nomeUsuario, short ativo, String senha, String login) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.ativo = ativo;
        this.senha = Util.encrypt(senha);
        this.login = login;
    }
    
    /**
     * Gets the nmUsuario of this Usuario.
     * @return the nmUsuario
     */
    public String getNomeUsuario() {
        return this.nomeUsuario;
    }
    
    /**
     * Sets the nmUsuario of this Usuario to the specified value.
     * @param nmUsuario the new nmUsuario
     */
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    /**
     * Gets the dmAtivo of this Usuario.
     * @return the dmAtivo
     */
    public short getAtivo() {
        return this.ativo;
    }
    
    /**
     * Sets the dmAtivo of this Usuario to the specified value.
     * @param dmAtivo the new dmAtivo
     */
    public void setAtivo(short ativo) {
        this.ativo = ativo;
    }
    
    /**
     * Gets the dsEmail of this Usuario.
     * @return the dsEmail
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * Sets the dsEmail of this Usuario to the specified value.
     * @param dsEmail the new dsEmail
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the id of this Usuario.
     * @return the id
     */
    public Long getId() {
        return this.id;
    }
    
    /**
     * Sets the id of this Usuario to the specified value.
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the dsSenha of this Usuario.
     * @return the dsSenha
     */
    public String getSenha() {
        return this.senha;
    }
    
    /**
     * Sets the dsSenha of this Usuario to the specified value.
     * @param dsSenha the new dsSenha
     */
    public void setSenha(String senha) {
        this.senha = Util.encrypt(senha);
    }
    

    
    
    /**
     * Returns a hash code value for the object.  This implementation computes
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    /**
     * Determines whether another object is equal to this Usuario.  The result is
     * <code>true</code> if and only if the argument is not null and is a Usuario object that
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario)object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) return false;
        return true;
    }
    
    /**
     * Returns a string representation of the object.  This implementation constructs
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "entidade.Usuario[id=" + id + "]";
    }
        
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
}
