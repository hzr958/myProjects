package com.smate.web.mobile.controller.data.web;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.test.utils.JunitPropertiesUtils;

class SnsPubOperateControllerTest {
  protected static final Logger logger = LoggerFactory.getLogger(SnsPubOperateControllerTest.class);
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, String> proMap;
  static {
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils
          .loadProperties("src/test/java/com/smate/test/base/properties/" + runEnv + "_test_TestExample.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
  }
  String domainscm = proMap.get("junit_domainMobile");
  String token = proMap.get("token");
  // String token = "LiXqZ%2FMZ%2FEAuEuq4b8onMoC8MvV%2B8uog5P7Clc9x9RE%3D";
  Long snsPubId = NumberUtils.toLong(proMap.get("snsPubId"));
  String des3SnsPubId = Des3Utils.encodeToDes3(Objects.toString(snsPubId));
  Long otherPsnPubId = NumberUtils.toLong(proMap.get("otherPsnPubId"));
  Long otherPsnId = NumberUtils.toLong(proMap.get("otherPsnId"));

  String des3OtherPsnPubId = Des3Utils.encodeToDes3(proMap.get("otherPsnPubId"));

  Map<String, Object> resultMap = new HashMap<String, Object>();

  @Test
  @DisplayName("测试个人库成果点赞")
  void testLikeOpt() {
    String likeUrl = domainscm + "/data/pub/optsns/ajaxlike";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);

    param.set("operate", "1");// 赞
    resultMap = this.postUrl(param, likeUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
    assertTrue(Objects.toString(resultMap.get("results")).contains("action=0"));

    param.set("operate", "0");// 取消赞
    resultMap = this.postUrl(param, likeUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
    assertTrue(Objects.toString(resultMap.get("results")).contains("action=1"));

  }

  @Test
  @DisplayName("测试个人库成果评论")
  void testCommentOpt() {
    String commentUrl = domainscm + "/data/pub/optsns/ajaxcomment";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);
    param.set("content", "单元测试评论了这条成果");

    resultMap = this.postUrl(param, commentUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @Test
  @DisplayName("测试个人库成果分享更新分享统计数")
  void testShareOpt() {
    String shareUrl = domainscm + "/data/pub/optsns/ajaxshare";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);
    param.set("platform", "1");

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));

  }

  @Test
  @DisplayName("测试成果的收藏和取消收藏")
  void testDealCollectedPub() {
    String collectUrl = domainscm + "/data/pub/optsns/ajaxcollect";
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);
    param.set("pubDb", "SNS");
    param.set("collectOperate", "0");

    resultMap = this.postUrl(param, collectUrl);
    assertTrue("500".equals(Objects.toString(resultMap.get("status"))));

    param.set("des3PubId", des3OtherPsnPubId);
    resultMap = this.postUrl(param, collectUrl);
    if ("500".equals(Objects.toString(resultMap.get("status")))) {// 错误可能是已经收藏过了
      param.set("collectOperate", "1");
      resultMap = this.postUrl(param, collectUrl);
      assertTrue("200".equals(Objects.toString(resultMap.get("status"))));

      param.set("collectOperate", "0");
      resultMap = this.postUrl(param, collectUrl);
      assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
    } else {
      assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
    }

    param.set("collectOperate", "1");
    resultMap = this.postUrl(param, collectUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));

  }

  @Test
  @DisplayName("获取成果评论数")
  void testAjaxGetCommentNumber() {
    String shareUrl = domainscm + "/data/pub/optsns/ajaxcommentnumber";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @Test
  @DisplayName("查看成果增加阅读数")
  void testViewOpt() {
    String shareUrl = domainscm + "/data/pub/optsns/ajaxview";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);
    param.set("des3ReadPsnId", Des3Utils.encodeToDes3(Objects.toString(otherPsnId)));

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @Test
  @DisplayName("查看成果评论列表")
  void testPubCommentList() {
    String shareUrl = domainscm + "/data/pub/optsns/ajaxcommentlist";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);
    param.set("des3ReadPsnId", Des3Utils.encodeToDes3(Objects.toString(otherPsnId)));

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @Test
  void testAgreePubFulltextRequest() {
    fail("Not yet implemented");
  }

  @Test
  void testAddPubFulltextRequest() {
    fail("Not yet implemented");
  }

  @Test
  void testConfirmPub() {
    fail("Not yet implemented");
  }

  @Test
  void testRejectPubFulltext() {
    fail("Not yet implemented");
  }

  @Test
  @DisplayName("查看成果的操作统计数")
  void testFindPdwhPubStatistics() {
    String shareUrl = domainscm + "/data/pub/optsns/ajaxstatistics";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
  }

  @Test
  @DisplayName("人员是否已赞过该基准库成果 ")
  void testGetPubAwardAndCollectStatus() {
    String shareUrl = domainscm + "/data/pub/optsns/status";
    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
    param.set("des3PubId", des3SnsPubId);

    resultMap = this.postUrl(param, shareUrl);
    assertTrue("200".equals(Objects.toString(resultMap.get("status"))));
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

}
