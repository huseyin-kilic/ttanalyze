/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttanalyze.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a Twitter status update (e.g., a "tweet").
 * @author Craig Walls
 */
public class NativeTweet implements Serializable {
  private static final Long serialVersionUID = 1L;

  private boolean possibly_sensitive;
  private String coordinates;
  private boolean favorited;
  private String geo;
  private Long id;
  private String in_reply_to_user_id_str;
  private Date created_at;
  private String place;
  private boolean retweeted;
  private String entities;
  private boolean truncated;
  private String in_reply_to_screen_name;
  private String lang;
  private Long favorite_count;
  private String source;
  private String in_reply_to_status_id_str;
  private String id_str;
  private String text;
  private Long in_reply_to_user_id;
  private String user;
  private String contributors;
  private Long in_reply_to_status_id;
  private boolean is_quote_status;
  private Long retweet_count;

  public boolean isPossibly_sensitive() {
    return possibly_sensitive;
  }

  public NativeTweet setPossibly_sensitive(boolean possibly_sensitive) {
    this.possibly_sensitive = possibly_sensitive;
    return this;
  }

  public String getCoordinates() {
    return coordinates;
  }

  public NativeTweet setCoordinates(String coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public boolean isFavorited() {
    return favorited;
  }

  public NativeTweet setFavorited(boolean favorited) {
    this.favorited = favorited;
    return this;
  }

  public String getGeo() {
    return geo;
  }

  public NativeTweet setGeo(String geo) {
    this.geo = geo;
    return this;
  }

  public Long getId() {
    return id;
  }

  public NativeTweet setId(Long id) {
    this.id = id;
    return this;
  }

  public String getIn_reply_to_user_id_str() {
    return in_reply_to_user_id_str;
  }

  public NativeTweet setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
    this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    return this;
  }

  public Date getCreated_at() {
    return created_at;
  }

  public NativeTweet setCreated_at(Date created_at) {
    this.created_at = created_at;
    return this;
  }

  public String getPlace() {
    return place;
  }

  public NativeTweet setPlace(String place) {
    this.place = place;
    return this;
  }

  public boolean isRetweeted() {
    return retweeted;
  }

  public NativeTweet setRetweeted(boolean retweeted) {
    this.retweeted = retweeted;
    return this;
  }

  public String getEntities() {
    return entities;
  }

  public NativeTweet setEntities(String entities) {
    this.entities = entities;
    return this;
  }

  public boolean isTruncated() {
    return truncated;
  }

  public NativeTweet setTruncated(boolean truncated) {
    this.truncated = truncated;
    return this;
  }

  public String getIn_reply_to_screen_name() {
    return in_reply_to_screen_name;
  }

  public NativeTweet setIn_reply_to_screen_name(String in_reply_to_screen_name) {
    this.in_reply_to_screen_name = in_reply_to_screen_name;
    return this;
  }

  public String getLang() {
    return lang;
  }

  public NativeTweet setLang(String lang) {
    this.lang = lang;
    return this;
  }

  public Long getFavorite_count() {
    return favorite_count;
  }

  public NativeTweet setFavorite_count(Long favorite_count) {
    this.favorite_count = favorite_count;
    return this;
  }

  public String getSource() {
    return source;
  }

  public NativeTweet setSource(String source) {
    this.source = source;
    return this;
  }

  public String getIn_reply_to_status_id_str() {
    return in_reply_to_status_id_str;
  }

  public NativeTweet setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
    this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    return this;
  }

  public String getId_str() {
    return id_str;
  }

  public NativeTweet setId_str(String id_str) {
    this.id_str = id_str;
    return this;
  }

  public String getText() {
    return text;
  }

  public NativeTweet setText(String text) {
    this.text = text;
    return this;
  }

  public Long getIn_reply_to_user_id() {
    return in_reply_to_user_id;
  }

  public NativeTweet setIn_reply_to_user_id(Long in_reply_to_user_id) {
    this.in_reply_to_user_id = in_reply_to_user_id;
    return this;
  }

  public String getUser() {
    return user;
  }

  public NativeTweet setUser(String user) {
    this.user = user;
    return this;
  }

  public String getContributors() {
    return contributors;
  }

  public NativeTweet setContributors(String contributors) {
    this.contributors = contributors;
    return this;
  }

  public Long getIn_reply_to_status_id() {
    return in_reply_to_status_id;
  }

  public NativeTweet setIn_reply_to_status_id(Long in_reply_to_status_id) {
    this.in_reply_to_status_id = in_reply_to_status_id;
    return this;
  }

  public boolean isIs_quote_status() {
    return is_quote_status;
  }

  public NativeTweet setIs_quote_status(boolean is_quote_status) {
    this.is_quote_status = is_quote_status;
    return this;
  }

  public Long getRetweet_count() {
    return retweet_count;
  }

  public NativeTweet setRetweet_count(Long retweet_count) {
    this.retweet_count = retweet_count;
    return this;
  }
}
