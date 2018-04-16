/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author huseyin.kilic
 */
@Configuration
public class ClientConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
