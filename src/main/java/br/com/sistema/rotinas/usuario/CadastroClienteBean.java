package br.com.sistema.rotinas.usuario;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.sistema.enuns.TipoUsuario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.FacesUtil;
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

	private String logradouro;
	private String bairro;
	private String cidade;
	private String uf;
	private String numero;
	private String enderecoCompleto;
	private String cep;

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
				new UsuarioDAO().update(this.objeto, session);
			} else {
				new UsuarioDAO().save(this.objeto, session);
			}

			session.flush();

			tx.commit();

			Mensagens.gerarMensagemGenerica("Usuário salvo com sucesso");

			FacesUtil.addCallbackParam("showDialog", true);
			
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
			System.out.println(e.getMessage());
		}
	}

	public void cadastrarEndereco() throws Exception {

		Session session = HibernateUtil.getSession();

		Transaction tx = null;

		try {
			UsuarioEndereco endereco = new UsuarioEndereco();

			String precoDols1 = Caracter.getRandomIntRes(1);
			String precoDols2 = Caracter.getRandomInt(1);
			String precoCents = Caracter.getRandomInt(2);
			String preco = precoDols1 + precoDols2 + "." + precoCents;

			enderecoCompleto = buscarCep(this.cep);

			endereco.setPrecoFrete(Double.parseDouble(preco));
			endereco.setUsuario(this.objeto);
			endereco.setEndereco(enderecoCompleto);

			tx = session.beginTransaction();

			session.save(endereco);

			session.flush();

			tx.commit();

			FacesContext.getCurrentInstance().getExternalContext().redirect("Login" + Constantes.EXTENSAO_JSF);
		} catch (HibernateException ex) {
			tx.rollback();
			throw ex;
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			session.close();
		}
	}

	public void buscarCEP() throws MensagemException {
		if(this.cep != null) {
			if(this.numero == null)
				this.numero = "sem nº";
				
			enderecoCompleto = buscarCep(this.cep);
		}else {
			throw new MensagemException(Mensagens.getMensagem("Digite o CEP corretamente."));
		}
	}

	public String buscarCep(String cep) {
		String json;

		try {
			URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			StringBuilder jsonSb = new StringBuilder();

			br.lines().forEach(l -> jsonSb.append(l.trim()));
			json = jsonSb.toString();

			json = json.replaceAll("[{},:]", "");
			json = json.replaceAll("\"", "\n");
			String array[] = new String[30];
			array = json.split("\n");

			logradouro = array[7];
			bairro = array[15];
			cidade = array[19];
			uf = array[23];

			logradouro = new String(logradouro.getBytes(), "UTF-8");
			bairro = new String(bairro.getBytes(), "UTF-8");
			cidade = new String(cidade.getBytes(), "UTF-8");
			uf = new String(uf.getBytes(), "UTF-8");

			return logradouro + "," + numero + " - " + bairro + "," + cidade + "," + uf;

		} catch (Exception e) {
			throw new RuntimeException(e);
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

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

}