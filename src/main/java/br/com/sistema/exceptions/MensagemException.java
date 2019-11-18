package br.com.sistema.exceptions;

public class MensagemException extends Exception {

	private static final long serialVersionUID = 7170541050070649516L;

	public MensagemException(String message) {
		super(message);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}