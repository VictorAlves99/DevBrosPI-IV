package br.com.sistema.rotinas.produtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.sistema.enuns.TipoPlataformaProduto;
import br.com.sistema.jsf.Mensagens;

@Entity
public class Produto implements Serializable {

	private static final long serialVersionUID = -4627023780267375102L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "nome", nullable = false, length = 100)
	private String nome;

	@Column(name = "descricao", nullable = false, length = 300)
	private String descricao;

	@Column(name = "codigoDeBarras", nullable = false, length = 300)
	private String codigoDeBarras;

	@Column(name = "preco", nullable = false, length = 30)
	private double preco;

	@Column(name = "quantidade", nullable = false, length = 10)
	private int quantidade;
	
	@Enumerated
	@Column(name = "plataforma", nullable = false)
	private TipoPlataformaProduto plataforma;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "produto_id", nullable = true)
	private List<PerguntasERespostas> perguntasERespostas;
	
	@Transient
	private ImagemProduto imagemPrincipal;
	
	public boolean equals(Object o) {
		if (o instanceof Produto)
			return this.getId() == ((Produto) o).getId();
		else
			return false;
	}

	public Produto() {
		this.perguntasERespostas = new ArrayList<>();
	}
	
	public Produto(String nome) {
		this.nome = nome;
	}

	public Produto(String nome, String descricao, String codigoDeBarras, double preco, int quantidade,
			TipoPlataformaProduto plataforma) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.codigoDeBarras = codigoDeBarras;
		this.preco = preco;
		this.quantidade = quantidade;
		this.plataforma = plataforma;
	}

	public TipoPlataformaProduto getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(TipoPlataformaProduto plataforma) {
		this.plataforma = plataforma;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public List<PerguntasERespostas> getPerguntasERespostas() {
		return perguntasERespostas;
	}

	public void setPerguntasERespostas(List<PerguntasERespostas> perguntasERespostas) {
		this.perguntasERespostas = perguntasERespostas;
	}

	public ImagemProduto getImagemPrincipal() {
		return imagemPrincipal;
	}

	public void setImagemPrincipal(ImagemProduto imagemPrincipal) {
		this.imagemPrincipal = imagemPrincipal;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public void adicionarProduto() {
		try {
			new ProdutosDAO().save(this);
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
