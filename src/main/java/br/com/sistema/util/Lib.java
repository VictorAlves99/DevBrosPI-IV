package br.com.sistema.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.HibernateException;

public class Lib {

	public static Date getDataAtual() throws ParseException {
		Calendar data = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataAtual = data.getTime();

		dataAtual = sdf.parse(sdf.format(data.getTime()));

		return dataAtual;
	}

	public static Date getDataInfinita02022079() {
		Calendar c02022079 = Calendar.getInstance();
		c02022079.set(Calendar.DAY_OF_MONTH, 02);
		c02022079.set(Calendar.MONTH, 01);
		c02022079.set(Calendar.YEAR, 2079);
		c02022079.set(Calendar.HOUR, 0);
		c02022079.set(Calendar.MINUTE, 0);
		c02022079.set(Calendar.SECOND, 0);
		c02022079.set(Calendar.MILLISECOND, 0);

		return c02022079.getTime();
	}

	public static Date getDataComHoraZerada(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getDataComUltimoDiaDoMes(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static Date getDataComUltimoMinutoDoDia(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static int getUltimoDiaDoMes(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);

	}

	public static Calendar ajustaDataParaDiaUtil(Calendar data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data.getTime());

		int somar = 0;
		if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			somar = 2;
		else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			somar = 1;

		calendar.add(Calendar.DAY_OF_MONTH, somar);
		return calendar;
	}

	public static XMLGregorianCalendar getXMLGregorianCalendarFromDate(Date data)
			throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(data);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	}

	public static Date getDateFromXMLGregorianCalendar(XMLGregorianCalendar data) {
		return data.toGregorianCalendar().getTime();
	}

	public static Calendar incrementarDias(Date data, int qtdDias, boolean isSomenteUtil) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(data);
		int qtdNaoUtil = 0;

