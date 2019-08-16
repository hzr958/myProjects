package com.smate.web.mobile.controller.web.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.vo.PubCommentVO;
import com.smate.web.mobile.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.mobile.v8pub.vo.pdwh.PubImportVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/**
 * @Author LIJUN
 * @Description 基准库成果操作
 * @Date 17:57 2018/8/15
 **/
@RestController("mWebPdwhPubOperateController")
public class PdwhPubOperateController extends WeChatBaseController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private Page<?> page = new Page<Object>();
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 基准库成果是否存在请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   *
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxpdwhisexists", method = RequestMethod.POST)
  public Object pdwhIsExists(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    String des3PdwhPubId = pdwhPubOperateVO.getDes3PdwhPubId();
    if (StringUtils.isNotBlank(des3PdwhPubId)) {
      try {
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.EXISTS_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile判断基准库成果是否存在出错,des3PdwhPubId={}", des3PdwhPubId, e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 基准库成果赞/取消赞 操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   *
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxlike", method = RequestMethod.POST)
  public Object pdwhLikeOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (psnId > 0L && pdwhPubId != null && pdwhPubId > 0L && pdwhPubOperateVO.getOperate() != null) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.LIKE_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果赞操作出错,psnId={},pdwhpubId={}", psnId, pdwhPubId, e);
      }
    } else {
      logger.error("mobile基准库成果赞操作接口调用参数校验不通过,psnId={},pdwhpubId={}", psnId, pdwhPubId);
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 基准库成果评论操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   *
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxcomment", method = RequestMethod.POST)
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
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果评论操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      logger.error("mobile基准库成果评论操作,参数校验不通过,psnId={},pdwhpubId={},content={}", psnId, pdwhPubId,
          pdwhPubOperateVO.getContent());
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 基准库成果分享回调 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"3"}
   *
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxshare", method = RequestMethod.POST)
  public Object pdwhShareOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (psnId > 0L && pdwhPubId > 0L && StringUtils.isNotBlank(pdwhPubOperateVO.getComment())) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.SHARE_CALLBACK_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("基准库成果分享回调操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      logger.error("mobile基准库成果分享回调操作,参数校验不通过,psnId={},pdwhpubId={},comment={}", psnId, pdwhPubId,
          pdwhPubOperateVO.getComment());
      map.put("errmsg", "参数校验不通过");
    }

    return map;
  }

  /**
   * 基准库成果查看,增加阅读数
   * <p>
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   *
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/outside/ajaxpdwhview", method = RequestMethod.POST)
  public Object pdwhViewOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    if (pdwhPubId > 0L) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.VIEW_CALLBACK_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile基准库成果查看增加记录出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 收藏或删除个人收藏的成果 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","pubDb":"0","collectOperate":"1"}
   *
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxcollect", method = RequestMethod.POST)
  public Object dealCollectedPub(PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (psnId > 0L && pubId > 0L && pubOperateVO.getPubDb() != null && pubOperateVO.getCollectOperate() != null) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COLLECT_PUB, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile收藏或删除pdwhpub收藏的成果操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobile收藏或删除pdwhpub收藏的成果,参数校验不通过,psnId={},pubId={}", psnId, pubId);
      map.put("errmsg", "参数校验不通过");

    }
    return map;
  }

  /**
   * 基准库成果评论列表
   *
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * @return
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxcommentlist", method = RequestMethod.POST)
  public Object pdwhPubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pubCommentVO.getPubId();
    if (psnId > 0L && pdwhPubId > 0L) {
      try {
        pubCommentVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENT_LIST_PDWH, pubCommentVO, Map.class);
      } catch (Exception e) {
        map.put("errmsg", "调用远程数据接口异常");
        logger.error("mobile获取基准库评论列表出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 获取基准库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/pub/optpdwh/ajaxstatistics", "/pub/outside/optpdwh/ajaxstatistics"},
      method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  public Object findPdwhPubStatistics(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (!NumberUtils.isNullOrZero(pdwhPubId)) {
      try {
        pdwhPubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.STATISTICS_PDWH, pdwhPubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("获取基准库成果操作统计数出错, pubId = " + pdwhPubOperateVO.getPdwhPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 基准库获取评论数量
   */
  @RequestMapping(value = "/pub/optpdwh/ajaxcommentnumber", method = RequestMethod.POST)
  public Object ajaxGetPdwhCommentNumber(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pubCommentVO.getPubId();
    if (psnId > 0L && pdwhPubId > 0L) {
      try {
        pubCommentVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.COMMENTNUMBER_PDWH, pubCommentVO, Map.class);
      } catch (Exception e) {
        logger.error("获取评论数量失败, pubId = " + pubCommentVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

  /**
   * 基准库导入成果到我的成果库
   * 
   * @param pubImportVO
   * @return
   */
  @RequestMapping("/pub/optpdwh/ajaximport")
  public Object importPdwhPubToMe(PubImportVO pubImportVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (checkParamsBiggerThanZero(psnId) && StringUtils.isNotBlank(pubImportVO.getPubJsonParams())) {
        List<Map<String, String>> paramList = JacksonUtils.jsonListUnSerializer(pubImportVO.getPubJsonParams());
        for (Map<String, String> param : paramList) {
          Map<String, Object> params = new HashMap<String, Object>();
          params.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
          params.put("des3PubId", Des3Utils.encodeToDes3(param.get("pubId").toString()));
          String result =
              restTemplate.postForObject(domainscm + PubApiInfoConsts.IMPORT_PDWH_PUB, params, String.class);
          if ("SUCCESS".equalsIgnoreCase(JacksonUtils.jsonToMap(result).get("status").toString())) {
            if ("dup".equalsIgnoreCase(JacksonUtils.jsonToMap(result).get("msg").toString())) {
              map.put("result", "dup");
            } else {
              map.put("result", "success");
            }
          } else {
            map.put("result", "error");
          }
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("移动端导入基准库成果到我的成果库失败， pubJsonParams = " + pubImportVO.getPubJsonParams(), e);
    }
    return map;
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
