package com.smate.web.psn.action.autocomplete;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * @Description APP基金显示相关数据接口测试用例
 * @author YWL
 * @Date 2019/1/31
 */
class APPAutoCompleteActionTest {
  protected static Map<String, String> proMap;
  protected static String domainMobile;
  protected static Map<String, String> map = new HashMap<>();
  protected static RestTemplate restTemplate = new RestTemplate();
  protected static HttpHeaders headers;
  protected static MultiValueMap<String, String> requestBody;

  // 加载环境参数的配置文件
  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils.loadProperties(
        "src/test/java/com/smate/web/psn/action/autocomplete/properties/" + runEnv + "_test.properties");
    domainMobile = proMap.get("junit_domainMobile");
    initHttpHeaders();
  }

  // 初始化请求头
  public static void initHttpHeaders() {
    headers = new HttpHeaders();
    requestBody = new LinkedMultiValueMap<String, String>();
    requestBody.add("appflag", proMap.get("psnId"));
    headers.add("token", proMap.get("token"));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  // 发送请求并获取返回数据
  public Map<String, String> getReslutMap(MultiValueMap<String, String> requestBody, String uri) {
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(requestBody, headers);
    return restTemplate.postForObject(domainMobile + "/app/psnweb" + uri, HttpEntity, Map.class);
  }


  @ParameterizedTest
  @DisplayName("个人主页-获取自动填充的部门(学院)接口的校验")
  // 数据来源于CONST_INS_UNIT_SEARCH表
  @ValueSource(strings = {"Zh", "En"})
  void testGetAutoInsUnit(String locale) {
    String reqUri = "/ajaxautoinsunit";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("searchKey", proMap.get("searchKey" + locale));
    requestBody.add("insName", proMap.get("insName" + locale));
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    List object = (ArrayList<Object>) JacksonUtils.jsonToMap(results).get("commentlist");
    assertTrue("200".equals(map.get("status")) && object.size() > 0);
  }

  @Test
  @DisplayName("个人主页-获取自动填充的职称接口的校验")
  void testGetAutoPostion() {
    String reqUri = "/ajaxautoposition";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("searchKey", "教授");
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    List object = (ArrayList<Object>) JacksonUtils.jsonToMap(results).get("commentlist");
    assertTrue("200".equals(map.get("status")) && object.size() > 0);
  }

  @ParameterizedTest
  @DisplayName("个人主页-获取自动填充的机构名称接口的校验")
  // 数据来源INSTITUTION表
  @ValueSource(strings = {"Zh", "En"})
  void testGetAutoInstitution(String locale) {
    String reqUri = "/ajaxautoinstitution";
    for (Integer i = 1; i <= 3; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("searchKey", proMap.get("institutionSearchKey" + locale + i.toString()));
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      List object = (ArrayList<Object>) JacksonUtils.jsonToMap(results).get("commentlist");
      assertTrue("200".equals(map.get("status")) && object.size() > 0);
    }
  }

}
