package com.smate.web.mobile.controller.web.pub;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.consts.V8pubConst;
import com.smate.web.mobile.v8pub.dto.PubQueryDTO;
import com.smate.web.mobile.v8pub.vo.PubCommentVO;
import com.smate.web.mobile.v8pub.vo.PubListVO;
import com.smate.web.mobile.v8pub.vo.PubQueryModel;
import com.smate.web.mobile.v8pub.vo.pdwh.PubImportVO;
import com.smate.web.mobile.v8pub.vo.sns.PubFulltextReqVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @Author LIJUN
 * @Description //个人库成果操作
 * @Date 16:29 2018/8/14
 **/
@RestController("mWebSnsPubOperateController")
public class SnsPubOperateController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Page<?> page = new Page<Object>();
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * @return java.lang.String
   * @Author LIJUN
   * @Description // 个人库成果赞/取消赞 操作
   * @Date 20:57 2018/8/14
   * @Param [pubOperateVO]请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   **/
  @RequestMapping(value = "/pub/optsns/ajaxlike", method = RequestMethod.POST)
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
    return map;
  }

  /**
   * 个人库成果评论 操作 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/optsns/ajaxcomment", method = RequestMethod.POST)
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
    return map;
  }

  /**
   * 个人库成果分享回调 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/pub/optsns/ajaxshare", method = RequestMethod.POST)
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
    return map;
  }

  /**
   * 收藏或删除个人收藏的成果 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","pubDb":"1","collectOperate":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/pub/optsns/ajaxcollect", method = RequestMethod.POST)
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
    return map;
  }

  /**
   * 获取评论数量 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   */
  @RequestMapping(value = "/pub/optsns/ajaxcommentnumber", method = RequestMethod.POST)
  public Object ajaxGetCommentNumber(PubCommentVO pubCommentVO) {
    Integer pubCommentsCount = null;
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubCommentVO.getPubId();
    Map<String, Object> map = new HashMap<>();
    if (pubId > 0L && psnId > 0L) {
      try {
        pubCommentVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENTNUMBER_SNS, pubCommentVO, Map.class);

      } catch (Exception e) {
        logger.error("mobile获取个人成果评论数量失败" + pubCommentVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile获取个人成果评论数量,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }

    return map;
  }

  /**
   * 个人库成果查看，增加阅读数
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/outside/ajaxsnsview", method = RequestMethod.POST)
  public Object viewOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    Long readPsnId = pubOperateVO.getReadPsnId();
    if (pubId > 0L && readPsnId > 0L) {
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
    return map;
  }

  /**
   * 个人库成果评论列表
   *
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/optsns/ajaxcommentlist")
  public Object pubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubCommentVO.getPubId();
    if (psnId > 0L && pubId > 0L) {
      try {
        pubCommentVO.setPsnId(psnId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(V8pubConst.DES3_PUB_ID, pubCommentVO.getDes3PubId());
        params.add("serviceType", "snsPub");
        params.add("psnId", psnId.toString());
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_SNS,
            RestUtils.buildPostRequestEntity(params), Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("获取个人库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 处理全文请求，只接收POST请求
   * 
   * @param dealStatus : 1==同意 2==忽略/拒绝 3==上传全文
   * @param msgId MsgRelation表的ID
   * @param pubId 成果id
   */
  @SuppressWarnings("unchecked")
  @RequestMapping("/pub/fulltext/ajaxagree")
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
        map.put("errmsg", "参数校验不通过");
        map.put("status", "error");
      }

    } catch (Exception e) {
      logger.error("移动端处理成果全文请求出错， psnId = " + psnId + ", pubId = " + pubId + ", operate = " + operate, e);
      map.put("status", "error");
    }
    return map;
  }

  /**
   * 添加全文请求
   * 
   * @param reqVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/fulltext/ajaxreqadd")
  @ResponseBody
  public Object addPubFulltextRequest(PubFulltextReqVO reqVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    reqVO.setPubType(StringUtils.lowerCase(reqVO.getPubType()));
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (checkFulltextRequestParam(reqVO)) {
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
    return map;
  }

  private boolean checkFulltextRequestParam(PubFulltextReqVO reqVO) {
    boolean pubIdNull = StringUtils.isNotBlank(reqVO.getDes3PubId());
    boolean isPdwh = "pdwh".equalsIgnoreCase(reqVO.getPubType());
    boolean isSns = "sns".equalsIgnoreCase(reqVO.getPubType()) && !NumberUtils.isNullOrZero(reqVO.getRecvPsnId());
    return pubIdNull && (isPdwh || isSns);
  }

  /**
   * 基准库导入成果到我的成果库
   * 
   * @param pubImportVO
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping("/pub/optsns/ajaximport")
  public Object importPdwhPubToMe(PubImportVO pubImportVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (!NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(pubImportVO.getPubJsonParams())) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currentPsnId", psnId);
        params.put("articleType", 1);
        params.put("pubJsonParams", pubImportVO.getPubJsonParams());
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.IMPORT_SNS_PUB, params, Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端导入个人库成果到我的成果库失败， pubJsonParams = " + pubImportVO.getPubJsonParams(), e);
    }
    return map;
  }

  /**
   * 获取个人库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/optsns/ajaxstatistics", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  public Object findPdwhPubStatistics(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long pubId = pubOperateVO.getPubId();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (!NumberUtils.isNullOrZero(pubId)) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.SNS_PUB_STATISTICS, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("获取个人库成果操作统计数出错, pubId = " + pubOperateVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * @return java.lang.Object
   * @Author WSN
   * @Description 查询单个成果信息
   * @Date 14:19 2018/8/17
   * @Param [des3SearchPsnId 成果列表拥有人的id, des3PubId 成果ID, pubDB 个人库（SNS）还是基准库(PDWH)成果]
   **/
  @RequestMapping(value = "/pub/query/single")
  @SuppressWarnings("unchecked")
  public Object querySnsPubListByPubId(PubQueryModel pubQueryModel) {
    Map<String, Object> resultmap = new HashMap<String, Object>();
    List<Map<String, Object>> resultList = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    // 要查询的成果ID
    Long searchPubId = pubQueryDTO.getSearchPubId();
    // 成果所属库，基准库还是个人库
    String pubDB = pubQueryModel.getPubDB();
    if (NumberUtils.isNullOrZero(pubQueryDTO.getPsnId())) {
      pubQueryDTO.setPsnId(SecurityUtils.getCurrentUserId());
    }
    PubListVO pubListVO = new PubListVO();
    if (searchPubId != null && searchPubId > 0L && StringUtils.isNotBlank(pubDB)) {
      try {
        pubQueryDTO.setServiceType("pubQueryByPubId");
        if ("PDWH".equals(pubDB)) {
          pubQueryDTO.setServiceType("pdwhPubQueryByPubId");
        }
        pubListVO.setPubQueryDTO(pubQueryDTO);
        Map<String, Object> object = (Map<String, Object>) restTemplate
            .postForObject(domainscm + PubApiInfoConsts.PUB_LIST, pubListVO.getPubQueryDTO(), Object.class);
        if (object != null && object.get("status").equals(V8pubConst.SUCCESS)) {
          resultList = (List<Map<String, Object>>) object.get("resultList");
          List<PubInfo> list = new ArrayList<>(1);
          pubListVO.setResultList(list);
          if (resultList != null && resultList.size() > 0) {
            Map<String, Object> map = resultList.get(0);
            PubInfo pubInfo = new PubInfo();
            list.add(pubInfo);
            try {
              BeanUtils.populate(pubInfo, map);
            } catch (IllegalAccessException | InvocationTargetException e) {
              logger.error("复制属性异常", e);
            }
          }
        }
        resultmap.put("result", "success");
        resultmap.put("pubListVO", pubListVO);
      } catch (Exception e) {
        resultmap.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取查询单个成果数据出错,pubId=" + searchPubId + ", pubDB = " + pubDB, e);
      }
    } else {
      resultmap.put("result", "error");
      resultmap.put("errmsg", "参数校验不通过");
    }
    return resultmap;
  }

  /**
   * 查看个人库成果权限
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping(value = {"/pub/fulltext/getpermission", "/pub/fulltext/ajaxpermission"})
  public String getPubPermission(PubQueryModel pubQueryModel) {
    Map<String, Object> map = new HashMap<>();
    try {
      String des3SearchPubId = pubQueryModel.getDes3SearchPubId();
      String des3FileId = pubQueryModel.getDes3FileId();
      Long psnId = SecurityUtils.getCurrentUserId();
      map.put("des3PsnId", Des3Utils.encodeToDes3(Objects.toString(psnId)));
      if (des3SearchPubId != null && des3FileId != null) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("des3PubId", des3SearchPubId);
        params.put("des3FileId", des3FileId);
        params.put("reqPsnId", String.valueOf(SecurityUtils.getCurrentUserId()));
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.PUB_FULLTEXT_PERMIS, params, Map.class);
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取个人库成果全文权限信息出错,des3PubId=" + pubQueryModel.getDes3SearchPubId(), e);
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }

  /**
   * 更改个人库成果权限
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping(value = "/pub/optsns/updatepermission", method = RequestMethod.POST)
  public Object updatePermissionOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.UPDATE_PERMISSION, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile个人库成果更改权限操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile个人库成果更改权限操作,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 移动端获取个人库成果是否删除
   * 
   * @param des3PubId
   * @return
   */
  @RequestMapping(value = "/pub/optsns/checkPub", produces = "application/json; charset=utf-8")
  @ResponseBody
  public Object snsPubIsExist(String des3PubId) {
    Map<String, String> result = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (StringUtils.isNotEmpty(des3PubId) && NumberUtils.isNotNullOrZero(psnId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3PubId", des3PubId);
        Map<String, Object> queryResult = restTemplate.postForObject(domainscm + "/data/pub/ajaxCheckPub",
            RestUtils.buildPostRequestEntity(params), Map.class);
        boolean isSuccess = Objects.nonNull(queryResult) && Objects.nonNull(queryResult.get("status"))
            && !"error".equals(queryResult.get("status").toString()) ? true : false;
        if (isSuccess) {
          result.put("result", queryResult.get("status").toString());
          result.put("status", "success");
          result.put("msg", "get data success");
        } else {
          result.put("status", "error");
          result.put("msg", "query fail");
        }
      } catch (Exception e) {
        logger.error("远程调用接口查询个人库成果是否存在出错,des3PubId={}", des3PubId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param varify fail");
    }
    return JacksonUtils.mapToJsonStr(result);
  }
}
