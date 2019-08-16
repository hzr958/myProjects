package com.smate.web.mobile.controller.data.detail.pub;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * 测试类 测试移动端成果详情接口的测试类
 * 
 * @author SYL
 *
 */
public class PubDataDetailsControllerTest {
  public static Map<String, String> proMap;

  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * 根据环境变量加载不同的properties文件
   * 
   * @throws IOException
   */
  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap =
        JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/detail/pub/properties/"
            + runEnv + "_test_PubDataDetailsControllerTest.properties");
  }

  private RestTemplate template = new RestTemplate();
  private String domain = proMap.get("junit_domainMobile") + "/data/pub/details";

  /**
   * 使用给定参数和url,发起请求,返回json数据
   * 
   * @param url
   * @param params
   * @return
   */
  private Map<String, Object> sendRequest(String url, Map<String, Object> params) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("token", proMap.get("token"));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    for (String param : params.keySet()) {
      body.add(param, params.get(param));
    }
    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
    String jsonStr = template.postForObject(url, httpEntity, String.class);
    return JacksonUtils.jsonToMap(jsonStr);
  }


  @ParameterizedTest
  @DisplayName("测试查看个人库成果详情接口")
  @CsvSource({"sasas,des3PubId非法", ",正常数据", "sFwtdzGREBFzUyT1hKbVjA%3D%3D,des3PubId错误"})
  public void testPubDetails(String des3PubId, String caseFlag) {
    Map<String, Object> params = new HashMap<>();
    if (caseFlag.equals("正常数据")) {
      params.put("des3PubId", proMap.get("snsDes3PubId"));
    } else {
      params.put("des3PubId", des3PubId);
    }
    Map<String, Object> jsonToMap = sendRequest(domain + "/sns", params);
    switch (caseFlag) {
      case "正常数据":
        assertTrue(jsonToMap.get("status").equals("success"));
        assertTrue(jsonToMap.get("result") != null);
        break;
      case "des3PubId非法":
        assertTrue(jsonToMap.get("status").toString().equals("error"));
        break;
      case "des3PubId错误":
        assertTrue(jsonToMap.get("status").equals("error"));
        assertTrue(jsonToMap.get("errmsg").equals("调用远程数据接口异常"));
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @DisplayName("测试基准库成果详情接口")
  @CsvSource({"'',wqeqweqeqw,pubId非法", "4255662,ascadasd,des3PubId非法", ",,正常数据"})
  public void testPubPdwhDetails(String pubId, String des3PubId, String caseFlag) {
    Map<String, Object> params = new HashMap<>();
    if (caseFlag.equals("正常数据")) {
      pubId = proMap.get("pdwhPubId");
      des3PubId = proMap.get("pdwhDes3PubId");
    }
    params.put("pubId", pubId);
    params.put("des3PubId", des3PubId);
    Map<String, Object> map = sendRequest(domain + "/pdwh", params);
    switch (caseFlag) {
      case "正常数据":
        assertTrue(map.get("status").equals("success"));
        assertTrue(map.get("result") != null);
        assertTrue(((LinkedHashMap<String, Object>) (map.get("result"))).get("pubId").toString().equals(pubId));
        break;
      case "des3PubId非法":
        assertTrue(map.get("status").equals("success"));
        assertTrue(map.get("result") != null);
        assertTrue(((LinkedHashMap<String, Object>) (map.get("result"))).get("pubId").toString().equals(""));
        break;
      case "pubId非法":
        assertTrue(map.get("errmsg").equals("参数校验不通过") && map.get("status").equals("error"));
        break;
    }
  }

  /*
   * @ParameterizedTest
   * 
   * @DisplayName("测试项目详情接口")
   * 
   * @CsvSource({"thxMLiZ2TRYNNxssqxrRYg%3D%3D,0"}) public void testPrjDetails(String des3PrjId, int
   * caseFlag) { Map<String, Object> params = new HashMap<>(); params.put("des3PrjId", des3PrjId);
   * Map<String, Object> map = sendRequest(domain + "/prj", params); switch (caseFlag) { case 0:
   * 
   * break; case 1: assertTrue(map.get("status").equals("error") &&
   * map.get("errmsg").equals("参数校验不通过")); case 2:
   * 
   * default: break; } }
   */

  /*
   * @ParameterizedTest
   * 
   * @DisplayName("测试基金详情接口")
   * 
   * @CsvSource({"thxMLiZ2TRYNNxssqxrRYg%3D%3D"}) public void testFundDetails(String des3FunId) {
   * Map<String, Object> params = new HashMap<>(); params.put("des3FundId", des3FunId); Map<String,
   * Object> map = sendRequest(domain + "/fund", params); }
   */
}
