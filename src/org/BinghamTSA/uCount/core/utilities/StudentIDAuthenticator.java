package org.BinghamTSA.uCount.core.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.BinghamTSA.uCount.core.CredentialService;
import org.BinghamTSA.uCount.core.entityBeans.Credential;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * This class contains methods for each type of student ID authentication.
 */
@Stateless
public class StudentIDAuthenticator {

  @EJB
  private CredentialService credentialService;

  /**
   * This cookie is retrieved at the start of the program and is kept in memory until it is needed
   * during authentication.
   * 
   * The cookie is required by Overdrive. An error is returned if it is not supplied.
   */
  private static final String JSDCookie = getOverdriveCookie();

  /**
   * Gets a cookie from Overdrive.
   */
  private static String getOverdriveCookie() {
    PollLogger.logVerbose("Getting Cookie from Overdrive");
    try {
      String url = "https://jordanut.libraryreserve.com/10/45/en/SignIn.htm?url=Default.htm";

      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpGet httpGet = new HttpGet(url);

      httpGet.setHeader("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36");

      HttpResponse httpResponse = httpClient.execute(httpGet);

      PollLogger.logVerbose("\nSending 'GET' request to URL : " + url);
      PollLogger.logVerbose("Response Code : " + httpResponse.getStatusLine().getStatusCode());
      PollLogger.logVerbose("Cookie: " + httpResponse.getFirstHeader("Set-Cookie").getValue());

      return httpResponse.getFirstHeader("Set-Cookie").getValue();

    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * Authenticates the given student ID using Overdrive. If no exception is thrown, the ID is
   * considered valid.
   * 
   * @param studentId The student ID to authenticate.
   * @throws AuthenticationException If the student ID is invalid or there was another issue
   *         authenticating, this will return a reason.
   */
  public void authenticateIdUsingOverdrive(int studentId) throws AuthenticationException {
    try {
      PollLogger.logVerbose("Authenticating StudentID " + studentId + " with Overdrive");

      String url = "https://jordanut.libraryreserve.com/10/45/en/BANGAuthenticate.dll";

      HttpClient httpClient = HttpClientBuilder.create().build();
      HttpPost httpPost = new HttpPost(url);

      httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
      httpPost.setHeader("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36");
      httpPost.setHeader("Cookie", JSDCookie);

      List<NameValuePair> urlParameters = new ArrayList<>();
      urlParameters.add(new BasicNameValuePair("URL", "Default.htm"));
      urlParameters.add(new BasicNameValuePair("LibraryCardILS", "jordan"));
      urlParameters.add(new BasicNameValuePair("lcn", studentId + ""));

      httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

      HttpResponse httpResponse = httpClient.execute(httpPost);
      String location = httpResponse.getFirstHeader("Location").getValue();

      if (location.contains("Error.htm")) {
        PollLogger.logVerbose("ID Was Invalid!");
        throw new AuthenticationException("The entered Student ID is invalid.");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Authenticates the given student ID using the Credentials Table. If no exception is thrown, the
   * ID is considered valid.
   * 
   * @param studentId The student ID to authenticate.
   * @return The valid Credential, if one exists.
   * @throws AuthenticationException If the student ID is invalid or there was another issue
   *         authenticating, this will return a reason.
   */
  public Credential authenticateIdUsingCredentialsTable(int studentId)
      throws AuthenticationException {
    PollLogger.logVerbose("Authenticating StudentID with Credentials table.");

    Credential credential = credentialService.getByStudentId(studentId);
    if (credential == null) {
      throw new AuthenticationException("The Student ID you entered is invalid. Please try again.");
    } else {
      PollLogger.logVerbose("Found existing Credential.");
      return credential;
    }
  }

  /**
   * Authenticates the given student ID using the basic methods. In essence, if the ID is between
   * 8000000 and 9999999, it is considered valid. If no exception is thrown, the ID is considered
   * valid.
   * 
   * @param studentId The student ID to authenticate.
   * @throws AuthenticationException If the student ID is invalid or there was another issue
   *         authenticating, this will return a reason.
   */
  public void authenticateIdUsingBasicVerification(int studentId) throws AuthenticationException {
    PollLogger.logVerbose("Authenticating StudentID with basic methods.");

    if (studentId > 9999999 || studentId < 8000000) {
      PollLogger.logVerbose("Invalid Format!");
      throw new AuthenticationException("The Student ID you entered is invalid. Please try again.");
    } else {
      PollLogger.logVerbose("ID Was Valid!");
    }
  }

  @SuppressWarnings("serial")
  public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
      super(message);
    }

  }

}
