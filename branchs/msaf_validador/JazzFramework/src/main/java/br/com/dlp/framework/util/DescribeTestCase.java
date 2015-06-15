package br.com.dlp.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;

import br.com.dlp.framework.vo.AutorizadoVO;

public class DescribeTestCase {

	public static void main(String[] args) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		AutorizadoVO autorizadoVO = new AutorizadoVO();

		// autorizadoVO.setCodigo("12");
		autorizadoVO.setNome("Darcio");
		autorizadoVO.setTipo("zuba");

		Map map = BeanUtilsBean.getInstance().describe(autorizadoVO);

		System.out.println(map);
	}

}
