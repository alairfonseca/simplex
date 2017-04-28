package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;

public class CelulaTabela {

	private BigDecimal celulaSuperior;
	private BigDecimal celulaInferior;

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
		// para os casos onde a celula ainda nao foi preenchida, retorne not a
		// number para evitar nullpointer.
		if (celulaSuperior == null) {
			celulaSuperior = null;
		}
		return celulaSuperior;
	}

	public void setCelulaSuperior(BigDecimal celulaSuperior) {
		this.celulaSuperior = celulaSuperior;
	}

	public BigDecimal getCelulaInferior() {
		// para os casos onde a celula ainda nao foi preenchida, retorne not a
		// number para evitar nullpointer.
		if (celulaInferior == null) {
			celulaInferior = null;
		}
		return celulaInferior;
	}

	public void setCelularInferior(BigDecimal celulaInferior) {
		this.celulaInferior = celulaInferior;
	}

}
