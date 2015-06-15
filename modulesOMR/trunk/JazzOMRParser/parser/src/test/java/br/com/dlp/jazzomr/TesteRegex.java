package br.com.dlp.jazzomr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TesteRegex {
	/*aco_Key
	-par.PK
	-exa.PK
	-qnr.PK
	-perg.PK
	-crt.PK
	-exVar
	-PAGE*/
	//"aco_Key-"+$F{par.PK}+"-"+$F{exa.PK}+"-"+$F{qnr.PK}+"-"+$F{perg.PK}+"-"+$F{crt.PK}+"-"+(($F{par.PK}%$P{examVariants})+1)+"-"+$V{PAGE_NUMBER}+"-"+$F{crt.critType}
	//                                                   part  exa qn  pe    cr    ev   pg  TY            
	private static final String ACO_REGEX_KEY = "aco_Key-(.*)-((.*-.*-(.*))-(.*))-(.*)-(.*)-(.*)";
	private static final Integer GRUPO_PAGINA = 7;
	private static final Integer GRUPO_CRIT_TYPE = 8;
	private static final Integer GRUPO_CHAVE = 2;
	private static final Integer GRUPO_PARTICIPACAO = 1;
	private static final Integer GRUPO_EXAM_VARIANT = 6;
	private static final Integer GRUPO_PERGUNTA = 4;
	private static final Integer GRUPO_CRITERION = 5;
	

	public static void main(String[] args) {
		String val = "aco_Key-part-exa-qn-pe-cr-ev-pg-TP";
		
		Pattern omr_mark_pattern = Pattern.compile(ACO_REGEX_KEY);
		
		Matcher matcher = omr_mark_pattern.matcher(val);
		
		System.out.println(matcher.matches());
		
		System.out.println("("+GRUPO_PAGINA+")grupo pagina = "+matcher.group(GRUPO_PAGINA));
		System.out.println("("+GRUPO_CHAVE+")grupo chave 	= "+matcher.group(GRUPO_CHAVE));
		System.out.println("("+GRUPO_PARTICIPACAO+")grupo part 	= "+matcher.group(GRUPO_PARTICIPACAO));
		System.out.println("("+GRUPO_EXAM_VARIANT+")grupo exVar 	= "+matcher.group(GRUPO_EXAM_VARIANT));
		System.out.println("("+GRUPO_PERGUNTA+")grupo Perg 	= "+matcher.group(GRUPO_PERGUNTA));
		System.out.println("("+GRUPO_CRIT_TYPE+")grupo TP 	= "+matcher.group(GRUPO_CRIT_TYPE));
		System.out.println("("+GRUPO_CRITERION+")grupo CRIT 	= "+matcher.group(GRUPO_CRITERION));
		
		
		
	}

}
