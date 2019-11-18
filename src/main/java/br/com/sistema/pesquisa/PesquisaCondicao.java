package br.com.sistema.pesquisa;

import java.io.Serializable;

public class PesquisaCondicao implements Serializable {

	private static final long serialVersionUID = -5313347580785823251L;

	private String alias;
	private String campo;
	private Object valor;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
