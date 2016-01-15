package com.AptiTekk.Poll.core.entityBeans;

import java.util.ArrayList;
import java.util.List;

import com.AptiTekk.Poll.web.ViewModels.ContestantViewModel;
import com.AptiTekk.Poll.web.ViewModels.PollViewModel;

public class ViewModelConverter {

	public static PollViewModel toViewModel(Poll e) {
		return new PollViewModel(e.getId(), e.getName(), e.getDescription());
	}

	public static List<PollViewModel> toPollViewModels(List<Poll> e) {
		List<PollViewModel> ret = new ArrayList<>();
		for (Poll m : e) {
			ret.add(toViewModel(m));
		}
		return ret;
	}

	public static ContestantViewModel toViewModel(Contestant e) {
		return new ContestantViewModel(e.getId(), e.getVoteGroup().getId(), e.getVoteGroup().getPoll().getName(),
				e.getFirstName(), e.getLastName(), e.getPictureFileName());
	}

	public static List<ContestantViewModel> toContestantViewModels(List<Contestant> e) {
		List<ContestantViewModel> ret = new ArrayList<>();
		for (Contestant m : e) {
			ret.add(toViewModel(m));
		}
		return ret;
	}

}
