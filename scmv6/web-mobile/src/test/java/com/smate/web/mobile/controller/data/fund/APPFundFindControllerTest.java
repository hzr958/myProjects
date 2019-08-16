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

import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;

/**
 * @Description 基金发现APP接口测试用例
 * @author YWL
 * @Date 2019/1/29
 */
class APPFundFindControllerTest {
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


  @Test
  @DisplayName("基金发现地区条件数据接口")
  void testGetAgencycondition() {
    String reqUri = "/fundfindregion";
    requestBody = new LinkedMultiValueMap<>();
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @ParameterizedTest
  @DisplayName("基金发现--基金列表数据加载接口校验")
  @ValueSource(strings = {"0", "1", "2"})
  void testGetagencydetail(String searchseniority) {
    String reqUri = "/fundfindlist";
    String[] regionCodes = {"157", "340000", "410000", "446"};
    for (String regionCode : regionCodes) {
      String searchRegionCodes = regionCode;
      String[] parameter = {"201,202,203", "DNA"};
      List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
      checkPars.add(parameter);
      for (String[] checkPar : checkPars) {
        requestBody = new LinkedMultiValueMap<>();
        requestBody.add("searchRegionCodes", searchRegionCodes);
        requestBody.add("searchseniority", searchseniority);
        requestBody.add("scienceCodesSelect", checkPar[0]);
        requestBody.add("searchKey", checkPar[1]);
        requestBody.add("page.pageNo", "1");
        map = getReslutMap(requestBody, reqUri);
        assertTrue("200".equals(map.get("status")));
      }
    }
  }
}
