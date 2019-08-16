package com.smate.web.mobile.controller.data.list.pub;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
 * @Description 成果列表相关接口测试用例
 * @author YWL
 * @Date 2019/1/29
 */
class PubDataListControllerTest {
  protected static Map<String, String> proMap;
  protected static String domainMobile;
  protected static Map<String, String> map = new HashMap<>();
  protected static RestTemplate restTemplate = new RestTemplate();
  protected static HttpHeaders headers;
  protected static MultiValueMap<String, String> requestBody;
  protected static String reqUri;

  // 加载环境参数的配置文件
  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils.loadProperties(
        "src/test/java/com/smate/web/mobile/controller/data/list/pub/properties/" + runEnv + "_test.properties");
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
    return restTemplate.postForObject(domainMobile + "/data/pub/querylist" + uri, HttpEntity, Map.class);
  }


  @Test
  @DisplayName("成果列表数据接口的空参数校验")
  void testCheckQueryMyPubListPra() {
    String reqUri = "/psn";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("nextId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }


  @Test
  @DisplayName("成果列表数据")
  void testQueryMyPubList() {
    String reqUri = "/psn";
    String[] des3PubIds = {proMap.get("des3SearchPsnId"), proMap.get("des3SearchPsnIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3SearchPsnId", des3PubIds[i]);
      requestBody.add("nextId", "0");
      map = getReslutMap(requestBody, reqUri);
      String jsonStr = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(jsonStr).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200"));
      } else {
        assertTrue(map.get("status").equals("200") && commentlist.contains("totalCount=0"));
      }
    }
  }


  @Test
  @DisplayName("成果收藏列表")
  void testQueryCollectPubList() {
    String reqUri = "/collect";
    String[] des3PubIds = {proMap.get("des3SearchPsnId"), proMap.get("des3SearchPsnIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3SearchPsnId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      if (i == 0) {
        assertTrue(map.get("status").equals("200"));
      } else {
        assertTrue(map.get("status").equals("200") && results.contains("\"commentlist\":[]"));
      }
    }
  }

  @Test
  @DisplayName("获取个人成果认领列表接口校验")
  void testGetPubconfirmList() {
    String reqUri = "/confirm";
    String[] des3PubIds = {proMap.get("des3SearchPsnId"), proMap.get("des3SearchPsnIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3SearchPsnId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      if (i == 0) {
        assertTrue(map.get("status").equals("200"));
      } else {
        assertTrue(map.get("status").equals("200") && results.contains("\"commentlist\":[]"));
      }
    }
  }

  @Test
  @DisplayName("获取我的代表成果列表数据")
  void testGetRepresentPublist() {
    String reqUri = "/represent";
    String[] des3PubIds = {proMap.get("des3SearchPsnId"), proMap.get("des3SearchPsnIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3SearchPsnId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      if (i == 0) {
        assertTrue(map.get("status").equals("200"));
      } else {
        assertTrue(map.get("status").equals("200") && results.contains("\"commentlist\":[]"));
      }
    }
  }

  @Test
  @DisplayName("查询单个成果信息接口的空参数校验")
  void testQuerySnsPubListByPubIdPra() {
    String reqUri = "/single";
    String[] parameter = {proMap.get("des3SNSPubId").toString(), "SNS"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", checkPar[0]);
      requestBody.add("pubDB", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }


  @ParameterizedTest
  @DisplayName("查询单个成果信息接口校验")
  @ValueSource(strings = {"SNS", "PDWH"})
  void testQuerySnsPubListByPubId(String pubDB) {
    String reqUri = "/single";
    String searchPubId = "des3" + pubDB + "PubId";
    String[] des3PubIds = {proMap.get(searchPubId), proMap.get(searchPubId + "IsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("pubDB", pubDB);
      requestBody.add("des3SearchPubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200") && StringUtils.isNoneBlank(StringUtils.strip(commentlist, "[]")));
      } else {
        assertTrue(map.get("status").equals("200") && StringUtils.isBlank(StringUtils.strip(commentlist, "[]")));
      }
    }
  }



  @ParameterizedTest
  @DisplayName("从solr中检索基准库成果接口校验")
  @ValueSource(strings = {"", "DNA"})
  void testsearchPdwhPub(String searchString) {
    String reqUri = "/ajaxpdwhpub";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("searchString", searchString);
    for (int i = 0; i < 2; i++) {
      if (i == 1) {
        requestBody.add("searchArea", proMap.get("des3AreaId"));
        requestBody.add("des3AreaId", proMap.get("searchArea"));
      }
    }
    map = getReslutMap(requestBody, reqUri);
    assertTrue(map.get("status").equals("200"));
  }

}
