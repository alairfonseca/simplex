package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.alair.simplex.service.funcoes.Funcao;

public class VenttselSimplex {

	private int qtdVariaveisBasicas;
	private TabelaPadronizada tabela;
	private Funcao[] funcoes;
	private Map<Integer, BigDecimal[]> funcoesTransformadas;

	public void executaSimplex(Map<Integer, BigDecimal[]> funcoesTransformadas) {
		tabela = new TabelaPadronizada(funcoesTransformadas, qtdVariaveisBasicas);

	}

	public Map<Integer, BigDecimal[]> transformaFuncoes() {
		funcoesTransformadas = new HashMap<Integer, BigDecimal[]>();

		for (int i = 0; i < funcoes.length; i++) {
			funcoes[i] = funcoes[i].transformaFuncao();
			funcoesTransformadas.put(funcoes[i].getVariavelAuxiliar(), funcoes[i].getVariaveisLivres());
		}
		return funcoesTransformadas;
	}
//variaveisLivres = concatena(new BigDecimal[] {resultado}, variaveisLivres);
	public static void main(String[] args) {
		Funcao[] funcoes = new Funcao[] {
				new Funcao(new BigDecimal[] { new BigDecimal(4), new BigDecimal(6) }, new BigDecimal(24),
						new Integer(1), Boolean.TRUE),
				new Funcao(new BigDecimal[] { new BigDecimal(11), new BigDecimal(22) }, new BigDecimal(20),
						new Integer(25), Boolean.FALSE),
				new Funcao(new BigDecimal[] { new BigDecimal(30), new BigDecimal(33) }, new BigDecimal(30),
						new Integer(35), Boolean.TRUE) };
		
		System.out.println(funcoes[0].transformaFuncao().toString());
	}
}
