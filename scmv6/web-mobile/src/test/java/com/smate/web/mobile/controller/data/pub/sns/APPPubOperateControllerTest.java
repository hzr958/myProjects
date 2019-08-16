package com.smate.web.mobile.controller.data.pub.sns;

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
 * @Description 添加全文请求及导入个人库成果接口的测试用例
 * @author YWL
 * @Date 2019/1/29
 */
class APPPubOperateControllerTest {
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
        "src/test/java/com/smate/web/mobile/controller/data/pub/sns/properties/" + runEnv + "_test.properties");
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
    return restTemplate.postForObject(domainMobile + "/data/pub" + uri, HttpEntity, Map.class);
  }

  @Test
  @DisplayName("添加全文请求的空参数校验")
  void testAddPubFulltextRequestPra() {
    String reqUri = "/fulltext/ajaxreqadd";
    String[] parameter = {proMap.get("des3PubId"), "sns", proMap.get("des3RecvPsnIdd")};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", checkPar[0]);
      requestBody.add("pubType", checkPar[1]);
      requestBody.add("des3RecvPsnId", checkPar[2]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
    }
  }

  @ParameterizedTest
  @DisplayName("添加全文请求接口校验")
  @ValueSource(strings = {"sns", "pdwh"})
  void testAddPubFulltextRequest(String pubType) {
    String reqUri = "/fulltext/ajaxreqadd";
    String des3PubId = "des3" + pubType + "PubId";
    String[] des3PubIds = {proMap.get(des3PubId), proMap.get(des3PubId + "IsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      requestBody.add("pubType", pubType);
      if (pubType.equals("sns")) {
        requestBody.add("des3RecvPsnId", proMap.get("des3RecvPsnId"));
      }
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        assertTrue("200".equals(map.get("status")) && commentlist.contains("msg=保存全文请求成功！")
            && commentlist.contains("status=success"));
      } else {
        assertTrue("500".equals(map.get("status")) && commentlist.contains("status=isDel"));
      }
    }
  }

  @Test
  @DisplayName("导入个人库成果接口的空参数校验")
  void testImportSnsPubToMePra() {
    String reqUri = "/optsns/ajaximport";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("des3PubId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("500".equals(map.get("status")) && "参数校验不通过".equals(map.get("msg")));
  }

  @Test
  @DisplayName("导入个人库成果接口校验")
  void testImportSnsPubToMe() {
    String reqUri = "/optsns/ajaximport";
    String[] des3PubIds = {proMap.get("des3snsPubId"), proMap.get("des3snsPubIdIsNotExist")};
    for (int i = 0; i < des3PubIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3PubId", des3PubIds[i]);
      map = getReslutMap(requestBody, reqUri);
      String results = JacksonUtils.jsonObjectSerializerNoNull(map.get("results"));
      String commentlist = JacksonUtils.jsonToMap(results).get("commentlist").toString();
      if (i == 0) {
        // 成果重复导入需初始化
        assertTrue(("200".equals(map.get("status")) && commentlist.contains("result=success"))
            || ("500".equals(map.get("status")) && commentlist.contains("result=dup")));
      } else {
        assertTrue("500".equals(map.get("status")) && commentlist.contains("result=isDel"));
      }
    }
  }
}
