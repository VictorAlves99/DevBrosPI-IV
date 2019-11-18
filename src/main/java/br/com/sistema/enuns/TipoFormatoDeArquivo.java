package br.com.sistema.enuns;

import br.com.sistema.util.Caracter;

public enum TipoFormatoDeArquivo {
	NaoDefinido("Não Definido", "", ""),

	PDF("PDF", ".pdf", "application/pdf"),

	DOCX("DOCX", ".docx", "application/docx"),

	DOC("DOC", ".doc", "application/msword"),

	HTM("HTM", ".htm", "application/html"),

	JPG("JPG", ".jpg", "image/jpeg"),

	PNG("PNG", ".png", "image/png"),

	BMP("BMP", ".bmp", "image/bmp"),

	XLS("XLS", ".xls", "application/vnd.ms-excel"),

	CSV("CSV", ".csv", "text/csv"),

	ZIP("ZIP", ".zip", "application/octet-stream"),

	XML("XML", ".xml", "application/xml"),

	TXT("TXT", ".txt", "text/plain"),

	RET("RET", ".ret", "text/plain"),

	JSON("JSON", ".json", "application/json"),

	MP3("MP3", ".mp3", "audio/mpeg3"),
	
	PFX("PFX", ".pfx", "application/pfx");

	private String descricao;
	private String extensao;
	private String contentType;

	private TipoFormatoDeArquivo(String descricao, String extensao, String contentType) {
		this.descricao = descricao;
		this.extensao = extensao;
		this.contentType = contentType;
	}

	public static TipoFormatoDeArquivo getFormatoDeArquivoPelaExtensao(String nomeDoArquivo) {
		String extensao = Caracter.getExtensaoDoArquivo(nomeDoArquivo);

		if (extensao.toUpperCase().contains("PDF"))
			return TipoFormatoDeArquivo.PDF;

		if (extensao.toUpperCase().contains("DOC"))
			return TipoFormatoDeArquivo.DOC;

		if (extensao.toUpperCase().contains("DOCX"))
			return TipoFormatoDeArquivo.DOCX;

		if (extensao.toUpperCase().contains("JPG"))
			return TipoFormatoDeArquivo.JPG;

		if (extensao.toUpperCase().contains("JPEG"))
			return TipoFormatoDeArquivo.JPG;

		if (extensao.toUpperCase().contains("PNG"))
			return TipoFormatoDeArquivo.PNG;

		if (extensao.toUpperCase().contains("BMP"))
			return TipoFormatoDeArquivo.BMP;

		if (extensao.toUpperCase().contains("XML"))
			return TipoFormatoDeArquivo.XML;

		if (extensao.toUpperCase().contains("TXT"))
			return TipoFormatoDeArquivo.TXT;

		if (extensao.toUpperCase().contains("RET"))
			return TipoFormatoDeArquivo.RET;

		if (extensao.toUpperCase().contains("XLS"))
			return TipoFormatoDeArquivo.XLS;

		if (extensao.toUpperCase().contains("PDF"))
			return TipoFormatoDeArquivo.PDF;

		if (extensao.toUpperCase().contains("PNG"))
			return TipoFormatoDeArquivo.PNG;

		if (extensao.toUpperCase().contains("MP3"))
			return TipoFormatoDeArquivo.MP3;

		if (extensao.toUpperCase().contains("PFX"))
			return TipoFormatoDeArquivo.PFX;
		
		return TipoFormatoDeArquivo.NaoDefinido;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
