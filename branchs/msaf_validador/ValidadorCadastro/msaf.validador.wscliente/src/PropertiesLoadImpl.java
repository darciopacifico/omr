

public class PropertiesLoadImpl {  
       
     private static PropertiesLoader loader = new PropertiesLoader();  
     
     /**
      * Obtem o valor 
      * @param chave
      * @return
      */
     public static String getValor(String chave){  
        return (String)loader.getValor(chave);  
     }  
   
} 
