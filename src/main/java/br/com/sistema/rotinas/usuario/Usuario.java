package br.com.sistema.rotinas.usuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.sistema.enuns.TipoUsuario;
import br.com.sistema.rotinas.produtos.Carrinho;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 568661782368920559L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "usuarioKey", nullable = false, length = 30)
	private String usuarioKey;

	@Column(name = "nome", nullable = false, length = 200)
	private String nome;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "senha", nullable = false, length = 200)
	private String senha;

	@Column(name = "telefone", length = 15)
	private String telefone;

	@Column(name = "celular", length = 15)
	private String celular;

	@Column(name = "cpf", length = 14)
	private String cpf;
	
	@Enumerated
	@Column(name = "tipoUsuario", length = 14)
	private TipoUsuario tipoUsuario;
	
	@Column(name = "dataDeNascimento")
	private Date dataDeNascimento;

	@Column(name = "dataDeCadastro", nullable = false)
	private Date dataDeCadastro;
	
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Carrinho carrinho;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "usuario_id", nullable = true)
	private List<UsuarioEndereco> enderecos;

	@Transient
	private String confirmacaoSenha;
	
	public Usuario() {
		carrinho = new Carrinho();
	}

	public Usuario(int id, String usuarioKey, String email, String senha) {
		super();
		this.id = id;
		this.usuarioKey = usuarioKey;
		this.email = email;
		this.senha = senha;
	}

	public Usuario(String nome, String email, String usuarioKey) {
		this.nome = nome;
		this.email = email;
		this.usuarioKey = usuarioKey;
	}
	
	public Usuario(Usuario usuarioLogado) {
		this.setId(usuarioLogado.getId());
		this.setUsuarioKey(usuarioLogado.getUsuarioKey());
		this.setNome(usuarioLogado.getNome());
	}

	public int getId() {
		return id;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuarioKey() {
		return usuarioKey;
	}

	public void setUsuarioKey(String usuarioKey) {
		this.usuarioKey = usuarioKey;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(Date dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public Date getDataDeCadastro() {
		return dataDeCadastro;
	}

	public void setDataDeCadastro(Date dataDeCadastro) {
		this.dataDeCadastro = dataDeCadastro;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public Carrinho getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

	public List<UsuarioEndereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<UsuarioEndereco> enderecos) {
		this.enderecos = enderecos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
