package com.smate.web.mobile.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

/**
 * @ClassName RestUtils
 * @Description TODO
 * @Author LIJUN
 * @Date 2018/8/15
 * @Version v1.0
 */
public class RestUtils {
  /**
   * @Author LIJUN
   * @Description //构建RequestEntity用于模拟form提交参数
   * @Date 20:34 2018/8/15
   * @Param [ MultiValueMap<String, String> params,封装参数，不要替换为Map与HashMap，否则参数无法传递]
   * @return org.springframework.http.HttpEntity<org.springframework.util.MultiValueMap<java.lang.String,java.lang.String>>
   **/
  public static HttpEntity<MultiValueMap<String, String>> buildPostRequestEntity(MultiValueMap<String, String> params) {
    HttpHeaders headers = new HttpHeaders();
    // 请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    return requestEntity;

  }

  public static HttpEntity<MultiValueMap<String, String>> buiPostRequestEntityJson(
      MultiValueMap<String, String> params) {
    HttpHeaders headers = new HttpHeaders();
    // 请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    return requestEntity;
  }
}
