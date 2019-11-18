package br.com.sistema.enuns;

public enum TipoProperties {

	AmbienteDeProducao("", "ambiente.de.producao"),

	ServerMaster("", "server.master"),

	URLDoServidorDoAmbiente("", "url.servidor.do.ambiente"),

	URLDoServidorDoSistema("", "url.servidor.do.sistema"),

	URLWebServiceDoAmbiente("", "webservice.do.ambiente"),

	ChavePublicaDoAmbiente("", "chave.publica.no.ambiente"),

	ChaveSecretaDoAmbiente("", "chave.secreta.no.ambiente"),

	TokenDoWebService("", "token.webservice"),

	IDDoSistemaNoAmbiente("", "id.do.sistema.no.ambiente"),

	IDDaEmpresaNoAmbiente("", "id.da.empresa.no.ambiente"),

	ServidorSMTP("", "email.smtp"),

	UsuarioSMTP("", "email.smtp.usuario"),

	SenhaSMTP("", "email.smtp.senha"),

	PastaTemplatesHtml("", "pasta.templates.html"),

	PastaTemplatesExcel("", "pasta.templates.excel"),

	PastaDosRelatorios("", "caminho.relatorios"),

	RemetentePadraoDeEmail("", "email.remetente.padrao"),

	DestinatarioPadraoDeEmail("", "email.destinatario.padrao"),

	RemetentePadraoDeEmailDeCobranca("", "email.remetente.cobranca.padrao"),

	PastaTemplatesCNAB("", "pasta.templates.cnab"),

	PastaTemplatesBoleto("", "pasta.templates.boleto"),

	PastaDeBackupsDoPDV("", "pasta.backups.pdv"),

	PastaNFeNFCe("", "pasta.nfe.nfce");

	private String descricao;
	private String propriedade;

	private TipoProperties(String descricao, String propriedade) {
		this.descricao = descricao;
		this.propriedade = propriedade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}
}
