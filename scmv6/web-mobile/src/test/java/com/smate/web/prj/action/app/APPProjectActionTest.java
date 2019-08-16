package com.smate.web.prj.action.app;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
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

/**
 * 项目与成果测试类
 * 
 * @author xiexing
 * @date 2019年1月29日
 */
public class APPProjectActionTest {
  private static final Logger logger = LoggerFactory.getLogger(APPProjectActionTest.class);
  private static String junit_domainMobile = "";
  private static String token = "";
  private static HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
  private static RestTemplate restTemplate = null;
  private static HttpHeaders headers = null;
  private static Map<String, String> proMap;
  static {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
    }
    String runEnv = System.getenv("RUN_ENV");
    try {
      proMap = JunitPropertiesUtils.loadProperties(
          "src/test/java/com/smate/web/prj/action/app/propertites/" + runEnv + "_test_APPProjectActionTest.properties");
    } catch (Exception e) {
      logger.error("文件读取失败", e);
    }
    junit_domainMobile = proMap.get("junit_domainMobile");
    token = proMap.get("token");
    if (requestEntity == null) {
      headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      headers.add("token", token);
      requestEntity = new HttpEntity<MultiValueMap<String, Object>>(headers);
    }
  }

  @BeforeEach
  void initMockito() {
    MockitoAnnotations.initMocks(this);
  }

  @ParameterizedTest
  @CsvSource({"JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,1", ",2", "1000000007166,3"})
  @DisplayName("测试获取成果统计数")
  void testGetPubStatics(String des3PrjId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PrjId", des3PrjId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result =
        restTemplate.postForObject(junit_domainMobile + "/app/prjweb/project/statistics", requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(result, "200");
        break;
      case 2:
        checkResult(result, "505");// 参数校验出错
        break;
      case 3:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,1", ",2", "1000000007166,3"})
  @DisplayName("测试获取评论列表（站内站外均要使用）")
  void testPrjCommentShow(String des3PrjId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PrjId", des3PrjId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result = restTemplate.postForObject(junit_domainMobile + "/app/prjweb/project/ajaxcommentshow",
        requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(result, "200");
        break;
      case 2:
        checkResult(result, "505");
        break;
      case 3:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,1", ",2", "1000000007166,3"})
  @DisplayName("测试项目赞")
  void testAjaxAddAward(String des3PrjId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PrjId", des3PrjId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result = restTemplate.postForObject(junit_domainMobile + "/app/prjweb/project/ajaxprjaddaward",
        requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(result, "200");
        break;
      case 2:
        checkResult(result, "505");
        break;
      case 3:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,1", ",2", "1000000007166,3"})
  @DisplayName("测试项目取消赞")
  void testAjaxCancelAward(String des3PrjId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PrjId", des3PrjId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result = restTemplate.postForObject(junit_domainMobile + "/app/prjweb/project/ajaxprjcancelaward",
        requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(result, "200");
        break;
      case 2:
        checkResult(result, "505");
        break;
      case 3:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,这是测试评论,1", ",这是测试评论,2", "JvUzHyT7%2BGKzkYiG5xzzCQ%3D%3D,,3", ",,4",
      "1000000007166,这是测试评论,5"})
  @DisplayName("测试项目添加评论")
  void testPrjAddComment(String des3PrjId, String comment, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PrjId", des3PrjId);
    params.add("comment", comment);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result = restTemplate.postForObject(junit_domainMobile + "/app/prjweb/project/ajaxaddcomment", requestEntity,
        String.class);
    switch (number) {
      case 1:
        checkResult(result, "200");
        break;
      case 2:
        checkResult(result, "505");
        break;
      case 3:
        checkResult(result, "505");
        break;
      case 4:
        checkResult(result, "505");
        break;
      case 5:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,1001000736992,2", ",1001000736992,2", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,,2",
      "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,1001000736992,", ",,2", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,,", ",1001000736992,", ",,"})
  @DisplayName("测试获取项目列表")
  void testQueryPrj(String des3PsnId, String psnId, String nextPage) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PsnId", des3PsnId);
    params.add("psnId", psnId);
    params.add("nextId", nextPage);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result =
        restTemplate.postForObject(junit_domainMobile + "/app/prjweb/findprjs", requestEntity, String.class);
    checkResult(result, "200");
  }

  // 该接口直接返回jsp页面,暂不测试
  // @ParameterizedTest
  // @CsvSource({"JvUzHyT7%2BGKlex3raS5Iaw%3D%3D,1", ",2"})
  // @DisplayName("测试获取项目详情")
  // void testQueryPrjXml(String des3PrjId, Integer number) {
  // MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
  // params.add("des3PrjId", des3PrjId);
  // requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
  // String result =
  // restTemplate.postForObject(junit_domainMobile + "/app/prjweb/findprjdetail", requestEntity,
  // String.class);
  // switch (number) {
  // case 1:
  // checkResult(result, "200");
  // break;
  // case 2:
  // checkResult(result, "400");
  // break;
  // default:
  // break;
  // }
  // }

  @ParameterizedTest
  @CsvSource({"2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,1001000736992,JvUzHyT7%2BGLTAK3Wa9cLcQ%3D%3D,1",
      ",1001000736992,JvUzHyT7%2BGLRs4xvfsnSTg%3D%3D,2",
      "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,,JvUzHyT7%2BGIS8qujGx3I0Q%3D%3D,3", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,1001000736992,,4",
      ",,JvUzHyT7%2BGKi%2FSHRo9QNeg%3D%3D,5", ",1001000736992,,6", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D,,,7", ",,,8",
      ",,1000000000402,9"})
  @DisplayName("测试项目详情 返回json数据")
  void testQueryPrjDetail(String des3PsnId, String psnId, String des3PrjId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3PsnId", des3PsnId);
    params.add("psnId", psnId);
    params.add("des3PrjId", des3PrjId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String result =
        restTemplate.postForObject(junit_domainMobile + "/app/prjweb/query/detail", requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(result, "not exists");
        break;
      case 2:
        checkResult(result, "no permission");
        break;
      case 3:
        checkResult(result, "not exists");// 项目已删除
        break;
      case 4:
        checkResult(result, "400");
        break;
      case 5:
        checkResult(result, "success");
        break;
      case 6:
        checkResult(result, "400");
        break;
      case 7:
        checkResult(result, "400");
        break;
      case 8:
        checkResult(result, "400");
        break;
      case 9:
        checkResult(result, "500");
        break;
      default:
        break;
    }
  }

  /**
   * 验证结果
   */
  public void checkResult(String str, String code) {
    Map<String, Object> map = JacksonUtils.jsonToMap(str);
    assertNotEquals(null, map);
    assertTrue(code.equals(map.get("status")));
    logger.info("results=" + map.get("results"));
    logger.info("msg=" + map.get("msg"));
    logger.info("map=" + map);
  }

}

