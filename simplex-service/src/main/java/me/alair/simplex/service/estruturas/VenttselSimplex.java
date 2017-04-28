package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.alair.simplex.service.estruturas.trocautils.AlgoritmoTrocaUtils;
import me.alair.simplex.service.funcoes.Funcao;
import me.alair.simplex.service.funcoes.FuncaoOtima;

public class VenttselSimplex {

	private int qtdVariaveisBasicas;
	private Funcao[] funcoes;

	public void executaSimplex(Map<Integer, BigDecimal[]> funcoesTransformadas) {

		// primeira fase do metodo simplex.
		// tabela.executaPrimeiraFase();

	}

	/**
	 * primeira fase do metodo simplex.
	 */
	public static void executaPrimeiraFase(TabelaPadronizada tabelaPadronizada) {
		int colunaPermitida = 0;
		int linhaPermissivel = 0;
		int linhaPermitida = 0;

		// Na tabela padronizada procuramos uma variável básica com membro livre
		// negativo.
		linhaPermissivel = tabelaPadronizada.primeiraFaseOperacaoUm();
		// Se essa variável existe, então passamos para a operação 2 do
		// presente algoritmo.
		if (linhaPermissivel > 0) {
			// executa a operacao dois da primeira fase.
			colunaPermitida = tabelaPadronizada.primeiraFaseOperacaoDois(linhaPermissivel);

			// Se o elemento negativo existe, então a coluna, onde está esse
			// elemento, é escolhida como permissível.
			if (colunaPermitida > 0) {
				// Busca-se a linha permitida a partir da identificação do
				// Elemento
				// Permitido (EP) que possuir o menor quociente entre os membros
				// livres que representam as variáveis básicas (VB).
				linhaPermitida = tabelaPadronizada.primeiraFaseOperacaoTres(colunaPermitida);
			} else {
				// a solução permissível não existe.
			}

			System.out.println(linhaPermitida + " " + colunaPermitida);
		} else {
			// executa a segunda etapa do metodo
		}

	}

	/**
	 * executa os passos do algoritmo da troca.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 * @param tabelaPadronizada
	 */
	public static void executaAlgoritmoDaTroca(int linhaPermitida, int colunaPermitida,
			TabelaPadronizada tabelaPadronizada) {
		// carrega o elemento permitido.
		CelulaTabela elementoPermitido = tabelaPadronizada.getMatriz()[linhaPermitida][colunaPermitida];
		// calcula o inverso do elemento permitido.
		BigDecimal inversoElementoPermitido = AlgoritmoTrocaUtils
				.inverteElemento(elementoPermitido.getCelulaSuperior());
		// preenche a celula inferior do elemento permitido com seu inverso.
		elementoPermitido.setCelulaInferior(inversoElementoPermitido);

		// multiplica toda a linha pelo ep inverso e preenche a celula inferior.
		tabelaPadronizada.multiplicaTodaALinhaPeloEPInverso(linhaPermitida, inversoElementoPermitido);
		// multiplica toda a coluna pelo - (ep inverso) e preenche a celula
		// inferior.
		tabelaPadronizada.multiplicaTodaAColunaPeloEPInverso(colunaPermitida, inversoElementoPermitido);
	}

	/**
	 * main criado apra testar a classe.
	 * 
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
				new Funcao(new BigDecimal[] { new BigDecimal(0), new BigDecimal(1) }, new BigDecimal(3), new Integer(3),
						Boolean.FALSE) };

		Map<Integer, BigDecimal[]> linhas = new HashMap<Integer, BigDecimal[]>();

		linhas.put(funcaoOtima.getVariavelAuxiliar(), funcaoOtima
				.concatena(new BigDecimal(funcaoOtima.getVariavelAuxiliar()), funcaoOtima.getVariaveisLivres()));

		for (int i = 0; i < funcoes.length; i++) {
			funcoes[i] = funcoes[i].transformaFuncao();
			linhas.put(funcoes[i].getVariavelAuxiliar(),
					funcoes[i].concatena(funcoes[i].getResultado(), funcoes[i].getVariaveisLivres()));
		}

		TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);

		System.out.println("\n\n\n------------------------");

		System.out.println(tabela.toString());

		System.out.println("\n\n\n------------------------");

		VenttselSimplex.executaPrimeiraFase(tabela);

		VenttselSimplex.executaAlgoritmoDaTroca(2, 1, tabela);

		System.out.println("\n\n\n------------------------");

		System.out.println(tabela.toString());
	}
}
