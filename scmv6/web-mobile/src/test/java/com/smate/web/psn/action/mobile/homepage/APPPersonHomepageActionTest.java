package com.smate.web.psn.action.mobile.homepage;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * 测试个人首页接口
 * 
 * @author SYL
 * 
 *         2019年2月
 *
 */
public class APPPersonHomepageActionTest {

  public static Map<String, String> proMap;
  private RestTemplate template = new RestTemplate();
  private String domain = proMap.get("junit_domainMobile");// proMap.get("junit_domainMobile") + "/data/pub/details";

  /**
   * 根据环境变量加载不同的properties文件
   * 
   * @throws IOException
   */

  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/mobile/homepage/properties/"
        + runEnv + "_test_APPPersonHomepageActionTest.properties");
  }

  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }

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
    System.out.println(jsonStr);
    return JacksonUtils.jsonToMap(jsonStr);
  }


  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @DisplayName("测试获取人员代表性项目")
  @CsvSource({"查看自己,JvUzHyT7%2BGJLRfjE6q8RdQ%3D%3D", "查看他人,JvUzHyT7%2BGJFWGMAEJoMxQ%3D%3D",
      "非法的des3OtherPsnId,saadasda"})
  public void showRepresentPrjTest(String description, String des3OtherPsnId) {
    Map<String, Object> params = new HashMap<>();
    if (description.equals("查看自己")) {
      params.put("des3OtherPsnId", proMap.get("des3PsnId"));
    } else if (description.equals("查看他人")) {
      params.put("des3OtherPsnId", proMap.get("des3OtherPsnId"));
    } else {
      params.put("des3OtherPsnId", des3OtherPsnId);
    }
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/outside/mobile/ajaxrepresentprj", params);
    switch (description) {
      case "查看自己":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      case "查看他人":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      case "非法的des3OtherPsnId":
        assertTrue(map.get("status").equals("500"));
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @CsvSource({"正常数据,1000000047599", "psnId异常,123456789"})
  @DisplayName("测试进入个人主页")
  public void myHomepageTest(String description, String psnId) {
    Map<String, Object> params = new HashMap<>();
    if (description.equals("正常数据")) {
      params.put("psnId", proMap.get("psnId"));
    } else {
      params.put("psnId", psnId);
    }
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/homepage", params);
    switch (description) {
      case "正常数据":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      case "psnId异常":
        assertTrue(map.get("status").equals("500"));
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @DisplayName("测试显示关键词信息")
  @CsvSource({"访问他人,JvUzHyT7%2BGJFWGMAEJoMxQ%3D%3D", "访问自己,", "不合法的des3PsnId,aaaaaaaa"})
  @ParameterizedTest
  public void showPsnKeyWordsTest(String description, String des3PsnId) {
    Map<String, Object> params = new HashMap<>();
    if (description.equals("访问他人")) {
      params.put("des3OtherPsnId", proMap.get("des3OtherPsnId"));
    } else {
      params.put("des3OtherPsnId", des3PsnId);
    }
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/outside/mobile/ajaxkeywords", params);
    switch (description) {
      case "访问他人":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      case "访问自己":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      case "不合法的des3PsnId":
        assertTrue(map.get("status").equals("200"));
        assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
        break;
      default:
        break;
    }
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @CsvSource({"des3ViewPsnId正常,JvUzHyT7%2BGJFWGMAEJoMxQ%3D%3D", "des3ViewPsnId为空,", "des3ViewPsnId不合法,aaaaaa"})
  @DisplayName("测试进入他人主页")
  public void otherHomepageTest(String description, String des3ViewPsnId) {
    Map<String, Object> params = new HashMap<>();
    if (description.equals("des3ViewPsnId正常")) {
      params.put("des3ViewPsnId", proMap.get("des3OtherPsnId"));
    } else {
      params.put("des3ViewPsnId", des3ViewPsnId);
    }
    Map<String, Object> map = sendRequest(domain + "/app/psnweb/otherhomepage", params);
    switch (description) {
      case "des3ViewPsnId正常":
        assertTrue(map.get("status").equals("200"));
        LinkedHashMap<String, Object> commentlist =
            (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) map.get("results")).get("commentlist");
        assertTrue(!commentlist.isEmpty());
        break;
      case "des3ViewPsnId为空":
        assertTrue(map.get("status").equals("400"));
        break;
      case "des3ViewPsnId不合法":
        assertTrue(map.get("status").equals("500"));
        break;
      default:
        break;
    }
  }

}
