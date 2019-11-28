package br.com.sistema.enuns;

public enum TipoStatusVenda {
	
	AguardandoPagamento("Aguardando Pagamento"), 
	PagamentoRejeitado("Pagamento Rejeitado"), 
	PagamentoSucesso("Pagamento Efetuado"), 
	AguardandoRetirada("Aguardando Retirada"), 
	EmTransito("Em Transito"), 
	Entregue("Entregue");

	private String descricao;

	private TipoStatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
