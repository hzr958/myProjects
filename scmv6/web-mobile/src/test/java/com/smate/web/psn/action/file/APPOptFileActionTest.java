package com.smate.web.psn.action.file;

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

import com.smate.core.base.utils.string.StringUtils;
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;

/**
 * @Description app我的文件操作数据接口测试用例
 * @author YWL
 * @Date 2019/1/31
 */
class APPOptFileActionTest {
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
    proMap = JunitPropertiesUtils
        .loadProperties("src/test/java/com/smate/web/psn/action/file/properties/" + runEnv + "_test.properties");
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
    return restTemplate.postForObject(domainMobile + "/app/psnweb" + uri, HttpEntity, Map.class);
  }


  @Test
  @DisplayName("删除我的文件接口空参校验")
  void testDelMyFilePar() {
    String reqUri = "/myfile/ajaxdelmyfile";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fileId", "");
    map = getReslutMap(requestBody, reqUri);
    assertTrue("400".equals(map.get("status")));
  }

  @Test
  @DisplayName("删除我的文件接口校验")
  void testDelMyFile() {
    String reqUri = "/myfile/ajaxdelmyfile";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fileId", proMap.get("fileId"));
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("保存文件描述接口的空参校验")
  void testAjaxdeletefundareaPar() {
    String reqUri = "/myfile/ajaxsavefiledesc";
    String[] parameter = {proMap.get("fileId"), "这是我的文件"};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("fileId", checkPar[0]);
      requestBody.add("fileDesc", checkPar[1]);
      map = getReslutMap(requestBody, reqUri);
      if (StringUtils.isBlank(checkPar[0])) {
        assertTrue("400".equals(map.get("status")));
      } else {
        assertTrue("200".equals(map.get("status")));
      }
    }
  }

  @ParameterizedTest
  @DisplayName("保存文件描述接口校验")
  @ValueSource(strings = {"这是我的文件", ""})
  void testAjaxdeletefundarea(String fileDesc) {
    String reqUri = "/myfile/ajaxsavefiledesc";
    requestBody = new LinkedMultiValueMap<>();
    requestBody.add("fileId", proMap.get("fileId"));
    requestBody.add("fileDesc", fileDesc);
    map = getReslutMap(requestBody, reqUri);
    assertTrue("200".equals(map.get("status")));
  }

  @Test
  @DisplayName("文件批量分享的空参校验")
  void testShareFileAllPar() {
    String reqUri = "/myfile/ajaxsharemyfiles";
    String[] parameter = {proMap.get("des3FileIds"), proMap.get("des3ReceiverIds"), proMap.get("fileNames")};
    List<String[]> checkPars = JunitUtils.buildCheckPar(parameter);
    for (String[] checkPar : checkPars) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FileIds", checkPar[0]);
      requestBody.add("des3ReceiverIds", checkPar[1]);
      requestBody.add("fileNames", checkPar[2]);
      map = getReslutMap(requestBody, reqUri);
      assertTrue("400".equals(map.get("status")));
    }
  }


  @Test
  @DisplayName("文件批量分享接口校验")
  void testShareFileAll() {
    String reqUri = "/myfile/ajaxsharemyfiles";
    String[] fundIds = {proMap.get("des3FileIds"), proMap.get("des3FileIdsIsDel")};
    for (int i = 0; i < fundIds.length; i++) {
      requestBody = new LinkedMultiValueMap<>();
      requestBody.add("des3FileIds", fundIds[i]);
      requestBody.add("des3ReceiverIds", proMap.get("des3ReceiverIds"));
      requestBody.add("fileNames", proMap.get("fileNames"));
      map = getReslutMap(requestBody, reqUri);
      if (i == 0) {
        assertTrue("200".equals(map.get("status")));
      } else {
        assertTrue("500".equals(map.get("status")));
      }
    }
  }

}
