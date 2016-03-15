package org.BinghamTSA.uCount.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.BinghamTSA.uCount.core.EntryService;
import org.BinghamTSA.uCount.core.PollService;
import org.BinghamTSA.uCount.core.entityBeans.Entry;
import org.BinghamTSA.uCount.core.entityBeans.Poll;
import org.BinghamTSA.uCount.core.entityBeans.VoteGroup;
import org.BinghamTSA.uCount.core.utilities.StatisticsHelper;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@ViewScoped
public class FinalResultsController {

  @EJB
  PollService pollService;
  @EJB
  EntryService entryService;

  private Poll poll;

  private int totalVotes;
  private int groups;
  private Map<VoteGroup, Integer> rankingMap;

  private HorizontalBarChartModel votesChartModel;
  private PieChartModel votingProportionModel;
  private LineChartModel votingFrequencyModel;

  @PostConstruct
  public void init() {
    String pollIdParam = FacesContext.getCurrentInstance().getExternalContext()
        .getRequestParameterMap().get("pollId");
    try {
      int pollId = Integer.parseInt(pollIdParam);
      setPoll(pollService.get(pollId));

      Map<VoteGroup, Integer> results = pollService.getResults(poll.getId());
      totalVotes = StatisticsHelper.sumValues(results);
      setGroups(results.size());
      
      setRankingMap(StatisticsHelper.sortByDescendingValue(pollService.getResults(poll.getId())));

      createHorizontalBarModel();
      createPieChart();
      createFreqModel();
    } catch (NumberFormatException ignored) {
    }
  }

  public Poll getPoll() {
    return poll;
  }

  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  /**
   * Gets the top three VoteGroups for the current Poll.
   * @return A List with the top three VoteGroups in order of descending rank.
   */
  public List<VoteGroup> getTopThree() {
    final Set<Map.Entry<VoteGroup, Integer>> mapValues = getRankingMap().entrySet();
    final int maplength = mapValues.size();
    final Map.Entry<VoteGroup, Integer>[] listRanking = new Map.Entry[maplength];
    mapValues.toArray(listRanking);

    List<VoteGroup> results = new ArrayList<VoteGroup>() {
      {
        add(listRanking[1].getKey());
        add(listRanking[0].getKey());
        add(listRanking[2].getKey());
      }
    };

    return results;

  }

  public HorizontalBarChartModel getVotesChartModel() {
    return votesChartModel;
  }

  /**
   * Creates a horizontal bar chart.
   */
  private void createHorizontalBarModel() {
    votesChartModel = new HorizontalBarChartModel();

    votesChartModel.setAnimate(true);

    ChartSeries votes = new ChartSeries();
    votes.setLabel("Votes");
    int max = 0;
    for (Map.Entry<VoteGroup, Integer> entry : getRankingMap().entrySet()) {
      votes.set(entry.getKey().getName(), entry.getValue().intValue());
      if (entry.getValue().intValue() > max)
        max = entry.getValue().intValue();
    }

    votesChartModel.addSeries(votes);

    votesChartModel.setTitle("Votes");

    Axis xAxis = votesChartModel.getAxis(AxisType.X);
    xAxis.setLabel("Votes");
    xAxis.setMin(0);
    xAxis.setMax(max + (max * 0.1));

    Axis yAxis = votesChartModel.getAxis(AxisType.Y);
    yAxis.setLabel("VoteGroup");
  }

  public PieChartModel getVotingProportionModel() {
    return votingProportionModel;
  }

  /**
   * Creates a pie chart.
   */
  private void createPieChart() {
    votingProportionModel = new PieChartModel();

    for (Map.Entry<VoteGroup, Integer> entry : getRankingMap().entrySet()) {
      votingProportionModel.set(entry.getKey().getName(), entry.getValue());
    }

    votingProportionModel.setTitle("Proportion");
    votingProportionModel.setLegendPosition("ne");
    votingProportionModel.setFill(false);
    votingProportionModel.setShowDataLabels(true);
    votingProportionModel.setDiameter(150);
  }

  /**
   * Creates a frequency model.
   */
  private void createFreqModel() {
    votingFrequencyModel = new LineChartModel();
    
    LineChartSeries ballotsTaken = new LineChartSeries();
    ballotsTaken.setLabel("Ballots Taken");
    ballotsTaken.setFill(false);

    int votes = 0;
    for(Entry entry : poll.getEntries()) {
      votes++;
      ballotsTaken.set(StatisticsHelper.dateFormat.format(entry.getSubmitDate().getTime()), votes);
      //System.out.println("Setting point at " + StatisticsHelper.dateFormat.format(entry.getSubmitDate().getTime()) + " with votes: " + votes);
    }
    
    votingFrequencyModel.addSeries(ballotsTaken);
    votingFrequencyModel.setAnimate(true);
    votingFrequencyModel.setZoom(true);
    votingFrequencyModel.setTitle("Voting Frequency");

    DateAxis axis = new DateAxis("Time");
    axis.setTickAngle(-50);
    axis.setMax(StatisticsHelper.dateFormat.format(new Date()));
    axis.setTickFormat("%b %#d - %H:%#M:%S");
    
    votingFrequencyModel.getAxes().put(AxisType.X, axis);
    
    Axis yAxis = votingFrequencyModel.getAxis(AxisType.Y);
    yAxis.setLabel("Ballots Taken");
    yAxis.setMin(0);
    yAxis.setMax(votes+10);

  }

  public int getGroups() {
    return groups;
  }

  public void setGroups(int groups) {
    this.groups = groups;
  }

  public LineChartModel getVotingFrequencyModel() {
    return votingFrequencyModel;
  }

  public void setVotingFrequencyModel(LineChartModel votingFrequencyModel) {
    this.votingFrequencyModel = votingFrequencyModel;
  }

  public Map<VoteGroup, Integer> getRankingMap() {
    return rankingMap;
  }

  public void setRankingMap(Map<VoteGroup, Integer> rankingMap) {
    this.rankingMap = rankingMap;
  }

}
