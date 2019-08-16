package com.smate.web.mobile.controller.data.pub.pdwh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * 移动端成果认领控制器
 * 
 * @author aijiangbin
 * @date 2018年9月4日
 */
@RestController
public class APPPubConfirmWeChatController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private HttpServletRequest request;

  /**
   * 获取成果认领和全文认领记录数
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/mobile/ajaxmsgtips", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getTips() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    if (psnId != null && psnId != 0L) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
      Map<String, Object> object =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      Map<String, Object> object2 =
          (Map<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_CONFIRM_COUNT,
              JacksonUtils.mapToJsonStr(paramMap), Object.class);
      map.put("pubFulltextCount", object.get("pubFulltextCount"));
      map.put("pubConfirmCount", object2.get("pubConfirmCount"));
      resultmap.put("result", "success");
    } else {
      resultmap.put("errmsg", "psnId is null");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * 成果全文列表
   * 
   * @param pubOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/mobile/pubfulltextlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubFulltextList(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<String, Object>();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    HashMap<String, Object> result = new HashMap<>();
    Object object = null;
    Integer totalCount = 0;
    try {
      if (psnId != null && psnId != 0L) {
        params.put("psnId", psnId);
        result = (HashMap<String, Object>) restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_LIST,
            params, Map.class);
        String status = (String) result.get("status");
        if ("success".equalsIgnoreCase(status)) {
          object = result.get("list");
          totalCount = NumberUtils.toInt(result.get("totalCount").toString());
        }
        resultmap.put("result", status);
      } else {
        resultmap.put("errmsg", "psnId is null");
      }
    } catch (Exception e) {
      resultmap.put("result", "error");
      logger.error("mobile个人库全文列表异常,psnId=" + psnId, e);
    }
    return AppActionUtils.buildReturnInfo(object, totalCount,
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * 获取成果认领列表
   * 
   * @param pubOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/confirmpublist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubConfirmList(PubOperateVO pubOperateVO) {
    PubListVO pubListVO = new PubListVO();
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> resultmap = new HashMap<String, Object>();
    if (!NumberUtils.isNullOrZero(psnId)) {
      pubListVO.setHasLogin(1);
      pubListVO.setFromPage(pubOperateVO.getToBack());
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("des3SearchPsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
        params.put("serviceType", "pubConfirmList");
        params.put("isAll", 1);
        params.put("isPage", 1);
        Map<String, Object> object = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.PUB_LIST, params, Object.class);
        if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
          String dataJson = JacksonUtils.jsonObjectSerializerNoNull(object.get("resultList"));
          List<PubInfo> resultList = JacksonUtils.jsonToCollection(dataJson, List.class, PubInfo.class);
          pubListVO.setResultList(resultList);
        }
        pubListVO.setOther(false);
        resultmap.put("result", "success");
      } catch (Exception e) {
        logger.error("获取成果认领列表出错！", e);
        resultmap.put("result", "error");
      }
    } else {
      resultmap.put("errmsg", "psnId is null");
    }
    return AppActionUtils.buildReturnInfo(pubListVO.getResultList(), pubListVO.getTotalCount(),
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

  /**
   * 确认成果认领
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/opt/ajaxAffirmPubComfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxAffirmPubComfirm(@ModelAttribute("pubId") String pubId) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 调用接口进行成果认领业务
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 构建成果保存对象
        Map<String, Object> data = new HashMap<>();
        data.put("des3PubId", Des3Utils.encodeToDes3(pubId));
        data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
        Map<String, Object> res = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.CONFIRM_PUB_OPT, requestEntity, Object.class);
        map.put("result", res.get("status"));
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("APP确认成果认领出错，pubId = " + pubId + ", psnId = " + psnId, e);
      map.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 忽略成果认领
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/opt/ajaxIgnorePubComfirm", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxIgnorePubComfirm(@ModelAttribute("pubId") String pubId) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 调用接口进行成果认领业务
        // 设置请求头部
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 构建成果保存对象
        Map<String, Object> data = new HashMap<>();
        data.put("des3PubId", Des3Utils.encodeToDes3(pubId));
        data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())));
        HttpEntity<String> requestEntity =
            new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
        Map<String, Object> res = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.REJECT_PUB_OPT, requestEntity, Object.class);
        if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
          map.put("result", "success");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 全文认领，单个全文认领信息
   * 
   * @param pubOperateVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/pub/mobile/pubfulltextdetails", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubFulltextDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    // 判断是否登录
    Long operatePsnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (operatePsnId != null && operatePsnId != 0L) {
        pubOperateVO.setPsnId(operatePsnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_DETAILS, pubOperateVO, Map.class);
        String status;
        if (map.get("list") != null) {
          status = "success";
        } else {
          status = "havaDeleted";
        }
        resultmap.put("result", status);
      } else {
        resultmap.put("errmsg", "psnId is null");
      }
    } catch (Exception e) {
      resultmap.put("result", "error");
      logger.error("mobile个人库全文列表异常,psnId=" + operatePsnId, e);
    }
    return AppActionUtils.buildReturnInfo(resultmap, 0,
        AppActionUtils.changeResultStatus(Objects.toString(resultmap.get("result"), "error")),
        Objects.toString(resultmap.get("errmsg"), ""));
  }

}
