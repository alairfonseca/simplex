package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;

public class CelulaTabela {

	private BigDecimal celulaSuperior;
	private BigDecimal celulaInferior;
	private boolean elementoPermitido;

	/**
	 * construtor da classe CelulaTabela.
	 * 
	 * @param celulaSuperior
	 * @param celulaInferior
	 */
	public CelulaTabela(BigDecimal celulaSuperior, BigDecimal celulaInferior) {
		this.celulaSuperior = celulaSuperior;
		this.celulaInferior = celulaInferior;
		this.elementoPermitido = false;
	}

	@Override
	public String toString() {
		String inferior;
		String superior;
		if (celulaInferior == null) {
			inferior = "";
		} else {
			inferior = celulaInferior.toString();
		}

		if (celulaSuperior == null) {
			superior = "";
		} else {
			superior = celulaSuperior.toString();
		}
		return "|	" + superior + "/" + inferior + "	|";
	}

	public BigDecimal getCelulaSuperior() {
		return celulaSuperior;
	}

	public void setCelulaSuperior(BigDecimal celulaSuperior) {
		this.celulaSuperior = celulaSuperior;
	}

	public BigDecimal getCelulaInferior() {
		return celulaInferior;
	}

	public boolean isElementoPermitido() {
		return elementoPermitido;
	}

	public void setElementoPermitido(boolean elementoPermitido) {
		this.elementoPermitido = elementoPermitido;
	}

	public void setCelulaInferior(BigDecimal celulaInferior) {
		this.celulaInferior = celulaInferior;
	}

}
