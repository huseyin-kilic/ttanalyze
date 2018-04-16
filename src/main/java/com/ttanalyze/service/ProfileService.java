/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.service;

import com.ttanalyze.dao.ProfileDao;
import com.ttanalyze.domain.UserProfile;
import com.ttanalyze.entity.User;
import com.ttanalyze.mapper.ProfileEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author huseyin.kilic
 */
@Transactional
@Service
public class ProfileService {

  @Autowired
  protected Twitter twitter;

  @Autowired
  private ProfileDao dao;

  @Autowired
  private ProfileEntityMapper mapper;


  public UserProfile getConnectedUserProfile() {
    UserProfile userProfile = getProfileByTwitterId(twitter.userOperations().getProfileId());

    TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();
    String profileImageUrl = twitterProfile.getProfileImageUrl();
    if (profileImageUrl.contains("default")) {
      profileImageUrl = "http://lorempixel.com/400/200/";
    }

    userProfile.setTwitterProfile(twitterProfile);
    userProfile.setProfileImageUrl(profileImageUrl);

    return userProfile;
  }

  @Transactional
  public boolean saveProfile(UserProfile userProfile) {
    try {
      User entity = mapper.convert(userProfile);
      dao.persist(entity);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public UserProfile getProfileByTwitterId(long twitterProfileId) {
    User userEntity = dao.findByTwitterId(twitterProfileId);
    UserProfile userProfile;
    if (userEntity == null) {
      userProfile = UserProfile.builder()
              .twitterId(twitterProfileId)
              .name(twitter.userOperations().getScreenName())
              .build();
      saveProfile(userProfile);
    } else {
      userProfile = mapper.convert(userEntity);
    }
    return userProfile;
  }

}