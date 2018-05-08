/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Data;

/**
 * @author huseyin.kilic
 */
@Data
public class DeadnessAnalysisResult {

  private double totalScore;
  private int recentTweetCount;
  private double recentTweetRatio;
  private int significantMentionsCount;
  private int totalTweetCount;

}
