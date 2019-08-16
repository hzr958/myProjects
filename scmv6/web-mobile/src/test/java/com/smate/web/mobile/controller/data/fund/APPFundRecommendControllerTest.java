package com.smate.web.mobile.controller.data.fund;

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

class APPFundRecommendControllerTest {
  protected static final Logger logger = LoggerFactory.getLogger(APPFundRecommendControllerTest.class);
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, String> proMap;
  static {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/fund/properties/"
          + runEnv + "_test_APPFundRecommendControllerTest.properties");
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
  @DisplayName("获取基金推荐关注资助机构测试")
  void testGetFundRecommendPsnAgency() {
    String commentUrl = domainscm + "/data/prj/getfundrecommendpsnagency";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    resultMap = this.postUrl(param, commentUrl);
    checkResult(resultMap);
  }

  @ParameterizedTest
  @CsvSource({"''", "'1000'", "'1000,10001'"})
  @DisplayName("基金推荐保存关注的资助机构测试")
  void testSaveFundRecommendPsnAgency(String saveAgencyIds) {
    String commentUrl = domainscm + "/data/prj/savefundrecommendpsnagency";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("saveAgencyIds", saveAgencyIds);
    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(saveAgencyIds)) {
      checkErrorResult(resultMap, "至少保存一个资助机构");
    } else {
      checkResult(resultMap);
    }
  }

  @Test
  @DisplayName("基金推荐获取全部的资助机构测试")
  void testEditFundRecommendPsnAgency() {
    String commentUrl = domainscm + "/data/prj/editfundrecommendpsnagency";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    resultMap = this.postUrl(param, commentUrl);
    checkResult(resultMap);
  }

  @ParameterizedTest
  @CsvSource({"'','l1kRJGIlh%2Fw%3D'", "'2','l1kRJGIlh%2Fw%3D'"})
  @DisplayName("动态首页基金推荐测试")
  void testGetDynPubRecommend(String pageNo, String des3FundIds) {
    String commentUrl = domainscm + "/data/prj/getfundrecommendshowindyn";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("pageNo", pageNo);
    param.add("des3FundIds", des3FundIds);
    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(pageNo)) {
      checkErrorResult(resultMap, "获取不到psnId或pageNo");
    } else {
      checkResult(resultMap);
    }
  }

  @ParameterizedTest
  @CsvSource({"''", "'l1kRJGIlh%2Fw%3D'"})
  @DisplayName("基金推荐不感兴趣测试")
  void testFundUnInterested(String des3FundId) {
    String commentUrl = domainscm + "/data/prj/funduninterested";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.add("des3FundId", des3FundId);
    resultMap = this.postUrl(param, commentUrl);
    if (StringUtils.isBlank(des3FundId)) {
      checkErrorResult(resultMap, "获取不到psnId或者des3FundId为空");
    } else {
      checkResult(resultMap);
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
