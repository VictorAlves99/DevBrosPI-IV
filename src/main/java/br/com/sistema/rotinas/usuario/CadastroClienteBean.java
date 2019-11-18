package br.com.sistema.rotinas.usuario;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.sistema.enuns.TipoUsuario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.seguranca.TripleDES;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.Combos;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.ParametrosSistema;

@ViewScoped
@ManagedBean(name = "cadastroClienteBean")
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = -1651697338193938256L;

	private String textoPesquisa;

	private Usuario objeto;
	
	private boolean pesquisa = true;

	private String senhaDescriptografada;
	private String endereco;
	
	private List<Usuario> usuarios;

	public CadastroClienteBean() {
		
		this.objeto = new Usuario();
		this.objeto.setId(0);
		this.objeto.setDataDeCadastro(ParametrosSistema.getDataHoraSistema());
		this.objeto.setUsuarioKey(Caracter.getRandomString(30));
		
	}
	
	public void pesquisar() {
			this.setUsuarios(new UsuarioDAO().getListaParaUsuario(this.textoPesquisa));
	}
	
	public boolean rejeitaEstoquista() {
		return ParametrosSistema.getUsuarioLogado().getTipoUsuario().ordinal() == 2;
	}

	public void cadastrar() {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {

			tx = session.beginTransaction();
			
			if (!Caracter.stringIsNullOrEmpty(this.senhaDescriptografada)) {
				this.objeto.setSenha(new TripleDES(Constantes.KEY_TRIPLE_DES).encrypt(this.senhaDescriptografada));
			} else {
				this.objeto.setSenha("");
			}

			this.objeto.setTipoUsuario(TipoUsuario.Cliente);
			
			if (this.objeto.getId() > 0) {
				new UsuarioDAO().update(this.objeto,session);
			}else {
				new UsuarioDAO().save(this.objeto,session);
			}
			
			UsuarioEndereco endereco = new UsuarioEndereco();

			String precoDols = Caracter.getRandomInt(2);
			String precoCents = Caracter.getRandomInt(2);
			String preco = precoDols + "." + precoCents;

			endereco.setPrecoFrete(Double.parseDouble(preco));
			endereco.setUsuario(this.objeto);
			endereco.setEndereco(this.endereco);

			session.save(endereco);

			session.flush();
			
			tx.commit();
			
			Mensagens.gerarMensagemGenerica("Usuário salvo com sucesso");
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("Login" + Constantes.EXTENSAO_JSF);
			
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
			System.out.println(e.getMessage());
		}
	}
	
	public void excluirCadastro(Usuario usuario) {
		try {
			this.objeto = usuario;
			
			if (this.objeto.getId() <= 0)
				throw new MensagemException("Não é possível excluir um cadastro que ainda não foi salvo");

			new UsuarioDAO().deleteUsu(this.objeto);
			Mensagens.gerarMensagemGenerica("Usuário excluído com sucesso");
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
			System.out.println(e.getMessage());
		}
	}

	public List<SelectItem> getComboTipoUsuario() {
		return Combos.getComboTipoUsuario();
	}
	
	public Usuario getObjeto() {
		return objeto;
	}

	public void setObjeto(Usuario objeto) {
		this.objeto = objeto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSenhaDescriptografada() {
		return senhaDescriptografada;
	}

	public void setSenhaDescriptografada(String senhaDescriptografada) {
		this.senhaDescriptografada = senhaDescriptografada;
	}

	public boolean isPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(boolean pesquisa) {
		this.pesquisa = pesquisa;
	}
	
	public void setPesquisa(Usuario usuario, boolean pesquisa) {
		this.pesquisa = pesquisa;
		this.objeto = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}