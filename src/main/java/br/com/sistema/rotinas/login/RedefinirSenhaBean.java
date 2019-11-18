package br.com.sistema.rotinas.login;

import javax.faces.bean.ManagedBean;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.usuario.UsuarioDAO;

@ManagedBean(name = "redefinirSenhaBean")
public class RedefinirSenhaBean {

	private String email;

	public void enviar() {
		try {
			String email = this.getEmail();
	
			if(new UsuarioDAO().verificaEmail(email)) {
				Mensagens.gerarMensagemGenerica("Fique atento ao seu email e telefones, entraremos em contato para confirmação!");
			}else {
				throw new MensagemException("Esse email não está em nosso banco de dados! Verifique se colocou o email correto!");
			}
		}catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Mensagens.gerarMensagemGenerica(e.getMessage());
		}
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
