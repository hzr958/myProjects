package com.smate.web.dyn.action.msg.mobile;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
import com.smate.test.utils.JsonStringUtils;
import com.smate.test.utils.JunitPropertiesUtils;

/**
 * app消息操作测试类
 * 
 * @author xiexing
 * @date 2019年1月30日
 */
public class AppOptMsgActionTest {
  private static final Logger logger = LoggerFactory.getLogger(AppOptMsgActionTest.class);
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
      proMap = JunitPropertiesUtils.loadProperties("src/test/java/com/smate/web/dyn/action/msg/mobile/properties/"
          + runEnv + "_test_AppMsgShowActionTest.properties");
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

  // 该接口暂未使用,不测试
  // @ParameterizedTest
  // @ValueSource(strings = {"1", "2", "3", "4", "5"})
  // @DisplayName("测试成果分享给好友(暂未用)")
  // void testAjaxSendPubShareToFriend(Integer number) {
  // MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
  // switch (number) {
  // case 1:
  // params.add("des3ReceiverIds", "gdC9pv0cs%2BsTnYniXfvtHQ%3D%3D,gdC9pv0cs%2BuWemwIlCNLCw%3D%3D");
  // params.add("des3PubIds", "uicwOqvCOzQmkcift1WYoQ%3D%3D,uicwOqvCOzSYogfWMfeI%2Bg%3D%3D");
  // params.add("smateInsideLetterType", "text");
  // params.add("content", "这是测试内容");
  // break;
  // case 2:
  // params.add("des3ReceiverIds", "");
  // params.add("des3PubIds", "uicwOqvCOzQmkcift1WYoQ%3D%3D,uicwOqvCOzSYogfWMfeI%2Bg%3D%3D");
  // params.add("smateInsideLetterType", "text");
  // params.add("content", "这是测试内容");
  // break;
  // case 3:
  // params.add("des3ReceiverIds", "gdC9pv0cs%2BsTnYniXfvtHQ%3D%3D,gdC9pv0cs%2BuWemwIlCNLCw%3D%3D");
  // params.add("smateInsideLetterType", "text");
  // params.add("content", "这是测试内容");
  // break;
  // case 4:
  // params.add("des3ReceiverIds",
  // "gdC9pv0cs%2BsTnYniXfvtHQ%3D%3D,gdC9pv0cs%2BuWemwIlCNLCw%3D%3D,2VbmIdkIerJi4B8gp3FNGQ%3D%3D");
  // params.add("des3PubIds", "uicwOqvCOzQmkcift1WYoQ%3D%3D,uicwOqvCOzSYogfWMfeI%2Bg%3D%3D");
  // params.add("smateInsideLetterType", "");
  // params.add("content", "这是测试内容3");
  // break;
  // case 5:
  // params.add("des3ReceiverIds", "gdC9pv0cs%2BsTnYniXfvtHQ%3D%3D,gdC9pv0cs%2BuWemwIlCNLCw%3D%3D");
  // params.add("des3PubIds", "uicwOqvCOzQmkcift1WYoQ%3D%3D,uicwOqvCOzSYogfWMfeI%2Bg%3D%3D");
  // params.add("smateInsideLetterType", "text");
  // params.add("content", "");
  // break;
  // default:
  // break;
  // }
  // requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
  // String str = restTemplate.postForObject(junit_domainMobile +
  // "/app/dynweb/showmsg/ajaxsendpubsharetofriend",
  // requestEntity, String.class);
  // switch (number) {
  // case 1:
  // checkResult(str, "200");
  // break;
  // case 2:
  // checkResult(str, "400");
  // break;
  // case 3:
  // checkResult(str, "400");
  // break;
  // case 4:
  // checkResult(str, "200");
  // break;
  // case 5:
  // checkResult(str, "200");
  // break;
  // default:
  // break;
  // }
  // }

