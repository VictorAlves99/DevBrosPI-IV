package br.com.sistema.util;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.Mensagens;

@SuppressWarnings("rawtypes")
public abstract class DAO<T> {

	protected Class classe;

	public Session getSessionPronta() {
		return HibernateUtil.getSession();
	}

	@SuppressWarnings("unchecked")
	public T load(int id, Session sessionExterna) throws HibernateException {
		return (T) sessionExterna.load(classe, id);
	}

	@SuppressWarnings("unchecked")
	public T load(int id) throws HibernateException {
		Session session = this.getSessionPronta();
		try {
			T objeto = (T) session.load(classe, id);
			Hibernate.initialize(objeto);
			return objeto;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Long count() throws HibernateException {
		Session session = this.getSessionPronta();
		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(classe);
			Root<T> root = query.from(classe);

			query.select(root);

			Query<T> q = session.createQuery(query);

			return (Long) q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public boolean exists(int id) throws HibernateException {
		return exists(id, null);
	}

	@SuppressWarnings("unchecked")
	public boolean exists(int id, Session sessionExterna) throws HibernateException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(classe);
			Root<T> root = query.from(classe);

			query.select(root).where(builder.equal(root.get("id"), id));

			Query<T> q = session.createQuery(query);

			Long count = (Long) q.getSingleResult();

			return count > 0;
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public T get(int id) throws HibernateException {
		Session session = this.getSessionPronta();
		try {
			T objeto = (T) session.get(classe, id);
			Hibernate.initialize(objeto);
			return objeto;
		} finally {
			session.close();
		}
	}

	public List<T> getLista(String orderBy) throws HibernateException {
		return getLista(null, orderBy);
	}

	@SuppressWarnings("unchecked")
	public List<T> getLista(Session sessionExterna, String orderBy) throws HibernateException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(classe);
			Root<T> root = query.from(classe);

			if (!Caracter.stringIsNullOrEmpty(orderBy)) {

				String[] order = orderBy.split("_");

				if (order.length > 1) {
					if (order[1].toUpperCase().equals("DESC"))
						query.orderBy(builder.desc(root.get(order[0])));
					else
						query.orderBy(builder.asc(root.get(order[0])));
				} else {
					query.orderBy(builder.asc(root.get(order[0])));
				}
			}

			Query<T> q = session.createQuery(query);

			return q.getResultList();
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	public void save(T dsp) throws Exception, HibernateException, MensagemException {
		this.save(dsp, null);
	}

	public void save(T dsp, Session sessionExterna) throws Exception, HibernateException, MensagemException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		Transaction tx = null;

		try {
			if (!transacaoExterna)
				tx = session.beginTransaction();

			validarCampos(session, dsp);

			beforeSaveOrUpdate(dsp, session);
			session.save(dsp);
			afterSaveOrUpdate(dsp, session);

			session.flush();
			
			if (!transacaoExterna)
				tx.commit();

		} catch (MensagemException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} catch (HibernateException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} catch (Exception ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	public void delete(T dsp) throws Exception, HibernateException, ConstraintViolationException {
		this.delete(dsp, null);
	}

	public void delete(T dsp, Session sessionExterna) throws Exception, HibernateException, MensagemException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			beforeDelete(dsp, session);
			session.delete(dsp);
			afterDelete(dsp, session);

			if (transacaoExterna) {
				tx.commit();
			}

			session.flush();
		} catch (ConstraintViolationException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw new MensagemException(Mensagens.registroPossuiDependencias());
		} catch (HibernateException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} catch (Exception ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	public void update(T dsp) throws Exception, HibernateException, MensagemException {
		this.update(dsp, null);
	}

	public void update(T dsp, Session sessionExterna) throws Exception, HibernateException, MensagemException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		Transaction tx = null;
		try {
			if (!transacaoExterna)
				tx = session.beginTransaction();

			validarCampos(session, dsp);

			beforeSaveOrUpdate(dsp, session);
			session.update(dsp);
			afterSaveOrUpdate(dsp, session);

			if (!transacaoExterna) {
				tx.commit();
			}
		} catch (MensagemException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} catch (HibernateException ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} catch (Exception ex) {
			if (!transacaoExterna)
				tx.rollback();
			throw ex;
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	protected void beforeSaveOrUpdate(T obj, Session session) throws HibernateException, MensagemException, Exception {
	}

	protected void afterSaveOrUpdate(T obj, Session session) throws HibernateException, MensagemException, Exception {
	}

	protected void beforeDelete(T obj, Session session) throws HibernateException, MensagemException, Exception {
	}

	protected void afterDelete(T obj, Session session) throws HibernateException, MensagemException, Exception {
	}

	protected void iniciarLazy(T obj) {
	}

	protected void iniciarLazy(List<T> lista) {
	}

	public boolean isVazio() {
		return this.count() == 0;
	}

	protected abstract void validarCampos(Session session, T obj) throws Exception, MensagemException;
}
