package br.com.dlp.jazzomr.person;

import javax.faces.convert.EnumConverter;

import br.com.dlp.jazzav.person.EAuthority;

/**
 * Conversor de enums para uso em componentes jsf
 * @author darcio
 *
 */
public class AuthoritiesEnumConverter extends EnumConverter{

	public AuthoritiesEnumConverter() {
		super(EAuthority.class);
	}
	
}
