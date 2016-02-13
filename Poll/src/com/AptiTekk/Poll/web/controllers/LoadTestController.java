package com.AptiTekk.Poll.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.AptiTekk.Poll.core.CredentialService;
import com.AptiTekk.Poll.core.EntryService;
import com.AptiTekk.Poll.core.PollService;
import com.AptiTekk.Poll.core.entityBeans.Credential;
import com.AptiTekk.Poll.core.entityBeans.Entry;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;
import com.AptiTekk.Poll.core.utilities.PollLogger;
import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator;
import com.AptiTekk.Poll.core.utilities.StudentIDAuthenticator.AuthenticationException;

@ManagedBean
@ViewScoped
public class LoadTestController {

	@EJB
	EntryService entryService;

	@EJB
	PollService pollService;

	@EJB
	CredentialService credentialService;

	@EJB
	StudentIDAuthenticator studentIdAuthenticator;

	public void authenticate() {
		Credential credential = null;
		int studentId = (int) (8220000 + (Math.random() * 9999));
		try {
			credential = studentIdAuthenticator.authenticateIdUsingOverdrive(studentId);
		} catch (AuthenticationException e) {
			credential = new Credential(studentId);
			credentialService.insert(credential);
		}

		if (pollService.getEnabledPoll() != null && credential != null) {
			int numVoteGroups = pollService.getEnabledPoll().getVoteGroups().size();
			int indexSize = numVoteGroups - 1;
			int voteGroupId = (int) Math.round((Math.random() * indexSize));

			if (voteGroupId < 0)
				voteGroupId = 0;
			if (voteGroupId > indexSize)
				voteGroupId = indexSize;

			PollLogger.logVerbose("Voting for Vote Group " + voteGroupId);
			VoteGroup voteGroup = pollService.getEnabledPoll().getVoteGroups().get(voteGroupId);

			Entry entry = new Entry(credential, voteGroup, pollService.getEnabledPoll());
			entryService.insert(entry);
			pollService.getEnabledPoll().getEntries().add(entry);
			voteGroup.getEntries().add(entry);
		}
	}

}
