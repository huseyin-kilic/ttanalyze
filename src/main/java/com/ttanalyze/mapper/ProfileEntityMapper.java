/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.mapper;

import com.ttanalyze.domain.UserProfile;
import com.ttanalyze.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author huseyin.kilic
 */
@Component
public class ProfileEntityMapper {


  public UserProfile convert(User entity) {
    return UserProfile.builder()
            .id(entity.getId())
            .twitterId(entity.getTwitterId())
            .name(entity.getName())
            .build();
  }

  public User convert(UserProfile dto) {
    return User.builder()
            .id(dto.getId())
            .twitterId(dto.getTwitterId())
            .name(dto.getName())
            .build();
  }

}
