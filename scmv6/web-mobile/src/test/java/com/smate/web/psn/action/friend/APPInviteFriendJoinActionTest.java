package com.smate.web.psn.action.friend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.test.utils.JunitPropertiesUtils;

/**
 * @Description app分享操作获取好友名字测试用例
 * @author YWL
 * @Date 2019/2/19
 */
class APPInviteFriendJoinActionTest {
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
    proMap = JunitPropertiesUtils
        .loadProperties("src/test/java/com/smate/web/psn/action/friend/properties/" + runEnv + "_test.properties");
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


  @Test
  @DisplayName("群组动态-分享到我的好友-获取我的好友名字空参校验")
  void testCheckAjaxGetMyFriendNamesPar() {
    String reqUri = "/friend/ajaxgetmyfriendnames";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("304".equals(map.get("status")));
  }

  @Test
  @DisplayName("app发送添加好友请求接口校验")
  void testCheckAjaxGetMyFriendNames() {
    String reqUri = "/friend/ajaxgetmyfriendnames";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("psnId", "date");
    requestBody.add("orderBy", "date");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("304".equals(map.get("status")));
  }

}
