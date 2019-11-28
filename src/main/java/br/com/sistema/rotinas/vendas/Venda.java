package br.com.sistema.rotinas.vendas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.sistema.enuns.TipoCondicaoPagamento;
import br.com.sistema.enuns.TipoFormaPagamento;
import br.com.sistema.enuns.TipoStatusVenda;
import br.com.sistema.rotinas.produtos.CarrinhoItem;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.rotinas.usuario.UsuarioEndereco;
import br.com.sistema.util.Caracter;

@Entity
public class Venda implements Serializable {

	private static final long serialVersionUID = -4627023780267375102L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	private Usuario cliente;

	@Column(name = "numeroDaVenda", nullable = false, length = 30)
	private String numeroDaVenda;

	@Column(name = "formaPagamento", nullable = false, length = 300)
	private TipoFormaPagamento formaPagamento;

	@Column(name = "parcelas", length = 300)
	private TipoCondicaoPagamento parcelas;

	@Column(name = "dataVenda", nullable = false, length = 30)
	private Date dataVenda;

	@Column(name = "precoTotal", nullable = false, length = 30)
	private double precoTotal;
	
	@Enumerated
	@Column(name = "status", nullable = false)
	private TipoStatusVenda status;

	@ManyToOne(optional = false)
	private UsuarioEndereco endereco;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "venda_id", nullable = true)
	private List<CarrinhoItem> itens;

	public Venda() {
		numeroDaVenda = Caracter.getRandomInt(30);
		dataVenda = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumeroDaVenda() {
		return numeroDaVenda;
	}

	public void setNumeroDaVenda(String numeroDaVenda) {
		this.numeroDaVenda = numeroDaVenda;
	}

	public TipoFormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(TipoFormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public TipoCondicaoPagamento getParcelas() {
		return parcelas;
	}

	public void setParcelas(TipoCondicaoPagamento parcelas) {
		this.parcelas = parcelas;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public double getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(double precoTotal) {
		this.precoTotal = precoTotal;
	}

	public List<CarrinhoItem> getItens() {
		return itens;
	}

	public void setItens(List<CarrinhoItem> itens) {
		this.itens = itens;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public UsuarioEndereco getEndereco() {
		return endereco;
	}

	public void setEndereco(UsuarioEndereco endereco) {
		this.endereco = endereco;
	}

	public TipoStatusVenda getStatus() {
		return status;
	}

	public void setStatus(TipoStatusVenda status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
