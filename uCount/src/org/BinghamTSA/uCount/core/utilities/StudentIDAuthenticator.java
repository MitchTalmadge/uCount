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

@Stateless
public class StudentIDAuthenticator {

	@EJB
	private CredentialService credentialService;

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

	public void authenticateIdUsingOverdrive(int studentId) throws AuthenticationException {
		try { // VERY HACKY METHOD of authenticating student IDs ---
				// Uses Jordan School District's Overdrive login.
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

	public Credential authenticateIdUsingCredentialsTable(int studentId) throws AuthenticationException {
		PollLogger.logVerbose("Authenticating StudentID with Credentials table.");

		Credential credential = credentialService.getByStudentId(studentId);
		if (credential == null) {
			throw new AuthenticationException("The Student ID you entered is invalid. Please try again.");
		} else {
			PollLogger.logVerbose("Found existing Credential.");
			return credential;
		}
	}

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
