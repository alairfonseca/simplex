package me.alair.simplex.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.alair.simplex.service.estruturas.CelulaTabela;
import me.alair.simplex.service.estruturas.TabelaPadronizada;
import me.alair.simplex.service.estruturas.trocautils.AlgoritmoTrocaUtils;
import me.alair.simplex.service.funcoes.Funcao;
import me.alair.simplex.service.funcoes.FuncaoOtima;

@Service
public class VenttselSimplex {

	/**
	 * Excuta o metodo simplex de Venttsel.
	 * 
	 * @param funcaoObjetivo
	 */
	public void executaSimplex(FuncaoOtima funcaoObjetivo) {

		// map que contem as linhas da tabela padronizada.
		Map<Integer, BigDecimal[]> linhas = new HashMap<Integer, BigDecimal[]>();

		// executa a transformacao da funcao objetivo.
		funcaoObjetivo.transformaFuncao();

		// adiciona a funcao objetivo na primeira posicao do map.
		linhas.put(funcaoObjetivo.getVariavelAuxiliar(), funcaoObjetivo
				.concatena(new BigDecimal(funcaoObjetivo.getVariavelAuxiliar()), funcaoObjetivo.getVariaveisLivres()));

		// array de funcoes que serao adicionadas ao map de linhas.
		Funcao[] funcoes = new Funcao[funcaoObjetivo.getRestricoes().size()];

		// preenche o array de funcoes.
		for (int i = 0; i < funcaoObjetivo.getRestricoes().size(); i++) {
			funcoes[i] = funcaoObjetivo.getRestricoes().get(i);
		}

		// transforma as funcoes e as adiciona ao map de linhas.
		for (int i = 0; i < funcoes.length; i++) {
			funcoes[i] = funcoes[i].transformaFuncao();
			linhas.put(funcoes[i].getVariavelAuxiliar(),
					funcoes[i].concatena(funcoes[i].getResultado(), funcoes[i].getVariaveisLivres()));
		}

		// cria a tabela padronizada.
		TabelaPadronizada tabelaPadronizada = new TabelaPadronizada(linhas, funcaoObjetivo.getQtdVariaveisNaoBasicas());

		// primeira fase do metodo simplex.
		executaPrimeiraFase(tabelaPadronizada);

	}

