
import java.io.IOException;  
import java.io.InputStream;  
import java.util.Properties;  
   
public class PropertiesLoader {  
   
     private Properties props;  
     private String nomeDoProperties = "cliente.properties";  
   
     /**
      * @Construtor
      */
     protected PropertiesLoader(){  
             props = new Properties();  
             InputStream in = this.getClass().getResourceAsStream(nomeDoProperties);  
             try{  
                
            	props.load(in);  
            	
            	in.close();  
             }  
             catch(IOException e){e.printStackTrace();}  
     }  
   
     /**
      * Obtem o valor do arquivo.
      * @param chave
      * @return
      */
     protected String getValor(String chave){  
         return (String)props.getProperty(chave);  
     }  
} 