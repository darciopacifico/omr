package br.com.mastersaf.util;


/**
 * Enumeration of type of ascending for sort operations   
 */

public class OrderOperator extends Enum {

   private OrderOperator(String operation){
		this.state = operation;
   }	

   /**
    * Ascending type
    */
  public static final OrderOperator ASC = new OrderOperator("ASC");;

  /**
   * Desacending type
   */
  public static final OrderOperator DESC = new OrderOperator("DESC");

  }