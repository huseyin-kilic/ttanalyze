/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Data;
import org.springframework.social.twitter.api.Tweet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huseyin.kilic
 */
@Data
public class AnalyzeRequest {
  private List<NativeTweet> timeline;
  private List<NativeTweet> mentions;
  private Map<String, String> user;

  public AnalyzeRequest(List<Tweet> timeline, String screenName, Long userId){
    this.timeline = convertToNativeTweetList(timeline);
    this.mentions = new ArrayList<>();
    this.user = new HashMap<>();
    this.user.put("screen_name", screenName);
    this.user.put("id", userId.toString());
  }

  private List<NativeTweet> convertToNativeTweetList(List<Tweet> timeline) {
    List<NativeTweet> nativeTweetList = new ArrayList<>();
    timeline.stream().forEach(tweet -> nativeTweetList.add(convertToNativeTweet(tweet)));
    return nativeTweetList;
  }

  private NativeTweet convertToNativeTweet(Tweet tweet) {
    NativeTweet nativeTweet = new NativeTweet();

    nativeTweet.setId(tweet.getId());
    nativeTweet.setPossibly_sensitive(false);
    nativeTweet.setFavorited(tweet.getFavoriteCount()>0);
    nativeTweet.setRetweet_count(tweet.getRetweetCount() == null ? null : tweet.getRetweetCount().longValue());
    nativeTweet.setCreated_at(tweet.getCreatedAt());
    nativeTweet.setRetweeted(tweet.getRetweetCount()>0);
    nativeTweet.setTruncated(false);
    nativeTweet.setLang(tweet.getLanguageCode());
    nativeTweet.setFavorite_count(tweet.getFavoriteCount() == null ? null : tweet.getFavoriteCount().longValue());
    nativeTweet.setId_str(tweet.getIdStr());
    nativeTweet.setText(tweet.getText());
    nativeTweet.setIn_reply_to_user_id(tweet.getInReplyToUserId());
    nativeTweet.setIn_reply_to_status_id(tweet.getInReplyToStatusId());
    nativeTweet.setIs_quote_status(false);
    nativeTweet.setRetweet_count(tweet.getRetweetCount() == null ? null : tweet.getRetweetCount().longValue());
    return nativeTweet;
  }

}
