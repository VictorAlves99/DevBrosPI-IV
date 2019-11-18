package br.com.sistema.enuns;

public enum TipoUsuario {

	NaoDefinido("Não Definido"),
	
	Administrador("Administrador"),

	Estoquista("Estoquista"),
	
	Cliente("Cliente");

	private String descricao;

	private TipoUsuario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
