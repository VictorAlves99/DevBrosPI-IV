package br.com.sistema.rotinas.login;

import java.util.Locale;

import javax.faces.bean.ManagedBean;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateConfiguration;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.ParametrosSistema;

@ManagedBean(name = "loginBean")
public class LoginBean {

	private String email;
	private String senha;
	private boolean armazenarDados;
	private boolean hidden;
	
	private String telaDesejada;
	private String cotacaoKey;
	private String usuarioKey;

	static {
		try {
			Locale.setDefault(new Locale("pt", "br"));

			new HibernateConfiguration();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public LoginBean() {
		/*
		 * String identificacaoStr =
		 * FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap() .get("identificacao");
		 * 
		 * if (!Caracter.stringIsNullOrEmpty(identificacaoStr))
		 * this.identificacao = Long.parseLong(identificacaoStr.trim());
		 * 
		 * this.usuario =
		 * FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap().get("usuario");
		 * 
		 * this.senha = FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap().get("senha");
		 * 
		 * this.criptografado = "true"
		 * .equals(FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap().get("crip"));
		 * 
		 * this.armazenarDados = "true"
		 * .equals(FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap().get("cookies"));
		 * 
		 * this.hidden = "true"
		 * .equals(FacesContext.getCurrentInstance().getExternalContext().
		 * getRequestParameterMap().get("hidden"));
		 * 
		 * if (!Caracter.stringIsNullOrEmpty(this.senha)) {
		 * 
		 * if (criptografado) { try { this.senha = new
		 * TripleDES(Constantes.KEY_TRIPLE_DES).decrypt(this.senha.trim()); }
		 * catch (Exception e) { Log.gravarLog(e); this.senha =
		 * this.senha.trim(); } } else { this.senha = this.senha.trim(); } }
		 * 
		 * if (!Caracter.stringIsNullOrEmpty(this.usuario)) this.usuario =
		 * this.usuario.trim();
		 * 
		 * if (!Caracter.stringIsNullOrEmpty(this.senha)) this.senha =
		 * this.senha.trim();
		 * 
		 * if (identificacao != null && identificacao > 0) logar(); else {
		 * popularCampos(); }
		 */
	}

	public void logar() {
		try {

			if (Caracter.stringIsNullOrEmpty(email) || Caracter.stringIsNullOrEmpty(senha))
				throw new MensagemException(Mensagens.getMensagem("Preencha corretamente os campos para fazer login."));

			// if (this.armazenarDados) {
			// this.gravarCookies(identificacao, usuario, senha,
			// armazenarDados);
			// }
				
				new BoLogin().logar(email, senha);
				
				

		} catch (MensagemException e) {
			this.senha = "";
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Mensagens.gerarMensagemGenerica(e.getMessage());
		}
	}

	public String getTelaDesejada() {
		return telaDesejada;
	}

	public void setTelaDesejada(String telaDesejada) {
		this.telaDesejada = telaDesejada;
	}
	
	public String getCotacaoKey() {
		return cotacaoKey;
	}

	public void setCotacaoKey(String cotacaoKey) {
		this.cotacaoKey = cotacaoKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isArmazenarDados() {
		return armazenarDados;
	}

	public void setArmazenarDados(boolean armazenarDados) {
		this.armazenarDados = armazenarDados;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setUsuarioKey(String usuarioKey) {
		this.usuarioKey = usuarioKey;
		
	}
	public String getUsuarioKey() {
		return usuarioKey;
	}

	public String getUsu() {
		return ParametrosSistema.getUsuarioLogado().getNome();
	}

}
