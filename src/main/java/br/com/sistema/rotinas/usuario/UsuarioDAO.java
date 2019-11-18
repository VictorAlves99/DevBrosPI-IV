package br.com.sistema.rotinas.usuario;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.seguranca.TripleDES;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.DAO;

public class UsuarioDAO extends DAO<Usuario> {

	public UsuarioDAO() {
		this.classe = Usuario.class;
	}

	public Usuario load(String email, boolean carregarGrupoDeAcesso) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.select(root).where(builder.equal(root.get("email"), email));

			Query<Usuario> q = session.createQuery(query);

			return q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public Usuario load(String email) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.select(root).where(builder.equal(root.get("email"), email));

			Query<Usuario> q = session.createQuery(query);
			return q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public boolean verificaEmail(String email) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.select(builder.countDistinct(root)).where(builder.equal((root.get("email")), email));

			Long totalRecordCount = session.createQuery(query).getSingleResult();

			return totalRecordCount > 0;
		} finally {
			session.close();
		}
	}

	public Usuario autenticarUsuario(String email, String senha) throws MensagemException, Exception {

		Usuario usu = new Usuario();

		if (verificaEmail(email)) {
			usu = this.load(email, true);
		} else {
			usu = null;
		}

		boolean autenticado = usu != null
				&& new TripleDES(Constantes.KEY_TRIPLE_DES).decrypt(usu.getSenha()).equals(senha);

		if (!autenticado)
			throw new MensagemException(Mensagens.getMensagem("Usuário ou senha inválida."));

		return usu;
	}

	public Usuario getUsuarioPorUsuarioKey(String usuarioKey) {
		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.select(root).where(builder.equal(root.get("usuarioKey"), usuarioKey));

			Query<Usuario> q = session.createQuery(query);
			return q.getSingleResult();
		} finally {
			session.close();
		}
	}

	public List<Usuario> getListaParaUsuario(String nome) {

		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.select(root)
					.where(builder.like(builder.lower(root.<String>get("nome")), "%" + nome.toLowerCase() + "%"));

			Query<Usuario> q = session.createQuery(query);
			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public List<Usuario> getListaParaUsuarioADM(String nome) {

		Session session = this.getSessionPronta();
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
			Root<Usuario> root = query.from(Usuario.class);

			query.orderBy(builder.asc(root.get("id"))).select(root)
					.where(builder.like(builder.lower(root.<String>get("nome")), "%" + nome.toLowerCase() + "%"));

			Query<Usuario> q = session.createQuery(query);
			return q.getResultList();
		} finally {
			session.close();
		}
	}

	public boolean usuarioJaExiste(Usuario usuario, Session sessionExterna) throws HibernateException {
		CriteriaBuilder builder = sessionExterna.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);

		query.select(root).where(builder.equal(root.get("email"), usuario.getEmail()),
				builder.notEqual(root.get("id"), usuario.getId()));

		Query<Usuario> q = sessionExterna.createQuery(query);
		return q.getResultList().size() > 0;
	}

	public void deleteUsu(Usuario dsp) throws Exception, HibernateException, ConstraintViolationException {
		this.deleteUsu(dsp, null);
	}

	public void deleteUsu(Usuario dsp, Session sessionExterna) throws Exception, HibernateException, MensagemException {

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
			throw new MensagemException(
					Mensagens.getMensagem("Esse usuário possui dependências! Você não pode excluí-lo."));
		} finally {
			if (!transacaoExterna)
				session.close();
		}
	}

	@Override
	protected void validarCampos(Session session, Usuario usuario) throws Exception, MensagemException {

		if (Caracter.stringIsNullOrEmpty(usuario.getNome())) {
			throw new MensagemException(Mensagens.campoObrigatorio("Nome"));
		}

		if (Caracter.stringIsNullOrEmpty(usuario.getEmail())) {
			throw new MensagemException(Mensagens.campoObrigatorio("E-mail"));
		}

		if (Caracter.stringIsNullOrEmpty(usuario.getCelular())) {
			throw new MensagemException(Mensagens.campoObrigatorio("Celular"));
		}

		if (Caracter.stringIsNullOrEmpty(usuario.getCpf())) {
			throw new MensagemException(Mensagens.campoObrigatorio("CPF"));
		}

		if (usuario.getDataDeNascimento() == null) {
			throw new MensagemException(Mensagens.campoObrigatorio("Data de Nascimento"));
		}

		if (usuario.getDataDeCadastro() == null) {
			throw new MensagemException(Mensagens.campoObrigatorio("Data de Cadastro"));
		}

		if (Caracter.stringIsNullOrEmpty(usuario.getSenha())) {
			throw new MensagemException(Mensagens.campoObrigatorio("Senha"));
		}

		if (!new TripleDES(Constantes.KEY_TRIPLE_DES).decrypt(usuario.getSenha()).equals(usuario.getConfirmacaoSenha()))
			throw new MensagemException(Mensagens.getMensagem("A confirmação da senha está incorreta."));

		if (usuarioJaExiste(usuario, session)) {
			throw new MensagemException(
					Mensagens.getMensagem("Já existe um usuário cadastrado como " + usuario.getEmail() + "."));
		}

		if (usuarioJaExistePorCPF(usuario, session)) {
			throw new MensagemException(
					Mensagens.getMensagem("Já existe um usuário cadastrado como " + usuario.getCpf() + "."));
		}

	}

	private boolean usuarioJaExistePorCPF(Usuario usuario, Session session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		Root<Usuario> root = query.from(Usuario.class);

		query.select(root).where(builder.equal(root.get("cpf"), usuario.getCpf()),
				builder.notEqual(root.get("id"), usuario.getId()));

		Query<Usuario> q = session.createQuery(query);
		return q.getResultList().size() > 0;

	}

	public UsuarioEndereco getEnderecoPorString(String endereco, Usuario usuario) {

		Session session = this.getSessionPronta();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<UsuarioEndereco> query = builder.createQuery(UsuarioEndereco.class);
		Root<UsuarioEndereco> root = query.from(UsuarioEndereco.class);

		query.select(root).where(builder.equal(root.get("usuario"),usuario),builder.equal(root.get("endereco"),endereco));

		Query<UsuarioEndereco> q = session.createQuery(query);
		
		return q.getSingleResult();

	}

}
