package br.com.sistema.util;

import java.io.Serializable;

import br.com.sistema.enuns.TipoProperties;
import br.com.sistema.seguranca.TripleDES;

public class Properties implements Serializable {

	private static final long serialVersionUID = 6057765378139714436L;

	public static boolean isAmbienteDeProducao() throws Exception {
		String valor = Caracter.getPropriedade(TipoProperties.AmbienteDeProducao);
		return "SIM_TRUE_1_PRODUCAO".contains(valor.toUpperCase());
	}

	public static boolean isServerMaster() throws Exception {
		String valor = Caracter.getPropriedade(TipoProperties.ServerMaster);
		return "SIM_TRUE_1_MASTER".contains(valor.toUpperCase());
	}

	public static String getPastaNFeNFCe() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaNFeNFCe);
	}

	public static String getPastaDosRelatorios() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaDosRelatorios);
	}

	public static String getURLServidorSistema() throws Exception {
		return Caracter.getPropriedade(TipoProperties.URLDoServidorDoSistema);
	}

	public static String getURLServidorAmbiente() throws Exception {
		return Caracter.getPropriedade(TipoProperties.URLDoServidorDoAmbiente);
	}

	public static String getURLWebServiceAmbiente() throws Exception {
		return Caracter.getPropriedade(TipoProperties.URLWebServiceDoAmbiente);
	}

	public static String getChavePublicaAmbiente() throws Exception {
		return Caracter.getPropriedade(TipoProperties.ChavePublicaDoAmbiente);
	}

	public static String getChaveSecretaAmbiente(boolean descriptografada) throws Exception {

		String chave = Caracter.getPropriedade(TipoProperties.ChaveSecretaDoAmbiente);

		if (descriptografada) {
			if (!Caracter.stringIsNullOrEmpty(chave)) {
				TripleDES cr = new TripleDES(Constantes.KEY_TRIPLE_DES);
				return cr.decrypt(chave);
			} else {
				return "";
			}

		} else {
			return chave;
		}
	}

	public static String getTokenWebService(boolean descriptografado) throws Exception {

		String token = Caracter.getPropriedade(TipoProperties.TokenDoWebService);

		if (descriptografado) {
			if (!Caracter.stringIsNullOrEmpty(token)) {
				TripleDES cr = new TripleDES(Constantes.KEY_TRIPLE_DES);
				return cr.decrypt(token);
			} else {
				return "";
			}

		} else {
			return token;
		}
	}

	public static int getIdSistemaAmbiente() throws Exception {
		return Caracter.tentaConverterParaInteiro(Caracter.getPropriedade(TipoProperties.IDDoSistemaNoAmbiente), 0);
	}

	public static int getIdEmpresaAmbiente() throws Exception {
		return Caracter.tentaConverterParaInteiro(Caracter.getPropriedade(TipoProperties.IDDaEmpresaNoAmbiente), 0);
	}

	public static String getServidorSmtp() throws Exception {
		return Caracter.getPropriedade(TipoProperties.ServidorSMTP);
	}

	public static String getUsuarioSmtp() throws Exception {
		return Caracter.getPropriedade(TipoProperties.UsuarioSMTP);
	}

	public static String getSenhaSmtp(boolean descriptografado) throws Exception {

		String senha = Caracter.getPropriedade(TipoProperties.SenhaSMTP);

		if (descriptografado) {
			if (!Caracter.stringIsNullOrEmpty(senha)) {
				TripleDES cr = new TripleDES(Constantes.KEY_TRIPLE_DES);
				return cr.decrypt(senha);
			} else {
				return "";
			}

		} else {
			return senha;
		}

	}

	public static String getPastaTemplatesHtml() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaTemplatesHtml);
	}

	public static String getPastaTemplatesExcel() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaTemplatesExcel);
	}

	public static String getPastaDeBackupsDoPDV() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaDeBackupsDoPDV);
	}

	public static String getDestinatarioPadraoDeEmail() throws Exception {
		return Caracter.getPropriedade(TipoProperties.DestinatarioPadraoDeEmail);
	}

	public static String getRemetentePadraoDeEmail() throws Exception {
		return Caracter.getPropriedade(TipoProperties.RemetentePadraoDeEmail);
	}

	public static String getRemetentePadraoDeEmailCobranca() throws Exception {
		return Caracter.getPropriedade(TipoProperties.RemetentePadraoDeEmailDeCobranca);
	}

	public static String getPastaTemplateBoleto() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaTemplatesBoleto);
	}

	public static String getPastaTemplatesCNAB() throws Exception {
		return Caracter.getPropriedade(TipoProperties.PastaTemplatesCNAB);
	}

	public static long getSerialversionuid() throws Exception {
		return serialVersionUID;
	}

}
