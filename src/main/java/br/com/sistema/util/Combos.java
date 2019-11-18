package br.com.sistema.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.sistema.enuns.TipoCondicaoPagamento;
import br.com.sistema.enuns.TipoFormaPagamento;
import br.com.sistema.enuns.TipoPlataformaProduto;
import br.com.sistema.enuns.TipoUsuario;

public class Combos {

	public static List<SelectItem> getComboTipoPlataformaProduto() {
		ArrayList<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoPlataformaProduto type : TipoPlataformaProduto.values()) {
			lista.add(new SelectItem(type.name(), type.getDescricao()));
		}
		return lista;
	}
	
	public static List<SelectItem> getComboTipoUsuario() {
		ArrayList<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoUsuario type : TipoUsuario.values()) {
			lista.add(new SelectItem(type.name(), type.getDescricao()));
		}
		return lista;
	}
	
	public static List<SelectItem> getComboTipoFormaPagamento() {
		ArrayList<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoFormaPagamento type : TipoFormaPagamento.values()) {
			lista.add(new SelectItem(type.name(), type.getDescricao()));
		}
		return lista;
	}
	
	public static List<SelectItem> getComboTipoCondicaoPagamento() {
		ArrayList<SelectItem> lista = new ArrayList<SelectItem>();
		for (TipoCondicaoPagamento type : TipoCondicaoPagamento.values()) {
			lista.add(new SelectItem(type.name(), type.getDescricao()));
		}
		return lista;
	}

}