package me.alair.simplex.service;

public class CelulaTabela {

	private Double celulaSuperior;
	private Double celulaInferior;

	public Double getCelulaSuperior() {
		// para os casos onde a celula ainda nao foi preenchida, retorne not a
		// number para evitar nullpointer.
		if (celulaSuperior == null) {
			celulaSuperior = Double.NaN;
		}
		return celulaSuperior;
	}

	public void setCelulaSuperior(Double celulaSuperior) {
		this.celulaSuperior = celulaSuperior;
	}

	public Double getCelulaInferior() {
		// para os casos onde a celula ainda nao foi preenchida, retorne not a
		// number para evitar nullpointer.
		if (celulaInferior == null) {
			celulaInferior = Double.NaN;
		}
		return celulaInferior;
	}

	public void setCelularInferior(Double celulaInferior) {
		this.celulaInferior = celulaInferior;
	}

}
