package com.smate.web.mobile.controller.data.pub.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.vo.PubCommentVO;
import com.smate.web.mobile.v8pub.vo.sns.PubFulltextReqVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @Author LIJUN
 * @Description //个人库成果操作
 * @Date 16:29 2018/8/14
 **/
@RestController("mSnsPubOperateController")
@RequestMapping("/data/pub/optsns")
public class SnsPubOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Page<?> page = new Page<Object>();
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * @return java.lang.String
   * @Author LIJUN
   * @Description // 个人库成果赞/取消赞 操作
   * @Date 20:57 2018/8/14
   * @Param [pubOperateVO]请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   **/
  @RequestMapping(value = "/ajaxlike", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object likeOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.LIKE_SNS, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile个人库成果赞操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile个人库成果赞操作,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(map, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 个人库成果评论 操作 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/ajaxcomment", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object commentOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L && StringUtils.isNotBlank(pubOperateVO.getContent())) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_SNS, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile个人库成果评论操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile个人库成果评论操作,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    return AppActionUtils.buildReturnInfo(Objects.toString(map.get("result"), "error"), 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 个人库成果分享回调 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/ajaxshare", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object shareOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L && pubOperateVO.getPlatform() != null) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.SHARE_CALLBACK_SNS, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile个人库成果分享回调操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile个人库成果分享回调操作,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("shareresult", Objects.toString(map.get("result"), "error"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 收藏或删除个人收藏的成果 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","pubDb":"1","collectOperate":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/ajaxcollect", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object dealCollectedPub(PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L && pubOperateVO.getPubDb() != null && pubOperateVO.getCollectOperate() != null) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COLLECT_PUB, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile收藏或删除个人收藏的成果操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile收藏或删除个人收藏的成果,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");

    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("result", Objects.toString(map.get("result"), "error"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 获取评论数量 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   */
  @RequestMapping(value = "/ajaxcommentnumber", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object ajaxGetCommentNumber(PubOperateVO pubOperateVO) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("des3PubId", pubOperateVO.getDes3PubId());

    Long pubId = pubOperateVO.getPubId();
    Map<String, Object> map = new HashMap<>();
    if (!NumberUtils.isNullOrZero(pubId) && !NumberUtils.isNullOrZero(psnId)) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENTNUMBER_SNS, params, Map.class);

      } catch (Exception e) {
        logger.error("mobile获取个人成果评论数量失败" + pubOperateVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile获取个人成果评论数量,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("pubCommentsCount", map.get("pubCommentsCount"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 个人库成果查看，增加阅读数
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/ajaxview", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object viewOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    Long readPsnId = pubOperateVO.getReadPsnId();
    if (psnId > 0L && pubId > 0L && readPsnId > 0L) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.VIEW_CALLBACK_SNS, pubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile个人库成果查看增加记录出错,psnId=" + psnId + ",pubId=" + pubId, e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 个人库成果评论列表
   *
   * @param des3PubId=uicwOqvCOzTqh%2BKiK9qwBg%3D%3D&pageNo=1&maxresult=10
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/ajaxcommentlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubCommentVO.getPubId();
    if (!NumberUtils.isNullOrZero(pubId) && !NumberUtils.isNullOrZero(psnId)) {
      try {
        pubCommentVO.setPsnId(psnId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(V8pubConst.DES3_PUB_ID, pubCommentVO.getDes3PubId());
        params.add("serviceType", "snsPub");
        params.add("psnId", psnId.toString());
        params.add("locale", "zh");
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_SNS,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("获取个人库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("isLogin", map.get("isLogin"));
    data.put("page", map.get("page"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("result"), "error")),
        Objects.toString(map.get("errmsg"), ""));
  }

  /**
   * 处理全文请求，只接收POST请求
   * 
   * @param dealStatus : 1==同意 2==忽略/拒绝 3==上传全文
   * @param msgId MsgRelation表的ID
   * @param pubId 成果id
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/fulltext/ajaxagree", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object agreePubFulltextRequest(PubFulltextReqVO reqVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = reqVO.getPubId();
    Integer operate = reqVO.getDealStatus();
    try {
      boolean flag = false; // 参数检测正确标志
      if (!NumberUtils.isNullOrZero(pubId) && !NumberUtils.isNullOrZero(reqVO.getMsgId()) && operate != null
          && NumberUtils.isNotZero(operate) && !NumberUtils.isNullOrZero(psnId)) {
        if (operate == 1 || operate == 2 || operate == 3) {
          flag = true;
        }
      }
      if (flag) {
        reqVO.setCurrentPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.DEAL_SNS_PUB_FULLTEXT_REQ, reqVO, Map.class);
      } else {
        map.put("msg", "参数校验不通过");
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("移动端处理成果全文请求出错， psnId = " + psnId + ", pubId = " + pubId + ", operate = " + operate, e);
      map.put("status", "error");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("result", map.get("status"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("msg"), ""));
  }

  /**
   * 添加全文请求
   * 
   * @param reqVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/fulltext/ajaxreqadd", produces = "application/json;charset=UTF-8")
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
        map.put("msg", "参数校验不通过");
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error(
          "移动端添加成果全文请求出错， psnId = " + psnId + ", pubId = " + reqVO.getPubId() + ", pubDB=" + reqVO.getPubType(), e);
      map.put("status", "error");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("reqId", map.get("reqId"));
    data.put("msgId", map.get("msgId"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("msg"), ""));
  }

  /**
   * 确认全文认领 是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/ajaxConfirmPubft", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String confirmPub(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    String ids = pubOperateVO.getIds();
    try {
      if (StringUtils.isNotBlank(ids) && currentPsnId != null && currentPsnId > 0L) {
        pubOperateVO.setDes3PsnId(Des3Utils.encodeToDes3(currentPsnId.toString()));
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.CONFIRM_PUB_FULLTEXT, pubOperateVO, Map.class);
      } else {
        map.put("result", "error");
        map.put("errMsg", "ids or psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端，全文认领操作，是这篇成果的全文操作出错，ids =" + ids + ", psnId = " + currentPsnId, e);
      map.put("status", "error");
    }

    Map<String, Object> data = new HashMap<String, Object>();
    data.put("result", map.get("status"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errMsg"), ""));
  }

  /**
   * 确认全文认领 不是这篇成果的全文
   * 
   * @param ids PUB_FULLTEXT_PSN_RCMD表id，多个逗号隔开
   * @param des3PsnId 当前操作者ID
   * @return
   */
  @RequestMapping(value = "/ajaxRejectPubft", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String rejectPubFulltext(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    String ids = pubOperateVO.getIds();
    try {
      pubOperateVO.setPsnId(currentPsnId);
      if (StringUtils.isNotBlank(ids) && currentPsnId != null && currentPsnId > 0L) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          for (Long id : idList) {
            // TODO 判断操作者和全文推荐的成果拥有者是否是同一个人
            map = restTemplate.postForObject(domainscm + PubApiInfoConsts.REJECT_PUB_FULLTEXT, pubOperateVO, Map.class);
          }
        }
      } else {
        map.put("result", "error");
        map.put("errMsg", "ids or psnId is null");
      }
    } catch (Exception e) {
      logger.error("移动端，全文认领操作，不是这篇成果的全文操作出错，ids =" + ids + ", psnId = " + currentPsnId, e);
      map.put("result", "error");
    }
    Map<String, Object> data = new HashMap<String, Object>();
    data.put("result", map.get("status"));
    return AppActionUtils.buildReturnInfo(data, 0,
        AppActionUtils.changeResultStatus(Objects.toString(map.get("status"), "error")),
        Objects.toString(map.get("errMsg"), ""));
  }

  /**
   * 获取个人库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/ajaxstatistics", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object findPdwhPubStatistics(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (!NumberUtils.isNullOrZero(pubId)) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.SNS_PUB_STATISTICS, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("获取个人库成果操作统计数出错, pubId = " + pubOperateVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
        map.put("result", "error");
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
   * 人员是否已赞过该基准库成果 {"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D", "des3PsnId": ""}
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/status", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getPubAwardAndCollectStatus(PubOperateVO pubOperateVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long pubId = pubOperateVO.getPubId();
      if (psnId != null && pubId != null && psnId > 0 && pubId > 0) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
        params.put("needLikeStatus", pubOperateVO.getNeedLikeStatus());
        params.put("needCollectStatus", pubOperateVO.getNeedCollectStatus());
        result = restTemplate.postForObject(domainscm + PubApiInfoConsts.OPT_STATUS_SNS, params, Map.class);
      } else {
        result.put("status", "error");
        result.put("errorMsg", "参数校验未通过");
      }
    } catch (Exception e) {
      logger.error("获取成果操作状态接口出错,PubId=" + pubOperateVO.getPubId(), e);
      result.put("status", "error");
    }
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")),
        Objects.toString(result.get("errorMsg"), ""));
  }

}
