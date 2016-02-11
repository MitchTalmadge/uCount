package com.AptiTekk.Poll.core.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator
public class ImageFileValidator implements Validator {

	public static final String[] VALID_IMAGE_TYPES = { "image/png", "image/jpeg", "image/gif" };

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		Logger.logVerbose("Validating Image Upload...");
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		if (file.getSize() > 1024 * 1024 * 2) {
			Logger.logVerbose("Image is too large. Maximum size is 2MB.");
			msgs.add(new FacesMessage("Image is too large. Maximum size is 2MB."));
		}
		boolean valid = false;
		for (String validType : VALID_IMAGE_TYPES) {
			if (validType.equals(file.getContentType())) {
				valid = true;
			}
		}
		if (!valid) {
			Logger.logVerbose("Invalid type. Use PNG, JPG, or GIF.");
			msgs.add(new FacesMessage("Invalid type. Use PNG, JPG, or GIF."));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		} else {
			Logger.logVerbose("Image is valid.");
		}
	}

}
