package com.smate.web.fund.action.wechat;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;

/**
 * @Description APP基金显示相关数据接口测试用例
 * @author YWL
 * @Date 2019/1/31
 */
class APPFundActionTest {
  protected static Map<String, String> proMap;
  protected static String domainMobile;
  protected static Map<String, String> map = new HashMap<>();
  protected static RestTemplate restTemplate = new RestTemplate();
  protected static HttpHeaders headers;
  protected static MultiValueMap<String, String> requestBody;

  // 加载环境参数的配置文件
  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils
        .loadProperties("src/test/java/com/smate/web/fund/action/wechat/properties/" + runEnv + "_test.properties");
    domainMobile = proMap.get("junit_domainMobile");
    initHttpHeaders();
  }

  // 初始化请求头
  public static void initHttpHeaders() {
    headers = new HttpHeaders();
    requestBody = new LinkedMultiValueMap<String, String>();
    requestBody.add("appflag", proMap.get("psnId"));
    headers.add("token", proMap.get("token"));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
  }

  // 发送请求并获取返回数据
  public Map<String, String> getReslutMap(MultiValueMap<String, String> requestBody, String uri) {
    HttpEntity<MultiValueMap> HttpEntity = new HttpEntity<MultiValueMap>(requestBody, headers);
    return restTemplate.postForObject(domainMobile + "/app/prjweb" + uri, HttpEntity, Map.class);
  }


  @Test
  @DisplayName("保存基金推荐条件科技领域空参校验")
  void testSaveFundConditionsScienceAreaSavePar() {
    String reqUri = "/fundconditions/ajaxsavefundarea";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("areaCodes", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }

  @Test
  @DisplayName("保存基金推荐条件科技领域接口校验")
  void testSaveFundConditionsScienceAreaSave() {
    String reqUri = "/fundconditions/ajaxsavefundarea";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("areaCodes", "201,202,203");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("删除基金推荐条件科技领域空参校验")
  void testAjaxdeletefundareaPar() {
    String reqUri = "/fundconditions/ajaxdeletefundarea";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("areaCodes", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }

  @Test
  @DisplayName("删除基金推荐条件科技领域接口校验")
  void testAjaxdeletefundarea() {
    String reqUri = "/fundconditions/ajaxdeletefundarea";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("areaCodes", "201,202,203");
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("获取我的基金列表接口校验")
  void testShowMyFund() {
    String reqUri = "/collectedfund/ajaxlist";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("保存基金推荐条件接口校验")
  void testsaveFundRecommendConditions() {
    String reqUri = "/fundconditions/ajaxsave";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("删除基金推荐条件关注地区空参校验")
  void testAjaxdeletefundregionPar() {
    String reqUri = "/fundconditions/ajaxdeletefundregion";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("regionCodes", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }

  @Test
  @DisplayName("删除基金推荐条件关注地区接口校验")
  void testAjaxdeletefundregion() {
    String reqUri = "/fundconditions/ajaxdeletefundregion";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("regionCodes", "440000");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("app保存基金推荐条件关注地区接口空参校验")
  void testSaveFundConditionsRegionSavePar() {
    String reqUri = "/fundconditions/ajaxsavefundregion";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("regionCodes", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }


  @Test
  @DisplayName("条件显示接口校验")
  void testShowRecommendFundConditions() {
    String reqUri = "/fundconditions/ajaxshow";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("默认推荐基金列表接口校验")
  void testFirstRecommendFund() {
    String reqUri = "/fundconditions/ajaxshow";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("推荐基金列表接口校验")
  void testSearchRecommendFund() {
    String reqUri = "/fund/ajaxrecommend";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("显示基金科技领域条件接口校验")
  void testShowFundScienceAreaBox() {
    String reqUri = "/fundscience/ajaxbox";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("显示基金详情页面空参校验")
  void testAwardAgencyOptPar() {
    String reqUri = "/mobile/ajaxAward";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", "");
    requestBody.add("encryptedFundId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }

  // 返回jsp页面需要做html解析校验，暂时不做测试用例
  /*
   * @Test
   * 
   * @DisplayName("显示基金详情页面校验") void testShowFundDetails() { String reqUri = "/funddetails/show";
   * String[] fundIds = {proMap.get("fundId"), proMap.get("fundIdIsNotExist")}; for (int i = 0; i <
   * fundIds.length; i++) { String encryptedFundId = Des3Utils.encodeToDes3(fundIds[i]); requestBody =
   * new LinkedMultiValueMap<>(); requestBody.add("fundId", fundIds[i]);
   * requestBody.add("encryptedFundId", encryptedFundId); HttpEntity<MultiValueMap> HttpEntity = new
   * HttpEntity<MultiValueMap>(requestBody, headers); String result =
   * restTemplate.postForObject(domainMobile + "/app/prjweb" + reqUri, HttpEntity, String.class); //
   * 返回jsp页面需要做html解析校验，暂时不做测试用例 } }
   */

  @Test
  @DisplayName("显示基金详情页面空参校验")
  void testQueryFundDetailPar() {
    String reqUri = "/fund/query/detail";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", "");
    requestBody.add("des3FundId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }


  @Test
  @DisplayName("显示基金详情页面校验")
  void testQueryFundDetail() {
    String reqUri = "/fund/query/detail";
    String[] fundIds = {proMap.get("fundId"), proMap.get("fundIdIsNotExist")};
    for (int i = 0; i < fundIds.length; i++) {
      String des3FundId = Des3Utils.encodeToDes3(fundIds[i]);
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("fundId", fundIds[i]);
      requestBody.add("des3FundId", des3FundId);
      map = getReslutMap(requestBody, reqUri);
      String result = JacksonUtils.jsonObjectSerializerNoNull(map.get("result"));
      String constFundCategoryInfo = JacksonUtils.jsonToMap(result).get("constFundCategoryInfo").toString();
      Map<String, String> fundInfomap = JunitUtils.strToMap(constFundCategoryInfo);
      if (i == 0) {
        assertTrue("success".equals(map.get("status")) && StringUtils.isNoneBlank(fundInfomap.get("fundAgencyName")));
      } else {
        assertTrue("success".equals(map.get("status")) && StringUtils.isBlank(fundInfomap.get("fundAgencyName")));
      }
    }
  }

  @Test
  @DisplayName("基金赞/取消赞操作空参校验")
  void testAwardRecommendFundPar() {
    String reqUri = "/fund/ajaxaward";
    String[] parameters = {proMap.get("fundId"), Des3Utils.encodeToDes3(proMap.get("fundId")), "0"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameters);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("fundId", checkPar[0]);
      requestBody.add("encryptedFundId", checkPar[1]);
      requestBody.add("awardOperation", checkPar[2]);
      if ((StringUtils.isNoneBlank(checkPar[0]) || StringUtils.isNoneBlank(checkPar[1]))
          && StringUtils.isNoneBlank(checkPar[2]))
        continue;
      map = getReslutMap(requestBody, reqUri);
      assertTrue("400".equals(map.get("status")));
    }
  }


  @ParameterizedTest
  @DisplayName("基金赞/取消赞操作接口校验")
  @ValueSource(strings = {"0", "1"})
  void testAwardRecommendFund(String awardOperation) {
    String reqUri = "/fund/ajaxaward";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", proMap.get("fundId"));
    requestBody.add("encryptedFundId", Des3Utils.encodeToDes3(proMap.get("fundId")));
    requestBody.add("awardOperation", awardOperation);
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    Map<String, String> commentMap = JunitUtils.strToMap(commentlist);
    if (awardOperation.equals("0")) {
      assertTrue("200".equals(map.get("status")) && "true".equals(commentMap.get("hasAward")));
    } else {
      assertTrue("200".equals(map.get("status")) && "false".equals(commentMap.get("hasAward")));
    }
  }


  @Test
  @DisplayName("基金收藏/取消收藏操作空参校验")
  void testRecommendFundCollectionPar() {
    String reqUri = "/fund/ajaxcollect";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", "");
    requestBody.add("encryptedFundId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }

  @ParameterizedTest
  @DisplayName("基金收藏/取消收藏操作接口校验")
  @ValueSource(strings = {"0", "1"})
  void testRecommendFundCollection(String collectOperate) {
    String reqUri = "/fund/ajaxcollect";
    String[] fundIds = {proMap.get("fundId"), proMap.get("fundIdIsNotExist")};
    for (int i = 0; i < fundIds.length; i++) {
      String encryptedFundId = Des3Utils.encodeToDes3(fundIds[i]);
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("fundId", fundIds[i]);
      requestBody.add("collectOperate", collectOperate);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue("200".equals(map.get("status")) && "success".equals(commentlist));
      } else {
        assertTrue("500".equals(map.get("status")) && "error".equals(commentlist));
      }
    }
  }

  @Test
  @DisplayName("更新分享统计数空参校验")
  void testfundShareCountPar() {
    String reqUri = "/fund/ajaxsharecount";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", "");
    requestBody.add("encryptedFundId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }

  @Test
  @DisplayName("更新分享统计数接口校验")
  void testfundShareCount() {
    String reqUri = "/fund/ajaxsharecount";
    String encryptedFundId = Des3Utils.encodeToDes3(proMap.get("fundId"));
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", proMap.get("fundId"));
    requestBody.add("encryptedFundId", encryptedFundId);
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    Map<String, String> commentMap = JunitUtils.strToMap(commentlist);
    assertTrue("200".equals(map.get("status")) && StringUtils.isNoneBlank(commentMap.get("shareCount")));
  }

  @Test
  @DisplayName("初始化基金操作空参校验")
  void testInitFundOperationPar() {
    String reqUri = "/fundop/ajaxinit";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", "");
    requestBody.add("encryptedFundId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }

  @Test
  @DisplayName("初始化基金操作接口校验")
  void testInitFundOperation() {
    String reqUri = "/fundop/ajaxinit";
    String encryptedFundId = Des3Utils.encodeToDes3(proMap.get("fundId"));
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fundId", proMap.get("fundId"));
    requestBody.add("encryptedFundId", encryptedFundId);
    map = getReslutMap(requestBody, reqUri);
    String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
    String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
    Map<String, String> commentMap = JunitUtils.strToMap(commentlist);
    assertTrue("200".equals(map.get("status")) && "success".equals(commentMap.get("result"))
        && StringUtils.isNoneBlank(commentMap.get("shareCount"))
        && StringUtils.isNoneBlank(commentMap.get("awardCount"))
        && StringUtils.isNoneBlank(commentMap.get("hasCollect"))
        && StringUtils.isNoneBlank(commentMap.get("awardStatus")));
  }

}
