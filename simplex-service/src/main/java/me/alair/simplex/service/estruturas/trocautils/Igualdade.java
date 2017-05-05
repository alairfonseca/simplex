package me.alair.simplex.service.estruturas.trocautils;

public enum Igualdade {
	MENOR("<"),
	MENOR_OU_IGUAL("<="),
	IGUAL("="),
	MAIOR(">"),
	MAIOR_OU_IGUAL(">=");
	
	private String representacao;
	
	private Igualdade(String representacao) {
		
	}

	public String getRepresentacao() {
		return representacao;
	}	

	public void setRepresentacao(String representacao) {
		this.representacao = representacao;
	}
}
