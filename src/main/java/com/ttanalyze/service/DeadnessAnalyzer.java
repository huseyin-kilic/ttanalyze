package com.ttanalyze.service;

import com.ttanalyze.domain.DeadnessAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:deadnessFactors.properties")
public class DeadnessAnalyzer {

  private final static Logger logger = LoggerFactory.getLogger(DeadnessAnalyzer.class);

  @Autowired
  private Twitter twitter;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${recentTweetDateDays}")
  private long recentTweetDateDays;

  public DeadnessAnalysisResult analyze(String screenName) {
    DeadnessAnalysisResult result = new DeadnessAnalysisResult();
    List<Tweet> tweetList = getTweets(screenName);

    result.setRecentTweetCount(getRecentTweetCount(tweetList));
    result.setRecentTweetRatio(getRecentTweetRatio(tweetList));

    result.setTotalScore(calculateDeadnessScore(result));
    return result;
  }

  private double calculateDeadnessScore(DeadnessAnalysisResult result) {
    return 0;
  }

  private int getRecentTweetCount(List<Tweet> tweetList) {
    long dateThresholdMillis = TimeUnit.DAYS.convert(new Date().getTime() -
            TimeUnit.DAYS.toMillis(recentTweetDateDays), TimeUnit.MILLISECONDS);
    int recentTweetCount = tweetList.stream().filter(
            tweet -> tweet.getCreatedAt().getTime() < (dateThresholdMillis))
            .collect(Collectors.toList()).size();
    return recentTweetCount;
  }

  private double getRecentTweetRatio(List<Tweet> tweetList) {
    double recentTweetCount = getRecentTweetCount(tweetList);
    double totalTweetCount = tweetList.size();

    return  setPrecision(recentTweetCount / totalTweetCount);
  }

  private List<Tweet> getTweets(String screenName) {
    List<Tweet> timeline = new ArrayList<>();
    List<Tweet> currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200);
    while (true) {
      timeline.addAll(currentPage);
      long lastTweetId = currentPage.get(currentPage.size()-1).getId();
      currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200, 0, lastTweetId);
      if (currentPage.size() < 2)
        break;
    }
    return timeline;
  }

  private double setPrecision(double d) {
    return Math.round(d*100d)/100d;
  }

}





