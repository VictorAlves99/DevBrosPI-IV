package br.com.sistema.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.pesquisa.PesquisaCondicao;

@SuppressWarnings("rawtypes")
public class AutoComplete<T> implements Serializable {

	private static final long serialVersionUID = 4058541891262983714L;

	private Class classe;
	private String campoBusca;
	private List<PesquisaCondicao> condicoesEqualFixa;
	private List<PesquisaCondicao> condicoesLikeFixa;
	private static final int ROW_COUNT = 20;

	private List<T> lista;

	public AutoComplete(String campoBusca, Class classe) {
		this.condicoesEqualFixa = new ArrayList<PesquisaCondicao>();
		this.condicoesLikeFixa = new ArrayList<PesquisaCondicao>();

		this.classe = classe;
		this.campoBusca = campoBusca;
	}

	@SuppressWarnings("unchecked")
	public List<T> complete(String like) {
		Session session = HibernateUtil.getSession();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(this.classe);
			Root<T> root = query.from(this.classe);

			Predicate pred = builder.and();

			for (PesquisaCondicao p : condicoesEqualFixa) {
				String campo = p.getCampo();

				pred = builder.and(pred, builder.equal(root.get(campo), p.getValor()));
			}

			for (PesquisaCondicao p : condicoesLikeFixa) {
				String campo = p.getCampo();

				pred = builder.and(pred, builder.like(builder.lower(root.<String>get(campo)),
						"%" + p.getValor().toString().toLowerCase() + "%"));
			}

			for (String c : campoBusca.split("_")) {
				pred = builder.and(pred,
						builder.like(builder.lower(root.<String>get(c)), "%" + like.toLowerCase() + "%"));
			}

			query.where(pred);

			Query<T> q = session.createQuery(query).setMaxResults(10);

			this.setLista(q.getResultList());

			return this.getLista();

		} catch (Exception e) {
		} finally {
			session.close();
		}

		return new ArrayList<T>();
	}

	public Class getClasse() {
		return classe;
	}

	public void setClasse(Class classe) {
		this.classe = classe;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public List<PesquisaCondicao> getCondicoesEqualFixa() {
		return condicoesEqualFixa;
	}

	public void setCondicoesEqualFixa(List<PesquisaCondicao> condicoesEqualFixa) {
		this.condicoesEqualFixa = condicoesEqualFixa;
	}

	public List<PesquisaCondicao> getCondicoesLikeFixa() {
		return condicoesLikeFixa;
	}

	public void setCondicoesLikeFixa(List<PesquisaCondicao> condicoesLikeFixa) {
		this.condicoesLikeFixa = condicoesLikeFixa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getRowCount() {
		return ROW_COUNT;
	}

	public List<T> getLista() {
		return lista;
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}

}
