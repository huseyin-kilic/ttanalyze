package com.ttanalyze.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ttanalyze.domain.BotnessAnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import twitter4j.HashtagEntity;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Service
@PropertySource("classpath:twitter4j.properties")
public class BotnessAnalyzer {

  private final static Logger logger = LoggerFactory.getLogger(BotnessAnalyzer.class);

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

  public BotnessAnalysisResult analyze(String screen_name) {
    if (StringUtils.isEmpty(screen_name)) {
      logger.error("screen_name argument incorrect, make sure populated.");
      return null;
    }
    ResponseEntity<String> botnessAnalysisResponse = null;
    try {
      JsonObject analyzeRequest = prepareAnalyzeRequest(screen_name);

      HttpHeaders headers = new HttpHeaders();
      headers.set("X-Mashape-Key", this.mashapeKey);
      HttpEntity entity = new HttpEntity(analyzeRequest.toString(), headers);

      botnessAnalysisResponse = restTemplate
              .postForEntity("https://osome-botometer.p.mashape.com/2/check_account", entity, String.class);

      ;
    } catch (HttpClientErrorException | TwitterException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return buildBotnessAnalysisResult(botnessAnalysisResponse);
  }


  private BotnessAnalysisResult buildBotnessAnalysisResult(ResponseEntity<String> responseEntity) {
    BotnessAnalysisResult botnessAnalysisResult = new BotnessAnalysisResult();
    try {
      HashMap<String,Object> result =
              new ObjectMapper().readValue(responseEntity.getBody(), HashMap.class);

      botnessAnalysisResult.setCategories((HashMap<String, Double>)result.get("categories"));
      botnessAnalysisResult.setScores((HashMap<String, Double>)result.get("scores"));
      botnessAnalysisResult.setUser((HashMap<String, String>)result.get("user"));

    } catch (Exception e) {
      logger.debug("error reading analyze response");
      prepareEmptyResponse(botnessAnalysisResult);
    }
    return botnessAnalysisResult;
  }

  private void prepareEmptyResponse(BotnessAnalysisResult botnessAnalysisResult) {
    botnessAnalysisResult.setCategories(new HashMap<>());
    botnessAnalysisResult.getCategories().put("content",0d);
    botnessAnalysisResult.getCategories().put("friend",0d);
    botnessAnalysisResult.getCategories().put("network",0d);
    botnessAnalysisResult.getCategories().put("sentiment",0d);
    botnessAnalysisResult.getCategories().put("user",0d);
    botnessAnalysisResult.setScores(new HashMap<>());
    botnessAnalysisResult.getScores().put("english", 0d);
    botnessAnalysisResult.getScores().put("universal", 0d);
    botnessAnalysisResult.setUser(new HashMap<>());
  }

  private JsonObject prepareAnalyzeRequest(String screenName) throws TwitterException {
    Twitter twitter = getTwitterClient();
    JsonObject requestBody = new JsonObject();  // Will include User information, Tweets and Mentions

    prepareUserField(screenName, twitter, requestBody);
    prepareTimelineField(screenName, twitter, requestBody);
    prepareMentionsField(screenName, twitter, requestBody);

    logger.debug("Completed JSON Payload: " + requestBody.toString());
    return requestBody;
  }

  private Twitter getTwitterClient() {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true);
    cb.setOAuthConsumerKey(this.consumer_key);
    cb.setOAuthConsumerSecret(this.consumer_secret);
    cb.setOAuthAccessToken(this.access_token);
    cb.setOAuthAccessTokenSecret(this.access_token_secret);
    cb.setJSONStoreEnabled(true);

    return new TwitterFactory(cb.build()).getInstance();
  }

  private void prepareMentionsField(String screen_name, Twitter twitter, JsonObject requestBody)
          throws TwitterException {
    // 2. Get Mentions Information
    List<Status> mentions = new ArrayList<>();

    Query query = new Query("@" + screen_name);
    query.setCount(100); // 100 Mentions per: https://market.mashape.com/OSoMe/botometer/overview
    query.setResultType(Query.RECENT);

    QueryResult result = twitter.search(query);
    mentions.addAll(result.getTweets());

    JsonArray mentions_array = new JsonArray();
    for (Status status : mentions)
      mentions_array.add(getJsonFromStatus(status));

    requestBody.add("mentions", mentions_array);
  }

  private void prepareTimelineField(String screen_name, Twitter twitter, JsonObject requestBody)
          throws TwitterException {
    // 1. Get Twitter Stream Information
    //https://stackoverflow.com/questions/17887984/is-it-possible-to-get-more-than-100-tweets
    int pages = 1;
    int max_tweets = 200; // 200 Tweets per: https://market.mashape.com/OSoMe/botometer/overview
    List<Status> statuses = new ArrayList();

    for (;;) {
      int size = statuses.size();
      Paging page = new Paging(pages++, 200);
      statuses.addAll(twitter.getUserTimeline(screen_name, page));

      if (statuses.size() == size || statuses.size() > max_tweets)
        break;
    }

    int count = 0;
    JsonArray statuses_json = new JsonArray();
    for (Status status : statuses) {
      statuses_json.add(getJsonFromStatus(status));

      count++;

      if (count == max_tweets)
        break;
    }

    requestBody.add("timeline", statuses_json);
    logger.debug("# Tweets retrieved: " + statuses.size());
  }

