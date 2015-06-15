/**
 * 
 */
package br.com.dlp.framework.hibernate.namingstrategy;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Strategia de nomeacao de tabelas JazzFramework
 * @author darcio
 */
public class JazzNamingStrategy extends ImprovedNamingStrategy {
	
	private static final String FK_PREFIX = "fk_";

	private static final long serialVersionUID = 2548644290764341981L;

	private String domainClassRegexPattern="(.+)(vo)$";
	private String groupTableName="$1";
	private String tablePrefix = "tb_";
	private String tableJoinPrefix = "tj_";
		
	@Override
	public String classToTableName(String className) {
		String tableName = super.classToTableName(className);
		
		//tira do sufixo "VO" do nome da classe 
		tableName = tableName.replaceAll(domainClassRegexPattern, groupTableName);
		
		//coloca do prefixo "tb_"
		tableName = tablePrefix + tableName;
		return tableName;
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		
		propertyTableName = StringUtils.isBlank(propertyTableName)?"":propertyTableName;
		propertyTableName = propertyTableName.toLowerCase();
		propertyTableName = propertyTableName.replaceAll(domainClassRegexPattern, groupTableName);
		
		propertyName = StringUtils.isBlank(propertyName)?"":propertyName;
		propertyName = propertyName.toLowerCase();
		propertyName = propertyName.replaceAll(domainClassRegexPattern, groupTableName);
		
		String fkName;
		if(propertyName.equals(propertyTableName)||StringUtils.isBlank(propertyName)){
			fkName = JazzNamingStrategy.FK_PREFIX+propertyTableName;
		}else{
			fkName = JazzNamingStrategy.FK_PREFIX+propertyName+"_"+propertyTableName;
		}
		
 		return fkName;
	}


	@Override
	public String tableName(String tableName) {
		tableName = this.getTableJoinPrefix() + super.tableName(tableName);
		return tableName;
	}
	
	
	
	
	/**
	 * @return the domainClassRegexPattern
	 */
	public String getDomainClassRegexPattern() {
		return domainClassRegexPattern;
	}

	/**
	 * @return the groupTableName
	 */
	public String getGroupTableName() {
		return groupTableName;
	}

	/**
	 * @return the tablePrefix
	 */
	public String getTablePrefix() {
		return tablePrefix;
	}

	/**
	 * @param domainClassRegexPattern the domainClassRegexPattern to set
	 */
	public void setDomainClassRegexPattern(String domainClassRegexPattern) {
		this.domainClassRegexPattern = domainClassRegexPattern;
	}

	/**
	 * @param groupTableName the groupTableName to set
	 */
	public void setGroupTableName(String groupTableName) {
		this.groupTableName = groupTableName;
	}

	/**
	 * @param tablePrefix the tablePrefix to set
	 */
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	/**
	 * @return the tableJoinPrefix
	 */
	public String getTableJoinPrefix() {
		return tableJoinPrefix;
	}

	/**
	 * @param tableJoinPrefix the tableJoinPrefix to set
	 */
	public void setTableJoinPrefix(String tableJoinPrefix) {
		this.tableJoinPrefix = tableJoinPrefix;
	}


	
	
}
