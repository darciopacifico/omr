package com.msaf.validador.integration.util;

import java.math.BigInteger;

public class MathUtil {
	public static final Long ZERO_LONG = 0L;

	public static Long parseLong(String codigo) {
		if(!Util.isNumeric(codigo)) return null;

		return Long.parseLong(codigo);
	}

	public static Integer parseInteger(String codigo) {
		if(!Util.isNumeric(codigo)) return null;

		return Integer.parseInt(codigo);
	}

	public static Integer parseInteger(BigInteger bigRevision) {
		if(bigRevision == null) return null;

		return bigRevision.intValue();
	}

	public static boolean isOdd(int i) {
		return i % 2 != 0;
	}

	public static boolean isPair(int i) {
		return !isOdd(i);
	}
}
