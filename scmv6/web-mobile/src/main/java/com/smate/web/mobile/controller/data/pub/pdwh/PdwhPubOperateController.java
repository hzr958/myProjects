package com.smate.web.mobile.controller.data.pub.pdwh;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PubCommentVO;
import com.smate.web.mobile.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.mobile.v8pub.vo.pdwh.PubImportVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @Author LIJUN
 * @Description 基准库成果操作
 * @Date 17:57 2018/8/15
 **/
@RestController("mPdwhPubOperateController")
@RequestMapping("/data/pub/optpdwh")
public class PdwhPubOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Page<?> page = new Page<Object>();
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 基准库成果赞/取消赞 操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   *
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/ajaxlike", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pdwhLikeOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (psnId > 0L && pdwhPubId != null && pdwhPubId > 0L && pdwhPubOperateVO.getOperate() != null) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.LIKE_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果赞操作出错,psnId={},pdwhpubId={}", psnId, pdwhPubId, e);
      }
    } else {
      map.put("result", "error");
      logger.error("mobile基准库成果赞操作接口调用参数校验不通过,psnId={},pdwhpubId={}", psnId, pdwhPubId);
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 基准库成果评论操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   *
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/ajaxcomment", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pdwhCommentOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (psnId != null && psnId > 0L && pdwhPubId != null && pdwhPubId > 0L
        && StringUtils.isNotBlank(pdwhPubOperateVO.getContent())) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果评论操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("result", "error");
      logger.error("mobile基准库成果评论操作,参数校验不通过,psnId={},pdwhpubId={},content={}", psnId, pdwhPubId,
          pdwhPubOperateVO.getContent());
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(Objects.toString(map.get("result"), "error"), 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 基准库成果分享回调 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"3"}
   *
   * @return
   */
  @RequestMapping(value = "/ajaxshare", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pdwhShareOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    Integer platform = pdwhPubOperateVO.getPlatform();
    if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(pdwhPubId)
        && StringUtils.isNotBlank(pdwhPubOperateVO.getComment()) && NumberUtils.isNotNullOrZero(platform)) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.SHARE_CALLBACK_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("基准库成果分享回调操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("result", "error");
      logger.error("mobile基准库成果分享回调操作,参数校验不通过,psnId={},pdwhpubId={},comment={}", psnId, pdwhPubId,
          pdwhPubOperateVO.getComment());
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("shareresult", Objects.toString(map.get("result"), "error"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 基准库成果查看,增加阅读数
   * <p>
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/ajaxview", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pdwhViewOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    Long readPsnId = pdwhPubOperateVO.getReadPsnId();
    if (psnId > 0L && NumberUtils.isNotNullOrZero(pdwhPubId) && NumberUtils.isNotNullOrZero(readPsnId)) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.VIEW_CALLBACK_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果查看增加记录出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("result", "error");
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    return AppActionUtils.buildReturnInfo(data, 0, Objects.toString(map.get("result"), "error"),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 收藏或删除个人收藏的成果 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","pubDb":"0","collectOperate":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/ajaxcollect", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object dealCollectedPub(PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && NumberUtils.isNotNullOrZero(pubId) && pubOperateVO.getPubDb() != null
        && pubOperateVO.getCollectOperate() != null) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COLLECT_PUB, pubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("mobile收藏或删除pdwhpub收藏的成果操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("result", "error");
      logger.error("mobile收藏或删除pdwhpub收藏的成果,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("result", Objects.toString(map.get("result"), "error"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 基准库成果评论列表
   *
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * @return
   */
  @RequestMapping(value = "/ajaxcommentlist", produces = "application/json;charset=UTF-8")

  @ResponseBody
  public Object pdwhPubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pubCommentVO.getPubId();
    if (!NumberUtils.isNullOrZero(psnId) && !NumberUtils.isNullOrZero(pdwhPubId)) {
      try {
        pubCommentVO.setPsnId(psnId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(V8pubConst.DES3_PUB_ID, pubCommentVO.getDes3PubId());
        params.add("serviceType", "pdwhPub");
        params.add("psnId", psnId.toString());
        params.add("page.pageSize", pubCommentVO.getPage().getPageSize().toString());
        params.add("page.pageNo", pubCommentVO.getPage().getPageNo().toString());
        params.add("page.ignoreMin", Boolean.TRUE.toString());
        params.add("locale", "zh_CN");
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_PDWH,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if (map.get("result") == null) {
          map.put("result", "success");
        }
      } catch (Exception e) {
        map.put("result", "error");
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取基准库评论列表出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("result", "error");
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> page = (Map<String, Object>) map.get("page");
    return AppActionUtils.buildReturnInfo(page != null ? page.get("result") : null, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 获取基准库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/ajaxstatistics", produces = "application/json;charset=UTF-8")

  @ResponseBody
  public Object findPdwhPubStatistics(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (!NumberUtils.isNullOrZero(pdwhPubId)) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.STATISTICS_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("获取基准库成果操作统计数出错, pubId = " + pdwhPubOperateVO.getPdwhPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("result", "error");
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 基准库获取评论数量
   */
  @RequestMapping(value = "/ajaxcommentnumber", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxGetPdwhCommentNumber(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pubCommentVO.getPubId();
    if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(pdwhPubId)) {
      try {
        pubCommentVO.setPsnId(psnId);
        pubCommentVO.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENTNUMBER_PDWH, pubCommentVO, Map.class);
      } catch (Exception e) {
        map.put("result", "error");
        logger.error("获取评论数量失败, pubId = " + pubCommentVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("result", "error");
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("pubCommentsCount", map.get("pubCommentsCount"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  @RequestMapping(value = "/ajaximport", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object importPdwhPubToMe(PubImportVO pubImportVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (checkParamsBiggerThanZero(psnId) && StringUtils.isNoneBlank(pubImportVO.getDes3PubId())) {
        Map<String, Object> jsonData = new HashMap<String, Object>();
        jsonData.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        jsonData.put("des3PubId", pubImportVO.getDes3PubId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(jsonData), headers);
        String result = restTemplate.postForObject(domainscm + PubApiInfoConsts.IMPORT_PDWH_PUB, entity, String.class);
        if ("SUCCESS".equals(JacksonUtils.jsonToMap(result).get("status").toString())) {
          if ("dup".equals(JacksonUtils.jsonToMap(result).get("msg").toString())) {
            map.put("result", "dup");
          } else {
            map.put("result", "success");
          }
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
        map.put("msg", "参数校验不通过");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("移动端导入基准库成果到我的成果库失败， pubId = " + pubImportVO.getPubId(), e);
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("msg"), ""));
  }

  /**
   * 人员是否已赞过该基准库成果 {"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D", "des3PsnId": ""}
   * 
   * @return
   */
  @RequestMapping(value = "/status", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getPdwhPubAwardAndCollectStatus(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
      if (psnId != null && pdwhPubId != null && psnId > 0 && pdwhPubId > 0) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.put("des3PdwhPubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
        params.put("needLikeStatus", pdwhPubOperateVO.getNeedLikeStatus());
        params.put("needCollectStatus", pdwhPubOperateVO.getNeedCollectStatus());
        result = restTemplate.postForObject(domainscm + PubApiInfoConsts.OPT_STATUS_PDWH, params, Map.class);
      } else {
        result.put("status", "error");
        result.put("errorMsg", "参数校验不通过");
      }
    } catch (Exception e) {
      logger.error("获取成果操作状态接口出错,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
      result.put("status", "error");
    }
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")),
        Objects.toString(result.get("errorMsg"), ""));
  }

  public Page<?> getPage() {
    return page;
  }

  public void setPage(Page<?> page) {
    this.page = page;
  }

  protected boolean checkParamsBiggerThanZero(Long param) {
    if (param != null && param > 0L) {
      return true;
    }
    return false;
  }
}
