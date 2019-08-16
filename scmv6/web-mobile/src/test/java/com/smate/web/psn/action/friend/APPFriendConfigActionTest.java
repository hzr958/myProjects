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

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * @Description 好友添加接口测试用例
 * @author YWL
 * @Date 2019/2/19
 */
class APPFriendConfigActionTest {
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
    return restTemplate.postForObject(domainMobile + "/app/friend" + uri, HttpEntity, Map.class);
  }


  @Test
  @DisplayName("app发送添加好友请求空参校验")
  void testCheckAddFriendReqPar() {
    String reqUri = "/addfriend";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }

  @Test
  @DisplayName("app发送添加好友请求接口校验")
  void testCheckAddFriendReq() {
    String reqUri = "/addfriend";
    String[] des3PsnIds = {proMap.get("des3PsnId"), proMap.get("refuseAddDes3PsnId")};
    for (int i = 0; i < des3PsnIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3Id", des3PsnIds[i]);
      map = getReslutMap(requestBody, reqUri);
      if (i == 0) {
        assertTrue("200".equals(map.get("status")));
      } else {
        String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
        String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
        assertTrue("304".equals(map.get("status")) && "用户不允许加他为好友".equals(commentlist));
      }
    }
  }

  @Test
  @DisplayName("扫描二维码添加好友跳转页空参校验")
  void testCheckpreAddFriendPar() {
    String reqUri = "/preaddfriend";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    assertTrue("400".equals(map.get("status")) && "请求参数不正确".equals(commentlist));
  }

  @Test
  @DisplayName("扫描二维码添加好友跳转页接口校验")
  void testCheckpreAddFriend() {
    String reqUri = "/preaddfriend";
    String[] des3PsnIds = {proMap.get("des3PsnId"), proMap.get("refuseAddDes3PsnId")};
    for (int i = 0; i < des3PsnIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3Id", des3PsnIds[i]);
      map = getReslutMap(requestBody, reqUri);
      if (i == 0) {
        assertTrue("200".equals(map.get("status")));
      } else {
        assertTrue("200".equals(map.get("status")));
      }
    }
  }


}
