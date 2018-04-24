package com.ttanalyze.service;

import com.ttanalyze.domain.DeadnessAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource("classpath:twitter4j.properties")
public class DeadnessAnalyzer {

  private final static Logger logger = LoggerFactory.getLogger(DeadnessAnalyzer.class);

  @Value("${mashape-key}")
  private String mashapeKey;
  @Value("${oauth.consumerKey}")
  private String consumer_key;
  @Value("${oauth.consumerSecret}")
  private String consumer_secret;
  @Value("${oauth.accessToken}")
  private String access_token;
  @Value("${oauth.accessTokenSecret}")
  private String access_token_secret;

  @Autowired
  private RestTemplate restTemplate;

  public DeadnessAnalysisResult analyze(String screen_name) {

    return new DeadnessAnalysisResult();
  }



}





