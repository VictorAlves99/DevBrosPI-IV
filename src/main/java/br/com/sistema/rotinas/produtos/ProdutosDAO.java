package br.com.sistema.rotinas.produtos;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import br.com.sistema.enuns.TipoPlataformaProduto;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.DAO;

public class ProdutosDAO extends DAO<Produto> {

	public ProdutosDAO() {
		this.classe = Produto.class;
	}

	public Produto load(String codigoBarras, boolean somenteAtivos, Session sessionExterna) {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna)
			session = this.getSessionPronta();
		else
			session = sessionExterna;

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.select(root).where(builder.equal(root.get("codigoDeBarras"), codigoBarras));

			Query<Produto> q = session.createQuery(query);
			return q.getSingleResult();

		} catch (ObjectNotFoundException eo) {
			return null;
		} finally {

			if (!transacaoExterna)
				session.close();
		}
	}

	public List<Produto> getListaParaPesquisa(String texto) {

		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.orderBy(builder.asc(root.get("codigoDeBarras"))).select(root)
					.where(builder.like(builder.lower(root.<String>get("descricao")), "%" + texto.toLowerCase() + "%"));

			Query<Produto> q = session.createQuery(query);

			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public List<Produto> getListaParaPesquisaHome(String texto) {

		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.orderBy(builder.asc(root.get("codigoDeBarras"))).select(root)
					.where(builder.like(builder.lower(root.<String>get("nome")), "%" + texto.toLowerCase() + "%"));

			Query<Produto> q = session.createQuery(query);

			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public List<Produto> getListaParaPesquisaADM(String texto) {

		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.orderBy(builder.asc(root.get("codigoDeBarras"))).select(root)
					.where(builder.like(builder.lower(root.<String>get("descricao")), "%" + texto.toLowerCase() + "%"));

			Query<Produto> q = session.createQuery(query);

			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public boolean registroJaExiste(Produto produto) throws HibernateException {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.select(root).where(builder.equal(root.get("nome"), produto.getNome()),
					builder.notEqual(root.get("id"), produto.getId()));

			Query<Produto> q = session.createQuery(query);
			return q.getResultList().size() > 0;
		} finally {
			session.close();
		}
	}

	public Produto getProdutoPorDescricao(String descricao) throws HibernateException {

		Session session = this.getSessionPronta();

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.select(root).where(builder.equal(root.get("descricao"), descricao));

			Query<Produto> q = session.createQuery(query);

			return q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public boolean isCodigoDeBarrasJaExiste(Produto produto) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.select(root).where(builder.notEqual(root.get("id"), produto.getId()),
					builder.equal(root.get("codigoDeBarras"), produto.getCodigoDeBarras()));

			Query<Produto> q = session.createQuery(query);

			return q.getResultList().size() > 0;
		} finally {
			session.close();
		}
	}

	public boolean isNomeJaExiste(Produto produto) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
			Root<Produto> root = query.from(Produto.class);

			query.select(root).where(builder.notEqual(root.get("id"), produto.getId()),
					builder.equal(root.get("nome"), produto.getNome()),
					builder.equal(root.get("plataforma"), produto.getPlataforma()));

			Query<Produto> q = session.createQuery(query);
			return q.getResultList().size() > 0;
		} finally {
			session.close();
		}
	}

	public CarrinhoItem getCarrinhoItem(CarrinhoItem c) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CarrinhoItem> query = builder.createQuery(CarrinhoItem.class);
			Root<CarrinhoItem> root = query.from(CarrinhoItem.class);

			query.select(root).where(builder.equal(root.get("id"), c.getId()));

			Query<CarrinhoItem> q = session.createQuery(query);

			for (CarrinhoItem item : q.getResultList()) {
				Hibernate.initialize(item.getProduto());
				Hibernate.initialize(item.getQuantidade());
			}

			return q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public List<CarrinhoItem> getItensDoCarrinho(Carrinho carrinho) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CarrinhoItem> query = builder.createQuery(CarrinhoItem.class);
			Root<CarrinhoItem> root = query.from(CarrinhoItem.class);

			query.select(root).where(builder.equal(root.get("carrinho"), carrinho));

			Query<CarrinhoItem> q = session.createQuery(query);

			for (CarrinhoItem item : q.getResultList()) {
				Hibernate.initialize(item.getProduto());
				Hibernate.initialize(item.getQuantidade());
			}

			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public void validarCampos(Session session, Produto produto) throws Exception, MensagemException {

		if (Caracter.stringIsNullOrEmpty(produto.getNome())) {
			throw new MensagemException(Mensagens.campoObrigatorio("Nome"));
		}

		if (produto.getQuantidade() <= 0) {
			throw new MensagemException(Mensagens.getMensagem("Quantidade não pode ser menor ou igual a zero!"));
		}

		if (Caracter.stringIsNullOrEmpty(produto.getCodigoDeBarras())) {
			throw new MensagemException(Mensagens.campoObrigatorio("Código de Barras"));
		}

		if (produto.getPlataforma() == TipoPlataformaProduto.NaoDefinido) {
			throw new MensagemException(Mensagens.campoObrigatorio("Plataforma"));
		}

		if (produto.getPreco() <= 0) {
			throw new MensagemException(Mensagens.campoObrigatorio("Preço"));
		}

		if (produto.getId() == 0) {

			if (isCodigoDeBarrasJaExiste(produto)) {
				throw new MensagemException(Mensagens.getMensagem("O Produto com código de barras '"
						+ produto.getCodigoDeBarras()
						+ "' já se encontra cadastrado, utilize a pesquisa e encontre-o, caso precise fazer alguma alteração."));
			}

			if (isNomeJaExiste(produto)) {
				throw new MensagemException(Mensagens.getMensagem(
						"O Produto '" + produto.getNome() + "' já se encontra cadastrado para esta plataforma"
								+ ", utilize a pesquisa e encontre-o, caso precise fazer alguma alteração."));
			}
		}

	}

}
