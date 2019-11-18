package br.com.sistema.hibernate;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {

	static {
		Configuration cfg = AnnotatedClasses.getConfiguration();

		HibernateUtil.inicializarSessionFactory(cfg,
				MultiTenancyStrategy.NONE);
	}
}
