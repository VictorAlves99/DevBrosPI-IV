package br.com.sistema.rotinas.dashboard;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.login.LoginBean;
import br.com.sistema.util.Constantes;

@ManagedBean(name = "dashboardBean")
@SessionScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = -1278900802157443399L;

	public DashboardBean() {
	}

	public void deslogar() {
		try {
			FacesUtil.deslogar(Constantes.PAGINA_LOGIN);
			
			LoginBean login = new LoginBean();
			login.setCotacaoKey("");
			login.setTelaDesejada("");
			login.setUsuarioKey("");
			
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
			System.out.println(e.getMessage());
		}
	}

	
}
