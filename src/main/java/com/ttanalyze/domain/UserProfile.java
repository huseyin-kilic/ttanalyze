/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * @author huseyin.kilic
 */
@Data
@Builder
public class UserProfile {

  private Long id;
  private long twitterId;
  private String name;
  private String email;
  private TwitterProfile twitterProfile;
  private String profileImageUrl;

}
