package com.smate.web.mobile.controller.data.pub.pdwh;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * 移动端成果认领控制器测试类
 * 
 * @author xiexing
 * @date 2019年1月25日
 */
public class APPPubConfirmWeChatControllerTest {
  private static final Logger logger = LoggerFactory.getLogger(APPPubConfirmWeChatControllerTest.class);
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
      proMap =
          JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/pub/pdwh/propertites/"
              + runEnv + "_test_APPPubConfirmWeChatControllerTest.properties");
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


  @Test
  @DisplayName("测试获取成果认领和全文认领记录数")
  void testGetTips() {
    String result =
        restTemplate.postForObject(junit_domainMobile + "/data/pub/mobile/ajaxmsgtips", requestEntity, String.class);
    checkResult(result, "200");
  }

  @Test
  @DisplayName("测试成果全文列表")
  void testPubFulltextList() {
    String result = restTemplate.postForObject(junit_domainMobile + "/data/pub/mobile/pubfulltextlist", requestEntity,
        String.class);
    checkResult(result, "200");
  }

  @Test
  @DisplayName("测试获取成果认领列表")
  void testPubConfirmList() {
    String result =
        restTemplate.postForObject(junit_domainMobile + "/data/pub/confirmpublist", requestEntity, String.class);
    checkResult(result, "200");
  }

  // 成果数据不好构造,暂时不测
  // @ParameterizedTest
  // @CsvSource({"1000001002146,1", "123456789,2", ",3", "-31232,4", "aaaaa,5"})
  // @DisplayName("测试确认成果认领")
  // void testAjaxAffirmPubComfirm(String pubId, Integer number) {
  // MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
  // params.add("pubId", pubId);
  // requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
  // String result = restTemplate.postForObject(junit_domainMobile +
  // "/data/pub/opt/ajaxAffirmPubComfirm", requestEntity,
  // String.class);
  // switch (number) {
  // case 1:
  // checkResult(result, "200");
  // break;
  // case 2:
  // checkResult(result, "500");
  // break;
  // case 3:
  // checkResult(result, "500");
  // break;
  // case 4:
  // checkResult(result, "500");
  // break;
  // case 5:
  // checkResult(result, "500");
  // break;
  // default:
  // break;
  // }
  // }

  // 成果数据不好构造,暂时不测
  // @ParameterizedTest
  // @CsvSource({"1000001002146,1", "123456789,2", ",3", "-31232,4", "aaaaa,5"})
  // @DisplayName("测试忽略成果认领")
  // void testAjaxIgnorePubComfirm(String pubId, Integer number) {
  // MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
  // params.add("pubId", pubId);
  // requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
  // String result = restTemplate.postForObject(junit_domainMobile +
  // "/data/pub/opt/ajaxIgnorePubComfirm", requestEntity,
  // String.class);
  // switch (number) {
  // case 1:
  // checkResult(result, "200");
  // break;
  // case 2:
  // checkResult(result, "500");
  // break;
  // case 3:
  // checkResult(result, "500");
  // break;
  // case 4:
  // checkResult(result, "500");
  // break;
  // case 5:
  // checkResult(result, "500");
  // break;
  // default:
  // break;
  // }
  // }

  @Test
  @DisplayName("测试全文认领，单个全文认领信息")
  void testPubFulltextDetails() {
    String result = restTemplate.postForObject(junit_domainMobile + "/data/pub/mobile/pubfulltextdetails",
        requestEntity, String.class);
    checkResult(result, "200");
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
