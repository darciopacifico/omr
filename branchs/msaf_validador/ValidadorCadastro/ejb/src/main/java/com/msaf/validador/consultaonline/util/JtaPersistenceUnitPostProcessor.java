package com.msaf.validador.consultaonline.util;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
 
/**
 * the {@link JtaPersistenceUnitPostProcessor} enables us to define the JTA capable datasource
 * which should be used by the JPA provider directly as a reference within the Spring configuration
 * file.
 */
public class JtaPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {
  /**
   * a reference to the JTA capable datasource which is add to the PersistenceUnitInfo during post
   * processing instead of beeing specified using the "jta-data-source" setting in the persistence.xml
   * configuration file
   */
  private DataSource jtaDataSource;
 
  /**
   * enrich the PersistenceUnitInfo read from the persistence.xml configuration file with a reference
   * to the jtaDataSource injected via the Spring configuration. the JTA capable datasource is then
   * used by the LocalContainerEntityManagerFactoryBean to create the EntityManagerFactory
   *
   * @see PersistenceUnitPostProcessor#postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo)
   */
  public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo mutablePersistenceUnitInfo) {
    mutablePersistenceUnitInfo.setJtaDataSource(getJtaDataSource());
  }
 
  /**
   * getter for jtaDataSource
   * 
   * @return the JTA capable datasource supplied via the setter
   */
  public DataSource getJtaDataSource() {
    return jtaDataSource;
  }
 
  /**
   * setter for jtaDataSource 
   *
   * @param jtaDataSource the JTA capable datasource added to the PersistenceUnitInfo during post processing
   */
  @Required
  public void setJtaDataSource(DataSource jtaDataSource) {
    this.jtaDataSource = jtaDataSource;
  }
}