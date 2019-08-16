package com.smate.web.mobile.controller.data.psn;

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
import com.smate.test.base.MockitoBase;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * app个人信息编辑测试类
 * 
 * @author xiexing
 * @date 2019年1月14日
 */
class APPPsnOperateControllerTest {
  private static final Logger logger = LoggerFactory.getLogger(MockitoBase.class);
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
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/psn/propertites/"
          + runEnv + "_test_APPPsnOperateControllerTest.properties");
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
  // @ValueSource(strings = {"1001000736992"})
  @ValueSource(strings = {"1000000727693"})
  @DisplayName("生成token,用于验证登录")
  void produceToken(String psnId) {
    /**
     * 以smate_unit_test@sina.com(id=1001000736992)进行测试
     */
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("appflag", psnId);
    Map<String, String> map = restTemplate.postForObject(junit_domainMobile + "/oauth/app/index", params, Map.class);
    assertNotEquals(null, map);
  }

  @Test
  @DisplayName("测试获取个人科技领域信息")
  void testEditPsnArea() {
    String str =
        restTemplate.postForObject(junit_domainMobile + "/data/psn/mobile/editareas", requestEntity, String.class);
    checkResult(str, "200");
  }

  @Test
  @DisplayName("测试获取个人关键字")
  void testEditPsnKeyWord() {
    String str =
        restTemplate.postForObject(junit_domainMobile + "/data/psn/mobile/editkeyword", requestEntity, String.class);
    checkResult(str, "200");
  }

  @ParameterizedTest
  @CsvSource({"序列,10,1", "序列,,2", ",10,3", ",,4", "序列,-1,5"})
  @DisplayName("测试查询学科关键词")
  void testAutoConstKeyDiscs(String searchKey, String keySize, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("searchKey", searchKey);
    params.add("keySize", keySize);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str = restTemplate.postForObject(junit_domainMobile + "/data/psn/mobile/autoconstkeydiscs", requestEntity,
        String.class);
    switch (number) {
      case 1:
        checkResult(str, "200");
        break;
      case 2:
        checkResult(str, "200");
        break;
      case 3:
        checkResult(str, "500");
        break;
      case 4:
        checkResult(str, "500");
        break;
      case 5:
        checkResult(str, "200");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"畜牧@@水产,1", ",2", "@@@@,3"})
  @DisplayName("测试保存人员关键词")
  void testSavePsnKeywords(String psnKeyStr, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("psnKeyStr", psnKeyStr);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str = restTemplate.postForObject(junit_domainMobile + "/data/psn/mobile/savepsnkeywords", requestEntity,
        String.class);
    switch (number) {
      case 1:
        checkResult(str, "200");
        break;
      case 2:
        checkResult(str, "500");
        break;
      case 3:
        checkResult(str, "200");
        break;
      default:
        break;
    }
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
