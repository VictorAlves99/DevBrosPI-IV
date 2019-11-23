package br.com.sistema.rotinas.produtos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.sistema.rotinas.vendas.Venda;

@Entity
public class CarrinhoItem implements Serializable {

	private static final long serialVersionUID = -4627023780267375102L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Produto produto;
	
	@Column(name = "quantidade", nullable = false)
	private int quantidade;
	
	@Column(name = "precoItemTotal", nullable = false)
	private double precoItemTotal;
	
	@ManyToOne
	@JoinColumn(name = "carrinho_id", insertable = true, updatable = true)
	private Carrinho carrinho;
	
	@ManyToOne
	@JoinColumn(name = "venda_id", insertable = true, updatable = true)
	private Venda venda;

	public boolean equals(Object o) {
		if (o instanceof PerguntasERespostas)
			return this.getId() == ((PerguntasERespostas) o).getId();
		else
			return false;
	}

	public CarrinhoItem() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Carrinho getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPrecoItemTotal() {
		return precoItemTotal;
	}

	public void setPrecoItemTotal(double precoItemTotal) {
		this.precoItemTotal = precoItemTotal;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
