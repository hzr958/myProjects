package com.smate.core.base.utils.url;

import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 
 * @author aijiangbin
 * @date 2018年9月5日
 */
public class RestUtils {

  /**
   * 获取远程信息
   * 
   * @param param
   * @param SERVER_URL
   * @param restTemplate
   * @return
   */
  public static Object getRemoteInfo(Object param, String SERVER_URL, RestTemplate restTemplate) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(param), requestHeaders);
    Object object = restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }

  public static void main(String[] args) {
    String url = "http://nankaiuat.scholarmate.com/insweb/insIndexValidate";
    String param = "type=1&ins_name=南开大学11";
    String s = HttpRequestUtils.sendPost(url, param);
    System.out.println(s);
    if(StringUtils.isNotBlank(s) && JacksonUtils.isJsonString(s)){
      Map<String, String> objectMap = JacksonUtils.jsonToMap(s);
      if(objectMap  != null && objectMap.get("is_exist").equalsIgnoreCase("no")){
        System.out.println("success");
      }
    }
  }
}
