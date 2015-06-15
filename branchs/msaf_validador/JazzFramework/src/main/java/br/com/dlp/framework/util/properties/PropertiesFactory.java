package br.com.dlp.framework.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

	private static PropertiesFactory instance;

	private PropertiesFactory() {
	}

	public static PropertiesFactory getInstance() {
		if (instance == null) {
			instance = new PropertiesFactory();
		}
		return instance;
	}

	/**
	 * CARREGA UM OBJETO DE PROPRIEDADES J� PRONTO COM UM ARQUIVO .properties
	 */
	public Properties getProperties(Properties properties, String fileProperties)
			throws PropertiesFactoryException {
		InputStream inputStream = this.getClass().getResourceAsStream(
				fileProperties);

		if (inputStream == null) {
			throw new PropertiesFactoryException(
					"N�o foi poss�vel encontrar o arquivo de propriedades '"
							+ fileProperties
							+ "'!. Verifique se o mesmo encontra-se no classpath!");
		}

		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new PropertiesFactoryException(
					"N�O FOI POSS�VEL CARREGAR O ARQUIVO DE PROPRIEDADES "
							+ properties, e);
		}
		return properties;
	}

	/**
	 * CRIA E CARREGA UM OBJETO DE PROPRIEDADES COM UM ARQUIVO .properties
	 */
	public Properties getProperties(String fileProperties)
			throws PropertiesFactoryException {
		Properties properties = new Properties();
		return getProperties(properties, fileProperties);
	}

	/**
	 * CRIA E CARREGA UM OBJETO DE PROPRIEDADES PARA UMA CLASSE. LEMBRANDO QUE
	 * DE ACORDO COM AS RECOMENDA��ES DE ARQUITETURA DO ACH�, SE UMA CLASSE
	 * POSSUI UM ARQUIVO PROPERTIE, ESTE ARQUIVO DEVE ESTAR CONTIDO NO MESMO
	 * PACKAGE DA CLASSE E POSSUIR O MESMO NOME DA CLASSE EX:
	 * 
	 * Classe:
	 * /br/com/dlp/framework/servicelocator/J2EEServiceLocatorImpl.properties<br>
	 * Propertie:
	 * br.com.dlp.framework.servicelocator.J2EEServiceLocatorImpl.class
	 */
	public Properties getProperties(Class class1)
			throws PropertiesFactoryException {
		String propertieName = class1.getName();
		propertieName = "/" + propertieName.replace('.', '/') + ".properties";
		Properties properties = this.getProperties(propertieName);
		return properties;
	}

}
