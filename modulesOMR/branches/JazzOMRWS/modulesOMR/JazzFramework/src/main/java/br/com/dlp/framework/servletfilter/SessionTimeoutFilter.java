package br.com.dlp.framework.servletfilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filtro para controle de timeout
 * 
 * @author t_dpacifico
 */
public class SessionTimeoutFilter implements Filter {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public static final String DEFAULT_TIMEOUT_PAGE = "sessionTimeout.html";
	protected static final String PARAM_TIMEOUT_PAGE = "PARAM_TIMEOUT_PAGE";
	
	private String timeoutPage;
	
	/**
	 * Inicialização do filtro
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		String pTimeoutPage = filterConfig.getInitParameter(SessionTimeoutFilter.PARAM_TIMEOUT_PAGE);
		
		if(StringUtils.isNotBlank(pTimeoutPage)){
			setParamTimeoutPage(pTimeoutPage);
		}else{
			setParamTimeoutPage(SessionTimeoutFilter.DEFAULT_TIMEOUT_PAGE);
		}
	}
	
	/**
	 * Manipula tratamento para timeout
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,	ServletException {
		
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			
			// is session expire control required for this request?
			if (isTimeoutHandleRequired(httpServletRequest)) {
				
				// is session invalid?
				if (isSessionInvalid(httpServletRequest)) {
					String timeoutUrl = httpServletRequest.getContextPath() + "/" + getParamTimeoutPage();
					log.info("session is invalid! redirecting to timeoutpage : " + timeoutUrl);
					
					//Cria uma nova sessao para que o usuario possa reiniciar sua navegacao.
					//caso contrario o usuario poderia nao sai mais da mensagem de erro.
					httpServletRequest.getSession(true);
					
					//redireciona para o endereço padrão de timeout
					httpServletResponse.sendRedirect(timeoutUrl);
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 
	 * session shouldn't be checked for some pages. For example: for timeout page..
	 * Since we're redirecting to timeout page from this filter,
	 * if we don't disable session control for it, filter will again redirect to it
	 * and this will be result with an infinite loop...
	 */
	private boolean isTimeoutHandleRequired(HttpServletRequest httpServletRequest) {
		String requestPath = httpServletRequest.getRequestURI();
		
		boolean controlRequired = !StringUtils.contains(requestPath, getParamTimeoutPage());
		
		return controlRequired;
	}
	
	/**
	 * Testa se sessão é válida
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
		boolean sessionInValid = httpServletRequest.getRequestedSessionId() != null	&& !httpServletRequest.isRequestedSessionIdValid();
		return sessionInValid;
	}
	
	/**
	 * Implementação obrigatoria do destroy
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
	
	
	/**
	 * @return
	 */
	public String getParamTimeoutPage() {
		return timeoutPage;
	}
	
	/**
	 * @param timeoutPage
	 */
	public void setParamTimeoutPage(String timeoutPage) {
		this.timeoutPage = timeoutPage;
	}
	
}