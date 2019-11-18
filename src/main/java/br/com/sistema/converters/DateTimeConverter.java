package br.com.sistema.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "dataConverter")
public class DateTimeConverter implements Converter {

	private String formato = "dd/MM/yyyy HH:mm";

	public DateTimeConverter() {

	}

	public DateTimeConverter(String formato) {
		this.formato = formato;
	}

	public Object getAsObject(FacesContext arg0, UIComponent arg1,
			String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		Date result = null;
		try {
			result = sdf.parse(dateStr);
		} catch (ParseException e) {
		}
		return result;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		Date date = (Date) value;
		String result = null;

		result = sdf.format(date);

		return result;
	}

}