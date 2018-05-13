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
import org.springframework.util.CollectionUtils;

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

  @Value("${recentTweetDateDays}")
  private long recentTweetDateDays;
  @Value("${coefficient.recentTweetRatio}")
  private long recentTweetRatioCoefficient;
  @Value("${threshold.recentTweetRatio}")
  private long recentTweetRatioThreshold;
  @Value("${threshold.recentTweetCount}")
  private long recentTweetCountThreshold;
  @Value("${threshold.totalTweetCount}")
  private long totalTweetCountThreshold;
  @Value("#{'${searchPatterns}'.split(',')}")
  private List<String> searchPatterns;
  @Value("${coefficient.significantMentionCount}")
  private long significantMentionCountCoefficient;
  @Value("${threshold.significantMentionCount}")
  private long significantMentionCountThreshold;


  public DeadnessAnalysisResult analyze(String screenName) {
    DeadnessAnalysisResult result = new DeadnessAnalysisResult();
    List<Tweet> tweetList = getTweets(screenName);
    List<Tweet> mentionList = getMentions(screenName);

    result.setRecentTweetCount(getRecentTweetCount(tweetList));
    result.setRecentTweetRatio(getRecentTweetRatio(tweetList, (double)result.getRecentTweetCount()));
    result.setSignificantMentionsCount(getSignificantMentionsCount(mentionList));
    result.setTotalTweetCount(twitter.userOperations().getUserProfile(screenName).getStatusesCount());

    result.setTotalScore(calculateDeadnessScore(result));
    return result;
  }


  private double calculateDeadnessScore(DeadnessAnalysisResult result) {
    double totalScore = 0;

    if (result.getTotalTweetCount() < totalTweetCountThreshold) {
      totalScore += recentTweetRatioCoefficient;
    } else if (result.getRecentTweetCount() == 0 && result.getTotalTweetCount() >= totalTweetCountThreshold) {
      totalScore += recentTweetRatioCoefficient;
    } else if (result.getRecentTweetCount() < recentTweetCountThreshold &&
            result.getRecentTweetRatio() < recentTweetRatioThreshold) {
      totalScore += (1-result.getRecentTweetRatio()) * recentTweetRatioCoefficient;
    }

    if (result.getSignificantMentionsCount() >= significantMentionCountThreshold) {
      totalScore += ((double)result.getSignificantMentionsCount()) * significantMentionCountCoefficient / 100d;
    }

    return setPrecision(totalScore/100d);
  }

  private int getRecentTweetCount(List<Tweet> tweetList) {
    long dateThresholdMillis = new Date().getTime() - TimeUnit.DAYS.toMillis(recentTweetDateDays);
    int recentTweetCount = tweetList.parallelStream().filter(
            tweet -> tweet.getCreatedAt().getTime() > (dateThresholdMillis))
            .collect(Collectors.toList()).size();
    return recentTweetCount;
  }

  private double getRecentTweetRatio(List<Tweet> tweetList, double recentTweetCount) {
    double totalTweetCount = tweetList.size();
    return  setPrecision(recentTweetCount / totalTweetCount);
  }

  private int getSignificantMentionsCount(List<Tweet> mentionList) {
    int significantMentionsCount = 0;
    for (String searchPattern : searchPatterns) {
      significantMentionsCount += mentionList.parallelStream()
              .filter(tweet -> tweet.getText().toLowerCase().contains(searchPattern))
              .collect(Collectors.toList())
              .size();
    }
    return significantMentionsCount;
  }


  private List<Tweet> getTweets(String screenName) {
    List<Tweet> timeline = new ArrayList<>();
    List<Tweet> currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200);
    while (!CollectionUtils.isEmpty(currentPage)) {
      timeline.addAll(currentPage);
      long lastTweetId = currentPage.get(currentPage.size()-1).getId();
      currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200, 0, lastTweetId);
      if (currentPage.size() < 2)
        break;
    }
    return timeline;
  }

  private List<Tweet> getMentions(String screenName) {
    List<Tweet> mentions = new ArrayList<>();
    List<Tweet> currentPage = twitter.searchOperations().search("@" + screenName, 200).getTweets();
    while (!CollectionUtils.isEmpty(currentPage)) {
      mentions.addAll(currentPage);
      long lastTweetId = currentPage.get(currentPage.size()-1).getId();
      currentPage = twitter.searchOperations()
              .search("@" + screenName, 200, lastTweetId, 0).getTweets();
      if (currentPage.size() < 2)
        break;
    }
    return mentions;
  }

  private double setPrecision(double d) {
    return Math.round(d*100d)/100d;
  }

}





