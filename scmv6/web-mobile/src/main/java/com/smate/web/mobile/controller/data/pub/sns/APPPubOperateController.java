package com.smate.web.mobile.controller.data.pub.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.vo.sns.PubFulltextReqVO;

@RestController
public class APPPubOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private HttpServletRequest request;

  /**
   * 添加全文请求
   * 
   * @param reqVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/fulltext/ajaxreqadd", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object addPubFulltextRequest(PubFulltextReqVO reqVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    boolean pubIdNull = StringUtils.isBlank(reqVO.getDes3PubId());
    boolean pubTypeError = !("sns".equals(reqVO.getPubType()) || "pdwh".equals(reqVO.getPubType()));
    boolean SNSPubNoReceiver = "sns".equals(reqVO.getPubType()) && NumberUtils.isNullOrZero(reqVO.getRecvPsnId());
    Integer operate = reqVO.getDealStatus();
    try {
      boolean flag = true; // 参数检测正确标志
      if (pubIdNull || pubTypeError || SNSPubNoReceiver) {
        flag = false;
      }
      if (flag) {
        reqVO.setCurrentPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.ADD_SNS_PUB_FULLTEXT_REQ, reqVO, Map.class);
      } else {
        map.put("errmsg", "参数校验不通过");
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error(
          "移动端添加成果全文请求出错， psnId = " + psnId + ", pubId = " + reqVO.getPubId() + ", pubDB=" + reqVO.getPubType(), e);
      map.put("status", "error");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  @RequestMapping(value = "/data/pub/optsns/ajaximport", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object importSnsPubToMe(PubFulltextReqVO reqVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0 && reqVO.getPubId() != null) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> pubJsonParams = new HashMap<String, String>();
        List<Map<String, String>> pubJsonList = new ArrayList<>();
        pubJsonParams.put("des3PubId", reqVO.getDes3PubId());
        pubJsonList.add(pubJsonParams);
        params.put("currentPsnId", psnId);
        params.put("articleType", 1);
        params.put("pubJsonParams", JacksonUtils.listToJsonStr(pubJsonList));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(params), headers);

        String result = restTemplate.postForObject(domainscm + PubApiInfoConsts.IMPORT_SNS_PUB, entity, String.class);
        String msg = Objects.toString(JacksonUtils.jsonToMap(result).get("result"));
        if ("success".equals(msg)) {
          map.put("result", "success");
        } else if ("dup".equals(msg)) {
          map.put("result", "dup");
        } else if ("isDel".equals(msg)) {
          map.put("result", "isDel");
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("msg", "参数校验不通过");
        map.put("status", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("移动端导入基准库成果到我的成果库失败， pubJsonParams = " + reqVO.getPubId(), e);
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("msg"), ""));
  }
}
