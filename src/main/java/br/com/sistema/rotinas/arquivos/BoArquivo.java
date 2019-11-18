package br.com.sistema.rotinas.arquivos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import br.com.sistema.amazon.AmazonUtil;
import br.com.sistema.enuns.TipoFormatoDeArquivo;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.util.Caracter;

public class BoArquivo {

	private int identificacao;
	private String bucketName;

	public BoArquivo(int identificacao, String bucketName) throws MensagemException {

		if (this.identificacao < 0)
			throw new MensagemException(Mensagens.getMensagem("Não foi informado o número de Identificação."));

		if (Caracter.stringIsNullOrEmpty(bucketName))
			throw new MensagemException(Mensagens.getMensagem("Não foi informado o nome do bucket."));

		this.identificacao = identificacao;
		this.bucketName = bucketName;
	}

	protected Session getSessionPronta() {
		return HibernateUtil.getSession();
	}

	public void carregarStream(Arquivo arq) throws IOException, MensagemException {
		arq.setStream(new AmazonUtil(identificacao, this.bucketName).downloadS3(arq.getNome()));
	}

	public void carregarURL(Arquivo arq) throws IOException, MensagemException {

		String url = new AmazonUtil(identificacao, this.bucketName).gerarURL(arq.getNome()).toString();

		arq.setUrl(url);
	}

	public List<Arquivo> getListaDeArquivos(TipoOrigemArquivoBinario origem, int idExterno, boolean carregarBinarios)
			throws HibernateException, IOException, MensagemException {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.orderBy(builder.desc(root.get("dataUpload"))).select(root)
					.where(builder.equal(root.get("idExterno"), idExterno), builder.equal(root.get("origem"), origem));

			Query<Arquivo> q = session.createQuery(query);

			List<Arquivo> lista = q.getResultList();
			if (carregarBinarios) {
				for (Arquivo a : lista) {
					this.carregarStream(a);
				}
			}

			return lista;
		} finally {
			session.close();
		}
	}

	public Arquivo getArquivoPeloID(int idDoArquivo, boolean carregarStream)
			throws HibernateException, IOException, MensagemException {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.select(root).where(builder.equal(root.get("id"), idDoArquivo));

			Query<Arquivo> q = session.createQuery(query);

			Arquivo arq = q.getSingleResult();

			if (arq != null && carregarStream)
				this.carregarStream(arq);

			return arq;
		} finally {
			session.close();
		}
	}

	public Arquivo getArquivoPeloNome(TipoOrigemArquivoBinario origem, String nome, boolean carregarStream)
			throws HibernateException, IOException, MensagemException {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.select(root).where(builder.equal(root.get("nome"), nome));

			Query<Arquivo> q = session.createQuery(query);

			Arquivo arq = q.getSingleResult();

			if (arq != null && carregarStream)
				this.carregarStream(arq);

			return arq;
		} finally {
			session.close();
		}
	}

	public Long contarArquivos(TipoOrigemArquivoBinario origem, int idExterno) throws HibernateException {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.select(root).where(builder.equal(root.get("idExterno"), idExterno),
					builder.equal(root.get("origem"), origem));

			Query<Arquivo> q = session.createQuery(query);

			List<Arquivo> lista = q.getResultList();

			return Long.parseLong(String.valueOf(lista.size()));
		} finally {
			session.close();
		}
	}

	public Double getTamanhoTotalDosArquivos() {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.select(root);

			Query<Arquivo> q = session.createQuery(query);

			double soma = 0.0;

			for (Arquivo arq : q.getResultList()) {
				soma += arq.getTamanho();
			}

			return soma;
		} finally {
			session.close();
		}
	}

	public void deletarArquivoPeloID(int idDoArquivo) throws HibernateException, MensagemException, Exception {
		this.deletarArquivoPeloID(idDoArquivo, null);
	}

	public void deletarArquivoPeloID(int idDoArquivo, Session sessionExterna)
			throws Exception, HibernateException, MensagemException {

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

			Arquivo arq = getArquivoPeloID(idDoArquivo, false);

			session.delete(arq);
			new AmazonUtil(identificacao, bucketName).deleteS3(arq.getNome());

			if (!transacaoExterna) {
				tx.commit();
				session.flush();
			}
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

	public void salvarArquivo(Arquivo arq) throws HibernateException, MensagemException, Exception {
		this.salvarArquivo(arq, null);
	}

	public void salvarArquivo(Arquivo arq, Session sessionExterna)
			throws Exception, HibernateException, MensagemException {

		boolean transacaoExterna = sessionExterna != null;

		Session session = null;

		if (!transacaoExterna) {
			session = this.getSessionPronta();
		} else {
			session = sessionExterna;
		}
		Transaction tx = null;
		try {
			if (!transacaoExterna)
				tx = session.beginTransaction();

			validarArquivo(arq, bucketName);
			
			
			if (this.arquivoJaExiste(arq)) {
				session.update(arq);
				new AmazonUtil(identificacao, bucketName).uploadS3(arq.getNome(),
						new ByteArrayInputStream(arq.getBinario()), true, arq.getFormato().getContentType());

			} else {
				session.save(arq);
				new AmazonUtil(identificacao, bucketName).uploadS3(arq.getNome(),
						new ByteArrayInputStream(arq.getBinario()), true, arq.getFormato().getContentType());
			}

			if (!transacaoExterna) {
				tx.commit();
			} else {
				session.flush();
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

	private boolean arquivoJaExiste(Arquivo arq) {

		Session session = this.getSessionPronta();

		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Arquivo> query = builder.createQuery(Arquivo.class);
			Root<Arquivo> root = query.from(Arquivo.class);

			query.select(root).where(builder.equal(root.get("nome"), arq.getNome()),
					builder.equal(root.get("idExterno"), arq.getIdExterno()));

			Query<Arquivo> q = session.createQuery(query);

			return q.getResultList().size() > 0;
		} finally {
			session.close();
		}

	}

	protected void validarArquivo(Arquivo a, String bucketName) throws Exception {

		a.setFormato(TipoFormatoDeArquivo.getFormatoDeArquivoPelaExtensao(a.getNome()));

		if (Caracter.stringIsNullOrEmpty(a.getNome()))
			throw new MensagemException(Mensagens.campoObrigatorio("Tamanho do Arquivo"));

		if (a.getStream() == null)
			throw new MensagemException(Mensagens.getMensagem("Necessário fazer upload correto do arquivo"));

		if (a.getTamanho() <= 0)
			throw new MensagemException(Mensagens.getMensagem(
					"O tamanho do arquivo não foi informado, favor verificar com o supervisor do Sistema."));

		if (a.getFormato() == TipoFormatoDeArquivo.NaoDefinido) {
			throw new MensagemException(Mensagens.campoObrigatorio("Formato do Arquivo"));

		}
	}

}