package br.com.mastersaf.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	public static final Locale LOCALE_BRASIL = new Locale("pt", "BR");
	public static final Locale LOCALE_USA = new Locale("en", "US");

	//public static final String FORMATACAO_DATA = "dd/MM/yyyy";
	public static final String FORMATACAO_DATA = "dd/MM/yyyy";
	public static final String FORMATACAO_DATA_AMERICANA = "yyyy/MM/dd";
	public static final String FORMATACAO_DATA_HORA_MINUTO = "dd/MM/yyyy HH:mm";
	public static final String FORMATACAO_DATA_HORA_MINUTO_SEGUNDO = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATACAO_DATA_RSS = "EEE, d MMM  yyyy HH:mm";
	public static final String FORMATACAO_HORA_MINUTO_SEGUNDO = "HH:mm:ss";
	public static final String FORMATACAO_HORA_MINUTO = "HH:mm";
	public static final String FORMATACAO_MINUTO_SEGUNDO = "mm:ss";
	public static final String FORMATACAO_MINUTO_SEGUNDO_MILISEGUNDO = "mm:ss:SS";
	private static final String DATA_NULA_MESSAGE = "Parãmetro data não pode ser nulo";
	private static final String HORA_NULA_MESSAGE = "Parãmetro Hora não pode ser nulo";

	public static final int JANEIRO = Calendar.JANUARY;
	public static final int FEVEREIRO = Calendar.FEBRUARY;
	public static final int MARCO = Calendar.MARCH;
	public static final int ABRIL = Calendar.APRIL;
	public static final int MAIO = Calendar.MAY;
	public static final int JUNHO = Calendar.JUNE;
	public static final int JULHO = Calendar.JULY;
	public static final int AGOSTO = Calendar.AUGUST;
	public static final int SETEMBRO = Calendar.SEPTEMBER;
	public static final int OUTUBRO = Calendar.OCTOBER;
	public static final int NOVEMBRO = Calendar.NOVEMBER;
	public static final int DEZEMBRO = Calendar.DECEMBER;


	public static final String DOMINGO = "dom";
	public static final String SEGUNDA = "seg";
	public static final String TERCA = "ter";
	public static final String QUARTA = "qua";
	public static final String QUINTA = "qui";
	public static final String SEXTA = "sex";
	public static final String SABADO = "sab";


	public static Date format(final String data, final String pattern) throws ParseException {
		return format(data, pattern, LOCALE_BRASIL);

	}

	public static Date format(final String data, final String pattern, Locale locale) throws ParseException {
		if(Util.isEmpty(data)) throw new IllegalArgumentException(DATA_NULA_MESSAGE);
		if(locale == null) locale =  LOCALE_BRASIL;

		final SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
		return formatter.parse(data);

	}

	public static String format(final Date data, final String pattern) {
		return format(data, pattern, LOCALE_BRASIL);
	}

	public static String format(final Date data, final String pattern, Locale locale) {
		if(data == null) throw new IllegalArgumentException(DATA_NULA_MESSAGE);
		if(locale == null) locale =  LOCALE_BRASIL;

		final DateFormat formatter = new SimpleDateFormat(pattern,locale);
		return formatter.format(data);
	}

	public static int getAno(final Date date) {
		return getField(date, Calendar.YEAR);
	}

	public static Calendar getCalendar(final Date date) {
		final Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}

	public static Date getDate(final String data) {
		if(data == null) throw new IllegalArgumentException("Argumento data não pode ser nulo");

		return getDate(data, FORMATACAO_DATA);
	}

	public static Date getDate(final String data, final String pattern) {
		if(data == null) throw new IllegalArgumentException("Argumento data não pode ser nulo");

		try {
			return new SimpleDateFormat(pattern).parse(data);
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int getDia(final Date date) {
		return getField(date, Calendar.DAY_OF_MONTH);
	}

	private static int getField(final Date date, final int field) {
		if (date == null) throw new IllegalArgumentException(DATA_NULA_MESSAGE);

		return getCalendar(date).get(field);
	}

	public static int getHora(final Date date) {
		return getField(date, Calendar.HOUR_OF_DAY);
	}

	public static int getMes(final Date date) {
		return getField(date, Calendar.MONTH);
	}

	public static int getMinuto(final Date date) {
		return getField(date, Calendar.MINUTE);
	}

	public static Date toMidnight(Date data) {
		final DateFormat formatter = new SimpleDateFormat(FORMATACAO_DATA);
		try {
			return formatter.parse(formatter.format(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Date getMidnight() {
		return toMidnight(new Date());
	}


	public static Date getDateSystem() {
		return new Date(System.currentTimeMillis());
	}
	public static Date getDate() {
		return new Date(System.currentTimeMillis());
	}

	public static String getDiaSemanaResumido() {
		Calendar c = new GregorianCalendar();
		int diaSemana = c.get(Calendar.DAY_OF_WEEK);

		 switch (diaSemana){
		   case 1: return DOMINGO;
		   case 2: return SEGUNDA;
		   case 3: return TERCA;
		   case 4: return QUARTA;
		   case 5: return QUINTA;
		   case 6: return SEXTA;
		   case 7: return SABADO;
	     }
		return null;
	}

	public static Date addDia(final Date date, int dias) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		calendar.add(Calendar.DAY_OF_MONTH, dias);

		return new Date(calendar.getTimeInMillis());
	}

	public static Date addHora(final Date data, int fusoHorario) {
		if (data == null) throw new IllegalArgumentException(DATA_NULA_MESSAGE);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);

		calendar.add(Calendar.HOUR, fusoHorario);

		return new Date(calendar.getTimeInMillis());
	}

	public static Date addMinuto(Date data, int minutos) {
		if (data == null) throw new IllegalArgumentException(DATA_NULA_MESSAGE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MINUTE, minutos);

		return calendar.getTime();
	}

	public static boolean isDataValida(String data, String formato){
		SimpleDateFormat spf = new SimpleDateFormat(formato);
		spf.setLenient(false);
		try {
			spf.parse(data);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isHoraValida(String hora, String formato){
		SimpleDateFormat spf = new SimpleDateFormat(formato);
		spf.setLenient(false);
		try {
			spf.parse(hora);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static String retornaHora(String HHmm, String formato) throws ParseException {
		DateFormat dHoraft = DateFormat.getTimeInstance(DateFormat.SHORT, LOCALE_BRASIL);
        String horaStr = dHoraft.format(DateUtil.format(HHmm, formato));
        return horaStr;
	}


	public static boolean compareSeTimeAtualEstaDepoisDe(String inicio) {
		if(Util.isEmpty(inicio)) throw new IllegalArgumentException(HORA_NULA_MESSAGE);
		try {
			Date dataInicioFaixa = format(inicio, FORMATACAO_HORA_MINUTO);
			String data = format(getDateSystem(), FORMATACAO_HORA_MINUTO);
			Date dataAtual = format(data, FORMATACAO_HORA_MINUTO);
			return (dataInicioFaixa.compareTo(dataAtual)==-1);
		} catch (ParseException e) {
			System.err.println("Hora invalida Invalido!");
			return false;
		}
	}

	public static boolean compareSeTimeAtualEstaAntesDe(String fim) {
		if(Util.isEmpty(fim)) throw new IllegalArgumentException(HORA_NULA_MESSAGE);
		try {
			Date dataInicioFaixa = format(fim, FORMATACAO_HORA_MINUTO);
			String data = format(getDateSystem(), FORMATACAO_HORA_MINUTO);
			Date dataAtual = format(data, FORMATACAO_HORA_MINUTO);
			return (dataInicioFaixa.compareTo(dataAtual)==1);
		} catch (ParseException e) {
			System.err.println("Hora invalida Invalido!");
			return false;
		}
	}

	public static String format(Date date) {
		return format(date,FORMATACAO_DATA);
	}
	public static String formatHoraMinuto(Date date) {
		return format(date,FORMATACAO_HORA_MINUTO);
	}

	public static Date clone(Date data) {
		return data == null?null:(Date)data.clone();
	}
	
	public static Date formatStruts(String string) {
		if(!Util.isEmpty(string)) {
			try {
				return DateUtil.format(string,DateUtil.FORMATACAO_DATA);
			} catch (ParseException e) {}
		}
		return null;
	}
	
	public static Date formatStruts(String string, String formato) {
		if(!Util.isEmpty(string)) {
			try {
				return DateUtil.format(string,formato,LOCALE_USA);
			} catch (ParseException e) {}
		}
		return null;
	}
	
	public static String formatStruts(Date date) {
		if(null == date) return "";
		return DateUtil.format(date, DateUtil.FORMATACAO_DATA);
	}
}
