/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.service;

import com.ttanalyze.domain.TotalAnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author huseyin.kilic
 */
@Transactional
@Service
public class AnalyzerService {

  //TODO mark users as bot/troll/dead

  @Autowired
  private BotnessAnalyzer botnessAnalyzer;
  @Autowired
  private TrollnessAnalyzer trollnessAnalyzer;
  @Autowired
  private DeadnessAnalyzer deadnessAnalyzer;

  public TotalAnalysisResult analyze(String screenName) {
    TotalAnalysisResult totalAnalysisResult = new TotalAnalysisResult();
    totalAnalysisResult.setBotness(botnessAnalyzer.analyze(screenName));
    totalAnalysisResult.setTrollness(trollnessAnalyzer.analyze(screenName));
    totalAnalysisResult.setDeadness(deadnessAnalyzer.analyze(screenName));
    return totalAnalysisResult;
  }

}