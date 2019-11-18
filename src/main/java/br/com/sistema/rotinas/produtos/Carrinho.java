package br.com.sistema.rotinas.produtos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.sistema.rotinas.usuario.Usuario;

@Entity
public class Carrinho implements Serializable {

	private static final long serialVersionUID = -4627023780267375102L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "carrinho_id", nullable = true)
	private List<CarrinhoItem> itens;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", insertable = true, updatable = true)
	private Usuario usuario;

	public boolean equals(Object o) {
		if (o instanceof PerguntasERespostas)
			return this.getId() == ((PerguntasERespostas) o).getId();
		else
			return false;
	}

	public Carrinho() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<CarrinhoItem> getItens() {
		return itens;
	}

	public void setItens(List<CarrinhoItem> itens) {
		this.itens = itens;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
