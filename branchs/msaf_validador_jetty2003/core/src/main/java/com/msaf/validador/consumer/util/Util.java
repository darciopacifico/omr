package com.msaf.validador.consumer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	public static final Pattern SPACE_PATTERN = Pattern.compile(" ");
	public static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
	public static final Pattern DUPLICATE_SPACE_PATTERN = Pattern.compile(" {2,}");
	public static final Pattern DUPLICATE_HYPHEN_PATTERN = Pattern.compile("-{2,}");
	public static final Pattern A_LOWER_ACENTUADO = Pattern.compile("[·‡‚„‰Â™]");
	public static final Pattern E_LOWER_ACENTUADO = Pattern.compile("[ÈÍÎË]");
	public static final Pattern I_LOWER_ACENTUADO = Pattern.compile("[ÌÓÔÏ]");
	public static final Pattern O_LOWER_ACENTUADO = Pattern.compile("[ˆÛÙÚı∫]");
	public static final Pattern U_LOWER_ACENTUADO = Pattern.compile("[¸˙˚˘]");
	public static final Pattern A_UPPER_ACENTUADO = Pattern.compile("[¡¿¬√ƒ≈]");
	public static final Pattern E_UPPER_ACENTUADO = Pattern.compile("[… À»]");
	public static final Pattern I_UPPER_ACENTUADO = Pattern.compile("[ÕŒœÃ]");
	public static final Pattern O_UPPER_ACENTUADO = Pattern.compile("[÷”‘“’]");
	public static final Pattern U_UPPER_ACENTUADO = Pattern.compile("[‹⁄€Ÿ]");
	public static final Pattern NUMERIC = Pattern.compile("\\d{1,}");
	public static final Pattern CEDILHA_UPPER = Pattern.compile("«");
	public static final Pattern CEDILHA_LOWER = Pattern.compile("Á");
	public static final Pattern UNACCEPTED_CHARACTERS = Pattern
			.compile("[^a-zA-Z0-9·‡‚„‰ÂÈÍÎËÌÓÔÏ™ˆÛÙÚı∫¸˙˚˘¡¿¬√ƒ≈… À»ÕŒœÃ÷”‘“’‹⁄€Ÿ «Á-]");

	/**
	 * Substitui espaÁos por hifens
	 *
	 * @param text
	 * @return text com espaÁos substituÌdos por -
	 */
	public static String hyphenize(String text) {
		// ------------------------------------------------------------
		String retorno = null;
		Matcher matcher = null;
		// ------------------------------------------------------------

		if (text != null) {
			matcher = SPACE_PATTERN.matcher(text);
			retorno = matcher.replaceAll("-");
		}

		return retorno;
	}

	/**
	 * Auxiliar para Substring de 0 a X caracteres, mantÈm o valor passado caso
	 * o length fornecido maior ou igual ao length da String passada.
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String left(String str, int length) {
		// ------------------------------------------------------------
		String retorno = null;
		// ------------------------------------------------------------

		if (str != null) {
			if (str.length() <= length)
				retorno = str;
			else
				retorno = str.substring(0, length);
		}

		return retorno;
	}

	/**
	 * Auxiliar para Substring de X a length caracteres, mantÈm o valor passado
	 * caso o length fornecido maior ou igual ao length da String passada.
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String right(String str, int length) {
        if(str == null) return null;
        if(str.length() <= length) return str;
        if(length < 0) return "";

        return str.substring(str.length() - length, str.length());
    }

	/**
	 * Auxiliar para Substring de 0 a X caracteres, mantÈm o valor passado caso
	 * o length fornecido maior ou igual ao length da String passada.
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String mid(String str, int start, int length) {
		String retorno = null;

		if (str == null)
			return null;
		if (str.length() <= start)
			return null;

		retorno = right(str, str.length() - start + 1);
		retorno = left(retorno, length);

		return retorno;
	}

	/**
	 * Coloca a String em caixa baixa, n„o d· NullPointer
	 *
	 * @param str
	 * @return
	 */
	public static String toLowerCase(String str) {
		return str == null?null:str.toLowerCase();
	}

	/**
	 * Verifica se express„o existe, n„o d· NullPointer
	 * @param str
	 * @return
	 */
	public static int indexOf(String str, String exp) {
		return str == null?-1:str.indexOf(exp);
	}

	/**
	 * Coloca a String em caixa baixa, n„o d· NullPointer
	 *
	 * @param str
	 * @return
	 */
	public static String removeDuplicateSpaces(String text) {
		// ------------------------------------------------------------
		String retorno = null;
		Matcher matcher = null;
		// ------------------------------------------------------------

		if (text != null) {
			matcher = DUPLICATE_SPACE_PATTERN.matcher(text);
			retorno = matcher.replaceAll(" ");
		}

		return retorno;
	}

	/**
	 * Coloca a String em caixa baixa, n„o d· NullPointer
	 *
	 * @param str
	 * @return
	 */
	public static String removeDuplicateHyphen(String text) {
		// ------------------------------------------------------------
		String retorno = null;
		Matcher matcher = null;
		// ------------------------------------------------------------

		if (text != null) {
			matcher = DUPLICATE_HYPHEN_PATTERN.matcher(text);
			retorno = matcher.replaceAll("-");

			if (retorno.endsWith("-"))
				retorno = retorno.substring(0, retorno.length() - 1);
			if (retorno.startsWith("-"))
				retorno = retorno.substring(1, retorno.length());
		}

		return retorno;
	}

	public static String replaceAcentos(String text) {
		// ------------------------------------------------------------
		String retorno = null;
		Matcher matcher = null;
		// ------------------------------------------------------------

		if (text != null) {
			// Lower -----------------------------------------------------
			matcher = A_LOWER_ACENTUADO.matcher(text);
			retorno = matcher.replaceAll("a");

			matcher = E_LOWER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("e");

			matcher = I_LOWER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("i");

			matcher = O_LOWER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("o");

			matcher = U_LOWER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("u");
			// ------------------------------------------------------------

			// Upper -----------------------------------------------------
			matcher = A_UPPER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("A");

			matcher = E_UPPER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("E");

			matcher = I_UPPER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("I");

			matcher = O_UPPER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("O");

			matcher = U_UPPER_ACENTUADO.matcher(retorno);
			retorno = matcher.replaceAll("U");
			// ------------------------------------------------------------
		}

		return retorno;
	}

	public static String replaceSpecialCharacters(String text) {
		// ------------------------------------------------------------
		String retorno = null;
		Matcher matcher = null;
		// ------------------------------------------------------------

		if (text != null) {
			// Lower -----------------------------------------------------
			matcher = CEDILHA_LOWER.matcher(text);
			retorno = matcher.replaceAll("c");
			// ------------------------------------------------------------

			// Upper -----------------------------------------------------
			matcher = CEDILHA_UPPER.matcher(retorno);
			retorno = matcher.replaceAll("C");
			// ------------------------------------------------------------
		}

		return retorno;
	}

	public static String replaceUnacceptableCharacters(String text, String toBeUsed) {
		return replaceUnacceptableCharacters(UNACCEPTED_CHARACTERS, text, toBeUsed);
	}

	public static String replaceUnacceptableCharacters(String regex, String text, String toBeUsed) {
		return replaceUnacceptableCharacters(Pattern.compile(regex), text, toBeUsed);
	}

	public static String replaceUnacceptableCharacters(Pattern regex, String text, String toBeUsed) {
		//------------------------------------------------------------
		String retorno	= null;
		Matcher matcher	= null;
		//------------------------------------------------------------

		if(text != null) {
			matcher = regex.matcher(text);
			retorno	= matcher.replaceAll(toBeUsed);
		}

		return retorno;
	}

	public static String removeHTML(String text) {
		return replaceUnacceptableCharacters(HTML_PATTERN, text, "");
	}

	/**
	 * Retorna se a String passada est· vazia
	 *
	 * @param texto
	 * @return
	 */
	public static boolean isEmpty(final String texto) {
		return texto == null || texto.trim().length() == 0;
	}

	/**
	 * Retorna se a ColeÁ„o passada est· vazia
	 *
	 * @param Collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> retorno) {
		return retorno == null || retorno.isEmpty();
	}

	/**
	 * Transforma uma data em formato String para uma data em formato Timestamp
	 *
	 * @param date
	 * @return Timestamp
	 * @throws ParseException
	 */
    public static String stringToDate(Date date) {
    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        if(date != null)
            return formato.format(date);
        else
            return null;
    }

	public static boolean isNumeric(String texto) {
		if(texto == null) return false;
		return NUMERIC.matcher(texto).matches();
	}

	public static String camelize(String texto) {
		if(isEmpty(texto)) throw new IllegalArgumentException("Argumento texto n„o pode ser vazio");

		return Util.left(texto, 1).toUpperCase() + Util.right(texto, texto.length() - 1);
	}

	/**
	 * Coloca a String em caixa alta, n„o d· NullPointer
	 *
	 * @param str
	 * @return
	 */
	public static String toUpperCase(String str) {
		return str == null?null:str.toUpperCase();
	}

	public static String trim(String str) {
		return str == null?null:str.trim();
	}

	public static String toString(Object o) {
		return o == null?"":o.toString();
	}

	public static String decamelize(String texto) {
		if(isEmpty(texto)) throw new IllegalArgumentException("Argumento texto n„o pode ser vazio");

		return Util.left(texto, 1).toLowerCase() + Util.right(texto, texto.length() - 1);
	}

	public static String getPartBefore(String texto, String separador) {
		if(texto == null) return null;
		if(separador == null) return texto;

		int index = texto.indexOf(separador);
		if(index == -1) return texto;

		return left(texto, index);
	}

	public static String getPartAfter(String texto, String separador) {
		if(texto == null) return null;
		if(separador == null) return texto;

		int index = texto.lastIndexOf(separador);
		if(index == -1) return texto;

		return right(texto, texto.length() - (index + separador.length()));
	}

	public static String replace(String content, String toReplaceRegex, String replacement) {
		if(content == null) return null;
		if(toReplaceRegex == null || replacement == null) return content;

		return content.replaceAll(toReplaceRegex, replacement);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isWindowsSO() {
		try {
			String osName = System.getProperty("os.name");
			return startsWithIgnoreCase(osName, "windows");
		} catch (Throwable e) {
			// Qualquer problema de seguranÁa considerar· como n„o sendo executado no Windows
		}

		return false;
	}

	public static boolean startsWith(String texto, String inicio) {
		if(texto == null) return false;

		return texto.startsWith(inicio);
	}


	public static boolean startsWithIgnoreCase(String texto, String inicio) {
		if(texto == null) return false;

		return texto.toLowerCase().startsWith(inicio.toLowerCase());
	}



	public static boolean equals(Object o1, Object o2) {
		if(o1 == o2) return true;
		if(o1 == null || o2 == null) return false;

		if(o1.getClass().isArray()) {
			if(!o1.getClass().isArray()) return false;

			return Arrays.equals((Object[])o1, (Object[])o2);
		}

		return o1.equals(o2);
	}

	/**
	 * Retorna o primeiro valor n„o nulo na lista de valores fornecidos, ou nulo caso todos os
	 * valores sejam nulos.
	 */
	public static <T> T getFirstNotNull(T... values) {
		if(values == null || values.length == 0) return null;

		for (T t : values)
			if(t != null) return t;

		return null;
	}

	/**
	 * Metodo criado para atender os Agendamentos do AGC
	 * dado um cron simples de somente minuto ou somente
	 * hora È devolvido um int em minutos.
	 * @param cron
	 * @return
	 */
	public static int tranformaCronInt(String cron) {
		if(Util.isEmpty(cron)) throw new IllegalArgumentException("Argumento cron n„o pode ser vazio");

		int i = 1;
		String[] lista = cron.split(" ");

		for(String x : lista) {
			final boolean valoresIguinorados = !"0".equals(x) && !"*".equals(x) && !"?".equals(x);
			if(valoresIguinorados) {
				x = x.substring(2);
				switch (i) {
				case 2:
					return Integer.parseInt(x);
				case 3:
					return Integer.parseInt(x)* 60;
				}
			}
			i++;
		}
		return 0;
	}
}
