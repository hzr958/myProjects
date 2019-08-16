package com.smate.web.mobile.controller.data.dyn;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.test.utils.JunitPropertiesUtils;

class APPDynamicActionTest {
  protected static final Logger logger = LoggerFactory.getLogger(APPDynamicActionTest.class);
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, String> proMap;
  static {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/dyn/properties/"
          + runEnv + "_test_APPDynamicActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
  }
  String domainscm = proMap.get("junit_domainMobile");
  String token = proMap.get("token");
  Long snsPubId = NumberUtils.toLong(proMap.get("snsPubId"));
  String des3SnsPubId = Des3Utils.encodeToDes3(Objects.toString(snsPubId));
  Long otherPsnPubId = NumberUtils.toLong(proMap.get("otherPsnPubId"));
  Long otherPsnId = NumberUtils.toLong(proMap.get("otherPsnId"));

  String des3OtherPsnPubId = Des3Utils.encodeToDes3(proMap.get("otherPsnPubId"));

  Map<String, Object> resultMap = new HashMap<String, Object>();

  @Test
  @DisplayName("获取首页动态测试")
  void testGetIOSDynList() {
    String commentUrl = domainscm + "/app/dynweb/show";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    resultMap = this.postUrl(param, commentUrl);
    assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
  }

  @Test
  @DisplayName("获取单条动态测试")
  public void testGetDynInfoById() {
    String commentUrl = domainscm + "/app/dynweb/getDynById";
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testGetDynInfoById"));
    for (Map<String, String> map : paramList) {
      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("des3DynId", map.get("des3DynId"));
      param.add("flag", map.get("flag"));
      resultMap = this.postUrl(param, commentUrl);
      if (StringUtils.isBlank(map.get("des3DynId")) || StringUtils.isBlank(map.get("flag"))) {
        assertTrue("400".equals(resultMap.get("status")));
      } else {
        assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
      }
    }

  }

  @Test
  @DisplayName("动态赞测试")
  public void testAjaxAward() {
    String commentUrl = domainscm + "/app/dynweb/awarddyn";
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testAjaxAward"));
    for (Map<String, String> map : paramList) {
      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("resType", map.get("resType"));
      param.add("des3ResId", map.get("des3ResId"));
      param.add("des3DynId", map.get("des3DynId"));
      param.add("des3ParentDynId", map.get("des3ParentDynId"));
      param.add("nextDynType", map.get("nextDynType"));
      param.add("dynType", map.get("dynType"));
      param.add("operatorType", map.get("operatorType"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }
  }



  @Test
  @DisplayName("获取动态统计数测试")
  public void getDynstatistics() {
    String commentUrl = domainscm + "/app/dynweb/getdynstatistics";
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("getDynstatistics"));
    for (Map<String, String> map : paramList) {
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3DynId", map.get("des3DynId"));
      param.add("resId", map.get("resId"));
      param.add("resType", map.get("resType"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }

  }

  @Test
  @DisplayName("动态详情获取测试")
  public void testDynamicDetail() {
    String commentUrl = domainscm + "/app/dynweb/detail";
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testDynamicDetail"));
    for (Map<String, String> map : paramList) {
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("des3DynId", map.get("des3DynId"));
      resultMap = this.postUrl(param, commentUrl);
      if (StringUtils.isBlank(map.get("des3DynId"))) {
        assertTrue("400".equals(resultMap.get("status")));
      } else {
        assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
      }
    }
  }

  @Test
  @DisplayName("动态评论信息获取测试")
  public void testLoadDynReply() {
    String commentUrl = domainscm + "/app/dynweb/dynreply";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testDynamicDetail"));
    for (Map<String, String> map : paramList) {
      param.add("des3DynId", map.get("des3DynId"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }
  }

  @Test
  @DisplayName("分享到动态测试")
  public void testDynamicRealtime() {
    String commentUrl = domainscm + "/app/dynweb/ajaxrealtime";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testDynamicRealtime"));
    for (Map<String, String> map : paramList) {
      param.add("dynType", map.get("dynType"));
      param.add("resType", map.get("resType"));
      param.add("des3PubId", map.get("des3PubId"));
      param.add("operatorType", map.get("operatorType"));
      param.add("databaseType", map.get("databaseType"));
      param.add("des3ResId", map.get("des3ResId"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }
  }

  @Test
  @DisplayName("分享到动态测试")
  public void testAjaxShare() {
    String commentUrl = domainscm + "/app/dynweb/quicksharedyn";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testDynamicRealtime"));
    for (Map<String, String> map : paramList) {
      param.add("dynType", map.get("dynType"));
      param.add("resType", map.get("resType"));
      param.add("des3PubId", map.get("des3PubId"));
      param.add("operatorType", map.get("operatorType"));
      param.add("databaseType", map.get("databaseType"));
      param.add("des3ResId", map.get("des3ResId"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }
  }

  @Test
  public void testSyncData() {
    String commentUrl = domainscm + "/app/dynweb/sync";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();

    resultMap = this.postUrl(param, commentUrl);
    checkResult(resultMap);
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
