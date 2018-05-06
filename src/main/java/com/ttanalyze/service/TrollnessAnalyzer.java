package com.ttanalyze.service;

import com.ttanalyze.domain.TrollnessAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:trollnessFactors.properties")
public class TrollnessAnalyzer {

  private final static Logger logger = LoggerFactory.getLogger(TrollnessAnalyzer.class);

  @Autowired
  private Twitter twitter;

  @Value("${calculateScoreForVerifiedAccounts}")
  private boolean calculateScoreForVerifiedAccounts;
  @Value("${threshold.retweetRatio}")
  private double retweetRatioThreshold;
  @Value("${coefficient.retweetRatio}")
  private double retweetRatioCoefficient;
  @Value("${threshold.linkRatio}")
  private double linkRatioThreshold;
  @Value("${coefficient.linkRatio}")
  private double linkRatioCoefficient;
  @Value("${threshold.mentionRatio}")
  private double mentionRatioThreshold;
  @Value("${coefficient.mentionRatio}")
  private double mentionRatioCoefficient;
  @Value("${threshold.dailyTweetRatio}")
  private double dailyTweetRatioThreshold;
  @Value("${coefficient.dailyTweetRatio}")
  private double dailyTweetRatioCoefficient;
  @Value("${newFollowerDateThreshold}")
  private long newFollowerDateThreshold;
  @Value("${threshold.newFollowerRatio}")
  private double newFollowerRatioThreshold;
  @Value("${coefficient.newFollowerRatio}")
  private double newFollowerRatioCoefficient;
  @Value("${threshold.followerWithDifferentLanguageRatio}")
  private double followerWithDifferentLanguageRatioThreshold;
  @Value("${coefficient.followerWithDifferentLanguage}")
  private double followerWithDifferentLanguageCoefficient;
  @Value("${inactiveFollowerTweetThreshold}")
  private double inactiveFollowerTweetThreshold;
  @Value("${threshold.inactiveFollowerRatio}")
  private double inactiveFollowerRatioThreshold;
  @Value("${coefficient.inactiveFollowerRatio}")
  private double inactiveFollowerRatioCoefficient;

  public TrollnessAnalysisResult analyze(String screenName) {
    TrollnessAnalysisResult trollnessAnalysisResult = new TrollnessAnalysisResult();

    List<Tweet> tweetList = getTweets(screenName);
    trollnessAnalysisResult.setRetweetRatio(getRetweetRatio(tweetList));
    trollnessAnalysisResult.setLinkRatio(getLinkRatio(tweetList));
    trollnessAnalysisResult.setMentionRatio(getMentionRatio(tweetList));
    trollnessAnalysisResult.setDailyTweetRatio(getDailyTweetRatio(screenName));


    List<TwitterProfile> followerList = getFollowers(screenName);
    trollnessAnalysisResult.setNewFollowerRatio(getNewFollowersRatio(followerList));
    trollnessAnalysisResult.setFollowersWithDifferentLanguageRatio(getFollowersWithDifferentLanguageRatio(
            twitter.userOperations().getUserProfile(screenName).getLanguage(), followerList));
    //trollnessAnalysisResult.setInactiveFollowerRatio(getInactiveFollowersRatio(followerList));


    if (!calculateScoreForVerifiedAccounts && twitter.userOperations().getUserProfile(screenName).isVerified()) {
      trollnessAnalysisResult.setTotalScore(0);
      trollnessAnalysisResult.setVerified(true);
    } else {
      trollnessAnalysisResult.setTotalScore(calculateTrollnessScore(trollnessAnalysisResult));
    }
    return trollnessAnalysisResult;
  }

