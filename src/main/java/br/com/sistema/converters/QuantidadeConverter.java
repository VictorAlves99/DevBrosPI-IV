//http://www.esseconhece.com.br/programacao/java/formatando-campos-monetarios-no-jsf-com-jquery-2/
package br.com.sistema.converters;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sistema.util.Caracter;

@FacesConverter(forClass = Float.class, value = "quantidadeConverter")
public class QuantidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (!Caracter.stringIsNullOrEmpty(value)) {

			String str = value.trim();
			boolean negativo = str.substring(0, 1).equals("-");

			StringBuilder sb = new StringBuilder();

			sb.append((negativo ? '-' : "") + "0");

			for (int i = 0; i < str.length(); i++) {

				char ch = str.charAt(i);

				if (Character.isDigit(ch) || ch == ',') {

					if (ch == ',')
						sb.append('.');
					else
						sb.append(ch);
				}
			}

			return new Float(sb.toString());
		}

		return new Float(0);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null) {
			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.setMaximumFractionDigits(3);
			decimalFormat.setMinimumFractionDigits(3);
			String valorArredondado = decimalFormat.format(value);

			StringBuilder s = new StringBuilder(valorArredondado);

			return s.toString();
		}
		return "0";
	}
}