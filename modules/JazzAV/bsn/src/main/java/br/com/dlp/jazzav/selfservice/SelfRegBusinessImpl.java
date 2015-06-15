package br.com.dlp.jazzav.selfservice;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzav.endereco.LogradouroVO;
import br.com.dlp.jazzav.exceptions.JazzOMRException;
import br.com.dlp.jazzav.person.PessoaBusiness;
import br.com.dlp.jazzav.person.PessoaVO;


@Component
//@WebService
public class SelfRegBusinessImpl {

	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Resource
	public HibernateTemplate hibernateTemplate;
	




	/**
	 * Checa se o login que está sendo enviado já existe no sistema. Não faz nenhum tratamento, pois espera-se que o jsf bean já tenha tratado isso.
	 * Apenas para garantir que, por exemplo, senhas não sejam alteradas.
	 * 
	 * @param pessoaVO
	 */
	protected void checkExistUserName(PessoaVO pessoaVO) {
		
		/*
		if(pessoaVO==null || pessoaVO.getLogin()==null){
			throw new JazzRuntimeException("A pessoa ou o login informado são nulos!", new NullPointerException("npe forçado"));
		}
		
		String login = pessoaVO.getLogin();
		
		if(pessoaBusiness.existsLogin(login)){
			throw new JazzRuntimeException("O login informado ("+login+") já existe!");
		}
		*/
		
	}



	/**
	 * @param imageFile
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected BufferedImage loadImage(String imageFile) {
		InputStream is = this.getClass().getResourceAsStream(imageFile);

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}

 	
}
