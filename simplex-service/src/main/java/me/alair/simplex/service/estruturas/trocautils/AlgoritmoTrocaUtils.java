package me.alair.simplex.service.estruturas.trocautils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class AlgoritmoTrocaUtils {

	/**
	 * inverte o elemento ex.(x -> 1/x).
	 * 
	 * @param elemento
	 * @return
	 */
	public static BigDecimal inverteElemento(BigDecimal elemento) {
		BigDecimal retorno;
		BigDecimal um = new BigDecimal(1);

		retorno = um.divide(elemento, 4, RoundingMode.HALF_UP);

		return retorno;
	}

	/**
	 * verifica se o sinal dos valores passados como parametro sao iguais ou
	 * diferentes.
	 */
	public static boolean comparaSinal(BigDecimal val1, BigDecimal val2) {
		if (val1.signum() < 0 && val2.signum() < 0) {
			return true;
		} else if (val1.signum() > 0 && val2.signum() > 0) {
			return true;
		} else if (val1.signum() == 0 && val2.signum() == 0) {
			return true;
		}
		
		return false;
	}

}
