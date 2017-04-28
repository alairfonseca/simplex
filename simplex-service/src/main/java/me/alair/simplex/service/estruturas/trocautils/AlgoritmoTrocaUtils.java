package me.alair.simplex.service.estruturas.trocautils;

import java.math.BigDecimal;

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

		retorno = um.divide(elemento);

		return retorno;
	}

}
