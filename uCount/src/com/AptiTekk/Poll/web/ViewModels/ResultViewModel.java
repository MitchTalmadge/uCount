package com.AptiTekk.Poll.web.ViewModels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.AptiTekk.Poll.core.entityBeans.Contestant;
import com.AptiTekk.Poll.core.entityBeans.VoteGroup;

public class ResultViewModel {

	private String key;
	private int value;

	public ResultViewModel() {
		// TODO Auto-generated constructor stub
	}

	public ResultViewModel(String key, int value) {
		setKey(key);
		setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static List<ResultViewModel> toViewModels(Map<VoteGroup, Integer> map) {
		List<ResultViewModel> resultViewModels = new ArrayList<>();
		for (Map.Entry<VoteGroup, Integer> entry : map.entrySet()) {
			StringBuilder stringBuilder = new StringBuilder(entry.getKey().getName());
			if (entry.getKey().getContestants().size() > 0) {
				stringBuilder.append(" - ");
				for (int i = 0; i < entry.getKey().getContestants().size(); i++) {
					Contestant contestant = entry.getKey().getContestants().get(i);
					stringBuilder.append(contestant.getName())
							.append(((i == entry.getKey().getContestants().size() - 1) ? "" : ", "));
				}
			}

			resultViewModels.add(new ResultViewModel(stringBuilder.toString(), entry.getValue()));
		}
		return resultViewModels;
	}

}
