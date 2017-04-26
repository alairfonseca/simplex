package me.alair.simplex.service;

import java.util.HashMap;
import java.util.Map;

public class TabelaPadronizada {

	int[] variaveisBasicas;
	int[] variaveisNaoBasicas;
	CelulaTabela[][] matriz;

	public TabelaPadronizada(Map<Integer, Double[]> linhas, int qtdVariaveisBasicas) {
		// inicializando variaveis basicas e nao basicas
		variaveisBasicas[0] = 0;
		for (int i = 1; i < qtdVariaveisBasicas; i++) {
			variaveisBasicas[i] = 1;
		}
		variaveisNaoBasicas[0] = 0;
		for (int i = 1; i < linhas.size(); i++) {
			variaveisNaoBasicas[i] = i;
		}
		matriz = new CelulaTabela[linhas.size()][qtdVariaveisBasicas];

		for (int i = 0; i < linhas.size(); i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				matriz[i][j] = new CelulaTabela();
				matriz[i][j].setCelulaSuperior(linhas.get(i)[j]);
			}
		}
	}

	public static void main(String[] args) {
		Map<Integer, Double[]> linhas = new HashMap<Integer, Double[]>();
		Double[] vaiaveisBasicas0 = new Double[] { 0.0, 80.0, 60.0 };
		Double[] vaiaveisBasicas1 = new Double[] { -24.0, -4.0, -6.0 };
		Double[] vaiaveisBasicas2 = new Double[] { 16.0, 4.0, 2.0 };
		Double[] vaiaveisBasicas3 = new Double[] { 3.0, 0.0, 1.0 };

		linhas.put(0, vaiaveisBasicas0);
		linhas.put(1, vaiaveisBasicas1);
		linhas.put(2, vaiaveisBasicas2);
		linhas.put(3, vaiaveisBasicas3);

		TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);
	}

}
