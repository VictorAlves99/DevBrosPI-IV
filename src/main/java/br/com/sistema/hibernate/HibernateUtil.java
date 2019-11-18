package br.com.sistema.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.spi.SessionFactoryImplementor;

public class HibernateUtil implements Serializable {

	private static final long serialVersionUID = -2716727729705080964L;

	private static SessionFactory factory;

	public static void inicializarSessionFactory(Configuration cfg, MultiTenancyStrategy multiTenantStrategy) {

		cfg.getProperties().put(Environment.MULTI_TENANT, multiTenantStrategy);

		SessionFactory sessionFactory = (SessionFactoryImplementor) cfg.buildSessionFactory();

		factory = sessionFactory;
	}

	public static Session getSession() {
		return factory.openSession();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> listAndCast(Criteria c) {
		return c.list();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> List<T> listAndCast(Query q) {
		return q.list();
	}

}