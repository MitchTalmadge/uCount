package com.AptiTekk.Poll.core;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.web.ViewModels.ContestantViewModel;

public class ModelConverter {

	public static Contestant toEntity(ContestantViewModel vm, Service<Contestant> service,
			VoteGroupService voteGroupService) {
		Contestant contestant = new Contestant();
		contestant.setId(vm.getId());
		contestant.setFirstName(vm.getFirstName());
		contestant.setLastName(vm.getLastName());
		contestant.setPictureFileName(vm.getPictureFileName());
		contestant.setVoteGroup(voteGroupService.get(vm.getVoteGroupId()));
		return contestant;
	}

}
