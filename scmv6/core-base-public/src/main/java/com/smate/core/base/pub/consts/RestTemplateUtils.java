package com.smate.core.base.pub.consts;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtils {
  public static String post(RestTemplate restTemplate, String url, String json) {
    String result = "";
    try {
      HttpHeaders headers = new HttpHeaders();
      MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
      headers.setContentType(type);
      HttpEntity<String> entity = new HttpEntity<String>(json, headers);
      result = restTemplate.postForObject(url, entity, String.class);
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
}
