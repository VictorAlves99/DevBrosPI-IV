package br.com.sistema.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sistema.enuns.TipoProperties;

public class Caracter {

	public static String getUrlCorrigida(String url) {
		return url.replace("\\", "/").replace("///", "/").replace("//", "/").replace(":/", "://");
	}

	// public static String getUrlFormatada(String... strings) {
	// String url = "";
	// for (String str : strings) {
	// url += str + "/";
	// }
	// return url;
	// }

	public static String getUrlMontada(String endereco, String parametros) {
		String url = endereco + Constantes.EXTENSAO_JSF + parametros;
		return getUrlCorrigida(url);
	}

	public static String urlCombine(String url, String urlBase) {
		return urlCombine(url, urlBase, "");
	}

	public static String urlCombine(String url, String urlBase, String parametros) {
		String urlFinal = "";

		if (url.endsWith("/") && urlBase.startsWith("/")) {
			urlFinal = url + urlBase.substring(1, urlBase.length());
		} else if (!url.endsWith("/") && !urlBase.startsWith("/")) {
			urlFinal = url + "/" + urlBase;
		} else {
			urlFinal = url + urlBase;
		}

		if (!Caracter.stringIsNullOrEmpty(parametros)) {
			String parametrosFinal = "";

			if (urlFinal.endsWith("/")) {
				urlFinal = urlFinal.substring(0, urlFinal.length() - 1);
			}

			if (parametros.startsWith("/")) {
				parametrosFinal = parametros.substring(1, parametros.length());
			} else {
				parametrosFinal = parametros;
			}

			urlFinal += "?" + parametrosFinal;
		}

		return urlFinal;
	}

	public static String somenteNumero(String numero) {
		if (stringIsNullOrEmpty(numero))
			return "";
		else
			return numero.replaceAll("\\D*", "");
	}

	public static String getEmptyIfNull(String string) {

		if (string == null)
			return "";

		return string;
	}

	public static String getDataAsString(Date data, String format) {
		if (data != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(data);
		} else
			return "";
	}

	public static String getDateTimeAsString(Date data, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(data);
	}

