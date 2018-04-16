/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huseyin.kilic
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

  @RequestMapping
  public String handleError(HttpServletRequest request, HttpServletResponse response) {
    if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
      return "errorNotFound";
    } else {
      return "errorGeneric";
    }
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}