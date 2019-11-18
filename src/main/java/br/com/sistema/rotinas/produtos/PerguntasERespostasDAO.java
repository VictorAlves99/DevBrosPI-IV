package br.com.sistema.rotinas.produtos;

import org.hibernate.Session;

import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.util.DAO;

public class PerguntasERespostasDAO extends DAO<PerguntasERespostas> {

	public PerguntasERespostasDAO() {
		this.classe = PerguntasERespostas.class;
	}

	@Override
	protected void validarCampos(Session session, PerguntasERespostas obj) throws Exception, MensagemException {
		// TODO Auto-generated method stub
		
	}

}
