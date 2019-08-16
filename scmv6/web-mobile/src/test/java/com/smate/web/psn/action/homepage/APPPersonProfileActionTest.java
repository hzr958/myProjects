package com.smate.web.psn.action.homepage;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

public class APPPersonProfileActionTest {
  protected static final Logger logger = LoggerFactory.getLogger(APPPersonProfileActionTest.class);

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
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/homepage/properties/"
          + runEnv + "_test_APPPersonProfileActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    domainscm = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    psnId = proMap.get("psnId");
  }

  @ParameterizedTest
  @CsvSource({"化学", "null", "aa"})
  @DisplayName("自动提示科技领域数据")
  public void testAjaxGetScienceArea(String searchKey) {
    String commentUrl = domainscm + "/app/psnweb/scienceArea/ajaxgetScienceArea";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("searchKey", searchKey);
    resultMap = this.postUrl(param, commentUrl);
    assertTrue("操作成功".equals(resultMap.get("msg")));
    assertTrue("200".equals(resultMap.get("status")));

  }

  @ParameterizedTest
  @CsvSource({"深圳,440000", "广东省,444444", "不存在的省,123456"})
  @DisplayName("自动提示科技领域")
  public void testAjaxAutoRegion(String searchKey, String superRegionId) {
    String commentUrl = domainscm + "/app/psnweb/homepage/ajaxautoregion";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("searchKey", searchKey);
    param.add("superRegionId", superRegionId);
    resultMap = this.postUrl(param, commentUrl);
    Map<String, Object> result = (Map<String, Object>) resultMap.get("results");
    Object list = result.get("commentlist");
    if (searchKey.equals("深圳") || searchKey.equals("广东省")) {
      assertTrue(!"[]".equals(list));
    } else if (searchKey.equals("不存在的省")) {
      assertTrue("[]".equals(list));
    }
    assertTrue("200".equals(resultMap.get("status")));
  }

  @ParameterizedTest
  @CsvSource({"'603,502'", "','"})
  @DisplayName("保存人员科技领域信息")
  public void testSavePsnScienceArea(String scienceAreaIds) {
    String commentUrl = domainscm + "/app/psnweb/sciencearea/ajaxsave";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("des3PsnId", Des3Utils.specEncodeToDes3(psnId));
    param.add("scienceAreaIds", scienceAreaIds);
    resultMap = this.postUrl(param, commentUrl);
    Map<String, Object> result = (Map<String, Object>) resultMap.get("results");
    Map<String, Object> commentlistMap = (Map<String, Object>) result.get("commentlist");
    String resultStatus = (String) commentlistMap.get("result");
    if (scienceAreaIds.equals("603,502")) {
      assertTrue("success".equals(resultStatus));
    } else if (scienceAreaIds.equals(",")) {
      assertTrue("error".equals(resultStatus));
    }
    assertTrue("200".equals(resultMap.get("status")));
  }

  @Test
  @DisplayName("编辑人员科技领域")
  public void testEditPsnScienceArea() {
    String commentUrl = domainscm + "/app/psnweb/sciencearea/ajaxedit";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("des3PsnId", Des3Utils.specEncodeToDes3(psnId));
    resultMap = this.postUrl(param, commentUrl);
    Map<String, Object> result = (Map<String, Object>) resultMap.get("results");
    Object list = result.get("commentlist");
    assertTrue(!"[]".equals(list));
    assertTrue("200".equals(resultMap.get("status")));
  }

  @Test
  @DisplayName("显示人员科技领域")
  public void testShowPsnScienceArea() {
    String commentUrl = domainscm + "/app/psnweb/sciencearea/ajaxshow";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("des3PsnId", Des3Utils.specEncodeToDes3(psnId));
    resultMap = this.postUrl(param, commentUrl);
    Map<String, Object> result = (Map<String, Object>) resultMap.get("results");
    Object list = result.get("commentlist");
    assertTrue(!"[]".equals(list));
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
