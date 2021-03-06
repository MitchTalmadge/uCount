package org.BinghamTSA.uCount.web.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.BinghamTSA.uCount.core.CredentialService;
import org.BinghamTSA.uCount.core.entityBeans.Credential;
import org.BinghamTSA.uCount.core.utilities.PollLogger;

/**
 * The CredentialsController is the backing bean for the credentials page. It manages everything
 * from uploading credentials to editing and deleting them.
 */
@ManagedBean
@ViewScoped
public class CredentialsController {

  @EJB
  CredentialService credentialService;

  private Part importedCredentials;

  private List<Credential> credentials;

  private int credentialIdCurrentlyEditing = -1;

  private int editableStudentId = -1;

  @PostConstruct
  public void init() {
    credentials = credentialService.getAll();
  }

  public void addCredential() {
    Credential credential = new Credential();
    credentialService.insert(credential);
    credentials.add(credential);
  }

  /**
   * Imports a CSV file with a list of credentials.
   */
  public void importCredentials() {
    PollLogger.logVerbose("Importing Credentials File...");
    if (importedCredentials != null) {
      try {
        InputStream inputStream = importedCredentials.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          if (line.equals("studentId"))
            continue;
          else {
            try {
              int studentId = Integer.parseInt(line);
              Credential credential = new Credential(studentId);
              credentialService.insert(credential);
            } catch (NumberFormatException e) {
              PollLogger.logVerbose("Couldn't read Student ID (Line = " + line + "). Skipping...");
            }
          }
        }

        bufferedReader.close();
        inputStream.close();

        // Refresh page
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);
        root.setViewId(viewId);
        context.setViewRoot(root);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void beginEditingCredential(Credential credential) {
    this.setCredentialIdCurrentlyEditing(credential.getId());
    this.setEditableStudentId(credential.getStudentId());
  }

  public void finishEditingCredential() {
    if (this.getEditableStudentId() != -1) {
      for (Credential credential : credentials) {
        if (credential.getId() == getCredentialIdCurrentlyEditing()) {
          credential.setId(getEditableStudentId());
          credentialService.merge(credential);
          break;
        }
      }

      setCredentialIdCurrentlyEditing(-1);
    }
  }

  public void deleteCredential(Credential credential) {
    if (credential != null) {
      credentialService.delete(credential.getId());
      Iterator<Credential> iterator = credentials.iterator();
      while (iterator.hasNext()) {
        Credential c = iterator.next();
        if (c.getId() == credential.getId())
          iterator.remove();
      }
      setCredentialIdCurrentlyEditing(-1);
    }
  }

  public void deleteAllCredentials() {
    credentialService.deleteAllCredentials();
    credentials.clear();
  }

  public List<Credential> getCredentials() {
    return credentials;
  }

  public Part getImportedCredentials() {
    return importedCredentials;
  }

  public void setImportedCredentials(Part importedCredentials) {
    this.importedCredentials = importedCredentials;
  }

  public int getCredentialIdCurrentlyEditing() {
    return credentialIdCurrentlyEditing;
  }

  public void setCredentialIdCurrentlyEditing(int credentialIdCurrentlyEditing) {
    this.credentialIdCurrentlyEditing = credentialIdCurrentlyEditing;
  }

  public int getEditableStudentId() {
    return editableStudentId;
  }

  public void setEditableStudentId(int editableStudentId) {
    this.editableStudentId = editableStudentId;
  }

}
