/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/friends")
public class FriendListController extends BaseController {

  @Autowired
  private Twitter twitter;

  @RequestMapping
  public String friendListView(Model model) {
    if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
      return "redirect:/login";
    }
    fillProfile(model);
    model.addAttribute("friends", twitter.friendOperations().getFollowers());
    return "friends";
  }

}