		for (int i = 0; i < qtdDias; i++) {

			calendar.add(Calendar.DAY_OF_MONTH, 1);

			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				qtdNaoUtil++;
			}

		}

		if (isSomenteUtil) {
			calendar.add(Calendar.DAY_OF_MONTH, qtdNaoUtil);
			calendar = ajustaDataParaDiaUtil(calendar);
		}
		return calendar;

	}

	public static String getOnlyNumbers(String value) {
		return Pattern.compile("[^0-9]").matcher(value).replaceAll("");
	}

	public static String getOnlyString(String value) {
		return Pattern.compile("[^a-zA-Z]").matcher(value).replaceAll("");
	}

	public static String adicionarZerosAEsquerda(int numero, int tamanhoFinal) {
		String s = String.valueOf(numero);

		if (s.length() < tamanhoFinal) {
			int zeros = tamanhoFinal - s.length();

			StringBuffer buffer = new StringBuffer();
			for (int i = 1; i <= zeros; i++) {
				buffer.append("0");
			}
			buffer.append(s);
			s = buffer.toString();
		}
		return s;
	}

	public static int dataDiff(Date dataLow, Date dataHigh) {

		int MILLIS_IN_DAY = 86400000;

		Calendar dataMenor = Calendar.getInstance();
		dataMenor.setTime(dataLow);
		dataMenor.set(Calendar.MILLISECOND, 0);
		dataMenor.set(Calendar.SECOND, 0);
		dataMenor.set(Calendar.MINUTE, 0);
		dataMenor.set(Calendar.HOUR_OF_DAY, 0);

		Calendar dataMaior = Calendar.getInstance();
		dataMaior.setTime(dataHigh);
		dataMaior.set(Calendar.MILLISECOND, 0);
		dataMaior.set(Calendar.SECOND, 0);
		dataMaior.set(Calendar.MINUTE, 0);
		dataMaior.set(Calendar.HOUR_OF_DAY, 0);
		return (int) ((dataMaior.getTimeInMillis() - dataMenor.getTimeInMillis()) / MILLIS_IN_DAY);
	}

	public static double hoursDiff(Date dataLow, Date dataHigh) {

		final int MILLI_TO_HOUR = 1000 * 60 * 60;
		return (double) (dataHigh.getTime() - dataLow.getTime()) / MILLI_TO_HOUR;
	}

	public static String timeDiffInFull(Date dataInicial, Date dataFinal) {

		Long millis = dataFinal.getTime() - dataInicial.getTime();

		if (millis < 0) {
			millis = 0L;
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder();
		sb.append(days);
		sb.append(days > 1 ? " Dias" : " Dia ");
		sb.append(hours);
		sb.append(hours > 1 ? " Horas " : " Hora ");
		sb.append(minutes);
		sb.append(minutes > 1 ? " Minutos " : " Minuto ");
		sb.append(seconds);
		sb.append(seconds > 1 ? " Segundos" : " Segundo");

		return sb.toString();
	}

	public static double minutesDiff(Date dataLow, Date dataHigh) {

		Calendar dataMenor = Calendar.getInstance();
		dataMenor.setTime(dataLow);
		dataMenor.set(Calendar.MILLISECOND, 0);
		dataMenor.set(Calendar.SECOND, 0);

		Calendar dataMaior = Calendar.getInstance();
		dataMaior.setTime(dataHigh);
		dataMaior.set(Calendar.MILLISECOND, 0);
		dataMaior.set(Calendar.SECOND, 0);

		return ((dataMaior.getTimeInMillis() - dataMenor.getTimeInMillis()) / (60 * 1000));
	}

	public static boolean isAnoBisexto(int ano) {
		return new GregorianCalendar().isLeapYear(ano);
	}

	public static String idadePorExtenso(Date dataInicial, Date dataFinal) {

		String sAnos = "";
		String sMeses = "";
		String sDias = "";

		int qtdAnos = 0;
		int qtdMeses = 0;
		int qtdDias = 0;

		Calendar cDataInicial = Calendar.getInstance();
		cDataInicial.setTime(Lib.getDataComHoraZerada(dataInicial));

		Calendar cDataFinal = Calendar.getInstance();
		cDataFinal.setTime(Lib.getDataComHoraZerada(dataFinal));
		boolean adicionouMes = false;
		while (cDataInicial.getTime().before(cDataFinal.getTime())) {
			cDataInicial.add(Calendar.MONTH, 1);
			adicionouMes = true;
			qtdMeses++;

		}

		qtdMeses--;

		if (qtdMeses > 11) {
			while (qtdMeses > 11) {
				qtdAnos++;
				qtdMeses -= 12;
			}
		}

		if (adicionouMes) {
			cDataInicial.add(Calendar.MONTH, -1);
		}

		qtdDias = Lib.dataDiff(cDataInicial.getTime(), cDataFinal.getTime());

		if (qtdAnos > 1) {
			sAnos = " anos";
		} else if (qtdAnos == 1) {
			sAnos += " ano";
		}

		if (qtdAnos > 0) {
			if (qtdMeses > 0 && qtdDias > 0) {
				sAnos += ", ";
			} else if (qtdMeses > 0 || qtdDias > 0) {
				sAnos += " e ";
			}
		}

		if (qtdMeses > 1) {
			sMeses = " meses";
		} else if (qtdMeses == 1) {
			sMeses += " mês";
		}

		if (qtdMeses > 0 && qtdDias > 0) {
			sMeses += " e ";
		}

		if (qtdDias == 0 || qtdDias == 1) {
			sDias = " dia";
		} else if (qtdDias > 1) {
			sDias = " dias";
		}

		return (qtdAnos > 0 ? qtdAnos : "") + sAnos + (qtdMeses > 0 ? qtdMeses : "") + sMeses
				+ (qtdDias > 0 ? qtdDias : "0") + sDias;
	}

	public static String getBrowserName(String userAgent) {

		if (userAgent.contains("OPR")) {
			return "Opera";
		}
		if (userAgent.contains("MSIE")) {
			return "Internet Explorer";
		}
		if (userAgent.contains("Firefox")) {
			return "Firefox";
		}
		if (userAgent.contains("Chrome")) {
			return "Chrome";
		}
		if (userAgent.contains("Opera")) {
			return "Opera";
		}
		if (userAgent.contains("Safari")) {
			return "Safari";
		}

		return "Unknown";
	}

	public static String incluirTagsPadroesHTML(String html) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");
		sb.append(html);
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	public static ByteArrayOutputStream getByteArrayOutputStreamFromString(String texto) throws IOException {
		return getByteArrayOutputStreamFromString(texto, StandardCharsets.UTF_8);
	}

	public static ByteArrayOutputStream getByteArrayOutputStreamFromString(String texto, Charset charSet)
			throws IOException {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		byteOutput.write(texto.getBytes(charSet));
		return byteOutput;
	}

	public static InputStream getInputStreamFromImage(Image image) throws IOException {

		BufferedImage newImage = getBufferedImageFromImage(image);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(newImage, "jpg", os);
		InputStream fis = new ByteArrayInputStream(os.toByteArray());
		return fis;
	}

	public static BufferedImage getBufferedImageFromImage(Image im) {
		BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}

	public static byte[] getByteArrayFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream bos = getByteArrayOutputStreamFromInputStream(is);
		byte[] result = bos.toByteArray();
		return result;
	}

	public static ByteArrayOutputStream getByteArrayOutputStreamFromInputStream(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int next = is.read();
		while (next > -1) {
			bos.write(next);
			next = is.read();
		}
		bos.flush();
		return bos;
	}

	public static String getStringFromInputStream(InputStream is) throws IOException {
		byte[] bytes = getByteArrayFromInputStream(is);
		return new String(bytes);
	}

	public static InputStream getInputStreamFromByteArray(byte[] arq) throws IOException {
		return new ByteArrayInputStream(arq);
	}

	public static InputStream getInputStreamFromString(String valor, Charset charSet) throws IOException {
		return getInputStreamFromByteArray(valor.getBytes(charSet));
	}

	public static InputStream getInputStreamFromString(String valor) throws IOException {
		return getInputStreamFromByteArray(valor.getBytes());
	}

	public static ByteArrayOutputStream getByteArrayOutputStreamFromByteArray(byte[] bytes) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
		baos.write(bytes, 0, bytes.length);

		return baos;
	}

	public static String getStackTrace(Exception e) {

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		if (e instanceof HibernateException)
			((HibernateException) e).printStackTrace(printWriter);
		else
			e.printStackTrace(printWriter);

		return writer.toString();
	}

	 

	public static <T> void removeDuplicate(List<T> list) {
		List<T> newList = new ArrayList<T>();

		for (T obj : list) {
			if (!newList.contains(obj))
				newList.add(obj);
		}
		list.clear();
		list.addAll(newList);
	}

	public static String getStringFromByteArray(byte[] bytes) throws IOException {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static byte[] getByteArrayFromString(String texto) throws IOException {
		return texto.getBytes();
	}

	public static boolean isValidEmailAddress(String email) {
		if (Caracter.stringIsNullOrEmpty(email))
			return false;

		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static String getUserFromEmailAddress(String email) {

		if (isValidEmailAddress(email)) {
			String[] split = email.split("@");
			return split[0];
		} else {
			return "";
		}
	}

	public static String getDomainFromEmailAddress(String email) {

		if (isValidEmailAddress(email)) {
			String[] split = email.split("@");
			return split[1];
		} else {
			return "";
		}
	}

	public static int getDiaDeVencimento(Date dataDeCadastro, int diasDeExperimentacao) {
		Calendar data = Calendar.getInstance();
		data.setTime(dataDeCadastro);
		data.add(Calendar.DAY_OF_MONTH, diasDeExperimentacao);

		int diaFinal = data.get(Calendar.DAY_OF_MONTH);

		/* Soma mais um dia */
		diaFinal += 1;

		int diaDeVencimento = 5;

		if (diaFinal >= 1 && diaFinal <= 5)
			diaDeVencimento = 5;
		else if (diaFinal >= 6 && diaFinal <= 10) {
			diaDeVencimento = 10;
		} else if (diaFinal >= 11 && diaFinal <= 15) {
			diaDeVencimento = 15;
		} else if (diaFinal >= 16 && diaFinal <= 20) {
			diaDeVencimento = 20;
		} else if (diaFinal >= 21 && diaFinal <= 25) {
			diaDeVencimento = 25;
		} else if (diaFinal >= 26 && diaFinal <= 30) {
			diaDeVencimento = 30;
		} else if (diaFinal == 31) {
			diaDeVencimento = 5;
		}

		return diaDeVencimento;
	}

	 

	public static String getCopyRight(String empresa, int anoInicial) {
		Calendar data = Calendar.getInstance();
		int anoAtual = data.get(Calendar.YEAR);
		return "© " + anoInicial + "-" + anoAtual + " " + empresa;
	}

	public static boolean isNumerosEmSequencia(List<Integer> seq) {

		Collections.sort(seq);

		for (int i = 0; i < seq.size() - 1; i++) {
			if (seq.get(i) + 1 != seq.get(i + 1)) {
				return false;
			}
		}

		return true;
	}

	public static List<Date> getExpirationDatePKCS12(InputStream certificado, String senha) throws Exception {
		List<Date> datas = new ArrayList<Date>();

		KeyStore keystore = KeyStore.getInstance("PKCS12");
		keystore.load(certificado, senha.toCharArray());

		Enumeration<String> aliases = keystore.aliases();
		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			if (keystore.getCertificate(alias).getType().equals("X.509")) {
				Date data = ((X509Certificate) keystore.getCertificate(alias)).getNotAfter();
				datas.add(data);
			}
		}

		return datas;
	}
}
