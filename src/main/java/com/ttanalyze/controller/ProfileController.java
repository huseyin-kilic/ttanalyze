/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

  @RequestMapping
  public String profileView(Model model) {
    if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
      return "redirect:/login";
    }
    fillProfile(model);
    return "profile";
  }

}