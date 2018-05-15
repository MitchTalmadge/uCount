package org.BinghamTSA.uCount.core.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 * Used for validating an uploaded Credentials CSV file. Accepts CSV files, with a maximum size of
 * 2MB.
 */
@FacesValidator
public class CredentialsFileValidator implements Validator {

  public static final String[] VALID_FILE_TYPES = {"text/csv"};

  @Override
  public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {
    PollLogger.logVerbose("Validating Credentials Upload...");
    List<FacesMessage> msgs = new ArrayList<FacesMessage>();
    Part file = (Part) value;
    if (file == null) {
      PollLogger.logVerbose("No credentials file was uploaded.");
      return;
    }
    if (file.getSize() > 1024 * 1024 * 2) {
      PollLogger.logVerbose("Credentials file is too large. Maximum size is 2MB.");
      msgs.add(new FacesMessage("Credentials file is too large. Maximum size is 2MB."));
    }
    boolean valid = false;
    if (file.getContentType().equals("application/octet-stream")) { // Unknown
      // file
      // type
      try {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = bufferedReader.readLine();
        if (line != null)
          if (line.equals("studentId"))
            valid = true;

        bufferedReader.close();
        inputStream.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      for (String validType : VALID_FILE_TYPES) {
        if (validType.equals(file.getContentType())) {
          valid = true;
        }
      }
    }
    if (!valid) {
      PollLogger.logVerbose("Invalid type. Use CSV. (Given was " + file.getContentType() + ")");
      msgs.add(new FacesMessage("Invalid type. Use CSV."));
    }
    if (!msgs.isEmpty()) {
      throw new ValidatorException(msgs);
    } else {
      PollLogger.logVerbose("Credentials file is valid.");
    }
  }

}
