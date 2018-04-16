/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping(path = "/contact")
public class ContactController extends BaseController {

  @RequestMapping
  public String contactView(Model model) {
    fillProfile(model);
    return "contact";
  }
}