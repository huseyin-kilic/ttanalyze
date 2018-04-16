/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import com.ttanalyze.domain.AnalyzeResponse;
import com.ttanalyze.service.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/analyze")
public class AnalyzeController extends BaseController {

  @Autowired
  private AnalyzerService analyzerService;

  @RequestMapping("/{screenName}")
  public String analyzeFriend(Model model, @PathVariable("screenName") String screenName) {
    if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
      return "redirect:/login";
    }
    fillProfile(model);
    AnalyzeResponse analyzeResponse = analyzerService.analyzeBotness(screenName);
    model.addAttribute("analysisResult", analyzeResponse);
    return "analysisResult";
  }

}