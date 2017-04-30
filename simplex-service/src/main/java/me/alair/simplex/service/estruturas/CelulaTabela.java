package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;

public class CelulaTabela {

	private BigDecimal celulaSuperior;
	private BigDecimal celulaInferior;
	private boolean elementoPermitido;
	private boolean celulaSuperiorMarcada;
	private boolean celulaInferiorMarcada;

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
		this.celulaSuperiorMarcada = false;
		this.celulaInferiorMarcada = false;
	}

	/**
	 * construtor da classe CelulaTabela.
	 */
	public CelulaTabela() {

	}

	@Override
	public String toString() {
		String inferior;
		String superior;
		if (celulaInferior == null) {
			inferior = "";
		} else {
			inferior = celulaInferior.toPlainString();
		}

		if (celulaSuperior == null) {
			superior = "";
		} else {
			superior = celulaSuperior.toPlainString();
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

	public boolean isCelulaSuperiorMarcada() {
		return celulaSuperiorMarcada;
	}

	public void setCelulaSuperiorMarcada(boolean celulaSuperiorMarcada) {
		this.celulaSuperiorMarcada = celulaSuperiorMarcada;
	}

	public boolean isCelulaInferiorMarcada() {
		return celulaInferiorMarcada;
	}

	public void setCelulaInferiorMarcada(boolean celulaInferiorMarcada) {
		this.celulaInferiorMarcada = celulaInferiorMarcada;
	}

}
