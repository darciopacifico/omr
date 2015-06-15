package br.com.dlp.framework.caching;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import br.com.dlp.framework.invocationhandler.AbstractInvocationHandler;

/**
 * Implementacao de BaseInvocationHandler para Caching
 */
public class CachingInvocationHandler extends AbstractInvocationHandler {
	private InstanceCacheVO instanceCacheVO;

	public CachingInvocationHandler() {
		super();
		instanceCacheVO = new InstanceCacheVO();
	}

	public Object invoke(Object ignoreThis, Method method, Object[] objects)
			throws Throwable {
		Object proxiedObject = getProxiedObject();

		Object returnValue;

		if (isCacheable(proxiedObject, method, objects)) {
			if (isCached(proxiedObject, method, objects)) {
				/* esta cacheado, recupera dados do cache */
				returnValue = getCachedResult(proxiedObject, method, objects);

			} else {
				/* ainda nao esta cacheado, invocar e fazer cache */
				returnValue = invokeMethod(proxiedObject, method, objects);
				cacheMethodInvoke(proxiedObject, method, objects, returnValue);

			}
		} else {
			/* invoca sem nenhum tipo de caching */
			returnValue = invokeMethod(proxiedObject, method, objects);
		}
		return returnValue;
	}

	protected MethodInvokeCacheVO getMethodInvokeCacheVO(Object proxiedObject,
			Method method, Object[] objects, boolean create)
			throws CachingException {
		/* recuperar ou criar/recuperar uma classe cacheada */
		ClassCachePK classCachePK = new ClassCachePK(proxiedObject.getClass());
		ClassCacheVO classCacheVO = (ClassCacheVO) instanceCacheVO
				.getCacheElement(classCachePK, create);
		if (classCacheVO == null) {
			return null;
		}

		/* recuperar ou criar/recuperar um metodo cacheado */
		MethodCachePK methodCachePK = new MethodCachePK(method);
		MethodCacheVO methodCacheVO = (MethodCacheVO) classCacheVO
				.getCacheElement(methodCachePK, create);
		if (methodCacheVO == null) {
			return null;
		}

		/* recuperar ou criar/recuperar um MethodInvoke cacheado */
		Class[] classes = method.getParameterTypes();
		MethodInvokeCachePK methodInvokeCachePK = new MethodInvokeCachePK(
				objects, classes);
		MethodInvokeCacheVO methodInvokeCacheVO = (MethodInvokeCacheVO) methodCacheVO
				.getCacheElement(methodInvokeCachePK, create);
		if (methodInvokeCacheVO == null) {
			return null;
		}

		return methodInvokeCacheVO;
	}

	/**
	 * @throws CachingException
	 */
	public void cacheMethodInvoke(Object proxiedObject, Method method,
			Object[] objects, Object result) throws CachingException {
		MethodInvokeCacheVO methodInvokeCacheVO = getMethodInvokeCacheVO(
				proxiedObject, method, objects, true);

		/* atribui resultado da pesquisa */
		methodInvokeCacheVO.setCacheResult(result);

	}

	public boolean isCacheable(Object proxiedObject, Method method,
			Object[] objects) {
		String methodName = method.getName();
		Class returnType = method.getReturnType();

		return methodName.startsWith("find")
				&& Collection.class.isAssignableFrom(returnType);
	}

	public boolean isCached(Object proxiedObject, Method method,
			Object[] objects) throws CachingException {
		MethodInvokeCacheVO methodInvokeCacheVO = getMethodInvokeCacheVO(
				proxiedObject, method, objects, false);

		return methodInvokeCacheVO != null;
	}

	public Object getCachedResult(Object proxiedObject, Method method,
			Object[] objects) throws CachingException {
		MethodInvokeCacheVO methodInvokeCacheVO = getMethodInvokeCacheVO(
				proxiedObject, method, objects, false);

		Object cachedResult = null;
		if (methodInvokeCacheVO != null) {
			cachedResult = methodInvokeCacheVO.getCacheResult();
		}

		return cachedResult;
	}

	public Object invokeMethod(Object proxiedObject, Method method,
			Object[] objects) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Object returnValue = method.invoke(proxiedObject, objects);
		return returnValue;
	}

}