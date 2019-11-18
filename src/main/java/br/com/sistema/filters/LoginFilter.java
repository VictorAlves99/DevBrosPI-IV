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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.sistema.util.Constantes;

public class LoginFilter implements Serializable, Filter {

	private static final long serialVersionUID = -5905507639638562692L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		Object chave = session.getAttribute(Constantes.USUARIO_AUTENTICADO);

		String url = req.getRequestURL().toString();

		if (chave != null || url.contains("Login") || url.contains("Live")) {
			chain.doFilter(request, response);
		} else {
			String endereco = req.getContextPath() + "/" + Constantes.PAGINA_LOGIN;

			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect(endereco);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}