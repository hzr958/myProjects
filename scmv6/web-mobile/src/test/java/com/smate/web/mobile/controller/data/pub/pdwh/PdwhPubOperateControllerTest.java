package com.smate.web.mobile.controller.data.pub.pdwh;

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
 * @Description 基准库操作接口测试用例
 * @author YWL
 * @Date 2019/1/29
 */
class PdwhPubOperateControllerTest {

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
        "src/test/java/com/smate/web/mobile/controller/data/pub/pdwh/properties/" + runEnv + "_test.properties");
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
    return restTemplate.postForObject(domainMobile + "/data/pub/optpdwh" + uri, HttpEntity, Map.class);
  }


  @Test
  @DisplayName("基准库成果赞/取消赞操作的空参数校验")
  void testCheckPdwhLikeOptPra() {
    String reqUri = "/ajaxlike";
    String[] parameter = {proMap.get("des3PdwhPubId").toString(), "0"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", checkPar[0]);
      requestBody.add("operate", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }

  @ParameterizedTest
  @DisplayName("基准库成果(存在和不存在)赞/取消赞操作")
  @ValueSource(strings = {"1", "0"})
  void testPdwhLikeOpt(String operate) {
    String reqUri = "/ajaxlike";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", des3PubIds[i]);
      requestBody.add("operate", operate);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        if (operate.equals("1")) {
          assertTrue(
              map.get("status").equals("200") && commentlist.contains("success") && commentlist.contains("action=0"));
        } else {
          assertTrue(
              map.get("status").equals("200") && commentlist.contains("success") && commentlist.contains("action=1"));
        }
      } else {
        assertTrue(map.get("status").equals("500") && commentlist.contains("error"));
      }
    }
  }


  @Test
  @DisplayName("基准库成果评论操作的空参数校验")
  void testCheckPdwhCommentOptPra() {
    String reqUri = "/ajaxcomment";
    String[] parameter = {proMap.get("des3PdwhPubId").toString(), "测试评论"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", checkPar[0]);
      requestBody.add("content", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }


  @Test
  @DisplayName("基准库成果评论操作")
  void testPdwhCommentOpt() {
    String reqUri = "/ajaxcomment";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", des3PubIds[i]);
      requestBody.add("content", "测试评论");
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200") && commentlist.contains("success"));
      } else {
        assertTrue(map.get("status").equals("500") && commentlist.contains("error"));
      }
    }
  }


  @ParameterizedTest
  @DisplayName("基准库成果分享回调的空参数校验")
  @ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
  void testCheckPdwhShareOptPra(String platform) {
    String reqUri = "/ajaxshare";
    String[] parameter = {proMap.get("des3PdwhPubId").toString(), "分享评论", platform};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", checkPar[0]);
      requestBody.add("comment", checkPar[1]);
      requestBody.add("platform", checkPar[2]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }

  @ParameterizedTest
  @DisplayName("基准库成果分享回调")
  @ValueSource(strings = {"1", "2", "3", "4", "5", "6", "7"})
  void testPdwhShareOpt(String platform) {
    String reqUri = "/ajaxshare";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", des3PubIds[i]);
      requestBody.add("comment", "分享评论");
      requestBody.add("platform", platform);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200") && commentlist.contains("success"));
      } else {
        assertTrue(map.get("status").equals("500") && commentlist.contains("error"));
      }
    }
  }

  @Test
  @DisplayName("基准库成果查看,增加阅读数的空参数校验")
  void testCheckPdwhViewOptPra() {
    String reqUri = "/ajaxview";
    String[] parameter = {proMap.get("des3PdwhPubId").toString(), proMap.get("des3ReadPsnId").toString()};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", checkPar[0]);
      requestBody.add("des3ReadPsnId", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("error".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }


  @Test
  @DisplayName("基准库成果查看,增加阅读数")
  void testPdwhViewOpt() {
    String reqUri = "/ajaxview";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", des3PubIds[i]);
      requestBody.add("des3ReadPsnId", proMap.get("des3PdwhPubIdIsNotExist"));
      map = getReslutMap(requestBody, reqUri);
      if (i == 0) {
        assertTrue(map.get("status").equals("success"));
      } else {
        assertTrue(map.get("status").equals("error"));
      }
    }
  }


  @Test
  @DisplayName("收藏或删除个人收藏的成果的空参数校验")
  void testCheckDealCollectedPubPra() {
    String reqUri = "/ajaxcollect";
    String[] parameter = {proMap.get("des3PdwhPubId").toString(), "PDWH", "1"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", checkPar[0]);
      requestBody.add("pubDb", checkPar[1]);
      requestBody.add("collectOperate", checkPar[2]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }

  @ParameterizedTest
  @DisplayName("收藏或删除个人收藏的成果")
  @ValueSource(strings = {"0", "1"})
  void testDealCollectedPub(String collectOperate) {
    String reqUri = "/ajaxcollect";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      requestBody.add("collectOperate", collectOperate);
      requestBody.add("pubDb", "PDWH");
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 1 && collectOperate.equals("0")) {
        assertTrue(map.get("status").equals("200") && commentlist.contains("isDel"));
      } else {
        assertTrue(map.get("status").equals("200") && commentlist.contains("success"));
      }
    }
  }

  @Test
  @DisplayName("准库成果评论列表的空参校验")
  void testCheckPdwhPubCommentListPar() {
    String reqUri = "/ajaxcommentlist";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3PubId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }


  @Test
  @DisplayName("准库成果评论列表")
  void testPdwhPubCommentList() {
    String reqUri = "/ajaxcommentlist";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      if (i == 0) {
        assertTrue(map.get("status").equals("200"));
      } else {
        assertTrue(map.get("status").equals("500"));
      }
    }
  }

  @Test
  @DisplayName("获取基准库成果操作统计数的空参校验")
  void testCheckFindPdwhPubStatisticsPar() {
    String reqUri = "/ajaxstatistics";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3PubId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }

  @Test
  @DisplayName("获取基准库成果操作统计数")
  void testFindPdwhPubStatistics() {
    String reqUri = "/ajaxcommentlist";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200") && StringUtils.isNoneBlank(StringUtils.strip(commentlist, "[]")));
      } else {
        assertTrue(map.get("status").equals("500") && StringUtils.isBlank(StringUtils.strip(commentlist, "[]")));
      }
    }
  }


  @Test
  @DisplayName("移动端导入基准库成果到个人成果库的空参校验")
  void testCheckImportPdwhPubToMePar() {
    String reqUri = "/ajaximport";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3PubId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }

  // 数据重复操作问题还待修改
  @Test
  @DisplayName("移动端导入基准库成果到个人成果库")
  void testImportPdwhPubToMe() {
    String reqUri = "/ajaximport";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("500") && commentlist.contains("result=dup"));
      } else {
        assertTrue(map.get("status").equals("500") && commentlist.contains("error"));
      }
    }
  }

  @Test
  @DisplayName("人员是否已赞过该基准库成果操作的空参校验")
  void testCheckGetPdwhPubAwardAndCollectStatusPar() {
    String reqUri = "/status";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3PdwhPubId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }

  @Test
  @DisplayName("人员是否已赞过该基准库成果")
  void testGetPdwhPubAwardAndCollectStatus() {
    String reqUri = "/status";
    String[] des3PubIds = {proMap.get("des3PdwhPubId"), proMap.get("des3PdwhPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PdwhPubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue(map.get("status").equals("200") && commentlist.contains("status=success"));
      } else {
        assertTrue(map.get("status").equals("500") && commentlist.contains("status=error"));
      }
    }
  }

}
