package br.com.sistema.enuns;

public enum TipoCondicaoPagamento {
	AVista("A vista"), 
	x2("2x"), 
	x3("3x"), 
	x4("4x"), 
	x5("5x"), 
	x6("6x"), 
	x7("7x"), 
	x8("8x"), 
	x9("9x"), 
	x10("10x"), 
	x11("11x"), 
	x12("12x");

	private String descricao;

	private TipoCondicaoPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}