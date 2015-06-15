/*
 * Data de Criacao 27/05/2005 22:40:37
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagvalue;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XDoc;
import xjavadoc.XProgramElement;
import xjavadoc.XTag;

/**
 * Encapsula os valores e as regras para as tags do wizard
 * 
 * @author Darcio L Pacifico - 27/05/2005 22:40:37
 */
public abstract class AbstractAtributosVO {
	private static final String W2JEE_NAMESPACE = TagHandlerUtil.W2JEE_NAMESPACE;

	protected Log logger = LogFactory.getLog(AbstractAtributosVO.class);

	private XDoc doc;

	private XTag tag;

	/**
	 * Constroi o AbstractTagValues disponibilizando doc e tag
	 */
	protected AbstractAtributosVO(XDoc doc) throws Wj2eeException {

		if (doc == null) {
			throw new Wj2eeException(
					"Não é possível construir uma implementação de AbstractTagValues a partir de um (XClass ou XMethod)XProgramElement==null!");
		}
		this.doc = doc;
		this.tag = doc.getTag(W2JEE_NAMESPACE);

		if (this.tag == null) {
			// logger.warn("Não é foi possível encontrar a tag
			// "+W2JEE_NAMESPACE+" neste XProgramElement!("+doc.getOwner()+")");
			// throw new Wj2eeException("Não é foi possível encontrar a tag
			// "+W2JEE_NAMESPACE+" neste XProgramElement!("+doc.getOwner()+")");
		}

	}

	/**
	 * Trata o XProgramElement extraindo o XDoc para o super construtor
	 */
	protected static synchronized XDoc getXDoc(XProgramElement programElement)
			throws Wj2eeException {

		if (programElement == null) {
			throw new Wj2eeException(
					"Não é possível construir uma implementação de AbstractTagValues a partir de um (XClass ou XMethod)XProgramElement==null!");
		}
		return programElement.getDoc();
	}

	/**
	 * Deve ser implementado para retornar o valor default do atributo caso este
	 * não seja encontrado
	 */
	protected abstract String getDefaultValue(String chave)
			throws AtributosException;

	/**
	 * Recupera um atributo dentro de uma tag XDoclet
	 */
	protected String getAttribute(String chave) throws AtributosException {
		if (tag == null) {
			return "";
		}

		String attributeValue = tag.getAttributeValue(chave);

		if (attributeValue == null || attributeValue.trim().equals("")) {
			attributeValue = getDefaultValue(chave);
		}

		return attributeValue;
	}

	/**
	 * Inicializa atributos da classe ou metodo
	 */
	protected void initializeAttributes(String[] attributes)
			throws AtributosException {
		for (int i = 0; i < attributes.length; i++) {

			String campo = attributes[i];
			String valor = getAttribute(campo);

			try {
				if (valor == null || valor.trim().equals("")) {
					valor = getDefaultValue(campo);
				}

				BeanUtils.setProperty(this, campo, valor);

			} catch (IllegalAccessException e) {
				throw new AtributosException(e,
						"Não foi possivel encontrar a propriedade '" + campo
								+ "' no objeto '" + this
								+ "' ou não foi possivel atribuir o valor '"
								+ valor + "' a ela!");

			} catch (InvocationTargetException e) {
				throw new AtributosException(e,
						"Não foi possivel encontrar a propriedade '" + campo
								+ "' no objeto '" + this
								+ "' ou não foi possivel atribuir o valor '"
								+ valor + "' a ela!");
			}
		}
	}

	/**
	 * Metodo Getter do atributo doc.
	 */
	public XDoc getDoc() {
		return doc;
	}

	/**
	 * Metodo Setter do atributo doc.
	 */
	public void setDoc(XDoc doc) {
		this.doc = doc;
	}
}
