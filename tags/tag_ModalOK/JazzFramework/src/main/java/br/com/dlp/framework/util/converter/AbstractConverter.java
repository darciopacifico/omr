package br.com.dlp.framework.util.converter;

import java.text.DecimalFormatSymbols;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementacao abstrata de Converter para forAar internacionalizacao do contrato
 */
public abstract class AbstractConverter implements Converter {
	protected Log logger = LogFactory.getLog(AbstractConverter.class);

	public static final String PROPERTY_FORMATO_DATA_VIEW = "formatoDataView";

	public static final String PROPERTY_DECIMAL_SEPARATOR = "decimalSeparator";

	public static final String PROPERTY_GROUPING_SEPARATOR = "groupingSeparator";

	public static final String PROPERTY_FORMATO_SHORT = "formatoShort";

	public static final String PROPERTY_FORMATO_INTEGER = "formatoInteger";

	public static final String PROPERTY_FORMATO_LONG = "formatoLong";

	public static final String PROPERTY_FORMATO_FLOAT = "formatoFloat";

	public static final String PROPERTY_FORMATO_DOUBLE = "formatoDouble";

	private static final String DEFAULT_FORMATO_DATA = "dd/MM/yyyy";

	private static final String DEFAULT_DECIMAL_SEPARATOR = ",";

	private static final String DEFAULT_GROUPING_SEPARATOR = ".";

	private static final String DEFAULT_FORMATO_SHORT = "#";

	private static final String DEFAULT_FORMATO_INTEGER = "#";

	private static final String DEFAULT_FORMATO_LONG = "#";

	private static final String DEFAULT_FORMATO_FLOAT = "###,###.00";

	private static final String DEFAULT_FORMATO_DOUBLE = "###,###.00";

	private ResourceBundle resourceBundle;

	/**
	 * Construtor que recebe resourceBundle para internacionalizacao dos formatos
	 */
	public AbstractConverter(ResourceBundle resourceBundle) {
		setResourceBundle(resourceBundle);
	}

	/**
	 * @deprecated Construtor para AbstractConverter contendo os formatadores de conversao padrAo<br>
	 *             e altamente recomendAvel que se construa este objeto informando o resourceBundle de internacionalizacao
	 */
	public AbstractConverter() {
		logger.warn("ATENCAO!! CONSTRUINDO UM RESOURCE BUNDLE COM A INTERNACIONALIZAcao DE " + "FORMATOS PADRAO!!!. ISTO PODE COMPROMETER A PORTABILIDADE DA APLICACAO");

		ResourceBundle resourceBundle = new ListResourceBundle() {
			protected Object[][] getContents() {
				String valores[][] = { { PROPERTY_FORMATO_DATA_VIEW, DEFAULT_FORMATO_DATA }, { PROPERTY_DECIMAL_SEPARATOR, DEFAULT_DECIMAL_SEPARATOR }, { PROPERTY_GROUPING_SEPARATOR, DEFAULT_GROUPING_SEPARATOR }, { PROPERTY_FORMATO_SHORT, DEFAULT_FORMATO_SHORT }, { PROPERTY_FORMATO_INTEGER, DEFAULT_FORMATO_INTEGER }, { PROPERTY_FORMATO_LONG, DEFAULT_FORMATO_LONG },
						{ PROPERTY_FORMATO_FLOAT, DEFAULT_FORMATO_FLOAT }, { PROPERTY_FORMATO_DOUBLE, DEFAULT_FORMATO_DOUBLE } };
				return valores;
			}
		};

		setResourceBundle(resourceBundle);
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	/**
	 * Recupera o padrao de casas decimais e separadores de milhar do sistema, independentemente das configuracoes regionais do SO
	 * 
	 * @return Objeto contendo as informacoes sobre formatacao de nAmeros utilizado para converter numeros para Strings e vice e versa
	 */
	public DecimalFormatSymbols getDecimalFormatSymbols() {

		ResourceBundle resourceBundle = getResourceBundle();

		String strDecimalSeparator = resourceBundle.getString(PROPERTY_DECIMAL_SEPARATOR);
		String strGroupingSeparator = resourceBundle.getString(PROPERTY_GROUPING_SEPARATOR);

		char decimalSeparator = strDecimalSeparator.toCharArray()[0];
		char groupingSeparator = strGroupingSeparator.toCharArray()[0];

		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormatSymbols.setGroupingSeparator(groupingSeparator);

		return decimalFormatSymbols;
	}

}
