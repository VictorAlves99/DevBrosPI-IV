package br.com.sistema.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.rotinas.usuario.Usuario;

public class ParametrosSistema {

	private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	public static Object getSessionMapThreadLocal(String session) {

		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<String, Object>());
		}

		return threadLocal.get().get(session);
	}

	public static void setSessionMapThreadLocal(String session, Serializable objeto) {

		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<String, Object>());
		}

		threadLocal.get().put(session, objeto);
	}

	public static Usuario getUsuarioLogado() {
		return (Usuario) FacesUtil.getSessionMap(Constantes.USUARIO_AUTENTICADO);
	}

	public static void setUsuarioLogado(Usuario usuarioLogado) {
		FacesUtil.setSessionMap(Constantes.USUARIO_AUTENTICADO, usuarioLogado);
	}

	public static Date getDataDoSistema() {
		return (Date) FacesUtil.getSessionMap(Constantes.DATA_DO_SISTEMA);
	}

	public static Date getDataHoraSistema() {
		return Calendar.getInstance().getTime();
	}

	public static void setDataDoSistema(Date dataDoSistema) {
		FacesUtil.setSessionMap(Constantes.DATA_DO_SISTEMA, dataDoSistema);
	}

	public static Long getIdentificacao() {
		if (FacesUtil.existeContextoJSF()) {
			return (Long) FacesUtil.getSessionMap(Constantes.IDENTIFICACAO_DO_CLIENTE);
		} else {
			return (Long) getSessionMapThreadLocal(Constantes.IDENTIFICACAO_DO_CLIENTE);
		}
	}

	public static void setIdentificacao(Long identificacao) {
		if (FacesUtil.existeContextoJSF()) {
			FacesUtil.setSessionMap(Constantes.IDENTIFICACAO_DO_CLIENTE, identificacao);
		} else {
			setSessionMapThreadLocal(Constantes.IDENTIFICACAO_DO_CLIENTE, identificacao);
		}
	}

	public static String getNomeFantasiaCliente() {
		return (String) FacesUtil.getSessionMap(Constantes.NOME_FANTASIA_CLIENTE);
	}

	public static void setNomeFantasiaCliente(String cliente) {
		FacesUtil.setSessionMap(Constantes.NOME_FANTASIA_CLIENTE, cliente);
	}

	public static void setEmpresaAutenticada(int empresa) {
		FacesUtil.setSessionMap(Constantes.EMPRESA_AUTENTICADA, empresa);
	}

	public static int getEmpresaAutenticada() {
		Object objeto = FacesUtil.getSessionMap(Constantes.EMPRESA_AUTENTICADA);
		return objeto == null ? 0 : (int) objeto;
	}

}
