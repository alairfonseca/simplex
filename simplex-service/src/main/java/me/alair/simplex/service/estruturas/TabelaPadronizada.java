package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

import me.alair.simplex.service.estruturas.trocautils.AlgoritmoTrocaUtils;

public class TabelaPadronizada {

	private int[] variaveisBasicas;
	private int[] variaveisNaoBasicas;
	private CelulaTabela[][] matriz;

	// coluna 0 da tabela padronizada representa os membros livres da tabela.
	public static final int COLUNA_MEMBROS_LIVRES = 0;
	public static final int LINHA_FUNCAO_OTIMA = 0;

	/**
	 * construtor da classe TabelaPadronizada.
	 * 
	 * @param funcoes
	 */
	public TabelaPadronizada(Map<Integer, BigDecimal[]> funcoes, int qtdVariaveisNaoBasicas) {
		// inicializando variaveis basicas e nao basicas.
		variaveisBasicas = new int[funcoes.size()];
		// linha 0 da matriz representa a funcao otima.
		variaveisBasicas[0] = LINHA_FUNCAO_OTIMA;
		/*
		 * preenche o array que representa a coluna 'variaveis basicas' da
		 * matriz com os valores extraidos das funcoes de restricao. ex. (para a
		 * funcao restricao 4x1 + 2x2 + x4 = 16 o valor a ser inserido no array
		 * seria 'x4', neste caso 4).
		 */
		int p = 0;
		for (Map.Entry<Integer, BigDecimal[]> pair : funcoes.entrySet()) {
			variaveisBasicas[p] = pair.getKey();
			p++;
		}

		/*
		 * array que representa a linha variaveis nao basicas na tabela
		 * padronizada, este array tem seu tamanho definido pela quantidade de
		 * variaveis + 1 (mais um), por conta do membro livre.
		 */
		variaveisNaoBasicas = new int[qtdVariaveisNaoBasicas];
		for (int i = 0; i < variaveisNaoBasicas.length; i++) {
			variaveisNaoBasicas[i] = i;
		}
		// inicializa a matriz.
		matriz = new CelulaTabela[funcoes.size()][qtdVariaveisNaoBasicas];
		// preenche a matriz com os valores enviados na lista de funcoes.
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				// preenche a matriz com as funcoes passadas como parametro.
				matriz[i][j] = new CelulaTabela(funcoes.get(variaveisBasicas[i])[j], null);
			}
		}
	}

	/**
	 * construtor padrao.
	 */
	public TabelaPadronizada() {

	}

	/**
	 * operacao um da primeira fase do metodo simplex.
	 */
	public int primeiraFaseOperacaoUm() {
		/*
		 * retorno com valor -1 pois esse valor sera alterado caso encontrarmos
		 * o elemento procurado, se nao o encontrarmos o valor permanecera -1 e
		 * isso quer dizer que ele nao existe.
		 */
		int linhaPermissivel = -1;
		// percorre as linhas da coluna de membros livres.
		// valor inicial de i = 1 pois devemos desconsiderar a linha que
		// representa a funcao otima.
		for (int i = 1; i < matriz.length; i++) {
			// verifica se o membro livre é negativo.
			if (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().compareTo(BigDecimal.ZERO) < 0) {
				/*
				 * atribui a coluna onde foi encontrado o membro livre negativo
				 * a variavel de retorno.
				 */
				linhaPermissivel = i;
				break;
			}
		}

		return linhaPermissivel;
	}

	/**
	 * operacao dois da primeira fase do metodo simplex.
	 */
	public int primeiraFaseOperacaoDois(int linha) {
		/*
		 * coluna permissivel com valor inicial de -1 pois caso nao encontre uma
		 * coluna permissivel nessa linha, este valor nao sera alterado e isso
		 * quer dizer que o elemento procurado nao existe.
		 */
		int colunaPermitida = -1;
		// j comeca com 1 pois a coluna 0 corresponde aos membros livres.
		for (int j = 1; j < matriz[1].length; j++) {
			if (matriz[linha][j].getCelulaSuperior().compareTo(BigDecimal.ZERO) < 0) {
				colunaPermitida = j;
				break;
			}
		}

		return colunaPermitida;
	}

	public int primeiraFaseOperacaoTres(int colunaPermitida) {
		BigDecimal menorQuociente = new BigDecimal(Integer.MAX_VALUE);
		BigDecimal quocienteAtual = new BigDecimal(0);
		// linha permitida com valor inicial de -1 pois caso nao encontremos
		// quociente possivel isso quer dizer que essa linha nao existe.
		int linhaPermitida = -1;
		/*
		 * percorremos as linhas da coluna permitida para encontrar o menor
		 * quociente entre os membros livres, este quociente representara o
		 * elemento permitido. percorremos a partir do 1 pois a linha 0
		 * representa a funcao otima.
		 */
		try {
			for (int i = 1; i < matriz.length; i++) {
				// numerador.
				BigDecimal membroLivreSCS = matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior();
				// denomidador.
				BigDecimal colunaPermitidaSCS = matriz[i][colunaPermitida].getCelulaSuperior();
				/**
				 * Somente será identificado como quociente válido aquela fração
				 * que possuir numerador e denominador com o mesmo sinal e
				 * denominador maior que zero.
				 */
				if (AlgoritmoTrocaUtils.comparaSinal(membroLivreSCS, colunaPermitidaSCS)
						&& colunaPermitidaSCS.compareTo(BigDecimal.ZERO) > 0) {
					quocienteAtual = (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior()
							.divide(matriz[i][colunaPermitida].getCelulaSuperior(), 4, RoundingMode.HALF_UP));

					System.out.println("quociente = "
							+ matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().toPlainString() + " / "
							+ matriz[i][colunaPermitida].getCelulaSuperior().toPlainString() + " = " + quocienteAtual);

					if (quocienteAtual.compareTo(menorQuociente) < 0) {
						menorQuociente = quocienteAtual;
						linhaPermitida = i;
						matriz[i][colunaPermitida].setElementoPermitido(true);
						System.out.println("SETANDO O ELEMENTO PERMITIDO : " + matriz[i][colunaPermitida].toString());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("impossivel dividir por zero.");
		}

		return linhaPermitida;
	}

	/**
	 * Na linha F(x) procuramos um elemento positivo (não consideramos o membro
	 * livre).
	 */
	public int segundaFaseOperacaoUm() {
		int colunaPermissiva = -1;
		// j = 1 pois nao devemos considerar o membro livre.
		for (int j = 1; j < matriz[1].length; j++) {
			if (matriz[LINHA_FUNCAO_OTIMA][j].getCelulaSuperior().compareTo(BigDecimal.ZERO) > 0) {
				colunaPermissiva = j;
			}
		}

		return colunaPermissiva;
	}

	/**
	 * Na coluna permitida, correspondente ao elemento positivo escolhido,
	 * procuramos o elemento positivo fora da linha F(x).
	 */
	public int segundaFaseOperacaoDois(int colunaPermitida) {
		int linhaPermissivel = -1;

		for (int i = 1; i < matriz.length; i++) {
			if (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().compareTo(BigDecimal.ZERO) > 0) {
				linhaPermissivel = i;
			}
		}

		return linhaPermissivel;
	}

	/**
	 * Busca-se a linha permitida a partir da identificação do Elemento
	 * Permitido (EP) que possuir o menor quociente entre os membros livres que
	 * representam as variáveis básicas (VB).
	 * 
	 * @return
	 */
	public int segundaFaseOperacaoTres(int colunaPermitida) {
		BigDecimal menorQuociente = new BigDecimal(Integer.MAX_VALUE);
		BigDecimal quocienteAtual = new BigDecimal(0);
		// linha permitida com valor inicial de -1 pois caso nao encontremos
		// quociente possivel isso quer dizer que essa linha nao existe.
		int linhaPermitida = -1;
		/*
		 * percorremos as linhas da coluna permitida para encontrar o menor
		 * quociente entre os membros livres, este quociente representara o
		 * elemento permitido. percorremos a partir do 1 pois a linha 0
		 * representa a funcao otima.
		 */
		try {
			for (int i = 1; i < matriz.length; i++) {
				// numerador.
				BigDecimal membroLivreSCS = matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior();
				// denomidador.
				BigDecimal colunaPermitidaSCS = matriz[i][colunaPermitida].getCelulaSuperior();
				/**
				 * Somente será identificado como quociente válido aquela fração
				 * que possuir numerador e denominador com o mesmo sinal e
				 * denominador maior que zero.
				 */
				if (AlgoritmoTrocaUtils.comparaSinal(membroLivreSCS, colunaPermitidaSCS)
						&& colunaPermitidaSCS.compareTo(BigDecimal.ZERO) > 0) {
					quocienteAtual = (membroLivreSCS.divide(colunaPermitidaSCS, 4, RoundingMode.HALF_UP));

					quocienteAtual = (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior()
							.divide(matriz[i][colunaPermitida].getCelulaSuperior(), 4, RoundingMode.HALF_UP));
					if (quocienteAtual.compareTo(BigDecimal.ZERO) > 0 && quocienteAtual.compareTo(menorQuociente) < 0) {
						menorQuociente = quocienteAtual;
						linhaPermitida = i;
						matriz[i][colunaPermitida].setElementoPermitido(true);
						System.out.println("SETANDO O ELEMENTO PERMITIDO : " + matriz[i][colunaPermitida].toString());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("impossivel dividir por zero.");
		}

		return linhaPermitida;
	}

	/**
	 * multiplica toda a linha permitida pelo inverso do elemento permitido e
	 * preenche suas respectivas celulas inferiores.
	 * 
	 * @param tabelaPadronizada
	 */
	public void multiplicaTodaALinhaPeloEPInverso(int linhaPermitida, BigDecimal inversoElementoPermitido) {
		CelulaTabela celula;
		for (int j = 0; j < matriz[1].length; j++) {
			celula = matriz[linhaPermitida][j];

			if (celula.getCelulaInferior() == null) {
				celula.setCelulaInferior(celula.getCelulaSuperior().stripTrailingZeros()
						.multiply(inversoElementoPermitido.stripTrailingZeros()).setScale(4, RoundingMode.HALF_UP));
			}
		}
	}

	/**
	 * multiplica toda a coluna permitida pelo - (inverso do elemento permitido)
	 * e preenche suas respectivas celulas inferiores.
	 * 
	 * @param linhaPermitida
	 * @param inversoElementoPermitido
	 */
	public void multiplicaTodaAColunaPeloEPInverso(int colunaPermitida, BigDecimal inversoElementoPermitido) {
		inversoElementoPermitido = inversoElementoPermitido
				.multiply(new BigDecimal(-1).setScale(4, RoundingMode.HALF_UP));
		CelulaTabela celula;
		for (int i = 0; i < matriz.length; i++) {
			celula = matriz[i][colunaPermitida];

			if (celula.getCelulaInferior() == null) {
				celula.setCelulaInferior(celula.getCelulaSuperior().stripTrailingZeros()
						.multiply(inversoElementoPermitido.stripTrailingZeros()).setScale(4, RoundingMode.HALF_UP));
			}
		}
	}

	/**
	 * marca todas as subcélulas superiores (SCS) da Linha Permitida.
	 */
	public void marcaSubCelulasSuperioresDaLinhaPermitida(int linhaPermitida) {
		CelulaTabela celula;
		for (int j = 0; j < matriz[1].length; j++) {
			celula = matriz[linhaPermitida][j];

			celula.setCelulaSuperiorMarcada(Boolean.TRUE);
		}
	}

	/**
	 * marca todas as sub-células Inferiores (SCI) da Coluna Permitida.
	 */
	public void marcaSubCelulasInferioresDaColunaPermitida(int colunaPermitida) {
		CelulaTabela celula;
		for (int i = 0; i < matriz.length; i++) {
			celula = matriz[i][colunaPermitida];

			celula.setCelulaInferiorMarcada(Boolean.TRUE);
		}
	}

	/**
	 * Nas (SCI) vazias, multiplica-se a (SCS) marcada em sua respectiva coluna
	 * com a (SCI) marcada de sua respectiva linha.
	 */
	public void multiplicaSCSMarcadaComSCI() {
		CelulaTabela celula;
		BigDecimal SCSMarcada;
		BigDecimal SCIMarcada;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				celula = matriz[i][j];
				if (celula.getCelulaInferior() == null) {
					SCSMarcada = buscaSCSMarcada(j);
					SCIMarcada = buscaSCIMarcada(i);

					celula.setCelulaInferior(SCSMarcada.stripTrailingZeros().multiply(SCIMarcada.stripTrailingZeros())
							.setScale(4, RoundingMode.HALF_UP));
					System.out.println(
							SCSMarcada.toPlainString() + " x " + SCIMarcada.toPlainString() + " = " + SCSMarcada
									.stripTrailingZeros().multiply(SCIMarcada.stripTrailingZeros()).toPlainString());
				}
			}
		}
	}

	/**
	 * encontra a celula superior marcada na coluna passada como parametro.
	 * 
	 * @param coluna
	 * @return
	 */
	private BigDecimal buscaSCSMarcada(int coluna) {
		CelulaTabela celula;
		BigDecimal retorno = new BigDecimal(0);
		for (int i = 0; i < matriz.length; i++) {
			celula = matriz[i][coluna];
			if (celula.isCelulaSuperiorMarcada()) {
				retorno = celula.getCelulaSuperior();
			}
		}

		return retorno;
	}

	/**
	 * Reescreva a tabela trocando de posição a variável não básica com a
	 * variável básica, ambas definidas como "Permitidas" na tabela anterior.
	 * 
	 * @param linha
	 * @param coluna
	 * @return
	 */
	public TabelaPadronizada reescreveTabela(int linha, int coluna) {
		TabelaPadronizada tabelaReescrita = new TabelaPadronizada();
		tabelaReescrita.setMatriz(matriz);
		int variavelBasica = variaveisBasicas[linha];
		int variavelNaoBasica = variaveisNaoBasicas[coluna];

		int[] variaveisBasicasReescritas = variaveisBasicas;
		int[] variaveisNaoBasicasReescritas = variaveisNaoBasicas;

		variaveisBasicasReescritas[linha] = variavelNaoBasica;
		variaveisNaoBasicasReescritas[coluna] = variavelBasica;

		tabelaReescrita.setVariaveisBasicas(variaveisBasicasReescritas);
		tabelaReescrita.setVariaveisNaoBasicas(variaveisNaoBasicasReescritas);

		System.out.println("VARIAVEIS BASICAS");
		for (int i = 0; i < variaveisBasicasReescritas.length; i++) {
			System.out.print(variaveisBasicasReescritas[i] + " ");
		}

		System.out.println("\n");

		System.out.println("VARIAVEIS NAO BASICAS");
		for (int i = 0; i < variaveisNaoBasicasReescritas.length; i++) {
			System.out.print(variaveisNaoBasicasReescritas[i] + " ");
		}

		System.out.println("\n");

		return tabelaReescrita;
	}

	/**
	 * Todas as (SCI) da Linha e Coluna Permitida da tabela original deverão ser
	 * copiadas para suas respectivas (SCS) da nova tabela.
	 * 
	 * @param tabelaReescrita
	 * @param linhaPermitida
	 * @param colunaPermitida
	 * @return
	 */
	public void copiaSCIsParaSCSs(int linhaPermitida, int colunaPermitida) {
		CelulaTabela[][] matrizReescrita = new CelulaTabela[matriz.length][matriz[1].length];

		limpaMatriz(matrizReescrita);

		for (int j = 0; j < matriz[1].length; j++) {
			matrizReescrita[linhaPermitida][j].setCelulaSuperior(matriz[linhaPermitida][j].getCelulaInferior());
		}

		for (int i = 0; i < matriz.length; i++) {
			matrizReescrita[i][colunaPermitida].setCelulaSuperior(matriz[i][colunaPermitida].getCelulaInferior());
		}

		matriz = matrizReescrita;
	}

	/**
	 * Somam-se as (SCI) com as (SCS) das demais células restantes da tabela
	 * original e seu resultado deverá ser copiado para sua respectiva (SCS) da
	 * nova tabela.
	 */
	public void somaSCIComSCSLinhasColunasNaoPermitidas(TabelaPadronizada tabelaPadronizada, int linhaPermitida,
			int colunaPermitida) {
		CelulaTabela[][] matrizPadronizada = tabelaPadronizada.getMatriz();
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[1].length; j++) {
				if (i != linhaPermitida && j != colunaPermitida) {
					matriz[i][j].setCelulaSuperior(matrizPadronizada[i][j].getCelulaSuperior().stripTrailingZeros()
							.add(matrizPadronizada[i][j].getCelulaInferior().stripTrailingZeros()));
				}
			}
		}

	}

	/**
	 * Se após os passos anteriores ainda houver valor negativo na coluna (ML)
	 * (exceto na célula da linha que representa a Função Objetivo).
	 * 
	 * @return
	 */
	public boolean buscaValorNegativoColunaML() {
		boolean retorno = false;
		// i com valor inicial de 1 pois devemos desconsiderar a celula da linha
		// que representa a funcao objetivo.
		for (int i = 1; i < matriz.length; i++) {
			if (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().compareTo(BigDecimal.ZERO) < 0) {
				retorno = true;
				break;
			}
		}

		return retorno;
	}

	private void limpaMatriz(CelulaTabela[][] matrizReescrita) {
		for (int i = 0; i < matrizReescrita.length; i++) {
			for (int j = 0; j < matrizReescrita[1].length; j++) {
				matrizReescrita[i][j] = new CelulaTabela();
			}
		}
	}

	/**
	 * encontra a celula inferior marcada na linha passada como parametro.
	 * 
	 * @param coluna
	 * @return
	 */
	private BigDecimal buscaSCIMarcada(int linha) {
		CelulaTabela celula;
		BigDecimal retorno = new BigDecimal(0);
		for (int j = 0; j < matriz[1].length; j++) {
			celula = matriz[linha][j];
			if (celula.isCelulaInferiorMarcada()) {
				retorno = celula.getCelulaInferior();
			}
		}

		return retorno;
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
	 * metodo extrai da tabela os valores das variaveis, formata os valores e
	 * retorna o resultado.
	 * 
	 * @return
	 */
	public String resultadoToString() {
		String retorno = "";
		String variavelResultado = "";
		String valorZ = "";

		BigDecimal resultadoZ = matriz[0][COLUNA_MEMBROS_LIVRES].getCelulaSuperior();

		if (resultadoZ.compareTo(BigDecimal.ZERO) < 0) {
			resultadoZ = resultadoZ.multiply(new BigDecimal(-1)).setScale(2, RoundingMode.HALF_UP);
		}

		valorZ = "Z = " + resultadoZ.toString() + "; ";

		for (int i = 1; i < matriz.length; i++) {
			variavelResultado = matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().toString();
			retorno += "x" + variaveisBasicas[i] + " = " + variavelResultado + "; ";
		}

		return valorZ + retorno;

	}

	public CelulaTabela[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(CelulaTabela[][] matriz) {
		this.matriz = matriz;
	}

	public int[] getVariaveisBasicas() {
		return variaveisBasicas;
	}

	public void setVariaveisBasicas(int[] variaveisBasicas) {
		this.variaveisBasicas = variaveisBasicas;
	}

	public int[] getVariaveisNaoBasicas() {
		return variaveisNaoBasicas;
	}

	public void setVariaveisNaoBasicas(int[] variaveisNaoBasicas) {
		this.variaveisNaoBasicas = variaveisNaoBasicas;
	}

	/**
	 * main criado apra testar a classe.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Map<Integer, BigDecimal[]> linhas = new HashMap<Integer,
		// BigDecimal[]>();
		// BigDecimal[] vaiaveisBasicas0 = new BigDecimal[] { new
		// BigDecimal(0.0), new BigDecimal(80.0),
		// new BigDecimal(60.0) };
		// BigDecimal[] vaiaveisBasicas1 = new BigDecimal[] { new
		// BigDecimal(-24.0), new BigDecimal(-4.0),
		// new BigDecimal(-6.0) };
		// BigDecimal[] vaiaveisBasicas2 = new BigDecimal[] { new
		// BigDecimal(16.0), new BigDecimal(4.0),
		// new BigDecimal(2.0) };
		// BigDecimal[] vaiaveisBasicas3 = new BigDecimal[] { new
		// BigDecimal(3.0), new BigDecimal(0.0),
		// new BigDecimal(1.0) };
		//
		// linhas.put(0, vaiaveisBasicas0);
		// linhas.put(1, vaiaveisBasicas1);
		// linhas.put(2, vaiaveisBasicas2);
		// linhas.put(3, vaiaveisBasicas3);
		//
		// TabelaPadronizada tabela = new TabelaPadronizada(linhas, 3);
		//
		// System.out.println(tabela.toString());

		int[] variaveisBasicasReescrita = new int[] { 0, 3, 4, 5 };
		int[] variaveisNaoBasicasReescrita = new int[] { 0, 1, 2 };

		int[] a = new int[] { 0, 3, 4, 5 };
		int[] b = new int[] { 0, 1, 2 };

		variaveisBasicasReescrita[2] = b[1];
		variaveisNaoBasicasReescrita[1] = a[2];

		for (int i = 0; i < variaveisBasicasReescrita.length; i++) {
			System.out.print(variaveisBasicasReescrita[i] + " ");
		}

		System.out.println("\n");

		for (int i = 0; i < variaveisNaoBasicasReescrita.length; i++) {
			System.out.print(variaveisNaoBasicasReescrita[i] + " ");
		}
	}
}
