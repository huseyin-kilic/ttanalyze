/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttanalyze.domain.AnalyzeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;

/**
 * @author huseyin.kilic
 */
@Transactional
@Service
public class AnalyzerService {

  private final static Logger logger = LoggerFactory.getLogger(AnalyzerService.class);

  @Autowired
  private BotometerClient botometerClient;

  public AnalyzeResponse analyzeBotness(String screenName) {
    ResponseEntity<String> botometerResponse = botometerClient.analyze(screenName);
    return buildAnalyzeResponse(botometerResponse);
  }

  private AnalyzeResponse buildAnalyzeResponse(ResponseEntity<String> responseEntity) {
    AnalyzeResponse analyzeResponse = new AnalyzeResponse();
    try {
      HashMap<String,Object> result =
              new ObjectMapper().readValue(responseEntity.getBody(), HashMap.class);

      analyzeResponse.setCategories((HashMap<String, Double>)result.get("categories"));
      analyzeResponse.setScores((HashMap<String, Double>)result.get("scores"));
      analyzeResponse.setUser((HashMap<String, String>)result.get("user"));

    } catch (Exception e) {
      logger.debug("error reading analyze response");
      return null;
    }
    return analyzeResponse;
  }

}