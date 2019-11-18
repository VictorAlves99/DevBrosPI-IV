package br.com.sistema.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sistema.rotinas.usuario.UsuarioDAO;
import br.com.sistema.rotinas.usuario.UsuarioEndereco;
import br.com.sistema.util.ParametrosSistema;

@FacesConverter(value = "enderecoConverter")
public class EnderecoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
		UsuarioDAO dao = new UsuarioDAO();
		UsuarioEndereco end = dao.getEnderecoPorString(string, ParametrosSistema.getUsuarioLogado());
		return end;
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object o) {
		UsuarioEndereco end = new UsuarioEndereco();
		end = (UsuarioEndereco) o;
		return end.getEndereco();
	}

}