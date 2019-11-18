package br.com.sistema.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	public InputStream getHibernateProperties() {
		return getClass().getResourceAsStream("/hibernate.properties");
	}

	public InputStream getSistemaProperties() throws Exception {
		Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream("/sistema.properties"));
		String valor = prop.getProperty("arquivo.properties");
		prop.clear();
		prop = null;
		return new FileInputStream(valor);
	}
}
