package com.smate.web.mobile.controller.data.web;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

class PubDataRecommendControllerTest {
  protected static final Logger logger = LoggerFactory.getLogger(SnsPubOperateControllerTest.class);
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, String> proMap;
  static {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/web/properties/"
          + runEnv + "_test_PubDataRecommendControllerTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
  }
  String domainscm = proMap.get("junit_domainMobile");
  String token = proMap.get("token");
  // String token = "%2Bq%2BZkxTSdouLH3ol%2BmiTjIC8MvV%2B8uog5P7Clc9x9RE%3D";
  Long snsPubId = NumberUtils.toLong(proMap.get("snsPubId"));
  String des3SnsPubId = Des3Utils.encodeToDes3(Objects.toString(snsPubId));
  Long otherPsnPubId = NumberUtils.toLong(proMap.get("otherPsnPubId"));
  Long otherPsnId = NumberUtils.toLong(proMap.get("otherPsnId"));

  String des3OtherPsnPubId = Des3Utils.encodeToDes3(proMap.get("otherPsnPubId"));

  Map<String, Object> resultMap = new HashMap<String, Object>();

  @Test
  @DisplayName("成果推荐条件设置获取测试")
  void testPubRecommendConditional() {
    String commentUrl = domainscm + "/data/pub/recommend/conditional";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

    resultMap = this.postUrl(param, commentUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @ParameterizedTest
  @CsvSource({"'',''", "'202','aa,bb'", "'202,203','aa,bb'", "'202,203','aa'", "'202,203',''", "'','aa,bb'",})
  @DisplayName("论文推荐查询接口测试")
  void testSearchPubList(String defultArea, String defultKeyJson) {
    String commentUrl = domainscm + "/data/pubrecommend/pubList";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("defultArea", defultArea);
    param.add("defultKeyJson", defultKeyJson);

    resultMap = this.postUrl(param, commentUrl);
    checkResult(resultMap);
  }

  @ParameterizedTest
  @CsvSource({"''", "'202'", "'202,203'"})
  @DisplayName("成果推荐添加科技领域测试")
  void testAddScienArea(String addPsnAreaCode) {
    String commentUrl = domainscm + "/data/pubrecommend/addScienAreas";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("addPsnAreaCode", addPsnAreaCode);

    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(addPsnAreaCode)) {
      checkErrorResult(resultMap, "psnId is null or addPsnAreaCodes is null");
    } else {
      checkResult(resultMap);
    }

  }

  @ParameterizedTest
  @CsvSource({"''", "'202'", "'202,203'"})
  @DisplayName("成果推荐删除科技领域测试")
  void testDeleteScienArea(String deletePsnAreaCode) {
    String commentUrl = domainscm + "/data/pub/recommend/deleteScienAreas";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("deletePsnAreaCode", deletePsnAreaCode);

    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(deletePsnAreaCode)) {
      checkErrorResult(resultMap, "psnId is null or deletePsnAreaCodes is null");
    } else {
      checkResult(resultMap);
    }
  }

  @ParameterizedTest
  @CsvSource({"''", "'ab'", "'aa,bb'"})
  @DisplayName("成果推荐增加关键词测试")
  void testAddKeyWord(String addPsnKeyWord) {
    String commentUrl = domainscm + "/data/pub/recommend/addKeyWords";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("addPsnKeyWord", addPsnKeyWord);

    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(addPsnKeyWord)) {
      checkErrorResult(resultMap, "psnId is null or addPsnKeyWords is null");
    } else {
      checkResult(resultMap);
    }
  }

  @ParameterizedTest
  @CsvSource({"''", "'ab'", "'aa,bb'"})
  @DisplayName("成果推荐删除关键词测试")
  void testDeleteKeyWord(String deletePsnKeyWord) {
    String commentUrl = domainscm + "/data/pub/recommend/deleteKeyWords";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("deletePsnKeyWord", deletePsnKeyWord);

    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(deletePsnKeyWord)) {
      checkErrorResult(resultMap, "psnId is null or deletePsnKeyWords is null");
    } else {
      checkResult(resultMap);
    }
  }

  @ParameterizedTest
  @CsvSource({"'','l1kRJGIlh%2Fw%3D'", "'2','l1kRJGIlh%2Fw%3D'"})
  @DisplayName("成果推荐动态首页论文推荐测试")
  void testGetDynPubRecommend(String pageNo, String des3PubId) {
    String commentUrl = domainscm + "/data/pub/getdynpubrecommend";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("pageNo", pageNo);
    param.add("des3PubId", des3PubId);
    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(pageNo)) {
      checkErrorResult(resultMap, "获取不到psnId或者pageNo");
    } else {
      checkResult(resultMap);
    }
  }

  @ParameterizedTest
  @CsvSource({"''", "'l1kRJGIlh%2Fw%3D'"})
  @DisplayName("成果推荐不感兴趣测试")
  void testNotViewPubRecommend(String des3PubId) {
    String commentUrl = domainscm + "/data/pub/pubuninterested";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("des3PubId", des3PubId);
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
