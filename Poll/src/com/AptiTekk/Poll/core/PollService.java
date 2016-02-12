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

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.Poll;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.PollLogger;
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
		PollLogger.logVerbose("Starting PollService");
		List<Poll> polls = getAll();
		for (Poll poll : polls) {
			if (poll.isEnabled()) {
				this.enabledPoll = poll;
				break;
			}
		}
		if (VotingController.USE_JSD_AUTH) { // Must get cookie from Overdrive
			PollLogger.logVerbose("Connecting to JSD Overdrive...");
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
			PollLogger.logVerbose("Setting Enabled Poll to " + poll.getName());
			poll.setEnabled(true);
			merge(poll);
		} else {
			PollLogger.logVerbose("Clearing Enabled Poll.");
		}
		this.enabledPoll = poll;
	}

	public void refreshEnabledPoll() {
		if (this.enabledPoll != null)
			this.enabledPoll = get(enabledPoll.getId());
		else
			this.enabledPoll = null;
	}

	@Override
	public void delete(int id) {
		Poll poll = get(id);
		if (poll != null) {
			for (VoteGroup votegroup : poll.getVoteGroups()) {
				for (Contestant contestant : votegroup.getContestants()) {
					ContestantService.deleteContestantImage(contestant.getPictureFileName());
				}
			}
		}
		
		super.delete(id);
	}

	public String getCookie() {
		return cookie;
	}

}