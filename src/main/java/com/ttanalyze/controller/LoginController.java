/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/")
public class LoginController {

  @Autowired
  private ConnectionRepository connectionRepository;

  @RequestMapping
  public String redirectToLogin() {
    return "redirect:/login";
  }

  @RequestMapping("/login")
  public String loginView() {
    if (CollectionUtils.isEmpty(connectionRepository.findConnections(Twitter.class))
            || connectionRepository.findPrimaryConnection(Twitter.class) == null) {
      return "login";
    }
    return "redirect:/profile";
  }

}