  private List<Tweet> getTweets(String screenName) {
    List<Tweet> timeline = new ArrayList<>();
    List<Tweet> currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200);
    while (true) {
      timeline.addAll(currentPage);
      long lastTweetId = currentPage.get(currentPage.size()-1).getId();
      currentPage = twitter.timelineOperations().getUserTimeline(screenName, 200, 0, lastTweetId);
      if (currentPage.size() <= 1)
        break;
    }
    return timeline;
  }

  private List<TwitterProfile> getFollowers(String screenName) {
    CursoredList<TwitterProfile> followers = twitter.friendOperations().getFollowers(screenName);
    ArrayList<TwitterProfile> allFollowers = followers;
    while (followers.hasNext()) {
      followers = twitter.friendOperations().getFriendsInCursor(followers.getNextCursor());
      allFollowers.addAll(followers);
    }
    return allFollowers;
  }

  private double calculateTrollnessScore(TrollnessAnalysisResult trollnessAnalysisResult) {
    double totalScore = 0;

    if (trollnessAnalysisResult.getRetweetRatio() > retweetRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getRetweetRatio() * retweetRatioCoefficient;
    }
    if (trollnessAnalysisResult.getLinkRatio() > linkRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getLinkRatio() * linkRatioCoefficient;
    }
    if (trollnessAnalysisResult.getMentionRatio() > mentionRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getMentionRatio() * mentionRatioCoefficient;
    }
    if (trollnessAnalysisResult.getDailyTweetRatio() > dailyTweetRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getDailyTweetRatio() * dailyTweetRatioCoefficient;
    }
    if (trollnessAnalysisResult.getFollowersWithDifferentLanguageRatio() > followerWithDifferentLanguageRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getFollowersWithDifferentLanguageRatio() * followerWithDifferentLanguageCoefficient;
    }
    if (trollnessAnalysisResult.getInactiveFollowerRatio() > inactiveFollowerRatioThreshold) {
      totalScore +=  trollnessAnalysisResult.getInactiveFollowerRatio() * inactiveFollowerRatioCoefficient;
    }

    return setPrecision(totalScore/100d);
  }

  private double getRetweetRatio(List<Tweet> tweetList) {
    double totalTweetCount = tweetList.size();
    double retweetCount = tweetList.stream().filter(Tweet::isRetweet).collect(Collectors.toList()).size();
    return setPrecision(setPrecision(retweetCount / totalTweetCount));
  }

  private double getLinkRatio(List<Tweet> tweetList) {
    double totalTweetCount = tweetList.size();
    double tweetWithLinkCount = tweetList.stream().filter(tweet -> tweet.getText().contains("http"))
            .collect(Collectors.toList()).size();
    return setPrecision(tweetWithLinkCount / totalTweetCount);
  }

  private double getMentionRatio(List<Tweet> tweetList) {
    double totalTweetCount = tweetList.size();
    double tweetWithMentionCount = tweetList.stream().filter(tweet -> tweet.getEntities().getMentions().size() > 0)
            .collect(Collectors.toList()).size();
    return setPrecision(tweetWithMentionCount / totalTweetCount);
  }

  private double getDailyTweetRatio(String screenName) {
    TwitterProfile userProfile = twitter.userOperations().getUserProfile(screenName);
    long daysSinceCreation = TimeUnit.DAYS.convert(new Date().getTime() - userProfile.getCreatedDate().getTime(),
            TimeUnit.MILLISECONDS);
    return setPrecision((double)userProfile.getStatusesCount() / daysSinceCreation);
  }

  private double getNewFollowersRatio(List<TwitterProfile> followerList) {
    LocalDate now = LocalDate.now();
    double totalFollowersCount = followerList.size();
    double newFollowersCount = followerList.stream()
            .filter(follower -> follower.getCreatedDate().after(convert(now.minusDays(newFollowerDateThreshold))))
            .collect(Collectors.toList()).size();
    return setPrecision(newFollowersCount / totalFollowersCount);
  }

  private double getFollowersWithDifferentLanguageRatio(String userLanguage, List<TwitterProfile> followerList) {
    double totalFollowersCount = followerList.size();
    double followersWithDifferentLanguageCount = followerList.stream()
            .filter(follower -> follower.getLanguage() != userLanguage)
            .collect(Collectors.toList()).size();
    return setPrecision(followersWithDifferentLanguageCount / totalFollowersCount);
  }

  private double getInactiveFollowersRatio(List<TwitterProfile> followerList) {
    double totalFollowersCount = followerList.size();

    double inactiveFollowersCount = followerList.stream()
            .filter(follower -> twitter.timelineOperations().getUserTimeline(
                    follower.getScreenName()).size() < inactiveFollowerTweetThreshold)
            .collect(Collectors.toList()).size();
    return setPrecision(inactiveFollowersCount / totalFollowersCount);
  }

  private Date convert(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay()
            .atZone(ZoneId.systemDefault()).toInstant());
  }

  private double setPrecision(double d) {
    return Math.round(d*100d)/100d;
  }

}





