package br.com.sistema.rotinas.vendas;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.rotinas.produtos.CarrinhoItem;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.DAO;

public class VendaDAO extends DAO<Venda> {

	public VendaDAO() {
		this.classe = Venda.class;
	}

	public void validarCampos(Session session, Venda produto) throws Exception, MensagemException {

	}

	public List<Venda> getListaPorData(Date dataInicio, Date dataFim, Usuario usuario, String textoPesquisa) throws MensagemException {		
		Session session = HibernateUtil.getSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Venda> query = builder.createQuery(Venda.class);
			Root<Venda> root = query.from(Venda.class);

			Predicate pred = builder.and();
			
			if(dataInicio != null && dataFim != null) 
				pred = builder.and(pred , builder.between(root.<Date>get("dataVenda"), dataInicio, dataFim));
				
			if(usuario != null) 
				pred = builder.and(pred , builder.equal(root.get("cliente"), usuario));
			
			if(!Caracter.stringIsNullOrEmpty(textoPesquisa)) 
				pred = builder.and(pred , builder.equal(root.get("numeroDaVenda"), textoPesquisa));
			
			query.select(root).where(pred);

			Query<Venda> q = session.createQuery(query);

			List<Venda> lista = q.getResultList();

			return lista;

		} catch (HibernateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		} finally {
			session.close();
		}

	}

	public Venda loadVenda(int id, String numeroDaVenda) {
		Session session = HibernateUtil.getSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Venda> query = builder.createQuery(Venda.class);
			Root<Venda> root = query.from(Venda.class);

			query.select(root).where(builder.equal(root.get("id"), id),builder.equal(root.get("numeroDaVenda"), numeroDaVenda));

			Query<Venda> q = session.createQuery(query);

			Venda venda = q.getSingleResult();

			for(CarrinhoItem c : venda.getItens()) {
				Hibernate.initialize(c);
				Hibernate.initialize(c.getProduto());
			}
			
			return venda;

		} catch (HibernateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		} finally {
			session.close();
		}
	}

}