	/**
	 * primeira fase do metodo simplex.
	 */
	private static void executaPrimeiraFase(TabelaPadronizada tabelaPadronizada) {
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
				/*
				 * Busca-se a linha permitida a partir da identificação do
				 * Elemento Permitido (EP) que possuir o menor quociente entre
				 * os membros livres que representam as variáveis básicas (VB).
				 */
				linhaPermitida = tabelaPadronizada.primeiraFaseOperacaoTres(colunaPermitida);
				executaAlgoritmoDaTroca(linhaPermitida, colunaPermitida, tabelaPadronizada);
			} else {
				System.out.println("Solucao permissivel nao existe!");
			}

		} else {
			executaSegundaFase(tabelaPadronizada);
		}

	}

	/**
	 * executa os passos do algoritmo da troca.
	 * 
	 * @param linhaPermitida
	 * @param colunaPermitida
	 * @param tabelaPadronizada
	 */
	private static void executaAlgoritmoDaTroca(int linhaPermitida, int colunaPermitida,
			TabelaPadronizada tabelaPadronizada) {
		// objeto que sera utilizado a partir do passo sete.
		TabelaPadronizada tabelaReescrita;
		System.out.println("LINHA PERMITIDA E COLUNA PERMITIDA\n - " + linhaPermitida + " - - " + colunaPermitida);

		// PASSO UM.
		// carrega o elemento permitido.
		CelulaTabela elementoPermitido = tabelaPadronizada.getMatriz()[linhaPermitida][colunaPermitida];
		// calcula o inverso do elemento permitido.
		BigDecimal inversoElementoPermitido = AlgoritmoTrocaUtils
				.inverteElemento(elementoPermitido.getCelulaSuperior());
		// preenche a celula inferior do elemento permitido com seu inverso.
		elementoPermitido.setCelulaInferior(inversoElementoPermitido);
		System.out.println("UM");
		System.out.println(tabelaPadronizada.toString());

		// PASSO DOIS.
		// multiplica toda a linha pelo ep inverso e preenche a celula inferior.
		tabelaPadronizada.multiplicaTodaALinhaPeloEPInverso(linhaPermitida, inversoElementoPermitido);
		System.out.println("DOIS");
		System.out.println(tabelaPadronizada.toString());

		// PASSO TRES
		// multiplica toda a coluna pelo - (ep inverso) e preenche a celula
		// inferior.
		tabelaPadronizada.multiplicaTodaAColunaPeloEPInverso(colunaPermitida, inversoElementoPermitido);
		System.out.println("TRES");
		System.out.println(tabelaPadronizada.toString());

		// PASSO QUATRO.
		tabelaPadronizada.marcaSubCelulasSuperioresDaLinhaPermitida(linhaPermitida);
		tabelaPadronizada.marcaSubCelulasInferioresDaColunaPermitida(colunaPermitida);
		System.out.println("QUATRO");
		System.out.println(tabelaPadronizada.toString());

		// PASSO CINCO.
		// nas (SCI) vazias, multiplica-se a (SCS) marcada em sua respectiva
		// coluna com a (SCI) marcada de sua respectiva linha.
		tabelaPadronizada.multiplicaSCSMarcadaComSCI();
		System.out.println("CINCO");
		System.out.println(tabelaPadronizada.toString());

		// PASSO SETE.
		// reescrever tabela.
		tabelaReescrita = tabelaPadronizada.reescreveTabela(linhaPermitida, colunaPermitida);
		System.out.println("SETE");
		System.out.println(tabelaReescrita.toString());

		// PASSO OITO.
		// copiar SCIs da tabela original para SCSs da tabela reescrita.
		tabelaReescrita.copiaSCIsParaSCSs(linhaPermitida, colunaPermitida);
		System.out.println("OITO");
		System.out.println(tabelaReescrita.toString());

		// PASSO NOVE.
		// soma os SCIs com o SCS da tabela padronizada transferindo-os para a
		// tabela reescrita.
		tabelaReescrita.somaSCIComSCSLinhasColunasNaoPermitidas(tabelaPadronizada, linhaPermitida, colunaPermitida);
		System.out.println("NOVE");
		System.out.println(tabelaReescrita.toString());

		// PASSO DEZ.
		/*
		 * verifica se ainda existe valor negativo na coluna dos membros livres,
		 * desconsiderando a primeira linha (linha da funcao otima), caso haja,
		 * chame novamente o algoritmo de troca.
		 */
		if (tabelaReescrita.buscaValorNegativoColunaML()) {
			executaPrimeiraFase(tabelaReescrita);
		} else {
			executaSegundaFase(tabelaReescrita);
		}

	}

	/**
	 * Segunda fase do metodo simplex.
	 * 
	 * @param tabelaPadronizada
	 */
	private static void executaSegundaFase(TabelaPadronizada tabelaPadronizada) {
		int colunaPermitida = 0;
		int linhaPermissivel = 0;
		int linhaPermitida = 0;

		// procuramos um elemento positivo na linha F(x).
		colunaPermitida = tabelaPadronizada.segundaFaseOperacaoUm();

		// Se o elemento positivo existe, então a coluna, onde está esse
		// elemento, é escolhida como permissível.
		if (colunaPermitida > 0) {
			// executa a operacao dois da segunda fase.
			linhaPermissivel = tabelaPadronizada.segundaFaseOperacaoDois(colunaPermitida);

			if (linhaPermissivel > 0) {
				/*
				 * Busca-se a linha permitida a partir da identificação do
				 * Elemento Permitido (EP) que possuir o menor quociente entre
				 * os membros livres que representam as variáveis básicas (VB).
				 */
				linhaPermitida = tabelaPadronizada.segundaFaseOperacaoTres(colunaPermitida);
				executaAlgoritmoDaTroca(linhaPermitida, colunaPermitida, tabelaPadronizada);
			} else {
				System.out.println("Solucao otima nao existe!");
			}

		} else {
			System.out.println("Solucao otima encontrada!");
		}

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
						new Integer(3), Boolean.TRUE),
				new Funcao(new BigDecimal[] { new BigDecimal(4), new BigDecimal(2) }, new BigDecimal(16),
						new Integer(4), Boolean.FALSE),
				new Funcao(new BigDecimal[] { new BigDecimal(0), new BigDecimal(1) }, new BigDecimal(3), new Integer(5),
						Boolean.FALSE) };

		Map<Integer, BigDecimal[]> linhas = new HashMap<Integer, BigDecimal[]>();

		funcaoOtima.transformaFuncao();

		linhas.put(funcaoOtima.getVariavelAuxiliar(), funcaoOtima
				.concatena(new BigDecimal(funcaoOtima.getVariavelAuxiliar()), funcaoOtima.getVariaveisLivres()));

		for (int i = 0; i < funcoes.length; i++) {
			funcoes[i] = funcoes[i].transformaFuncao();
			linhas.put(funcoes[i].getVariavelAuxiliar(),
					funcoes[i].concatena(funcoes[i].getResultado(), funcoes[i].getVariaveisLivres()));
		}

		// FIXME
		// enviar quantidade de variaveis
		TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);

		// System.out.println("\n\n\n------------------------");
		//
		// System.out.println(tabela.toString());
		//
		// System.out.println("\n\n\n------------------------");

		executaPrimeiraFase(tabela);
	}
}
