package br.com.sistema.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sistema.rotinas.produtos.Produto;
import br.com.sistema.rotinas.produtos.ProdutosDAO;

@FacesConverter(value = "produtoConverter")
public class ProdutoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
		ProdutosDAO dao = new ProdutosDAO();
		Produto pro = dao.getProdutoPorDescricao(string);
		return pro;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object o) {
		Produto pro = new Produto();
		pro = (Produto) o;
		return pro.getNome();
	}

}