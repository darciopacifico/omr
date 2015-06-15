/*
 * Data de Criacao 26/05/2005 11:17:34
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.dlp.framework.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.pk.PKException;

/**
 * @hibernate.subclass
 * @wj2ee
 */
public abstract class AbstractCadastroBaseVO implements ICadastroBaseVO {
	/**
	 * Utilizar este método apenas em casos de cálculo de Status. Para conversão
	 * de String->Data e Data->String utilizar os converters do framework e o
	 * modelo de Wrapper
	 */
	protected static String dateToString(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);

	}

	private IPK pk;

	/**
	 * 
	 */

	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractCadastroBaseVO() {
	}

	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractCadastroBaseVO(IPK ipk) {
		try {
			setIPK(ipk);
		} catch (PKException e) {
			// não tratado
		}
	}

	/** Override do metodo equals da superclasse */
	public boolean equals(Object arg) {

		if (arg == null || !(arg instanceof ICadastroBaseVO)) {
			return false;
		}

		String myClassName = this.getClass().getName();
		String argClassName = arg.getClass().getName();

		if (!myClassName.equals(argClassName)) {
			return false;
		}

		/* cast do argumento para ICadastroBaseVO */
		ICadastroBaseVO cadastroBaseVO = (ICadastroBaseVO) arg;

		/* peço p o argumento comparar se a chave dele é igual a minha */
		return getIPK().compare(cadastroBaseVO.getIPK());

	}

	/**
	 * @hibernate.id generator-class="native"
	 */
	public IPK getIPK() {
		return pk;
	}

	protected final void setIPK(IPK pk) throws PKException {
		if (pk == null) {
			throw new PKException(
					"Não é permitido atribuir uma implementacao de PK nula");
		}
		this.pk = pk;
	}

}