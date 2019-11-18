/*package br.com.sistema.rotinas.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import br.com.sistema.enuns.TipoStatusCotacao;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.rotinas.cotacao.Cotacao;
import br.com.sistema.rotinas.cotacao.CotacaoItemResposta;
import br.com.sistema.rotinas.empresa.Empresa;
import br.com.sistema.util.DAO;

public class DashboardDAO extends DAO<Cotacao>{

	public DashboardDAO() {

	}
	
	public double calculaValores(Empresa empresa, Date dataInicial, Date dataFinal) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Cotacao> query = builder.createQuery(Cotacao.class);
			Root<Cotacao> root = query.from(Cotacao.class);
			
			Predicate pred = builder.and();
			
			pred = builder.and(pred ,builder.equal(root.get("status"), TipoStatusCotacao.Aprovado),builder.between(root.<Date>get("dataDeEmissao"), dataInicial, dataFinal));
			
			if(empresa != null) {
				pred = builder.and(pred,builder.equal(root.get("empresa"), empresa));
			}
			
			query.select(root).where(pred);
			
			Query<Cotacao> q = session.createQuery(query);
			
			double preco = 0;
			
			for(Cotacao cot : q.getResultList()) {
				preco += pegarPreco(cot);
				preco = preco*100;
				preco = Math.round(preco);
				preco = preco/100;
			}
						
			return preco;
		} finally {
			session.close();
		}
	}
	
	public List<CotacaoItemResposta> calculaValoresPizza(Empresa empresa, Date dataInicial, Date dataFinal) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Cotacao> query = builder.createQuery(Cotacao.class);
			Root<Cotacao> root = query.from(Cotacao.class);
			
			Predicate pred = builder.and();
			
			pred = builder.and(pred ,builder.equal(root.get("status"), TipoStatusCotacao.Aprovado),builder.between(root.<Date>get("dataDeEmissao"), dataInicial, dataFinal));
			
			if(empresa != null) {
				pred = builder.and(pred,builder.equal(root.get("empresa"), empresa));
			}
			
			query.select(root).where(pred);
			
			Query<Cotacao> q = session.createQuery(query);
			
			List<CotacaoItemResposta> listaCotacoes = new ArrayList<>();
			
			for(Cotacao cot : q.getResultList()) {
				for(CotacaoItemResposta cotItem : adicionarCotacaoItemResposta(cot)) {
					listaCotacoes.add(cotItem);
				}
			}
			
			return listaCotacoes;
		} finally {
			session.close();
		}
	}
	
	public List<CotacaoItemResposta> adicionarCotacaoItemResposta(Cotacao cot) {
		
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CotacaoItemResposta> query = builder.createQuery(CotacaoItemResposta.class);
			Root<CotacaoItemResposta> root = query.from(CotacaoItemResposta.class);

			Predicate pred = builder.and();
			
			pred = builder.and(pred ,builder.equal(root.get("item").get("cotacao"), cot));
			
			pred = builder.and(pred,builder.equal(root.get("aprovada"), true));
			
			query.select(root).where(pred);
			
			Query<CotacaoItemResposta> q = session.createQuery(query);
			
			return q.getResultList();
		}finally {
			session.close();
		}
	}
	
	public double pegarCotacoes(Empresa empresa, Date dataInicial, Date dataFinal) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CotacaoItemResposta> query = builder.createQuery(CotacaoItemResposta.class);
			Root<CotacaoItemResposta> root = query.from(CotacaoItemResposta.class);
			
			Predicate pred = builder.and();
			
			pred = builder.and(pred ,builder.equal(root.get("status"), TipoStatusCotacao.Aprovado),builder.between(root.<Date>get("dataHora"), dataInicial, dataFinal));
			
			if(empresa != null) {
				pred = builder.and(pred,builder.equal(root.get("empresa"), empresa));
			}
			
			query.select(root).where(pred);
			
			Query<CotacaoItemResposta> q = session.createQuery(query);
			
			double preco = 0;
			
			for(CotacaoItemResposta cot : q.getResultList()) {
				preco += cot.getPreco();
				preco = preco*100;
				preco = Math.round(preco);
				preco = preco/100;
			}
						
			return preco;
		} finally {
			session.close();
		}
	}
	
	public double calculaCredito(Empresa empresa, Date dataInicial, Date dataFinal) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Cotacao> query = builder.createQuery(Cotacao.class);
			Root<Cotacao> root = query.from(Cotacao.class);
			
			Predicate pred = builder.and();
			
			pred = builder.and(pred ,builder.equal(root.get("status"), TipoStatusCotacao.Aprovado));
			
			pred = builder.and(pred,builder.between(root.<Date>get("dataDeEmissao"), dataInicial, dataFinal));
			
			if(empresa != null) {
				pred = builder.and(pred,builder.equal(root.get("empresa"), empresa));
			}
			
			query.select(root).where(pred);
			
			Query<Cotacao> q = session.createQuery(query);
			
			double preco = 0;
			
			for(Cotacao cot : q.getResultList()) {
				preco += pegarPreco(cot,empresa);
				preco = preco*100;
				preco = Math.round(preco);
				preco = preco/100;
			}
						
			return preco;
		} finally {
			session.close();
		}
	}
	
	public int calculaCompras(Empresa empresa, Date dataInicial, Date dataFinal){
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Cotacao> query = builder.createQuery(Cotacao.class);
			Root<Cotacao> root = query.from(Cotacao.class);

			Predicate pred = builder.and();
			
			pred = builder.and(pred, builder.equal(root.get("status"), TipoStatusCotacao.Aprovado));
			
			pred = builder.and(pred, builder.between(root.<Date>get("dataDeEmissao"), dataInicial, dataFinal));
					
			if(empresa != null) {
				pred = builder.and(pred,builder.equal(root.get("empresa"), empresa));
			}
			
			query.select(root).where(pred);
			
			Query<Cotacao> q = session.createQuery(query);
			
			return q.getResultList().size();

		} finally {
			session.close();
		}
	}
	
	public double pegarPreco(Cotacao cot, Empresa empresa) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CotacaoItemResposta> query = builder.createQuery(CotacaoItemResposta.class);
			Root<CotacaoItemResposta> root = query.from(CotacaoItemResposta.class);
			
			query.select(root).where(builder.equal(root.get("aprovada"), true),builder.equal(root.get("empresa"),empresa),builder.equal(root.get("item").get("cotacao"), cot));
			
			Query<CotacaoItemResposta> q = session.createQuery(query);
			
			double precoItem = 0;
			for(CotacaoItemResposta item : q.getResultList()) {
				precoItem += item.getPreco();
			}
			
			return precoItem;
		} finally {
			session.close();
		}
	}
	
	public double pegarPreco(Cotacao cot) {
		Session session = this.getSessionPronta();
		
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CotacaoItemResposta> query = builder.createQuery(CotacaoItemResposta.class);
			Root<CotacaoItemResposta> root = query.from(CotacaoItemResposta.class);
			
			query.select(root).where(builder.equal(root.get("aprovada"), true),builder.equal(root.get("item").get("cotacao"), cot));
			
			Query<CotacaoItemResposta> q = session.createQuery(query);
			
			double precoItem = 0;
			
			for(CotacaoItemResposta item : q.getResultList()) {
				precoItem += item.getPreco();
			}
			
			return precoItem;
		} finally {
			session.close();
		}
	}

	@Override
	protected void validarCampos(Session session, Cotacao obj) throws Exception, MensagemException {
		
	}
	
}

*/