package br.com.mastersaf.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;

/**
 * Class Util
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public final class UtilSS {
	
	private static Logger log = Logger.getLogger(UtilSS.class);
	
	@SuppressWarnings("unchecked")
	public static void copyProperties(Object object, Map mapAttributesAndValues) throws Exception {
		try {
			/*if(object != null && mapAttributesAndValues != null && mapAttributesAndValues.size() > 0){
				Method[] methods = object.getClass().getMethods();
				for (int i = 0; i < methods.length; i++) {
					copyProperties(object, mapAttributesAndValues);
				}
				String setMethod = "set";
				Method method = null;
				Object value = null;
				for(Iterator i = mapAttributesAndValues.keySet().iterator(); i.hasNext();){
					String element = (String) i.next();
					value = mapAttributesAndValues.get(element);
					setMethod += element.substring(0, 1).toUpperCase() + element.substring(1);
				}
				method = null;
				value = null;
			}*/
		} catch(Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings({"unchecked", "unused"})
	private static void copyProperties(Object object, Method method, String value) throws Exception {
		if(object != null && method != null && value != null && !value.equals("")){
			Class[] clazzs = method.getParameterTypes();
			if(clazzs.length == 0){
				Class clazz = clazzs[0];
				if(clazz == Integer.class){
					method.invoke(object, new Object[]{new Integer(value)});
				}
			}
		}
	}
	
	/**
	 * Get a Map from the Bean attributes, where the keys are the names of the attributes.
	 */
	@SuppressWarnings("unchecked")
	public static Map getMapAttribute(Object object) {
		Map map = null;
		if(UtilSS.verifyObject(object)){
			try {
				Method[] methods = object.getClass().getMethods();
				map = new HashMap(methods.length);
				String methodName = null;
				for(int i = 0; i < methods.length; i++){
					Method method = methods[i];
					methodName = method.getName();
					if(!methodName.equals("getClass") && methodName.startsWith("get")){
						Object objectReturn = method.invoke(object, new Object[]{});
						if(objectReturn instanceof Bean){
							Map mapChildreen = getMapAttribute(objectReturn);
							if(mapChildreen != null && mapChildreen.size() > 0){
								StringBuffer buffer = null;
								String key = null;
								Object value = null;
								for(Iterator iter = mapChildreen.keySet().iterator(); iter.hasNext();){
									buffer = new StringBuffer(methodName.substring(3, 4).toLowerCase()+methodName.substring(4));
									key = (String) iter.next();
									buffer.append(".");
									buffer.append(key);
									value = mapChildreen.get(key);
									if(value != null && !value.toString().equals("") && !(value instanceof Collection)){
										map.put(buffer.toString(), value);
									}
								}
							}
						} else if(objectReturn != null && !objectReturn.toString().equals("") && !(objectReturn instanceof Collection)){
							map.put(methodName.substring(3, 4).toLowerCase()+methodName.substring(4), objectReturn);
						}
					}
				}
			} catch(Exception e) {
				log.error("Error in the method getMapAttribute(Object object)", e);
			}
		}
		return map;
	}
	
	public static void setObject(Object object) {
		try {
			if(object == null){
				throw new Exception("Object is null");
			}
			Method[] methods = object.getClass().getMethods();
			String methodName = null;
			for(int i = 0; i < methods.length; i++){
				Method method = methods[i];
				methodName = method.getName();
				if(!methodName.equals("getClass") && methodName.startsWith("get")){
					Object objectReturn = method.invoke(object, new Object[]{});
					if(objectReturn == null){
						setMethod(object, methodName.substring(3), null);
					}
				}
			}
			
		} catch(Exception e) {
			log.error("Error in the method getMethod(Object object, String atributte)", e);
		}
	}
	
	/**
	 * Get a value of a attribute of the bean.
	 */
	public static Object getMethod(Object object, String atributte) {
		Object objectReturn = null;
		try {
			if(object != null && atributte != null){
				StringBuffer sbMethod = new StringBuffer("get");
				sbMethod.append(atributte.substring(0, 1).toUpperCase());
				sbMethod.append(atributte.substring(1));
				
				Method methodObject = object.getClass().getMethod(sbMethod.toString(), new Class[]{});
				objectReturn = methodObject.invoke(object, new Object[]{});
			}
		} catch(Exception e) {
			log.error("Error in the method getMethod(Object object, String atributte)", e);
		}
		return objectReturn;
	}
	
	/**
	 * Check if anywhere attributes of a object not is empty.
	 */
	public static boolean verifyObject(Object object) {
		boolean objectPopulate = false;
		try {
			if(object != null){
				Method[] methods = object.getClass().getMethods();
				Method method = null;
				Object objectReturn = null;
				for(int i = 0; i < methods.length; i++){
					method = methods[i];
					if(!method.getName().equals("getClass") && method.getName().startsWith("get")){
						objectReturn = method.invoke(object, new Object[]{});
						if(objectReturn != null && !objectReturn.equals("")){
							if(objectReturn instanceof Collection){
								continue;
							} else {
								objectPopulate = true;
								break;
							}
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("Error in the method verifyObject(Object object)", e);
		}
		return objectPopulate;
	}
	
	/**
	 * Clear all values of attributes of Object.
	 * @param object
	 */
	public static void clearObject(Bean object) {
		try {
			if(object != null){
				Method[] methods = object.getClass().getMethods();
				Object objectReturn = null;
				Method methodSet = null;
				for (Method method : methods) {
					if(!method.getName().equals("getClass") && method.getName().startsWith("get") || method.getName().startsWith("is")){
						objectReturn = method.invoke(object, new Object[]{});
						if(objectReturn != null){
							methodSet = object.getClass().getMethod("s"+method.getName().substring(1), getClass(objectReturn));
							methodSet.invoke(object, new Object[]{null});
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("Error in the method clearObject(Object object)", e);
		}
	}
	
	/**
	 * Clear all the values of atributes in the objectNew where is not present in objectCurrent. 
	 */
	@SuppressWarnings("unchecked")
	public static Object clearObject(Object objectCurrent, Object objectNew) { 
		Object object = null;
		try {
			if(objectCurrent != null && objectNew != null && objectCurrent.getClass().isInstance(objectNew)){
				object = objectNew.getClass().newInstance();
				Method[] methodCurrents = objectCurrent.getClass().getMethods();
				Method[] methodNews = objectNew.getClass().getMethods();
				Method method = null;
				Method methodNew = null;
				Object objectReturn = null;
				Object objectReturnNew = null;
				String getMethod = null;
				String setMethod = null;
				for(int i = 0; i < methodCurrents.length; i++){
					method = methodCurrents[i];
					getMethod = method.getName();
					if(!getMethod.equals("getClass") && getMethod.startsWith("get")){
						objectReturn = method.invoke(objectCurrent, new Object[]{});
						if(objectReturn != null){
							for(int j = 0; j < methodNews.length; j++){
								methodNew = methodNews[j];
								if(methodNew.getName().equals(getMethod)){
									objectReturnNew = methodNew.invoke(objectNew, new Object[]{});
									if(objectReturnNew != null){
										setMethod = "s"+getMethod.substring(1);
										Method methodUpdate = object.getClass().getMethod(setMethod, getClass(objectReturnNew));
										methodUpdate.invoke(object, getObject(objectReturnNew));
									}
								}
							}
						}
					}
				}
			}
		} catch(Exception e) {
			log.error("Error in the method clearObject(Object objectCurrent, Object objectNew)", e);
		}
		return object;
	}
	
	/**
	 * Set a value in a atribute of the bean.
	 */
	public static void setMethod(Object object, String method, Object value) {
		try { 
			if(object != null && method != null){
				StringBuffer getMethod = new StringBuffer("get");
				getMethod.append(method.substring(0, 1).toUpperCase());
				getMethod.append(method.substring(1));
				Method methodGet = object.getClass().getMethod(getMethod.toString(), new Class[]{});
				if(methodGet.getReturnType().isInstance(new Date())){
					value = stringToDate(String.valueOf(value));
				} else if(methodGet.getReturnType().isInstance(new String())){
					value = (value != null && !value.equals("") ? String.valueOf(value) : "");
				} else if(methodGet.getReturnType().isInstance(new Long(0))){
					value = new Long((value != null && !value.equals("") ? String.valueOf(value) : "0"));
				} else if(methodGet.getReturnType().isInstance(new Integer(0))){
					value = new Integer((value != null && !value.equals("") ? String.valueOf(value) : "0"));
				} else if(methodGet.getReturnType().isInstance(new BigDecimal(0))){
					value = new BigDecimal((value != null && !value.equals("") ? String.valueOf(value) : "0"));
				}  else if(methodGet.getReturnType().isInstance(new BigInteger("0"))){
					value = new BigInteger((value != null && !value.equals("") ? String.valueOf(value) : "0"));
				}
				
				StringBuffer sbMethod = new StringBuffer("set");
				sbMethod.append(method.substring(0, 1).toUpperCase());
				sbMethod.append(method.substring(1));
				
				Method methodObject = object.getClass().getMethod(sbMethod.toString(), getClass(value));
				methodObject.invoke(object, new Object[]{value});
			}
		} catch(Exception e) {
			log.error("Error in the method setMethod(Object object, String method, Object value)", e);
		}
	}
	
	/**
	 * Return array of type Class argument.
	 */
	@SuppressWarnings("unchecked")
	private static Class[] getClass(Object value) {
		Class[] clazz = new Class[1];
		if(value != null){
			if(value instanceof String){
				clazz[0] = String.class;
			} else if(value instanceof Integer){
				clazz[0] = Integer.class;
			} else if(value instanceof Long){
				clazz[0] = Long.class;
			} else if(value instanceof Boolean){
				clazz[0] = Boolean.class;
			} else if(value instanceof Float){
				clazz[0] = Float.class;
			} else if(value instanceof Date){
				clazz[0] = Date.class;
			} else if(value instanceof BigDecimal){
				clazz[0] = BigDecimal.class;
			} else if(value instanceof BigInteger){
				clazz[0] = BigInteger.class;
			} else {
				//Implements type if not exists.
			}
		}
		return clazz;
	}
	
	/**
	 * Return array of type Object argument
	 */
	private static Object[] getObject(Object value) {
		Object[] objects = new Object[1];
		if(value != null){
			if(value instanceof String){
				objects[0] = String.valueOf(value);
			} else if(value instanceof Integer){
				objects[0] = new Integer(String.valueOf(value));
			} else if(value instanceof Long){
				objects[0] = new Long(String.valueOf(value));
			} else if(value instanceof Boolean){
				objects[0] = new Boolean(String.valueOf(value));
			} else if(value instanceof Float){
				objects[0] = new Float(String.valueOf(value));
			} else if(value instanceof Date){
				objects[0] = value;
			}  else if(value instanceof BigDecimal){
				objects[0] = value;
			} else if(value instanceof BigInteger){
				objects[0] = value;
			} else {
				//Implements type if not exists.
			}
		}
		return objects;
	}
	
	/**
	 * Get a list of object type of the param classe with the values of the token struture. 
	 * @param token Data struture with pattern atribute=value|atribute2=value2;atribute=value|atribute2=value2
	 */
	@SuppressWarnings("unchecked")
	public static List getComposite(Object object, String token) {
		List list = null;
		try {
			if(object != null && token != null && token.indexOf("|") != -1){
			   StringTokenizer tokenizer = new StringTokenizer(token, ";");
			   list = new ArrayList(tokenizer.countTokens());
			   StringTokenizer tokenObject = null;
			   StringTokenizer tokenMethod = null;
			   String method = null;
			   Object value = null;
			   Object newObject = null;
			   while(tokenizer.hasMoreElements()){
				   tokenObject = new StringTokenizer((String) tokenizer.nextElement(), "|");
				   newObject = object.getClass().newInstance();
				   while(tokenObject.hasMoreElements()){
					   tokenMethod = new StringTokenizer((String) tokenObject.nextElement(), ":");
					   method = (String) tokenMethod.nextElement();
					   value = tokenMethod.nextElement();
					   setMethod(newObject, method, value);
				   }
				   list.add(newObject);
			   }
			}
		} catch(Exception e) {
			log.error("Error in the method getComposite(Object object, String token)", e);
		}
		return list;
	}
	
	/**
	 * Get a list of object type of the param classe with the values of the token struture.
	 * @param object
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getMountPk(Object object, String token) {
		List list = null;
		try {
			if(object != null && token != null && token.indexOf(":") != -1){
			   StringTokenizer tokenizer = new StringTokenizer(token, ";");
			   list = new ArrayList(tokenizer.countTokens());
			   StringTokenizer tokenObject = null;
			   String method = null;
			   Object value = null;
			   Object newObject = null;
			   while(tokenizer.hasMoreElements()){
				   tokenObject = new StringTokenizer((String) tokenizer.nextElement(), ":");
				   newObject = object.getClass().newInstance();
				   while(tokenObject.hasMoreElements()){
					   method = (String) (String) tokenObject.nextElement();
					   value = tokenObject.nextElement();
					   setMethod(newObject, method, value);
				   }
				   list.add(newObject);
			   }
			}
		} catch(Exception e) {
			log.error("Error in the method getComposite(Object object, String token)", e);
		}
		return list;
	}
	
	/**
	 * Get a list of {@link Sort} the param classe with the values of the token struture.
	 * @param token
	 * @return
	 */
	public static Sort[] getMountSimpleSort(String token) {
		Sort[] simpleSorts = null;
		try {
			if(token != null && token.indexOf(":") != -1){ 
			   StringTokenizer tokenizer = new StringTokenizer(token, ":");
			   simpleSorts = new Sort[1];
			   Sort simpleSort = null;
			   int count = 0;
			   while(tokenizer.hasMoreElements()){
				   simpleSort = new Sort();
				   simpleSort.setNameProperty((String) tokenizer.nextElement());
				   String orderToken = (String) tokenizer.nextElement();
				   simpleSort.setOrderOperator(orderToken != null && orderToken.toLowerCase().equals("asc") ? OrderOperator.ASC : OrderOperator.DESC);
				   simpleSorts[count] = simpleSort;
				   count++;
			   }
			}
		} catch(Exception e) {
			log.error("Error in the method getMountSimpleSort(String token)", e);
		}
		return simpleSorts;
	}
	
	/**
	 * Convert a string to a date with format from the bundle called format.date.
	 */
	public static Date stringToDate(String stringDate){
		Date date = null;
		if(stringDate != null && !stringDate.equals("")){
			if(stringDate.length() > 10){
				stringDate = stringDate.substring(0, 10);
			}
			String format = ResourceBundle.getBundle("ApplicationResources").getString("format.date.screen");
			if(stringDate.indexOf("-") > 0){
				format = ResourceBundle.getBundle("ApplicationResources").getString("format.date.database");
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try {
				date = dateFormat.parse(stringDate);
			} catch(Exception e){
				log.error("Error in the method stringToDate(String stringDate)", e);
			}
		}
		return date;
	}
	
	/**
	 * Convert a Date to a String with format from the bundle called format.date.screen.
	 */
	public static String dateToString(Date date){
		String stringDate = "";
		if(date != null){
			DateFormat dateFormat = new SimpleDateFormat(ResourceBundle.getBundle("ApplicationResources").getString("format.date.screen"));
			try {
				stringDate = dateFormat.format(date);
			} catch(Exception e){
				log.error("Error in the method dateToString(Date date)", e);
			}
		}
		return stringDate;
	}
	
	/**
	 * Convert a Date to String in the format argument. 
	 */
	public static String dateToString(Date date, String format) {
		String stringDate = "";
		if(date != null){
			DateFormat dateFormat = new SimpleDateFormat(format);
			try {
				stringDate = dateFormat.format(date);
			} catch(Exception e){
				log.error("Error in the method dateToString(Date date, String format)", e);
			}
		}
		return stringDate;
	}
	
	/**
	 * Convert a Date to String in the format argument. 
	 */
	public static String dateToString(String date, String actualFormat, String newFormat) {
		String stringDate = "";
		if(date != null){
			DateFormat dateFormat = new SimpleDateFormat(actualFormat);
			try {
				Date newDate = dateFormat.parse(date);
				dateFormat = new SimpleDateFormat(newFormat);
				stringDate = dateFormat.format(newDate);
			} catch(Exception e){
				log.error("Error in the method dateToString(Date date, String format)", e);
			}
		}
		return stringDate;
	}
	
	/**
	 * Return comparation type of Object and order(ASC or DESC).
	 * @param first
	 * @param second
	 * @param type
	 * @param order If true equal order ASC.
	 * @return
	 */
	private static int comparatorType(Object first, Object second, Object type, boolean order) {
		if(type instanceof String){
			return  (order ? ((String) first).compareTo((String) second) : ((String) second).compareTo((String) first));
		} else if(type instanceof Date){
			return  (order ? ((Date) second).compareTo((Date) first) : ((Date) first).compareTo((Date) second));
		} else if(type instanceof Integer){
			return  (order ? ((Integer) second).compareTo((Integer) first) : ((Integer) first).compareTo((Integer) second));
		} else if(type instanceof Long){
			return  (order ? ((Long) second).compareTo((Long) first) : ((Long) first).compareTo((Long) second));
		} else if(type instanceof BigDecimal){
			return  (order ? ((BigDecimal) second).compareTo((BigDecimal) first) : ((BigDecimal) first).compareTo((BigDecimal) second));
		} else if(type instanceof BigInteger){
			return  (order ? ((BigInteger) second).compareTo((BigInteger) first) : ((BigInteger) first).compareTo((BigInteger) second));
		} else {
			log.error("Type not found Implemented.");
			return -1;
		}
	}
	
	/**
	 * Private member to sort.
	 */
	private static Object objectType = null;
	/**
	 * Private member to sort.
	 */
	private static boolean order;
	
	/**
	 * Method what sort a List usign with parameteres the attributes your orders(ASC or DESC).
	 * @param attributes
	 * @param clazz
	 * @param list
	 * @param orders If true equal order ASC.
	 */
	@SuppressWarnings("unchecked")
	public static void sort(String[] attributes, Class clazz, List list, boolean[] orders) {
		try {
			if(list != null && !list.isEmpty() && attributes.length == orders.length){
				StringBuffer sbMethod = null;
				objectType = null;
				Class clazzType = null;
				for(int i = 0; i < attributes.length; i++){
					order = orders[i];
					sbMethod = new StringBuffer("get");
					sbMethod.append(attributes[i].substring(0, 1).toUpperCase());
					sbMethod.append(attributes[i].substring(1));
					
					clazzType = clazz.getMethod(sbMethod.toString(), new Class[]{}).getReturnType();
					if(clazzType == Integer.class){
						objectType = new Integer(0);
					} else if(clazzType == Long.class){
						objectType = new Long(0);
					} else if(clazzType == BigInteger.class){
						objectType = new BigInteger("0");
					} else if(clazzType == BigDecimal.class){
						objectType = new BigDecimal(0);
					} else if(clazzType == Date.class){
						objectType = new Date();
					} else if(clazzType == String.class){
						objectType = new String();
					}
					
					BeanComparator orderList = new BeanComparator(attributes[i],
					   new Comparator() {
					     public int compare(Object first, Object second) {
					       return comparatorType(first, second, objectType, order);   
					     }   
					   }
					);   
					Collections.sort(list, orderList);
					objectType = null;
				}
			}
			
		} catch(Exception e){
			log.error("Error in the method sort(String[] attributes, Class clazz, List list, boolean order)", e);
		}
	}
	
	/**
	 * Method what sort a List of Objects using with arguments Object {@link Sort}
	 * @param simpleSorts
	 * @param clazz
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public static void sort(Sort[] simpleSorts, Class clazz, List list) {
		if(simpleSorts != null && clazz != null && list != null && !list.isEmpty()){
			String[] attributes = new String[simpleSorts.length];
			boolean[] orders = new boolean[simpleSorts.length];
			int count = 0;
			for(int i = 0; i < simpleSorts.length; i++){
				if(simpleSorts[i] != null){
					attributes[count] = simpleSorts[i].getNameProperty();
					orders[count] = simpleSorts[i].getOrderOperator() != null && simpleSorts[i].getOrderOperator().getValue() != null && simpleSorts[i].getOrderOperator().getValue().equals("ASC");
				}
			}
			if(attributes.length > 0 && attributes.length == orders.length){
				sort(attributes, clazz, list, orders);
			}
		}
	}
	
}