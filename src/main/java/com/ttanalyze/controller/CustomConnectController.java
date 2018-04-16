/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/connect")
public class CustomConnectController extends ConnectController {

  @Autowired
  private Twitter twitter;

  public CustomConnectController(ConnectionFactoryLocator connectionFactoryLocator,
          ConnectionRepository connectionRepository) {
    super(connectionFactoryLocator, connectionRepository);
  }

  @Override
  protected String connectView(String providerId) {
    return "redirect:/login";
  }

  @Override
  protected String connectedView(String providerId) {
    return "redirect:/profile";
  }

  @Override
  @RequestMapping(value="/{providerId}", method= RequestMethod.DELETE)
  public RedirectView removeConnections(@PathVariable String providerId, NativeWebRequest request) {

    return removeConnection(providerId, twitter.userOperations().getProfileId() + "" ,request);
  }
}