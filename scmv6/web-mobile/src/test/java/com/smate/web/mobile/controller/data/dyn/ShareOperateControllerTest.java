package com.smate.web.mobile.controller.data.dyn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
 * 测试类 测试移动端分享资源操作接口
 * 
 * @author SYL
 * 
 * @date 2019年1月14日
 */
public class ShareOperateControllerTest {

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
    proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/mobile/controller/data/dyn/properties/"
        + runEnv + "_test_ShareOperateControllerTest.properties");
  }

  private RestTemplate restTemplate = new RestTemplate();

  private String url = proMap.get("junit_domainMobile") + "/data/dyn/updatesharestatic";

  private String token = proMap.get("token");// "BEdf8A2yL5QGngx4Y6Y6smI0i4AE5JPht%2F3jat21xnE%3D";

  private String des3ResId = proMap.get("des3ResId");// "sFVtDzGREBFzUyT1hKbVjA%3D%3D";

  private String platform = "1"; // 分享的平台

  private String resType = "1";// 分享资源的类型

  /**
   *
   * 发送post请求，并返回json数据
   * 
   * @param des3ResId
   * @param resType
   * @param platform
   * @return
   */
  private String sendRequest(String requestUrl, String token, String des3ResId, String resType, String platform) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("token", token);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("des3ResId", des3ResId);
    body.add("resType", resType);
    body.add("platform", platform);
    HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
    String jsonStr = restTemplate.postForObject(requestUrl, httpEntity, String.class);
    System.out.println(jsonStr);
    return jsonStr;
  }

  /**
   * 生成的参数设置成缺失参数的列表
   * 
   * @param strings
   * @return
   */
  private List<String[]> getAllGroup(String[] strings) {
    if (strings == null || strings.length == 0) {
      return null;
    }
    List<String[]> groups = new ArrayList<>();
    int i = (int) Math.pow(2, strings.length);
    for (int j = 0; j < i - 1; j++) {
      String binaryString = Integer.toBinaryString(j);
      binaryString = String.format("%" + strings.length + "d", Integer.parseInt(binaryString));
      char[] charArray = binaryString.toCharArray();
      String[] arr = new String[strings.length];
      for (int k = 0; k < arr.length; k++) {
        if (charArray[k] == '1') {
          arr[k] = String.valueOf(strings[k]);
        } else {
          arr[k] = "";
        }
      }
      groups.add(arr);
    }
    return groups;
  }

  /**
   * 定义断言，检查返回的值是否正确
   * 
   * @param map
   * @param statusCode
   * @param status
   * @param mesg
   * @param errorMesg
   */
  @SuppressWarnings("unchecked")
  private void checkResponse(Map<String, Object> map, String statusCode, String status, String mesg, String errorMesg) {
    assertTrue(map.get("status").equals(statusCode) && map.get("msg").equals(mesg));
    Map<String, Object> resultsMap = (Map<String, Object>) map.get("results");
    Map<String, Object> commentlistMap = (Map<String, Object>) resultsMap.get("commentlist");
    assertTrue(commentlistMap.get("status").equals(status) && commentlistMap.get("errorMsg").equals(errorMesg));
  }


  @Test
  @DisplayName("测试参数一个或多个为空校验功能") // BEdf8A2yL5QGngx4Y6Y6smI0i4AE5JPht%2F3jat21xnE%3D
  void testParameterNull() {
    List<String[]> allGroup = getAllGroup(new String[] {des3ResId, resType, platform});
    for (String[] strings : allGroup) {
      String responseJson = sendRequest(url, token, strings[0], strings[1], strings[2]);
      Map<String, Object> map = JacksonUtils.jsonToMap(responseJson);
      checkResponse(map, "500", "error", "des3ResId或shareType参数异常", "des3ResId或shareType参数异常");
    }
  }

  @ParameterizedTest
  @DisplayName("测试shareType不合法")
  @ValueSource(strings = {"12", "-1", ""})
  void testShareTypeIllegal(String shareType) {
    String responseJson = sendRequest(url, token, des3ResId, shareType, platform);
    Map<String, Object> map = JacksonUtils.jsonToMap(responseJson);
    checkResponse(map, "500", "error", "des3ResId或shareType参数异常", "des3ResId或shareType参数异常");
  }

  @ParameterizedTest
  @DisplayName("测试platform不合法")
  @ValueSource(strings = {"-1", "1111", ""})
  void testResTypeIllegal(String pf) {
    String responseJson = sendRequest(url, token, des3ResId, resType, pf);
    Map<String, Object> map = JacksonUtils.jsonToMap(responseJson);
    checkResponse(map, "500", "error", "des3ResId或shareType参数异常", "des3ResId或shareType参数异常");
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest
  @ValueSource(strings = {"asdqawd", "asdas", "1111", "测试"})
  @DisplayName("测试des3ResId不合法")
  void testDes3ResIdIllegal(String des3ResId1) {
    String responseJson = sendRequest(url, token, des3ResId1, resType, platform);
    System.out.println(responseJson);
    Map<String, Object> map = JacksonUtils.jsonToMap(responseJson);
    assertTrue(map.get("status").equals("200") && map.get("msg").equals(""));
    Map<String, Object> resultsMap = (Map<String, Object>) map.get("results");
    Map<String, Object> commentlistMap = (Map<String, Object>) resultsMap.get("commentlist");
    assertTrue(commentlistMap.get("status").equals("success"));
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("测试所有参数正常情况")
  void testParameterlegal() {
    String responseJson = sendRequest(url, token, des3ResId, resType, platform);
    System.out.println(responseJson);
    Map<String, Object> map = JacksonUtils.jsonToMap(responseJson);
    assertTrue(map.get("status").equals("200") && map.get("msg").equals(""));
    Map<String, Object> resultsMap = (Map<String, Object>) map.get("results");
    Map<String, Object> commentlistMap = (Map<String, Object>) resultsMap.get("commentlist");
    assertTrue(commentlistMap.get("status").equals("success"));
  }
}
