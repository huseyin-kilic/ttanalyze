/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Data;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * @author huseyin.kilic
 */
@Data
public class TotalAnalysisResult {
  private BotnessAnalysisResult botness;
  private TrollnessAnalysisResult trollness;
  private DeadnessAnalysisResult deadness;
  private TwitterProfile userProfile;
}
