/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Data;

/**
 * @author huseyin.kilic
 */
@Data
public class TrollnessAnalysisResult {

  //TODO sentiment analysis
  //TODO user ratings
  private boolean verified;
  private double totalScore;
  private double retweetRatio;
  private double linkRatio;
  private double mentionRatio;
  private double newFollowerRatio;
  private double followersWithDifferentLanguageRatio;
  private double inactiveFollowerRatio;
  private double dailyTweetRatio;
  private int significantTweetsCount;
}