  private void prepareUserField(String screen_name, Twitter twitter, JsonObject requestBody)
          throws TwitterException {
    User user = twitter.showUser(screen_name);

    JsonObject user_json = new JsonObject();
    user_json.addProperty("id", Long.toString(user.getId()));
    user_json.addProperty("screen_name", user.getScreenName());

    requestBody.add("user", user_json);

    logger.debug("User information retrieved.");
  }

  /**
   * Takes a Status, and create a JSON object that
   * can be parsed by the Botometer web service. Direct deserialization
   * using Gson would be ideal, but the fields don't line up exactly.
   *
   * @param status object.
   * @return JsonObject
   */
  private JsonObject getJsonFromStatus(Status status) {
    if (status == null) {
      logger.error("constructor arguments incorrect, make sure all populated.");
    }

    JsonObject status_json = new JsonObject();

    status_json.addProperty("id", status.getId());
    status_json.addProperty("id_str", Long.toString(status.getId()));
    status_json.addProperty("possibly_sensitive", status.isPossiblySensitive());
    status_json.addProperty("favorited", status.isFavorited());
    status_json.addProperty("lang", status.getLang());
    status_json.addProperty("retweeted", status.isRetweeted());
    status_json.addProperty("retweet_count", status.getRetweetCount());
    status_json.addProperty("truncated", status.isTruncated());
    status_json.addProperty("contributors", (String) null); // All 'null' in Botometer sample payload.
    status_json.addProperty("favorite_count", status.getFavoriteCount());
    status_json.addProperty("source", status.getSource());
    status_json.addProperty("text", status.getText());
    status_json.addProperty("in_reply_to_screen_name", status.getInReplyToScreenName());
    status_json.addProperty("coordinates", (String) null); // All 'null' in Botometer sample payload.
    status_json.addProperty("geo", (String) null);         // All 'null' in Botometer sample payload.
    status_json.addProperty("place", (String) null);       // All 'null' in Botometer sample payload.
    status_json.addProperty("created_at", getFormattedDate(status.getCreatedAt()));

    if (status.getInReplyToUserId() == -1) {
      status_json.addProperty("in_reply_to_user_id", (String) null);
      status_json.addProperty("in_reply_to_user_id_str", (String) null);
    } else {
      status_json.addProperty("in_reply_to_user_id", status.getInReplyToUserId());
      status_json.addProperty("in_reply_to_user_id_str", Long.toString(status.getInReplyToUserId()));
    }

    if (status.getInReplyToStatusId() == -1) {
      status_json.addProperty("in_reply_to_status_id", (String) null);
      status_json.addProperty("in_reply_to_status_id_str", (String) null);
    } else {
      status_json.addProperty("in_reply_to_status_id", status.getInReplyToStatusId());
      status_json.addProperty("in_reply_to_status_id_str", Long.toString(status.getInReplyToStatusId()));
    }

    if (status.getQuotedStatus() == null) // Field "only surfaces when the Tweet is a 'quote tweet'." https://developer.twitter.com/en/docs/tweets/data-dictionary/overview/tweet-object
      status_json.addProperty("is_quote_status", false);
    else
      status_json.addProperty("is_quote_status", true);

    JsonArray urls_array = new JsonArray();
    URLEntity[] url_entities = status.getURLEntities();

    for (int i = 0; i < url_entities.length; i++) {
      JsonObject temp_entity = new JsonObject();
      JsonArray temp_array = new JsonArray();

      temp_entity.addProperty("url", url_entities[i].getURL());
      temp_entity.addProperty("expanded_url", url_entities[i].getExpandedURL());
      temp_entity.addProperty("display_url", url_entities[i].getDisplayURL());

      temp_array.add(url_entities[i].getStart());
      temp_array.add(url_entities[i].getEnd());
      temp_entity.add("indices", temp_array);

      urls_array.add(temp_entity);
    }

    JsonArray symbols_array = new JsonArray(); // All 'null' in Botometer sample payload.

    JsonArray hashtags_array = new JsonArray();
    HashtagEntity[] hashtag_entities = status.getHashtagEntities();

    for (int i = 0; i < hashtag_entities.length; i++) {
      JsonObject temp_entity = new JsonObject();
      JsonArray temp_array = new JsonArray();

      temp_entity.addProperty("text", hashtag_entities[i].getText());

      temp_array.add(hashtag_entities[i].getStart());
      temp_array.add(hashtag_entities[i].getEnd());
      temp_entity.add("indices", temp_array);

      hashtags_array.add(temp_entity);
    }

    JsonArray user_mentions_array = new JsonArray();
    UserMentionEntity[] user_mention_entities = status.getUserMentionEntities();

    for (int i = 0; i < user_mention_entities.length; i++) {
      JsonObject temp_entity = new JsonObject();
      JsonArray temp_array = new JsonArray();

      temp_entity.addProperty("name", user_mention_entities[i].getName());
      temp_entity.addProperty("id_str", Long.toString(user_mention_entities[i].getId()));
      temp_entity.addProperty("id", user_mention_entities[i].getId());
      temp_entity.addProperty("screen_name", user_mention_entities[i].getScreenName());

      temp_array.add(user_mention_entities[i].getStart());
      temp_array.add(user_mention_entities[i].getEnd());
      temp_entity.add("indices", temp_array);

      user_mentions_array.add(temp_entity);
    }

    JsonObject entities = new JsonObject();
    entities.add("urls", urls_array);
    entities.add("symbols", symbols_array);
    entities.add("user_mentions", user_mentions_array);
    entities.add("hashtags", hashtags_array);

    status_json.add("entities", entities);

    if (status.getRetweetedStatus() != null)
      status_json.add("retweeted_status", getJsonFromStatus(status.getRetweetedStatus()));

    status_json.add("user", getJsonFromUser(status.getUser()));

    return status_json;
  }

