package com.smate.web.psn.action.search;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;
import com.smate.web.prj.action.app.APPProjectActionTest;

/**
 * @description APP人员检索接口测试类
 * @author xiexing
 * @date 2019年2月19日
 */
public class APPPsnSearchActionTest {
  private static final Logger logger = LoggerFactory.getLogger(APPProjectActionTest.class);
  private static String junit_domainMobile = "";
  private static String token = "";
  private static HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
  private static RestTemplate restTemplate = null;
  private static HttpHeaders headers = null;
  private static Map<String, String> proMap;

  @BeforeAll
  public static void init() {
    restTemplate = new RestTemplate();
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/search/properties/" + runEnv
          + "_test_APPPsnSearchActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    junit_domainMobile = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("token", token);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(headers);
  }

  @Test
  @DisplayName("测试获取人员列表")
  void testGetMobileList() {
    String[] strings = {"测试", "mobileSearchFriend"};
    String[] param = {"searchString"};// 有此参数就有数据返回
    List<String[]> list = JunitUtils.buildCheckPar(strings);
    for (String[] string : list) {
      String code = "200";
      MultiValueMap<String, Object> params = buildParam(string);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
      if (!specialParam(params, param)) {
        code = "304";
      }
      checkResult(request("/mobile/ajaxlist", requestEntity), code);
    }
  }

  @ParameterizedTest
  @CsvSource({"2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,1", ",2"})
  @DisplayName("测试Mobile-批量获取人员头像.")
  void testGetMobileAvatarUrls(String des3PsnIdsStr, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PsnIdsStr", des3PsnIdsStr);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    checkResult(request("/mobile/ajaxavatarurls", requestEntity), "200");
  }

  @ParameterizedTest
  @CsvSource({"测试,1", ",2"})
  @DisplayName("测试消息中心全站检索人员")
  void testGetPsnsForMsg(String searchString, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("searchString", searchString);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String code = "200";
    if (num != null && num == 2) {
      code = "400";
    }
    checkResult(request("/search/ajaxmsgallpsnlist", requestEntity), code);
  }


  /**
   * 必要参数不为空则操作成功
   * 
   * @param param
   * @param params
   * @return
   */
  public boolean specialParam(MultiValueMap<String, Object> param, String[] params) {
    if (param == null || param.size() == 0 || params == null || params.length == 0) {
      return false;
    }
    for (String string : params) {
      if (Objects.isNull(param.get(string)) || param.get(string).toString().length() < 3) {
        return false;
      }
    }
    return true;
  }

  /**
   * 构建参数
   * 
   * @param params
   * @return
   */
  public MultiValueMap<String, Object> buildParam(String[] params) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
    param.add("searchString", params[0]);
    param.add("fromPage", params[1]);
    return param;
  }

  /**
   * 公共请求与请求结果返回
   * 
   * @param requestUri
   * @param requestEntity
   * @return
   */
  public String request(String requestUri, HttpEntity<MultiValueMap<String, Object>> requestEntity) {
    if (StringUtils.isEmpty(requestUri)) {
      return "";
    }
    return restTemplate.postForObject(junit_domainMobile + "/app/psnweb" + requestUri, requestEntity, String.class);
  }

  /**
   * 验证结果
   * 
   * @param string
   * @param code
   */
  public void checkResult(String string, String code) {
    Map<String, Object> map = JacksonUtils.jsonToMap(string);
    assertNotEquals(null, map);
    assertTrue(code.equals(map.get("status")));
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
  }

}
