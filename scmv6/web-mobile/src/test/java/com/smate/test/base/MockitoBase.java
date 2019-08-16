package com.smate.test.base;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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
 * 使用 mokito 测试类 的父类
 * 
 * @author xiexing
 *
 * @date 2019年1月15日
 */
public class MockitoBase {
  protected static final Logger logger = LoggerFactory.getLogger(MockitoBase.class);
  protected static String junit_domainMobile = "";
  protected static String token = "";
  protected static HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
  protected static RestTemplate restTemplate = null;
  protected static HttpHeaders headers = null;
  private static Map<String, String> proMap;
  static {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
    }
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties(
          "src/test/java/com/smate/test/base/properties/" + runEnv + "_test_APPPsnOperateControllerTest.properties");
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

  public void checkResult(String str, String code) {
    Map<String, Object> map = JacksonUtils.jsonToMap(str);
    assertNotEquals(null, map);
    assertTrue(code.equals(map.get("status")));
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
  }


}
