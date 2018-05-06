/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import com.ttanalyze.domain.TotalAnalysisResult;
import com.ttanalyze.service.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    TotalAnalysisResult analysisResult = analyzerService.analyze(screenName);
    model.addAttribute("userProfile", twitter.userOperations().getUserProfile(screenName));
    model.addAttribute("analysisResult", analysisResult);
    return "friendDetails";
  }

}