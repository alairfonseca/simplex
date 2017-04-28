package me.alair.simplex.service.funcoes;

import java.math.BigDecimal;

public class FuncaoOtima {

	private BigDecimal[] variaveisLivres;
	private boolean maximizacao;
	private Integer variavelAuxiliar;

	/**
	 * Construtor da classe FuncaoOtima.
	 * 
	 * @param variaveisLivres
	 * @param variavelAuxiliar
	 * @param maximizacao
	 */
	public FuncaoOtima(BigDecimal[] variaveisLivres, boolean maximizacao) {
		this.variaveisLivres = variaveisLivres;
		this.maximizacao = maximizacao;
	}

	public FuncaoOtima() {

	}

	@Override
	public String toString() {
		String retorno = "";
		String variaveisL = "";

		for (int i = 0; i < variaveisLivres.length; i++) {
			variaveisL += variaveisLivres[i].toString() + " ";
		}

		retorno += variavelAuxiliar.toString() + " " + variaveisL;

		return retorno;
	}

	// metodo que executa a transformação da função
	public FuncaoOtima transformaFuncao() {
		// BigDecimal menosUm = new BigDecimal(-1);
		variavelAuxiliar = 0;

		// for (int i = 0; i < variaveisLivres.length; i++) {
		// variaveisLivres[i] = variaveisLivres[i].multiply(menosUm);
		// }

		// inverte o objetivo da funcao otima. Ex (F(X) -> MAX) transforma-se em
		// (F(X) -> MIN).
		maximizacao = !maximizacao;

		return this;
	}

	public BigDecimal[] concatena(BigDecimal[] arrayA, BigDecimal[] arrayB) {
		int tamanhoA = arrayA.length;
		int tamanhoB = arrayB.length;
		BigDecimal[] arrayConcatenado = new BigDecimal[tamanhoA + tamanhoB];
		System.arraycopy(arrayA, 0, arrayConcatenado, 0, tamanhoA);
		System.arraycopy(arrayB, 0, arrayConcatenado, tamanhoA, tamanhoB);
		return arrayConcatenado;
	}

	public BigDecimal[] concatena(BigDecimal a, BigDecimal[] arrayB) {
		BigDecimal[] arrayA = new BigDecimal[] { a };
		int tamanhoA = arrayA.length;
		int tamanhoB = arrayB.length;
		BigDecimal[] arrayConcatenado = new BigDecimal[tamanhoA + tamanhoB];
		System.arraycopy(arrayA, 0, arrayConcatenado, 0, tamanhoA);
		System.arraycopy(arrayB, 0, arrayConcatenado, tamanhoA, tamanhoB);
		return arrayConcatenado;
	}

	public BigDecimal[] getVariaveisLivres() {
		return variaveisLivres;
	}

	public void setVariaveisLivres(BigDecimal[] variaveisLivres) {
		this.variaveisLivres = variaveisLivres;
	}

	public boolean isMaximizacao() {
		return maximizacao;
	}

	public void setMaximizacao(boolean maximizacao) {
		this.maximizacao = maximizacao;
	}

	// retorna 0, pois a variavel auxiliar na funcao objetiva terá sempre este
	// valor.
	public Integer getVariavelAuxiliar() {
		return 0;
	}

	public void setVariavelAuxiliar(Integer variavelAuxiliar) {
		this.variavelAuxiliar = variavelAuxiliar;
	}

}
