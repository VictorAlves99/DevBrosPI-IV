package br.com.sistema.rotinas.arquivos;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.com.sistema.enuns.TipoFormatoDeArquivo;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.util.Lib;

@Entity
public class Arquivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "idExterno", nullable = false)
	private int idExterno;

	@Enumerated
	@Column(name = "origem", nullable = false)
	private TipoOrigemArquivoBinario origem;

	@Column(name = "descricao", length = 500)
	private String descricao;

	@Column(name = "tamanho", nullable = false)
	private double tamanho;

	@Enumerated
	@Column(name = "formato", nullable = false)
	private TipoFormatoDeArquivo formato;

	@Column(name = "nome", nullable = false, length = 200)
	private String nome;

	@Column(name = "dataUpload", nullable = false)
	private Date dataUpload;

	@Transient
	private InputStream stream;

	@Transient
	private String url;

	public Arquivo() {
		this.origem = TipoOrigemArquivoBinario.NaoDefinido;
		this.formato = TipoFormatoDeArquivo.NaoDefinido;
	}

	public byte[] getBinario() throws IOException {
		return Lib.getByteArrayFromInputStream(this.stream);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getTamanho() {
		return tamanho;
	}

	public void setTamanho(double tamanho) {
		this.tamanho = tamanho;
	}

	public TipoFormatoDeArquivo getFormato() {
		return formato;
	}

	public void setFormato(TipoFormatoDeArquivo formato) {
		this.formato = formato;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataUpload() {
		return dataUpload;
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIdExterno() {
		return idExterno;
	}

	public void setIdExterno(int idExterno) {
		this.idExterno = idExterno;
	}

	public TipoOrigemArquivoBinario getOrigem() {
		return origem;
	}

	public void setOrigem(TipoOrigemArquivoBinario origem) {
		this.origem = origem;
	}

}