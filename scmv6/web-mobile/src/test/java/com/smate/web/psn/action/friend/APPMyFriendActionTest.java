package com.smate.web.psn.action.friend;

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
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.test.enums.StatusCode;
import com.smate.test.utils.JunitPropertiesUtils;
import com.smate.test.utils.JunitUtils;
import com.smate.web.prj.action.app.APPProjectActionTest;

/**
 * @description APP好友相关操作测试类
 * @author xiexing
 * @date 2019年2月19日
 */
public class APPMyFriendActionTest {
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
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/psn/action/friend/properties/" + runEnv
          + "_test_APPMyFriendActionTest.properties");
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

  @ParameterizedTest
  @ValueSource(strings = {"all", "friend"})
  @DisplayName("测试消息中心-站内信-好友列表")
  void testShowMsgChatPsnList(String psnTypeForChat) {
    String[] strings = {"1001000736992", "沙瑞金", psnTypeForChat};
    List<String[]> list = JunitUtils.buildCheckPar(strings);
    String[] stringParam = {"psnId"};// 该参数不为空则响应成功
    for (String[] string : list) {
      String code = StatusCode.getCodeByNum(3);
      MultiValueMap<String, Object> params = buildParam(string, 1);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
      code = getCode(params, stringParam, code);
      checkResult(request("/friend/ajaxgetchatfriendlist", requestEntity), code);
    }
  }

  @Test
  @DisplayName("测试消息中心联合查询人员")
  void testMergeSearchPsn() {
    String[] strings = {"1001000736992", "沙瑞金"};
    List<String[]> list = JunitUtils.buildCheckPar(strings);
    String[] stringParam = {"psnId"};
    for (String[] string : list) {
      String code = StatusCode.getCodeByNum(3);
      MultiValueMap<String, Object> params = buildParam(string, 2);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
      code = getCode(params, stringParam, code);
      checkResult(request("/search/psnandfrd", requestEntity), code);
    }
  }

  @ParameterizedTest
  @CsvSource({"gdC9pv0cs%2BvfDqWtAQOMbg%3D%3D,1", ",2"})
  @DisplayName("测试同意好友请求")
  void testAcceptAddFriendRequest(String tempPsnId, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    /**
     * 申请添加
     */
    String result = requestForAdd(params);
    logger.info("result={}", result);
    /**
     * 同意操作
     */
    headers.remove("token");
    headers.add("token", token);
    params.clear();
    params.add("des3ReqPsnIds", tempPsnId);// 申请者id
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String code = StatusCode.getCodeByNum(num);
    checkResult(request("/friendreq/ajaxaccept", requestEntity), code);
  }


  @ParameterizedTest
  @CsvSource({"gdC9pv0cs%252BvfDqWtAQOMbg%253D%253D,1", ",2"})
  @DisplayName("测试好友请求列表-忽略操作")
  void testAjaxNeglet(String des3TempPsnId, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    /**
     * 申请添加
     */
    String result = requestForAdd(params);
    logger.info("result={}", result);
    /**
     * 忽略操作
     */
    headers.remove("token");
    headers.add("token", token);
    params.clear();
    params.add("des3TempPsnId", des3TempPsnId);// 申请者id
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String code = StatusCode.getCodeByNum(num);
    checkResult(request("/friendreq/ajaxneglet", requestEntity), code);
  }


  @ParameterizedTest
  @CsvSource({"JvUzHyT7%252BGLiOvtPDyNdZA%253D%253D,1", ",2"})
  @DisplayName("测试删除好友")
  void testAjaxDelFriend(String friendDes3PsnIds, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("friendDes3PsnIds", friendDes3PsnIds);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String code = StatusCode.getCodeByNum(num);
    checkResult(request("/friend/ajaxdel", requestEntity), code);
  }


  @ParameterizedTest
  @CsvSource({"JvUzHyT7%252BGLSWGMm5IJ5Pw%253D%253D,1", ",2"})
  @DisplayName("测试我的-好友推荐列表-移除")
  void testAjaxRemove(String tempPsnId, Integer num) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3TempPsnId", tempPsnId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String code = StatusCode.getCodeByNum(num);
    checkResult(request("/recommend/ajaxremove", requestEntity), code);
  }

  /**
   * 发送请求添加好友
   * 
   * @param params
   * @return
   */
  public String requestForAdd(MultiValueMap<String, Object> params) {
    /**
     * 发送好友请求(以1000000727693为例,其token=XVtkcaN5P6SZEohPG05cri5JfIJ0aNUJ77Ye1w%2BjI50%3D)
     */
    headers.remove("token");
    headers.add("token", "XVtkcaN5P6SZEohPG05cri5JfIJ0aNUJ77Ye1w%2BjI50%3D");
    params.add("des3Id", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D");// 被申请者id
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    return restTemplate.postForObject(junit_domainMobile + "/app/friend/addfriend", requestEntity, String.class);
  }


  /**
   * 根据传参返回不同的编码
   * 
   * @param params
   * @param strings
   * @return
   */
  public String getCode(MultiValueMap<String, Object> params, String[] strings, String code) {
    if (params == null || params.size() == 0 || StringUtils.isNotEmpty(code)) {
      return code;
    }
    if (specialParam(params, strings)) {
      code = StatusCode.getCodeByNum(1);
    }
    if (!specialParam(params, strings)) {
      code = StatusCode.getCodeByNum(2);
    }
    return code;
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
   * 判断若有一个参数不为空即为真
   * 
   * @param param
   * @param params
   * @return
   */
  public boolean hasNoneNulParam(MultiValueMap<String, Object> param, String[] params) {
    if (param == null || param.size() == 0 || params == null || params.length == 0) {
      return false;
    }
    for (String string : params) {
      if (Objects.nonNull(param.get(string)) && param.get(string).toString().length() > 2) {
        return true;
      }
    }
    return false;
  }

  /**
   * 构建参数
   * 
   * @param params
   * @return
   */
  public MultiValueMap<String, Object> buildParam(String[] params, Integer num) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
    param.add("psnId", params[0]);
    param.add("searchKey", params[1]);
    if (num != null && num == 1) {
      param.add("psnTypeForChat", params[2]);
    }
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