	public static String getFormatoMoeda(double vlr) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return nf.format(vlr);
	}

	public static String getDoubleAsString(double valor) {
		return getDoubleAsString(valor, 2);
	}

	public static String getDoubleAsString(double valor, int casasDecimais) {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(casasDecimais);
		decimalFormat.setMinimumFractionDigits(casasDecimais);
		return decimalFormat.format(valor);
	}

	public static double round(double value) {
		return round(value, 2);
	}

	public static double round(double value, int casasDecimais) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(casasDecimais, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String stringRepeat(String valor, int tamanhoAReplicar) {
		return new String(new char[tamanhoAReplicar]).replace("\0", valor);
	}

	public static String stringRepeatLeft(String valor, int quantidadeCaracteres, String textoAReplicar) {

		if (valor.length() > quantidadeCaracteres) {
			return valor;
		} else {
			int tamanhoAtual = valor.length();
			int tamanhoAReplicar = quantidadeCaracteres - tamanhoAtual;

			return new String(new char[tamanhoAReplicar]).replace("\0", textoAReplicar) + valor;
		}
	}

	public static String stringRepeatRight(String valor, int quantidadeCaracteres, String textoAReplicar) {

		int tamanhoAtual = valor.length();
		int tamanhoAReplicar = quantidadeCaracteres - tamanhoAtual;

		return valor + new String(new char[tamanhoAReplicar]).replace("\0", textoAReplicar);
	}

	public static String getExtensaoDoArquivo(String nomeDoArquivo) {
		if (nomeDoArquivo.indexOf('.') < 0)
			return "";
		else
			return nomeDoArquivo.substring(nomeDoArquivo.indexOf('.'), nomeDoArquivo.length());
	}

	public static boolean stringIsNullOrEmpty(String a) {
		return a == null || "".equals(a);
	}

	public static String retirarQuebraDeLinha(String string) {

		String line_separator = System.getProperty("line.separator");

		return string.replaceAll("\\n|" + line_separator, "");
	}

	public static String formatarCPFOuCNPJ(String cpfCnpj) {

		if (stringIsNullOrEmpty(cpfCnpj))
			return "";
		else if (somenteNumero(cpfCnpj).length() == 11)
			return formatarCPF(cpfCnpj);
		else if (somenteNumero(cpfCnpj).length() == 14)
			return formatarCNPJ(cpfCnpj);

		return "";
	}

	public static String formatarCPF(String cpf) {
		Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
		Matcher matcher = pattern.matcher(cpf);
		if (matcher.matches())
			cpf = matcher.replaceAll("$1.$2.$3-$4");
		return cpf;
	}

	public static String formatarCNPJ(String cnpj) {
		Pattern pattern = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
		Matcher matcher = pattern.matcher(cnpj);
		if (matcher.matches())
			cnpj = matcher.replaceAll("$1.$2.$3/$4-$5");
		return cnpj;
	}

	public static Integer tentaConverterParaInteiro(String valor, Integer valorPadrao) {
		try {
			return Integer.parseInt(somenteNumero(valor));
		} catch (Exception e) {
			return valorPadrao;
		}
	}

	public static Long tentaConverterParaLong(String valor, Long valorPadrao) {
		try {
			return Long.parseLong(somenteNumero(valor));
		} catch (Exception e) {
			return valorPadrao;
		}
	}

	public static Double tentaConverterParaDouble(String valor, Double valorPadrao) {
		try {
			valor = valor.replaceAll("[^\\d,\\.]++", "");

			if (valor.matches(".+\\.\\d+,\\d+$"))
				return Double.parseDouble(valor.replaceAll("\\.", "").replaceAll(",", "."));

			if (valor.matches(".+,\\d+\\.\\d+$"))
				return Double.parseDouble(valor.replaceAll(",", ""));

			return Double.parseDouble(valor.replaceAll(",", "."));

		} catch (Exception e) {
			return valorPadrao;
		}
	}

	public static String formatarCEP(String cep) {
		Pattern pattern = Pattern.compile("(\\d{5})(\\d{3})");
		Matcher matcher = pattern.matcher(cep);
		if (matcher.matches())
			cep = matcher.replaceAll("$1-$2");
		return cep;
	}

	public static String formatarTelefone(String tel) {

		if (Caracter.stringIsNullOrEmpty(tel))
			return "";

		tel = somenteNumero(tel);

		/* SE O DDD ESTIVER 011 AO INVES DE 11, TIRA O 0 */
		if (tel.substring(0, 1).equals("0"))
			tel = tel.substring(1, tel.length());

		Pattern pattern = null;

		if (tel.length() == 11)
			pattern = Pattern.compile("(\\d{2})(\\d{4})(\\d{5})");
		else
			pattern = Pattern.compile("(\\d{2})(\\d{4})(\\d{4})");

		Matcher matcher = pattern.matcher(tel);
		if (matcher.matches())
			tel = matcher.replaceAll("($1) $2-$3");

		return tel;
	}

	public static List<String> getSplitToList(String texto) {
		List<String> lst = new ArrayList<String>();

		if (!stringIsNullOrEmpty(texto))
			lst.addAll(Arrays.asList(texto.split(";")));
		return lst;
	}

	public static String getRandomUUID() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public static String getRandomString(int tamanho) {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZabcdefghijklmnopqrstuvywxz0123456789";

		Random random = new Random();

		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < tamanho; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}

		return armazenaChaves;
	}
	
	public static String getRandomInt(int tamanho) {
		String letras = "0123456789";

		Random random = new Random();

		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < tamanho; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}

		return armazenaChaves;
	}
	
	public static String getRandomIntRes(int tamanho) {
		String letras = "012";

		Random random = new Random();

		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < tamanho; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}

		return armazenaChaves;
	}

	public static String retirarCaracterEspecial(String s) {
		String temp = Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]", "");
	}

	public static String addQuotedSQL(String texto) {
		return "'" + texto + "'";
	}

	public static String reverseString(String string) {
		return new StringBuilder(string).reverse().toString();
	}

	public static String getMesPorExtenso(Date date, String pattern) {
		return getMesPorExtenso(date, pattern, new Locale("pt", "BR"));
	}

	public static String getMesPorExtenso(Date date, String pattern, Locale locale) {

		DateFormat df2 = new SimpleDateFormat(pattern, locale);

		return df2.format(date);
	}

	public static String getStringFromArquivoTXT(String path) throws Exception {

		BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));

		StringBuilder linha = new StringBuilder();

		linha.append(lerArq.readLine());

		while (lerArq.ready()) {
			linha.append(lerArq.readLine());
		}
		lerArq.close();
		lerArq = null;

		return linha.toString();
	}

	public static boolean isHtml(String s) {

		// http://ideone.com/HakdHo
		// http://stackoverflow.com/questions/3052052/how-to-find-if-string-contains-html-data

		// adapted from re posted by Phil Haack and modified to match better
		final String tagStart = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
		final String tagEnd = "\\</\\w+\\>";
		final String tagSelfClosing = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
		final String htmlEntity = "&[a-zA-Z][a-zA-Z0-9]+;";
		final Pattern htmlPattern = Pattern.compile(
				"(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")", Pattern.DOTALL);

		boolean ret = false;
		if (s != null) {
			ret = htmlPattern.matcher(s).find();
		}

		return ret;
	}

	public static String pathCombine(String diretorio, String... outros) {
		return Paths.get(diretorio, outros).toString();
	}

	public static String limparSQLInjection(String value) {
		return value.replaceAll("'", "''");
	}

	public static String getTempoDeDuracaoPorExtenso(long millis) {

		if (millis < 0) {
			return "";
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);

		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);

		if (days > 0) {
			sb.append(days);
			sb.append(days > 1 ? " Dias, " : " Dia, ");
		}

		if (hours > 0) {
			sb.append(hours);
			sb.append(hours > 1 ? " Horas, " : " Hora, ");
		}

		if (minutes > 0) {
			sb.append(minutes);
			sb.append(minutes > 1 ? " Minutos " : " Minuto ");
		}

		if (seconds > 0) {
			sb.append(minutes > 0 ? "e " : "");
			sb.append(seconds);
			sb.append(seconds > 1 ? " Segundos" : " Segundo");
		}

		return (sb.toString());
	}

	public static String replaceQuebraDeLinhaJavaPorQuebraDeLinhaHTML(String txt) {
		if (!stringIsNullOrEmpty(txt))
			return txt.replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");
		else
			return txt;
	}

	public static String formatarString(String string, Object... parametros) {
		return String.format(string, parametros);
	}

	public static String getStringFormatadaParaHTMLNegrito(String string) {

		if (!Caracter.stringIsNullOrEmpty(string)) {
			// return descricao.replace("<", "<b>").replace(">", "</b>");
			return string.replace(">", "|").replace("<", "<b>").replace("|", "</b>");
		} else {
			return string;
		}
	}

	public static String repetirString(String string, int qtd) {
		return String.format(String.format("%%0%dd", qtd), 0).replace("0", string);
	}

	public static String[] splitEmail(String email) {
		if (!stringIsNullOrEmpty(email))
			return email.replace(",", ";").replace("/", ";").split(";");
		else
			return null;
	}

	public static String mudarCharSet(String string, Charset charSet) {
		return new String(string.getBytes(), charSet);
	}

	public static String truncString(String string, int tamanhoMaximo) {

		String retorno = "";

		if (stringIsNullOrEmpty(string) || string.length() <= tamanhoMaximo)
			retorno = string;
		else {
			retorno = string.substring(0, tamanhoMaximo);
		}

		return retorno;
	}

	public static String readerToString(BufferedReader reader) throws Exception {

		StringBuilder sb = new StringBuilder();

		String line = null;

		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		reader.close();

		return sb.toString();
	}

	public static boolean possuiCaracterEspecial(String string) {
		return !Normalizer.isNormalized(string, Normalizer.Form.NFD);
	}

	public static String tranformaNomeComumParaNomeAmigavel(String a) {

		if (stringIsNullOrEmpty(a))
			return a;

		return retirarCaracterEspecial(transformaParaMinusculo(a).replace(" ", "-"));
	}

	public static String transformaParaMinusculo(String string) {

		if (stringIsNullOrEmpty(string))
			return string;

		return string.toLowerCase();

	}

	public static String escapeSpecialRegexChars(String str) {
		Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

		return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
	}

	public static String getPropriedade(TipoProperties propriedade) throws Exception {
		return getPropriedade(propriedade.getPropriedade());
	}

	private static String getPropriedade(String propriedade) throws Exception {
		Properties prop = new Properties();
		InputStream stream = new PropertiesLoader().getSistemaProperties();
		prop.load(stream);
		String valor = prop.getProperty(propriedade);

		stream.close();
		stream = null;

		prop.clear();
		prop = null;

		return valor;
	}
	
}
