package com.smate.web.dyn.action.msg.mobile;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * APP消息通知测试类
 * 
 * @author xiexing
 * @date 2019年1月30日
 */
public class AppMsgShowActionTest {
  private static final Logger logger = LoggerFactory.getLogger(AppMsgShowActionTest.class);
  private static String junit_domainMobile = "";
  private static String token = "";
  private static HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
  private static RestTemplate restTemplate = null;
  private static HttpHeaders headers = null;
  private static Map<String, String> proMap;
  static {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
    }
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/dyn/action/msg/mobile/properties/"
          + runEnv + "_test_AppMsgShowActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    junit_domainMobile = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    if (requestEntity == null) {
      headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      headers.add("token", token);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(headers);
    }
  }

  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }

  @ParameterizedTest
  @CsvSource({"1,1", ",2", "-1,3"})
  @DisplayName("测试获取站内信聊天会话列表")
  void testGetChatPsnList(String pageNo, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("pageNo", pageNo);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str = restTemplate.postForObject(junit_domainMobile + "/app/dynweb/showmsg/ajaxgetchatpsnlist",
        requestEntity, String.class);
    checkResult(str, "200");
  }

  @ParameterizedTest
  @CsvSource({"1,1", ",2", "100,3"})
  @DisplayName("测试获取app-获取消息列表")
  void testGetMsg(String pageNo, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("pageNo", pageNo);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxgetmsglist", requestEntity, String.class);
    checkResult(str, "200");
  }


  @ParameterizedTest
  @CsvSource({"1,33543,1000000733782,1", "2,33543,1000000733782,2", "3,33543,1000000733782,3", "1,,1000000733782,4",
      "2,33543,,5", ",,,6"})
  @DisplayName("测试将app-标记消息为已读")
  void testSetReadMsg(String readMsgType, String msgRelationId, String senderId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("readMsgType", readMsgType);
    params.add("msgRelationId", msgRelationId);
    params.add("senderId", senderId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxsetread", requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(str, "200");
        break;
      case 2:
        checkResult(str, "200");
        break;
      case 3:
        checkResult(str, "200");
        break;
      case 4:
        checkResult(str, "200");
        break;
      case 5:
        checkResult(str, "200");
        break;
      case 6:
        checkResult(str, "500");
        break;
      default:
        break;
    }
  }

  @Test
  @DisplayName("测试app-获取未读消息数")
  void testGetMsgTips() {
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxcountmsg", requestEntity, String.class);
    checkResult(str, "200");
  }

  @ParameterizedTest
  @ValueSource(strings = {"33543", ""})
  @DisplayName("测试站内信删除聊天消息")
  void testAjaxdelchatMsg(String msgRelationId) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("msgRelationId", msgRelationId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxdelchatmsg", requestEntity, String.class);
    checkResult(str, "200");
  }

  @ParameterizedTest
  @ValueSource(strings = {"33543,33542,33564", "33543", ""})
  @DisplayName("测试app-删除消息中心消息")
  void testAjaxDelMsg(String msgRelationIds) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("msgRelationIds", msgRelationIds);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str = restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxdelmsg", requestEntity, String.class);
    checkResult(str, "200");
  }

  /**
   * 验证结果
   */
  public void checkResult(String str, String code) {
    Map<String, Object> map = JacksonUtils.jsonToMap(str);
    assertNotEquals(null, map);
    assertTrue(code.equals(map.get("status")));
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
  }

}

