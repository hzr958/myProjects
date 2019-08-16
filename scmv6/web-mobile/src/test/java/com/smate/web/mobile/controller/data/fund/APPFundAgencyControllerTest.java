package com.smate.web.mobile.controller.data.fund;

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
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;

/**
 * @Description 资助机构APP接口测试用例
 * @author YWL
 * @Date 2019/1/29
 */
class APPFundAgencyControllerTest {
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
    proMap = JunitPropertiesUtils.loadProperties(
        "src/test/java/com/smate/web/mobile/controller/data/fund/properties/" + runEnv + "_test.properties");
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
    return restTemplate.postForObject(domainMobile + "/data/prj" + uri, HttpEntity, Map.class);
  }

  @ParameterizedTest
  @DisplayName("资助机构列表接口校验")
  @ValueSource(strings = {"", "156", "330000,156", "340000,130000,230000"})
  void testGetFundagencylist(String regionAgency) {
    String reqUri = "/fundagencylist";
    String[] searchKeys = {"", "机构"};
    for (String searchKey : searchKeys) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("searchKey", searchKey);
      requestBody.add("regionAgency", regionAgency);
      requestBody.add("page.pageNo", "1");
      map = getReslutMap(requestBody, reqUri);
      assertTrue("200".equals(map.get("status")));
    }
  }


  @Test
  @DisplayName("地区条件筛选数据校验")
  void testGetAgencycondition() {
    String reqUri = "/agencycondition";
    requestBody = new LinkedMultiValueMap<>();
    String[] Parameters = {"机构", "330000,156"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(Parameters);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("searchKey", checkPar[0]);
      requestBody.add("regionAgency", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("200".equals(map.get("status")));
    }
  }

  @Test
  @DisplayName("资助机构详情空参校验")
  void testGetagencydetailPar() {
    String reqUri = "/agencydetail";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3FundAgencyId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")));
  }

  @Test
  @DisplayName("资助机构详情")
  void testGetagencydetail() {
    String reqUri = "/agencydetail";
    requestBody = new LinkedMultiValueMap<>();
    String[] searchKeys = {"", "机构"};
    for (String searchKey : searchKeys) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", proMap.get("des3FundAgencyId"));
      requestBody.add("searchKey", searchKey);
      requestBody.add("page.pageNo", "1");
      map = getReslutMap(requestBody, reqUri);
      assertTrue("200".equals(map.get("status")));
    }
  }


  @Test
  @DisplayName("资助机构赞/取消赞操作空参校验")
  void testAwardAgencyOptPar() {
    String reqUri = "/mobile/ajaxAward";
    String[] parameter = {proMap.get("des3FundAgencyId"), "0"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", checkPar[0]);
      requestBody.add("optType", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")));
    }
  }

  @ParameterizedTest
  @DisplayName("资助机构赞/取消赞操作校验")
  @ValueSource(strings = {"0", "1"})
  void testAwardAgencyOpt(String optType) {
    String reqUri = "/mobile/ajaxAward";
    String[] des3FundAgencyIds = {proMap.get("des3FundAgencyId"), proMap.get("des3FundAgencyIdIsNotExist")};
    for (int i = 0; i < des3FundAgencyIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", des3FundAgencyIds[i]);
      requestBody.add("optType", optType);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue("200".equals(map.get("status")));
      } else {
        assertTrue("200".equals(map.get("status")) && "agencyId is null or not exists".equals(map.get("msg")));
      }
    }
  }

  @Test
  @DisplayName("资助机构列表分享操作空参校验")
  void testShareAgencyOptPar() {
    String reqUri = "/agency/updatestat";
    String[] parameter = {proMap.get("des3FundAgencyId"), "1", "分享一下"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", checkPar[0]);
      requestBody.add("shareToPlatform", checkPar[1]);
      requestBody.add("comments", checkPar[2]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("200".equals(map.get("status")));
    }
  }

  @ParameterizedTest
  @DisplayName("资助机构列表分享操作校验")
  // App目前没有做分享给个人和群组，只做了分享动态和站外的
  // 1：动态 2：联系人 3：群组 4：微信 5：新浪微博 6：Facebook 7:Linkedin
  @ValueSource(strings = {"1", "4", "5", "6", "7"})
  void testShareAgencyOpt(String shareToPlatform) {
    String reqUri = "/agency/updatestat";
    String[] des3FundAgencyIds = {proMap.get("des3FundAgencyId"), proMap.get("des3FundAgencyIdIsNotExist")};
    String[] comments = {"", "分享一下这个资助机构"};
    for (String comment : comments) {
      for (int i = 0; i < des3FundAgencyIds.length; i++) {
        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("des3FundAgencyId", des3FundAgencyIds[i]);
        requestBody.add("shareToPlatform", shareToPlatform);
        requestBody.add("comments", comment);
        map = getReslutMap(requestBody, reqUri);
        String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
        String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
        if (i == 0) {
          assertTrue("200".equals(map.get("status")));
        } else {
          assertTrue("200".equals(map.get("status")) && "agency is not exists".equals(map.get("msg")));
        }
      }
    }
  }

  @Test
  @DisplayName("资助机构列表 关注操作空参校验")
  void testInterestAgencyOptPar() {
    String reqUri = "/mobile/interest";
    String[] parameter = {proMap.get("des3FundAgencyId"), "1"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", checkPar[0]);
      requestBody.add("optType", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")));
    }
  }

  @ParameterizedTest
  @DisplayName("资助机构列表关注操作校验")
  @ValueSource(strings = {"1", "0"})
  void testInterestAgencyOpt(String optType) {
    String reqUri = "/mobile/interest";
    String[] des3FundAgencyIds = {proMap.get("des3FundAgencyId"), proMap.get("des3FundAgencyIdIsNotExist")};
    for (int i = 0; i < des3FundAgencyIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FundAgencyId", des3FundAgencyIds[i]);
      requestBody.add("optType", optType);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue("200".equals(map.get("status")));
      } else {
        assertTrue("200".equals(map.get("status")) && "agencyId is null or not exists".equals(map.get("msg")));
      }
    }
  }

}
