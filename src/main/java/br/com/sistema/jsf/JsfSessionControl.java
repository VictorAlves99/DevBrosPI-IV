package br.com.sistema.jsf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class JsfSessionControl {

	private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	public static void fecharSessoesDuplicadas(List<String> sessoesAbertasParaOUsuario) {

		for (String sessionId : sessoesAbertasParaOUsuario) {
			HttpSession httpSession = sessions.get(sessionId);

			try {
				if (httpSession != null)
					httpSession.invalidate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				sessions.remove(sessionId);
			}
		}
	}

	public static Map<String, HttpSession> getSessions() {
		return sessions;
	}

	public static void setSessions(Map<String, HttpSession> sessions) {
		JsfSessionControl.sessions = sessions;
	}

}
