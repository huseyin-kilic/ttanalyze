/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huseyin.kilic
 */
@Data
public class AnalyzeResponse {
  private Map<String, Double> categories = new HashMap<>();
  private Map<String, Double> scores = new HashMap<>();
  private Map<String, String> user = new HashMap<>();
}
