package br.com.sistema.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensagens {

	public static String campoObrigatorio(String campo) {
		return "O preenchimento do campo " + campo + " é obrigatório.";
	}

	public static String registroPossuiDependencias() {
		return "Este registro não pode ser Excluído, por que ele possui dependências!";
	}

	public static void registroNaoEncontrado(String nomeDoRegistro) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(nomeDoRegistro
				+ " não encontrado(a) no Banco de Dados"));
	}

	public static void gerarMensagemGenerica(String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(mensagem));
	}

	public static void gerarMensagemException(Exception e) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null,new FacesMessage("Ocorreu um erro na execução do processo:\nErro: " + e.getMessage()));
	}

	public static String getMensagem(String mensagem) {
		return mensagem;
	}

	public static void nenhumRegistroSelecionado() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(
				"Nenhum registro foi selecionado."));

	}
	
	public static void naoHaDados() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(
				"Não existem dados nas condições solicitadas."));

	}
}
