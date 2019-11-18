package br.com.sistema.rotinas.produtos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PerguntasERespostas implements Serializable {

	private static final long serialVersionUID = -4627023780267375102L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "pergunta", length = 100)
	private String pergunta;

	@Column(name = "resposta", length = 300)
	private String resposta;
	
	@ManyToOne
	@JoinColumn(name = "produto_id", insertable = true, updatable = true)
	private Produto produto;

	public boolean equals(Object o) {
		if (o instanceof PerguntasERespostas)
			return this.getId() == ((PerguntasERespostas) o).getId();
		else
			return false;
	}

	public PerguntasERespostas() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
