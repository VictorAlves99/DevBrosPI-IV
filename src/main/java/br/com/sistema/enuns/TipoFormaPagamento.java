package br.com.sistema.enuns;

public enum TipoFormaPagamento {
	
	NaoDefinida("Não Definida"),
	
	Boleto("Boleto"),
	
	Credito("Crédito"),
	
	Debito("Débito"),
	
	PayPal("PayPal"),
	
	PagSeguro("PagSeguro");

	private String descricao;
	
	private TipoFormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
