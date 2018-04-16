/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import com.ttanalyze.domain.UserProfile;
import com.ttanalyze.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * @author huseyin.kilic
 */
@Controller
public class BaseController {

  @Autowired
  protected ConnectionRepository connectionRepository;

  @Autowired
  protected ProfileService profileService;

  @Autowired
  protected Twitter twitter;

  public void fillProfile(Model model) {
    UserProfile user = profileService.getConnectedUserProfile();

    model.addAttribute("user", user);
    model.addAttribute("twitterProfile", user.getTwitterProfile());
    model.addAttribute("profileImageUrl", user.getProfileImageUrl());
  }

}