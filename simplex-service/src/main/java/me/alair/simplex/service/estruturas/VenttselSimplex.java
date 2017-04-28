package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.alair.simplex.service.funcoes.Funcao;
import me.alair.simplex.service.funcoes.FuncaoOtima;

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

	// variaveisLivres = concatena(new BigDecimal[] {resultado},
	// variaveisLivres);
	/**
	 * main criado apra testar a classe.
	 * @param args
	 */
	public static void main(String[] args) {
		FuncaoOtima funcaoOtima = new FuncaoOtima(new BigDecimal[] { new BigDecimal(80), new BigDecimal(60) },
				Boolean.TRUE);
		Funcao[] funcoes = new Funcao[] {
				new Funcao(new BigDecimal[] { new BigDecimal(4), new BigDecimal(6) }, new BigDecimal(24),
						new Integer(1), Boolean.TRUE),
				new Funcao(new BigDecimal[] { new BigDecimal(4), new BigDecimal(2) }, new BigDecimal(16),
						new Integer(2), Boolean.FALSE),
				new Funcao(new BigDecimal[] { new BigDecimal(0), new BigDecimal(1) }, new BigDecimal(3),
						new Integer(3), Boolean.FALSE) };

		Map<Integer, BigDecimal[]> linhas = new HashMap<Integer, BigDecimal[]>();

		linhas.put(funcaoOtima.getVariavelAuxiliar(), funcaoOtima
				.concatena(new BigDecimal(funcaoOtima.getVariavelAuxiliar()), funcaoOtima.getVariaveisLivres()));

		for (int i = 0; i < funcoes.length; i++) {
			funcoes[i] = funcoes[i].transformaFuncao();
			linhas.put(funcoes[i].getVariavelAuxiliar(),
					funcoes[i].concatena(funcoes[i].getResultado(), funcoes[i].getVariaveisLivres()));
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, BigDecimal[]> entry : linhas.entrySet()) {
			sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString());
		}
		
		TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);

		System.out.println(sb);

		System.out.println(funcoes[0].transformaFuncao().toString());

		System.out.println(funcaoOtima.transformaFuncao().toString());
		
		System.out.println("\n\n\n------------------------");
		
		System.out.println(tabela.toString());
	}
}
