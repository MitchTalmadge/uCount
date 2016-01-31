package com.AptiTekk.Poll.web.converters;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;

import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.entityBeans.Poll;

@ManagedBean
@RequestScoped
public class PollConverter implements Converter {

	@Inject
	PollService pollService;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
		if (submittedValue == null || submittedValue.isEmpty()) {
			return null;
		}

		try {
			int pollId = Integer.parseInt(submittedValue);
			return pollService.get(pollId);
		} catch (NumberFormatException e) {
			throw new ConverterException(new FacesMessage(submittedValue + " is not a valid Poll ID!"), e);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
		if (modelValue == null) {
			return "";
		}

		if (modelValue instanceof Poll) {
			return String.valueOf(((Poll) modelValue).getId());
		} else {
			throw new ConverterException(new FacesMessage(modelValue + " is not a valid Poll"));
		}
	}

}
