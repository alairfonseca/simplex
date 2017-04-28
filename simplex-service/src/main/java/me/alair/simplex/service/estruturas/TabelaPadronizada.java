package me.alair.simplex.service.estruturas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
	 * @param qtdVariaveisBasicas
	 */
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
				matriz[i][j] = new CelulaTabela(funcoes.get(i)[j], null);
			}
		}
	}

	/**
	 * operacao um da primeira fase do metodo simplex.
	 */
	public int primeiraFaseOperacaoUm() {
		// retorno com valor -1 pois esse valor sera alterado caso encontrarmos
		// o elemento procurado, se nao o encontrarmos o valor permanecera -1 e
		// isso quer dizer que ele nao existe.
		int linhaPermissivel = -1;
		// percorre as linhas da coluna de membros livres.
		for (int i = 0; i < matriz[1].length; i++) {
			// verifica se o membro livre é negativo.
			if (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior().compareTo(BigDecimal.ZERO) < 0) {
				// atribui a coluna onde foi encontrado o membro livre negativo
				// a
				// variavel de retorn.
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
		// coluna permissivel com valor inicial de -1 pois caso nao encontre uma
		// coluna permissivel nessa linha, este valor nao sera alterado e isso
		// quer dizer que o elemento procurado nao existe.
		int colunaPermitida = -1;
		// j comeca com 1 pois a coluna 0 corresponde aos membros livres.
		for (int j = 1; j < matriz.length; j++) {
			if (matriz[linha][j].getCelulaSuperior().compareTo(BigDecimal.ZERO) < 0) {
				colunaPermitida = j;
				break;
			}
		}

		return colunaPermitida;
	}

	public int primeiraFaseOperacaoTres(int colunaPermitida) {
		BigDecimal quocientePermitido = new BigDecimal(0);
		BigDecimal menorQuociente = new BigDecimal(0);
		// linha permitida com valor inicial de -1 pois caso nao encontremos
		// quociente possivel isso quer dizer que essa linha nao existe.
		int linhaPermitida = -1;
		// percorremos as linhas da coluna permitida para encontrar o menor
		// quociente entre os membros livres, este quociente representara o
		// elemento permitido.
		// percorremos a partir do 1 pois a linha 0 representa a funcao otima.
		try {
			for (int i = 1; i < matriz.length; i++) {
				menorQuociente = (matriz[i][COLUNA_MEMBROS_LIVRES].getCelulaSuperior()
						.divide(matriz[i][colunaPermitida].getCelulaSuperior()));
				if (quocientePermitido.compareTo(menorQuociente) < 0) {
					quocientePermitido = menorQuociente;
					linhaPermitida = i;
				} else if (menorQuociente.compareTo(quocientePermitido) < 0) {
					quocientePermitido = menorQuociente;
					linhaPermitida = i;
					matriz[i][colunaPermitida].setElementoPermitido(true);
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
				celula.setCelulaInferior(celula.getCelulaSuperior().multiply(inversoElementoPermitido));
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
		inversoElementoPermitido = inversoElementoPermitido.multiply(new BigDecimal(-1));
		CelulaTabela celula;
		for (int i = 0; i < matriz.length; i++) {
			celula = matriz[i][colunaPermitida];

			if (celula.getCelulaInferior() == null) {
				celula.setCelulaInferior(celula.getCelulaSuperior().multiply(inversoElementoPermitido));
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
	 * 
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

	public CelulaTabela[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(CelulaTabela[][] matriz) {
		this.matriz = matriz;
	}

}