  // 测试发文本、文件、项目、成果消息，请求全文以及请求添加好友是调用另外的接口
  @ParameterizedTest
  @ValueSource(strings = {"1", "2", "3", "4"})
  @DisplayName("测试发送消息")
  void testAjaxSendMsg(Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    switch (number) {
      case 1:
        params.add("receiverIds", "1000000733234");
        params.add("msgType", "7");
        params.add("content", "这是新测试内容");
        params.add("smateInsideLetterType", "text");
        break;
      case 2:
        params.add("receiverIds", "1000000733234");
        params.add("msgType", "7");
        params.add("des3PrjId", "JvUzHyT7%2BGIS8qujGx3I0Q%3D%3D");
        params.add("smateInsideLetterType", "prj");
        break;
      case 3:
        params.add("receiverIds", "1000000733234");
        params.add("msgType", "7");
        params.add("fileId", "1000000018762");
        params.add("belongPerson", "true");
        params.add("smateInsideLetterType", "file");
        break;
      case 4:
        params.add("receiverIds", "1000000733234");
        params.add("msgType", "7");
        params.add("pubId", "1000009308721");
        params.add("belongPerson", "true");
        params.add("smateInsideLetterType", "pub");
        break;
      default:
        break;
    }
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxsendmsg", requestEntity, String.class);
    checkResult(str, "200");
  }


  @ParameterizedTest
  @CsvSource({"1000000733356,1", ",2"})
  @DisplayName("测试创建会话")
  void testCreateMsgChat(String receiverId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("receiverId", receiverId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str =
        restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxcreatemsgchat", requestEntity, String.class);
    switch (number) {
      case 1:
        checkResult(str, "200");
        break;
      case 2:
        checkResult(str, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"1289,1", ",2"})
  @DisplayName("测试删除站内信会话")
  void testDelMsgChatRelation(String msgChatRelationId, Integer number) {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("msgChatRelationId", msgChatRelationId);
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String str = restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxdelmsgchatrelation", requestEntity,
        String.class);
    switch (number) {
      case 1:
        checkResult(str, "200");
        break;
      case 2:
        checkResult(str, "500");
        break;
      default:
        break;
    }
  }

  @ParameterizedTest
  @CsvSource({"1000001002929,1", "1000001002929,2", "1000001002929,3"})
  @DisplayName("测试全文请求接受/忽略")
  void testAjaxOptFulltextRequest(String pubId, String dealStatus) throws Exception {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    /**
     * 发起全文请求(利用320416069@qq.com发起请求,token为PElOYurSZIbZBNrvykCc2y5JfIJ0aNUJ77Ye1w%2BjI50%3D)
     */
    token = "PElOYurSZIbZBNrvykCc2y5JfIJ0aNUJ77Ye1w%2BjI50%3D";
    headers.remove("token");
    headers.add("token", token);
    params.add("des3RecvPsnId", "2VbmIdkIerKTnTPa9Mt0HQ%3D%3D");
    params.add("des3PubId", "uicwOqvCOzSYogfWMfeI%2Bg%3D%3D");
    params.add("pubType", "sns");
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    Map<String, Object> map =
        restTemplate.postForObject(junit_domainMobile + "/data/pub/fulltext/ajaxreqadd", requestEntity, Map.class);
    logger.info("请求结果,map={}", map);
    assertNotEquals(null, map);
    assertNotEquals(null, map.get("results"));
    /**
     * 所有参数已经校验是否为空的情况，无需再测试参数为空的情况,1==同意 2==忽略/拒绝 3==上传全文
     */
    token = "%2Bq%2BZkxTSdouLH3ol%2BmiTjIC8MvV%2B8uog5P7Clc9x9RE%3D";
    headers.remove("token");
    headers.add("token", token);
    params.add("pubId", pubId);
    params.add("dealStatus", dealStatus);
    params.add("msgRelationId", JsonStringUtils.getTargetValue(map.get("results").toString(), "msgId"));
    requestEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
    String strDeal = restTemplate.postForObject(junit_domainMobile + "/app/dynweb/ajaxoptfulltextrequest",
        requestEntity, String.class);
    checkResult(strDeal, "200");
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

