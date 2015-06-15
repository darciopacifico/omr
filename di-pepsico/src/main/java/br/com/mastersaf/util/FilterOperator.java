package br.com.mastersaf.util;


/**
 * Enumeration with the operations to be perform in filters criterias 
 */
public class FilterOperator extends Enum {
	
  private FilterOperator(String operation){
	this.state = operation;
  }
  
  /**
   * The same of a LIKE database operation 
   */
  public static final FilterOperator CONTAINS = new FilterOperator("CONTAINS");

  /**
   * A String that finish with a value 
   */
  public static final FilterOperator END_WITH = new FilterOperator("END_WITH");

  /**
   * A String that start with a value 
   */
  public static final FilterOperator START_WITH = new FilterOperator("START_WITH");

  /**
   * A equal comparation 
   */
  public static final FilterOperator EQUAL = new FilterOperator("EQUAL");

  /**
   * A greater comparation 
   */
  public static final FilterOperator GREATER_THAN = new FilterOperator("GREATER_THAN");

  /**
   * A greater or equal comparation 
   */
  public static final FilterOperator GREATER_THAN_OR_EQUAL = new FilterOperator("GREATER_THAN_OR_EQUALS");

  /**
   * A less comparation 
   */
  public static final FilterOperator LESS_THAN = new FilterOperator("LESS_THAN");

  
  /**
   * A less or equal comparation 
   */
  public static final FilterOperator LESS_THAN_OR_EQUAL = new FilterOperator("LESS_THAN_OR_EQUAL");

  /**
   * A not equal comparation 
   */
  public static final FilterOperator NOT_EQUAL = new FilterOperator("NOT_EQUAL");

  /**
   * A is null comparation
   */
  public static final FilterOperator IS_NULL = new FilterOperator("IS_NULL");
  }