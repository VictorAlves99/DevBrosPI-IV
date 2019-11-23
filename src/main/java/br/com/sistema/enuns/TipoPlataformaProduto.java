package br.com.sistema.enuns;

public enum TipoPlataformaProduto {

	NaoDefinido("Não Definido"),
	
	CONSOLE("Console"),

	PS3("PlayStation 3"),
	
	PS4("PlayStation 4"),
	
	SWITCH("Nintendo Switch"),
	
	XBOXONE("XBOX ONE"),
	
	XBOX360("XBOX 360");

	private String descricao;

	private TipoPlataformaProduto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
