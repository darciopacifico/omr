package br.com.dlp.jazzqa.util;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * Configure o arquivo /src/hibernateLocal.cfg.xml e execute esta aplicação como
 * uma simples java application
 */
public class MySchemaUpdate {
	public static void main(String[] args) {
		try {

			Configuration configuration = new Configuration();

			configuration
					.configure("/br/com/dlp/jazzqa/hibernateLocal.cfg.xml");

			SchemaUpdate schemaUpdate = new SchemaUpdate(configuration);
			schemaUpdate.execute(true, true);

		} catch (HibernateException e) {
			e.printStackTrace();
		}

	}
}