package org.BinghamTSA.uCount.web.controllers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.BinghamTSA.uCount.core.CredentialService;
import org.BinghamTSA.uCount.core.EntryService;
import org.BinghamTSA.uCount.core.PollService;
import org.BinghamTSA.uCount.core.entityBeans.Credential;
import org.BinghamTSA.uCount.core.entityBeans.Entry;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.PollLogger;
import org.BinghamTSA.uCount.core.utilities.StudentIDAuthenticator;
import org.BinghamTSA.uCount.core.utilities.StudentIDAuthenticator.AuthenticationException;

/**
 * The purpose of this class is to provide a way to simulate user entry, and is used when performing
 * load tests from loadimpact.com
 */
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

  private Poll enabledPoll;

  @PostConstruct
  public void init() {
    enabledPoll = pollService.getEnabledPoll();
  }

  /**
   * Performs a simulated authentication and entry insertion. The point is to see how the server
   * handles hundreds of SQL requests at once.
   */
  public void authenticate() {
    int studentId = (int) (8220000 + (Math.random() * 9999));
    try {
      studentIdAuthenticator.authenticateIdUsingOverdrive(studentId);
    } catch (AuthenticationException e) {
    }

    if (enabledPoll != null) {
      int numVoteGroups = enabledPoll.getVoteGroups().size();
      int indexSize = numVoteGroups - 1;
      int voteGroupId = (int) Math.round((Math.random() * indexSize));

      if (voteGroupId < 0)
        voteGroupId = 0;
      if (voteGroupId > indexSize)
        voteGroupId = indexSize;

      VoteGroup voteGroup = enabledPoll.getVoteGroups().get(voteGroupId);
      PollLogger.logVerbose("Voting for Vote Group: [" + voteGroup.getId() + "], with student ID: ["
          + studentId + "]");

      Entry entry = new Entry(studentId, voteGroup, enabledPoll);
      entryService.insert(entry);
    }
  }

}
