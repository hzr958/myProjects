package com.smate.web.dyn.action.dynamic;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.BeforeAll;
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

class APPDynamicOptActionTest {
  protected static final Logger logger = LoggerFactory.getLogger(APPDynamicOptActionTest.class);
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, String> proMap;

  static String domainscm;
  static String token;
  static Long snsPubId;
  static String des3SnsPubId;
  static Long otherPsnPubId;
  static Long otherPsnId;
  static String des3OtherPsnPubId;
  Map<String, Object> resultMap = new HashMap<String, Object>();

  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/dyn/action/dynamic/properties/" + runEnv
          + "_test_APPDynamicOptActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    domainscm = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    snsPubId = NumberUtils.toLong(proMap.get("snsPubId"));
    des3SnsPubId = Des3Utils.encodeToDes3(Objects.toString(snsPubId));
    otherPsnPubId = NumberUtils.toLong(proMap.get("otherPsnPubId"));
    otherPsnId = NumberUtils.toLong(proMap.get("otherPsnId"));
    des3OtherPsnPubId = Des3Utils.encodeToDes3(proMap.get("otherPsnPubId"));
  }

  @Test
  @DisplayName("删除动态测试")
  public void testDeleteDyn() {
    String commentUrl = domainscm + "/app/dynweb/ajaxdeletedyn";
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testDeleteDyn"));
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
  @DisplayName("添加动态评论测试")
  public void testReplyDyn() {
    List<Map<String, String>> paramList = JacksonUtils.jsonToList(proMap.get("testReplyDyn"));
    for (Map<String, String> map : paramList) {
      String commentUrl = domainscm + "/app/dynweb/ajaxreplydyn";
      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
      param.add("resType", map.get("resType"));
      param.add("des3ResId", map.get("des3ResId"));
      param.add("des3DynId", map.get("des3DynId"));
      param.add("des3ParentDynId", map.get("des3ParentDynId"));
      param.add("nextDynType", map.get("nextDynType"));
      param.add("dynType", map.get("dynType"));
      param.add("operatorType", map.get("operatorType"));
      param.add("replyContent", map.get("replyContent"));
      resultMap = this.postUrl(param, commentUrl);
      assertTrue("200".equals(resultMap.get("status")) || "304".equals(resultMap.get("status")));
    }
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
