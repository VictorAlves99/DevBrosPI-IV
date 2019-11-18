package br.com.sistema.rotinas.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UsuarioEndereco implements Serializable {

	private static final long serialVersionUID = 568661782368920559L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id", insertable = true, updatable = true)
	private Usuario usuario;

	@Column(name = "endereco", nullable = false, length = 100)
	private String endereco;

	@Column(name = "precoFrete", nullable = false, length = 100)
	private double precoFrete;

	public UsuarioEndereco() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public double getPrecoFrete() {
		return precoFrete;
	}

	public void setPrecoFrete(double precoFrete) {
		this.precoFrete = precoFrete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