  /**
   * Takes a User (from a Status), and create a JSON object that
   * can be parsed by the Botometer web service. Direct deserialization
   * using Gson would be ideal, but the fields don't line up exactly.
   *
   * @param user object.
   * @return JsonObject
   */
  private JsonObject getJsonFromUser(User user) {
    if (user == null) {
      logger.error("constructor arguments incorrect, make sure all populated.");
      System.exit(1);
    }

    JsonObject user_json = new JsonObject();

    user_json.addProperty("favourites_count", user.getFavouritesCount());
    user_json.addProperty("followers_count", user.getFollowersCount());
    user_json.addProperty("default_profile_image", user.isDefaultProfileImage());
    user_json.addProperty("description", user.getDescription());
    user_json.addProperty("url", user.getURL());
    user_json.addProperty("profile_background_color", user.getProfileBackgroundColor());
    user_json.addProperty("statuses_count", user.getStatusesCount());
    user_json.addProperty("follow_request_sent", user.isFollowRequestSent());
    user_json.addProperty("created_at", getFormattedDate(user.getCreatedAt()));
    user_json.addProperty("profile_image_url_https", user.getProfileImageURLHttps());
    user_json.addProperty("friends_count", user.getFriendsCount());
    user_json.addProperty("profile_image_url", user.getProfileImageURL());
    user_json.addProperty("profile_background_image_url_https", user.getProfileBackgroundImageUrlHttps());
    user_json.addProperty("profile_sidebar_fill_color", user.getProfileSidebarFillColor());
    user_json.addProperty("verified", user.isVerified());
    user_json.addProperty("contributors_enabled", user.isContributorsEnabled());
    user_json.addProperty("is_translator", user.isTranslator());
    user_json.addProperty("default_profile", user.isDefaultProfile());
    user_json.addProperty("lang", user.getLang());
    user_json.addProperty("protected", user.isProtected());
    user_json.addProperty("location", user.getLocation());
    user_json.addProperty("profile_text_color", user.getProfileTextColor());
    user_json.addProperty("profile_banner_url", user.getProfileBannerURL());
    user_json.addProperty("screen_name", user.getScreenName());
    user_json.addProperty("id_str", Long.toString(user.getId()));
    user_json.addProperty("name", user.getName());
    user_json.addProperty("profile_background_tile", user.isProfileBackgroundTiled());
    user_json.addProperty("profile_sidebar_border_color", user.getProfileSidebarBorderColor());
    user_json.addProperty("utc_offset", user.getUtcOffset());
    user_json.addProperty("id", user.getId());
    user_json.addProperty("profile_link_color", user.getProfileLinkColor());
    user_json.addProperty("profile_background_image_url", user.getProfileBackgroundImageURL());
    user_json.addProperty("listed_count", user.getListedCount());
    user_json.addProperty("geo_enabled", user.isGeoEnabled());
    user_json.addProperty("profile_use_background_image", user.isProfileUseBackgroundImage());
    user_json.addProperty("time_zone", user.getTimeZone());

    JsonObject entities = new JsonObject();

    JsonObject description_object = new JsonObject();
    description_object.add("urls", new JsonArray());
    entities.add("description", description_object);

    JsonArray urls_array = new JsonArray();
    JsonObject temp_entity = new JsonObject();
    JsonArray temp_array = new JsonArray();
    temp_entity.addProperty("url", user.getURLEntity().getURL());
    temp_entity.addProperty("expanded_url", user.getURLEntity().getExpandedURL());
    temp_entity.addProperty("display_url", user.getURLEntity().getDisplayURL());
    temp_array.add(user.getURLEntity().getStart());
    temp_array.add(user.getURLEntity().getEnd());
    temp_entity.add("indices", temp_array);
    urls_array.add(temp_entity);

    JsonObject url_object = new JsonObject();
    url_object.add("urls", urls_array);
    entities.add("url", url_object);
    user_json.add("entities", entities);

    return user_json;
  }

  /**
   * Takes a java.util.Date and returns a String in the format
   * that is expected by the Botometer service. E.g. "Mon Sep 06 13:31:27 +0000 2010".
   *
   * @param date object.
   * @return String
   */
  private String getFormattedDate(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

    return sdf.format(date);
  }
}





