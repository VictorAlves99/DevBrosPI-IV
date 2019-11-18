package br.com.sistema.util;


public class CnpjCpfValidator {

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {

		if (Caracter.stringIsNullOrEmpty(cpf))
			return false;

		String cpfAjustado = cpf.replace(".", "").replace("-", "");

		if (cpfAjustado.length() != 11)
			return false;

		Integer digito1 = calcularDigito(cpfAjustado.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpfAjustado.substring(0, 9) + digito1, pesoCPF);
		return cpfAjustado.equals(cpfAjustado.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean isValidCNPJ(String cnpj) {

		if (Caracter.stringIsNullOrEmpty(cnpj))
			return false;

		String cpnjAjustado = cnpj.replace(".", "").replace("-", "").replace("/", "");

		if (cpnjAjustado.length() != 14)
			return false;

		Integer digito1 = calcularDigito(cpnjAjustado.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cpnjAjustado.substring(0, 12) + digito1, pesoCNPJ);
		return cpnjAjustado.equals(cpnjAjustado.substring(0, 12) + digito1.toString() + digito2.toString());
	}
}