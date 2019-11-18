package br.com.sistema.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import br.com.sistema.util.Caracter;
import br.com.sistema.util.Lib;

public class FacesUtil {

	// private static final ThreadLocal<HashMap<String, Object>> sessionMap =
	// new ThreadLocal<HashMap<String, Object>>();

	private static Map<String, Object> getMap() {

		FacesContext contexto = FacesContext.getCurrentInstance();

		// if (contexto == null) {
		// if (sessionMap.get() == null) {
		// sessionMap.set(new HashMap<String, Object>());
		// }
		// return sessionMap.get();
		// } else
		return contexto.getExternalContext().getSessionMap();

	}

	public static void addCallbackParam(String parametro, Object valor) {
		RequestContext context = RequestContext.getCurrentInstance();
		context.getCallbackParams().put(parametro, valor);
	}

	public static String getContextPath() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request.getContextPath();
	}

	public static String getURLServidor() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		String requestURL = request.getRequestURL().toString();
		String requestURI = request.getRequestURI().toString();

		return requestURL.replace(requestURI, "") + "/";
	}

	public static Object getSessionMap(String session) {
		return getMap().get(session);
	}

	public static void setSessionMap(String session, Serializable objeto) {
		getMap().put(session, objeto);
	}

	public static void removerSession(String session) {
		getMap().remove(session);
	}

	public static void redirecionar(String pagina) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(/* ParametrosSistema.getContextPath() + "/" + */pagina);
	}

	public static String getBrowser() {

		String browser = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("User-Agent");

		return Lib.getBrowserName(browser);
	}

	public static String getSessionId() {
		return ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId();
	}

	public static void gravarCookie(String key, String valor, int expiry) {

		if (!Caracter.stringIsNullOrEmpty(valor)) {

			FacesContext context = FacesContext.getCurrentInstance();

			Cookie cookie = new Cookie(key, valor);
			cookie.setMaxAge(expiry);

			((HttpServletResponse) context.getExternalContext().getResponse()).addCookie(cookie);
		}
	}

	public static Cookie lerCookie(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		return (Cookie) context.getExternalContext().getRequestCookieMap().get(key);
	}

	public static void removerCookie(String key) {
		gravarCookie(key, "x", 0);
	}

	public static void deslogar(String redirecionarPara) throws IOException {
		FacesContext context;
		HttpSession session;
		context = FacesContext.getCurrentInstance();
		session = (HttpSession) context.getExternalContext().getSession(false);
		session.invalidate();

		if (!Caracter.stringIsNullOrEmpty(redirecionarPara))
			context.getExternalContext()
					.redirect(session.getServletContext().getContextPath() + "/" + redirecionarPara);
	}

	public static int getSessionMaxInactiveInterval() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMaxInactiveInterval();
	}

	public static void setLocale(Locale locale) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public static void setMaxInactiveInterval(int interval) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		session.setMaxInactiveInterval(interval * 60);
	}

	public static String getManagedBeanName(Class<?> clazz) {

		if (clazz.isAnnotationPresent(ManagedBean.class)) {
			ManagedBean mb = clazz.getAnnotation(ManagedBean.class);
			return mb.name();
		} else {
			return "";
		}
	}

	public static boolean existeContextoJSF() {
		return FacesContext.getCurrentInstance() != null;
	}
}
