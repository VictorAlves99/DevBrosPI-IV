package br.com.sistema.filters;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import br.com.sistema.rotinas.login.BoLogin;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.rotinas.usuario.UsuarioDAO;
import br.com.sistema.util.Caracter;

public class CotacaoFilter implements Serializable, Filter {

	private static final long serialVersionUID = -5905507639638562692L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		req.getSession(true);
		
		String usuarioKey = req.getParameter("uk");

		if (!Caracter.stringIsNullOrEmpty(usuarioKey)) {
			Usuario usuario = new UsuarioDAO().getUsuarioPorUsuarioKey(usuarioKey);
			if (usuario != null) {
				try {
					new BoLogin().logar(usuario);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}