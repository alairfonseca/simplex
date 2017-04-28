package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TabelaPadronizada {

	int[] variaveisBasicas;
	int[] variaveisNaoBasicas;
	CelulaTabela[][] matriz;

	public TabelaPadronizada(Map<Integer, BigDecimal[]> funcoes, int qtdVariaveisBasicas) {
		// inicializando variaveis basicas e nao basicas
		variaveisBasicas = new int[qtdVariaveisBasicas];
		for (int i = 0; i < qtdVariaveisBasicas; i++) {
			variaveisBasicas[i] = i;
		}
		variaveisNaoBasicas = new int[funcoes.size()];
		for (int i = 0; i < funcoes.size(); i++) {
			variaveisNaoBasicas[i] = i;
		}
		matriz = new CelulaTabela[funcoes.size()][qtdVariaveisBasicas];

		for (int i = 0; i < funcoes.size(); i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				matriz[i][j] = new CelulaTabela();
				matriz[i][j].setCelulaSuperior(funcoes.get(i)[j]);
			}
		}
	}

	@Override
	public String toString() {
		String retorno = "";
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				retorno += matriz[i][j];
			}
			retorno += "\n";
		}

		return retorno;
	}

	/**
	 * main criado apra testar a classe.
	 * @param args
	 */
	public static void main(String[] args) {
		Map<Integer, BigDecimal[]> linhas = new HashMap<Integer, BigDecimal[]>();
		BigDecimal[] vaiaveisBasicas0 = new BigDecimal[] { new BigDecimal(0.0), new BigDecimal(80.0),
				new BigDecimal(60.0) };
		BigDecimal[] vaiaveisBasicas1 = new BigDecimal[] { new BigDecimal(-24.0), new BigDecimal(-4.0),
				new BigDecimal(-6.0) };
		BigDecimal[] vaiaveisBasicas2 = new BigDecimal[] { new BigDecimal(16.0), new BigDecimal(4.0),
				new BigDecimal(2.0) };
		BigDecimal[] vaiaveisBasicas3 = new BigDecimal[] { new BigDecimal(3.0), new BigDecimal(0.0),
				new BigDecimal(1.0) };

		linhas.put(0, vaiaveisBasicas0);
		linhas.put(1, vaiaveisBasicas1);
		linhas.put(2, vaiaveisBasicas2);
		linhas.put(3, vaiaveisBasicas3);

		TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);

		System.out.println(tabela.toString());
	}

}
