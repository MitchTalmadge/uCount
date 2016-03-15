package org.BinghamTSA.uCount.web.ViewModels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.BinghamTSA.uCount.core.entityBeans.Contestant;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;

/**
 * Object representation of the data sent to the Live Results JavaScript file.
 */
public class ResultViewModel {

  /**
   * Will hold an identifier for the statistic, primarily VoteGroup and respective members
   */
  private String key;
  /**
   * Statistic of the given identifier
   */
  private int value;

  public ResultViewModel() {
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

  /**
   * Takes a given map of VoteGroups and their statistics (usually total
   * number of votes) and returns a list of ResultViewModels.
   * 
   * @param map - Entries being VoteGroup and statistic value
   * @return ResultViewModel list - exportable to json
   */
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
