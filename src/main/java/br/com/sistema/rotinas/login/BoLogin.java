package br.com.sistema.rotinas.login;

import javax.faces.context.FacesContext;

import br.com.sistema.enuns.TipoUsuario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.rotinas.usuario.UsuarioDAO;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.Lib;
import br.com.sistema.util.ParametrosSistema;

public class BoLogin {

	public void redirecionarParaMenu() throws Exception {
		if(ParametrosSistema.getUsuarioLogado().getTipoUsuario() == TipoUsuario.Cliente) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Sistema/Home" + Constantes.EXTENSAO_JSF);
		}else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Sistema/Dashboard" + Constantes.EXTENSAO_JSF);
		}
	}
	

	private Usuario autenticarUsuario(String email, String senha) throws Exception, MensagemException {

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.autenticarUsuario(email, senha);
		
		return usuario;
	}

	private void prepararSession(Usuario usuario) throws Exception {
		ParametrosSistema.setUsuarioLogado(usuario);
		ParametrosSistema.setDataDoSistema(Lib.getDataAtual());
	}

	public void logar(Usuario usuario) throws Exception {
		prepararSession(usuario);		
		redirecionarParaMenu();
			
	}

	public void logar(String email, String senha) throws Exception {
		Usuario usuario = autenticarUsuario(email, senha);
		if(usuario != null) {
			this.logar(usuario);
		}else {
			throw new MensagemException(Mensagens.getMensagem("Esse usuário não existe!"));
		}
	}
}
