package com.AptiTekk.Poll.core;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.web.controllers.VotingController;

@Startup
@Singleton
public class PollService extends Service<Poll> {
	private Poll enabledPoll;
	private String cookie = "";

	public PollService() {
		this.type = Poll.class;
	}

	@PostConstruct
	public void init() {
		System.out.println("Starting PollService");
		List<Poll> polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled()) {
				this.enabledPoll = poll;
				break;
			}
		}
		if (VotingController.USE_JSD_AUTH) { // Must get cookie from Overdrive
			System.out.println("Connecting to JSD Overdrive...");
			try {
				String url = "https://jordanut.libraryreserve.com/10/45/en/SignIn.htm?url=Default.htm";

				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpGet httpGet = new HttpGet(url);

				httpGet.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36");
				
				HttpResponse httpResponse = httpClient.execute(httpGet);

				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + httpResponse.getStatusLine().getStatusCode());
				System.out.println("Cookie: " + httpResponse.getFirstHeader("Set-Cookie").getValue());
				
				this.cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Poll getEnabledPoll() {
		return this.enabledPoll;
	}

	public void enablePoll(Poll poll) {
		if (this.enabledPoll != null) {
			enabledPoll.setEnabled(false);
			merge(enabledPoll);
		}
		if (poll != null) {
			System.out.println("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			System.out.println("Clearing Enabled Poll.");
		}
		this.enabledPoll = poll;
	}

	public void refreshEnabledPoll() {
		if (this.enabledPoll != null)
			this.enabledPoll = get(enabledPoll.getId());
		else
			this.enabledPoll = null;
	}

	public String getCookie() {
		return cookie;
	}

}
