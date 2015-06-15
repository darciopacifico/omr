package br.com.mastersaf.util;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

 /** 
 * 
 * @author Felipe A. Oliveira 
 */  
public class AutoFlushOpenSessionInViewFilter extends OpenSessionInViewFilter {  
   
     /** 
      * Busca uma Session do Hibernate e aplica flush mode para auto. 
      * @param sessionFactory a SessionFactory 
      * @return the Session para utilizar 
      * @throws DataAccessResourceFailureException se sessao nao for criada 
      * @see org.springframework.orm.hibernate3.SessionFactoryUtilsgetSession(SessionFactory, boolean) 
      * @see org.hibernate.FlushModeAUTO 
      */  
     protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {  
         Session session = SessionFactoryUtils.getSession(sessionFactory, true);  
         session.setFlushMode(FlushMode.AUTO);  
         return session;  
     }  
   
}  
