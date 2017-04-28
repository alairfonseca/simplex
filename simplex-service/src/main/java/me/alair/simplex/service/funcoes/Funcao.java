package me.alair.simplex.service.funcoes;

import java.math.BigDecimal;

public class Funcao {

	private BigDecimal[] variaveisLivres;
	private BigDecimal resultado;
	private Integer variavelAuxiliar;
	private boolean maiorQue;

	/**
	 * Construtor da classe Funcao.
	 * 
	 * @param variaveisLivres
	 * @param resultado
	 * @param variavelAuxiliar
	 * @param maximizacao
	 */
	public Funcao(BigDecimal[] variaveisLivres, BigDecimal resultado, Integer variavelAuxiliar,
			boolean maximizacao) {
		this.variaveisLivres = variaveisLivres;
		this.resultado = resultado;
		this.variavelAuxiliar = variavelAuxiliar;

		if (maximizacao) {
			this.variavelAuxiliar *= -1;
		} else {
			this.variavelAuxiliar = variavelAuxiliar;
		}

	}
	
	public Funcao() {
		
	}
	
	@Override
	public String toString() {
		String retorno = "";
		String variaveisL = "";
		
		for(int i = 0; i < variaveisLivres.length; i++) {
			variaveisL += variaveisLivres[i].toString() + " ";
		}
		
		retorno += variavelAuxiliar.toString() + " = " + resultado.toString() + " " + variaveisL;
		
		return retorno;
	}

	// metodo que executa a transformação da função
	public Funcao transformaFuncao() {
		BigDecimal menosUm = new BigDecimal(-1);

		/**
		 * primeiro passo. jogar as variaveis livres para a direita,
		 * igualando-os à variavel auxiliar, dessa forma inverte-se todos os
		 * sinais das variaveis livres.
		 */
		for (int i = 0; i < variaveisLivres.length; i++) {
			variaveisLivres[i].multiply(menosUm);
		}

		/**
		 * segundo passo. se a variavel auxiliar for negativa, multiplica-se a
		 * funcao por -1, invertendo todos os seus sinais.
		 */
		if (variavelAuxiliar < 0) {
			variavelAuxiliar *= -1;
			for (int i = 0; i < variaveisLivres.length; i++) {
				variaveisLivres[i].multiply(menosUm);
			}
			resultado.multiply(menosUm);
		}
		
		/**
		 * terceiro passo.
		 * inverte-se os sinais das variaveis livres.
		 */
		for (int i = 0; i < variaveisLivres.length; i++) {
			variaveisLivres[i].multiply(menosUm);
		}
		
		return this;
	}
	
	public BigDecimal[] concatena(BigDecimal[] arrayA, BigDecimal[] arrayB) {
		   int tamanhoA = arrayA.length;
		   int tamanhoB = arrayB.length;
		   BigDecimal[] arrayConcatenado= new BigDecimal[tamanhoA+tamanhoB];
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

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public Integer getVariavelAuxiliar() {
		return variavelAuxiliar;
	}

	public void setVariavelAuxiliar(Integer variavelAuxiliar) {
		this.variavelAuxiliar = variavelAuxiliar;
	}

	public boolean isMaximizacao() {
		return maiorQue;
	}

	public void setMaximizacao(boolean maximizacao) {
		this.maiorQue = maximizacao;
	}
}
