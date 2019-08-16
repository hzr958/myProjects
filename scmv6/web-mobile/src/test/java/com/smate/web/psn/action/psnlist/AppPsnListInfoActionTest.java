package com.smate.web.psn.action.psnlist;

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
 * 
 * @author SYL
 * 
 *         2019年2月
 *
 */
public class AppPsnListInfoActionTest {
  public static Map<String, String> proMap;
  private RestTemplate template = new RestTemplate();
  private String domain = proMap.get("junit_domainMobile");

  /**
   * 根据环境变量加载不同的properties文件
   * 
   * @throws IOException
   */

  @BeforeAll
  public static void loadProperties() throws IOException {
    String runEnv = System.getenv("RUN_ENV");
    proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/psnlist/properties/" + runEnv
        + "_test_AppPsnListInfoActionTest.properties");
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
  @DisplayName("测试获取好友请求列表")
  @ParameterizedTest
  @CsvSource({"参数正常,1000000000521", "psnId不存在,123456789", "psnId不合法,1232sas"})
  public void friendReqTest(String description, String psnId) {
    Map<String, Object> params = new HashMap<>();
    if (description.equals("参数正常")) {
      params.put("psnId", proMap.get("psnId"));
    } else {
      params.put("psnId", psnId);
    }
    String[] urls = new String[] {domain + "/app/psnweb/friendreq/ajaxlist", domain + "/app/psnweb/friendreq"};
    for (String url : urls) {
      Map<String, Object> map = sendRequest(url, params);
      switch (description) {
        case "参数正常":
          assertTrue(map.get("status").equals("200"));
          assertTrue(((LinkedHashMap<String, Object>) map.get("results")).get("commentlist") != null);
          break;
        case "psnId不存在":
          assertTrue(map.get("status").equals("200"));
          String list1 = (String) ((LinkedHashMap<String, Object>) map.get("results")).get("commentlist");
          assertTrue(list1.equals(""));
          break;
        case "psnId不合法":
          // 返回500页面
          break;
        default:
          break;
      }
    }
  }

}
