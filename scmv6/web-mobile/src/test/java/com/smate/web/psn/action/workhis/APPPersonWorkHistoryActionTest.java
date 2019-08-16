package com.smate.web.psn.action.workhis;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
 * @description APP个人工作经历测试类
 * @author xiexing
 * @date 2019年2月18日
 */
public class APPPersonWorkHistoryActionTest {
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
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/workhis/properties/" + runEnv
          + "_test_APPPersonWorkHistoryActionTest.properties");
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
  @DisplayName("测试新增或更新工作经历")
  void testSaveWorkHistory() {
    String[] strings = {"1001000736992", "2018/09", "2019/02", "爱瑞思软件", "科研之友个人业务部", "java开发工程师", "这是描述哈"};
    List<String[]> list = JunitUtils.buildCheckPar(strings);
    String[] stringParams = {"psnId", "startDate", "endDate", "insName"};// 这四个参数均不为空即要成功
    String code = "500";
    for (String[] params : list) {
      MultiValueMap<String, Object> param = buildParam(params);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(param, headers);
      if (specialParam(param, stringParams)) {
        code = "200";
      }
      checkResult(request("/ajaxsave", requestEntity), code);
    }
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
    param.add("psnId", params[0]);
    param.add("startDate", params[1]);
    param.add("endDate", params[2]);
    param.add("insName", params[3]);
    param.add("department", params[4]);
    param.add("position", params[5]);
    param.add("description", params[6]);
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
    return restTemplate.postForObject(junit_domainMobile + "/app/psnweb/workhistory" + requestUri, requestEntity,
        String.class);
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

