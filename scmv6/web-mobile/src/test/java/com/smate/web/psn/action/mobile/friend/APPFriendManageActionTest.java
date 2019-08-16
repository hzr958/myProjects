package com.smate.web.psn.action.mobile.friend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;


/**
 * 
 * @author SYL
 * 
 *         2019年2月
 *
 */
public class APPFriendManageActionTest {

  public static Map<String, String> proMap;
  private RestTemplate restTemplate = new RestTemplate();
  private String domain = proMap.get("junit_domainMobile");

  /**
   * 根据环境变量加载不同的properties文件
   * 
   * @throws IOException
   */

  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/mobile/friend/properties/"
        + runEnv + "_test_APPFriendManageActionTest.properties");
  }


  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }


  /**
   * 使用给定参数和url,发起请求,返回json数据
   * 
   * @param url
   * @param params
   * @return
   */
  private Map<String, Object> sendRequest(String url, Map<String, Object> params) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("token", proMap.get("token"));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    for (String param : params.keySet()) {
      body.add(param, params.get(param));
    }
    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
    String jsonStr = restTemplate.postForObject(url, httpEntity, String.class);
    System.out.println(jsonStr);
    return JacksonUtils.jsonToMap(jsonStr);
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @DisplayName("测试进入好友列表")
  @CsvSource({"psnId正常,", "psnId不存在,123456789", "psnId异常,12aaaa"})
  public void searchFriendListTest(String description, String psnId) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (description.equals("psnId正常")) {
      params.put("psnId", proMap.get("psnId"));
    } else {
      params.put("psnId", psnId);
    }
    Map<String, Object> jsonMap = sendRequest(domain + "/app/psnweb/friendlist", params);
    switch (description) {
      case "psnId正常":
        assertTrue(jsonMap.get("status").equals("200"));
        List<Object> friendsList =
            (List<Object>) ((LinkedHashMap<String, Object>) jsonMap.get("results")).get("commentlist");
        assertTrue(friendsList.size() >= 0);
        break;
      case "psnId不存在":
        assertTrue(jsonMap.get("status").equals("200"));
        List<Object> friendsList1 =
            (List<Object>) ((LinkedHashMap<String, Object>) jsonMap.get("results")).get("commentlist");
        assertTrue(friendsList1.size() == 0);
        break;
      case "psnId异常":
        // 这边会返回一个500页面
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("测试获取可能认识的人")
  public void loadMobileMayKnowTest() {
    Map<String, Object> params = new HashMap<String, Object>();
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/ajaxknownew", params);
    assertTrue(map.get("status").equals("200"));
    List<Object> friendsList = (List<Object>) ((LinkedHashMap<String, Object>) map.get("results")).get("commentlist");
    assertTrue(friendsList.size() >= 0);
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @CsvSource({"psnId正常,1000000047599", "psnId不存在,123456789", "psnId非法,12aaaa"})
  @DisplayName("测试获取好友请求数量")
  public void getfriendNumberTest(String description, String psnId) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (description.equals("psnId正常")) {
      params.put("psnId", proMap.get("psnId"));
    } else {
      params.put("psnId", psnId);
    }
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/ajaxgetreqfriendnumber", params);
    switch (description) {
      case "psnId正常":
        assertTrue(map.get("status").equals("200"));
        int num = (int) ((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) map.get("results"))
            .get("commentlist")).get("friendNumbers");
        assertTrue(num >= 0);
        break;
      case "psnId不存在":
        assertTrue(map.get("status").equals("200"));
        int num1 = (int) ((LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) map.get("results"))
            .get("commentlist")).get("friendNumbers");
        assertTrue(num1 == 0);
        break;
      case "psnId非法":
        // 这边出现了500错误
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @CsvSource({"正常数据,,", "psnId不存在,123456789,", "电话号码异常,,'15522as'", "电话号码为空,,"})
  @DisplayName("测试获取手机端通讯录联系人")
  public void getContactfriendTest(String description, String psnId, String receivePhone) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("psnId", psnId);
    params.put("receivePhone", receivePhone);
    switch (description) {
      case "正常数据":
        params.put("psnId", proMap.get("psnId"));
        params.put("receivePhone", proMap.get("receivePhone"));
        Map<String, Object> map = sendRequest(domain + "/app/psnweb/getcontactfriend", params);
        assertTrue(map.get("status").equals("200"));
        List<Object> friendsList =
            (List<Object>) ((LinkedHashMap<String, Object>) map.get("results")).get("commentlist");
        assertTrue(friendsList.size() >= 0);
        break;

      case "psnId不存在":
        params.put("psnId", psnId);
        params.put("receivePhone", proMap.get("receivePhone"));
        Map<String, Object> map1 = sendRequest(domain + "/app/psnweb/getcontactfriend", params);
        assertTrue(map1.get("status").equals("200"));
        List<Object> friendsList1 =
            (List<Object>) ((LinkedHashMap<String, Object>) map1.get("results")).get("commentlist");
        assertTrue(friendsList1.size() >= 0);
        break;

      case "电话号码异常":
        params.put("psnId", proMap.get("psnId"));
        params.put("receivePhone", receivePhone);
        Map<String, Object> map2 = sendRequest(domain + "/app/psnweb/getcontactfriend", params);
        assertTrue(map2.get("status").equals("200"));
        List<Object> friendsList2 =
            (List<Object>) ((LinkedHashMap<String, Object>) map2.get("results")).get("commentlist");
        assertTrue(friendsList2.size() == 0);
        break;

      case "电话号码为空":
        params.put("psnId", proMap.get("psnId"));
        params.put("receivePhone", receivePhone);
        Map<String, Object> map3 = sendRequest(domain + "/app/psnweb/getcontactfriend", params);
        assertTrue(map3.get("status").equals("200"));
        List<Object> friendsList3 =
            (List<Object>) ((LinkedHashMap<String, Object>) map3.get("results")).get("commentlist");
        assertTrue(friendsList3.size() == 0);
        break;
      default:
        break;
    }
  }
}
