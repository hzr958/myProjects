package com.smate.web.psn.action.mobile.psnlist;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.web.psn.action.mobile.influence.AppMobilePsnInfluenceActionTest;

public class APPPsnListViewActionTest {
  protected static final Logger logger = LoggerFactory.getLogger(AppMobilePsnInfluenceActionTest.class);

  protected static Map<String, String> proMap;
  private RestTemplate restTemplate = new RestTemplate();

  static String domainscm;
  static String token;
  static String psnId;

  Map<String, Object> resultMap = new HashMap<String, Object>();

  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/mobile/psnlist/properties/"
          + runEnv + "_test_APPPsnListViewActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    domainscm = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    psnId = proMap.get("psnId");
  }

  @ParameterizedTest
  @DisplayName("进入人员列表页面")
  @CsvSource({"kwIdentific,12993,502"})
  public void testGetKeywordIdentifyPsnList(String serviceType, String discId, String scienceAreaId) {
    String commentUrl = domainscm + "/app/psnweb/outside/mobile/identifypsn";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("serviceType", serviceType);
    param.add("discId", discId);
    param.add("scienceAreaId", scienceAreaId);

    param.add("des3PsnId", Des3Utils.encodeToDes3(psnId));

    resultMap = this.postUrl(param, commentUrl);
    assertTrue("200".equals(resultMap.get("status")));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private Map<String, Object> postUrl(MultiValueMap param, String url) {
    HttpEntity<MultiValueMap> httpEntity = this.getEntity(param);// 创建头部信息
    return restTemplate.postForObject(url, httpEntity, Map.class);
  }

  @SuppressWarnings("rawtypes")
  private HttpEntity getEntity(MultiValueMap param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("token", token);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(param, headers);
    return HttpEntity;
  }

  public void checkResult(Map<String, Object> map) {
    assertNotEquals(null, map);
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
    assertTrue("200".equals(map.get("status")));
  }

  public void checkErrorResult(Map<String, Object> map, String mesge) {
    assertNotEquals(null, map);
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
    assertTrue("500".equals(map.get("status")));
    assertTrue(mesge.equals(map.get("msg")));
  }
}
