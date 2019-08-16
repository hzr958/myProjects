package com.smate.web.v8pub.controller.data;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.PubStatisticsPO;
import com.smate.web.v8pub.service.searchimport.ImportOtherPubService;
import com.smate.web.v8pub.service.sns.CollectedPubService;
import com.smate.web.v8pub.service.sns.PubCommentService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubLikeService;
import com.smate.web.v8pub.service.sns.PubShareService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.sns.PubViewService;
import com.smate.web.v8pub.service.sns.fulltextpsnrcmd.PubFulltextPsnRcmdService;
import com.smate.web.v8pub.vo.PubCommentVO;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 个人库成果操作数据接口 Controller
 * 
 * @author LIJUN
 * @date 2018年8月16日
 */
@RestController
public class SnsPubOptDataController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubLikeService pubLikeService;
  @Autowired
  private PubCommentService pubCommentService;
  @Autowired
  private PubShareService pubShareService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubViewService pubViewService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private CollectedPubService collectedPubService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;
  @Autowired
  private ImportOtherPubService importOtherPubService;

  /**
   * 个人库成果赞/取消赞 操作
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","operate":"1"}
   * 
   * @param pubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/like")
  public Object likeOpt(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (psnId > 0 && checkPubId(pubId)) {
        pubOperateVO.setPubId(pubId);
        pubLikeService.likeOpt(pubOperateVO);
        int awardTimes = newPubStatisticsService.getAwardCounts(pubId);
        if (pubOperateVO.getOperate() == 1) {
          map.put("action", 0);
        } else {
          map.put("action", 1);
        }
        map.put("awardTimes", awardTimes);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果赞操作数据接口出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }

  /**
   * 个人库成果评论 操作
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","content":"评论内容"}
   * 
   * @param pubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/comment")
  public Object commentOpt(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      String content = pubOperateVO.getContent();
      // 判断psnId,评论内容及成果是否存在
      if (!checkParams(psnId, content) || !checkPubId(pubId)) {
        map.put("result", "error");
      } else {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubCommentService.commentOpt(pubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果评论操作数据接口出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }

  /**
   * 个人库成果分享回调
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","comment":"分享评论","platform":"3"}
   * 
   * @return
   */
  @PostMapping(value = "/data/pub/opt/share")
  public Object shareOpt(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (!checkPubId(pubId)) {
        map.put("result", "error");
      } else {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubShareService.shareOpt(pubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果分享回调操作数据接口出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }

  /**
   * 个人库成果查看
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   * 
   * @param pubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/view")
  public Object viewOpt(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      // 自己阅读自己的记录不予以保存
      if (psnId.equals(pubOperateVO.getReadPsnId())) {
        map.put("result", "error");
      } else {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubViewService.viewOpt(pubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果查看增加记录出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }

  /**
   * 个人库成果评论列表
   * 
   * @param pubCommentVO{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****"}
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/commentlist")
  public Object pubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = pubCommentVO.getPsnId();
    try {
      if (psnId == 0L) {
        pubCommentVO.setIsLogin(false);
      } else {
        pubCommentVO.setIsLogin(true);
      }
      /*
       * if (StringUtils.isNotBlank(pubCommentVO.getLocale())) { LocaleContextHolder.setLocale(new
       * Locale(pubCommentVO.getLocale())); }
       */
      // SCM-22969 生产机，移动端成果详情页评论的人名都是英文的
      // 移动端目前只支持中文
      LocaleContextHolder.setLocale(Locale.CHINA);
      if (pubCommentVO.getDes3PubId() != null) {
        pubCommentVO.setPsnId(psnId);
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubCommentVO.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(pubCommentVO.getPsnId())));
        pubSnsDetailService.getPubComment(pubCommentVO);
        map.put("isLogin", pubCommentVO.getIsLogin());
        map.put("page", pubCommentVO.getPage());
      }
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("获取个人库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
    }
    return map;
  }

  /**
   * 收藏或删除个人收藏的成果 {"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****"}
   * 
   * @return
   */
  @PostMapping(value = "/data/pub/opt/Collect")
  public Object dealCollectedPub(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    String pubDB = pubOperateVO.getPubDb();
    Integer collectOpt = pubOperateVO.getCollectOperate();
    try {
      if (NumberUtils.isNotNullOrZero(psnId)) {
        pubOperateVO.setPubId(pubId);
        if (StringUtils.isNotEmpty(pubDB) && collectOpt != null && NumberUtils.isNotNullOrZero(pubId)) {
          map = collectedPubService.dealCollectedPub(pubOperateVO);
        } else {
          logger.error("收藏或删除收藏论文出错");
          map.put("result", "error");
          map.put("errmsg", "参数校验不通过");
        }
      } else {
        logger.error("收藏或删除收藏论文出错");
        map.put("result", "error");
        map.put("errmsg", "psnId不能为空");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("收藏或删除收藏个人库成果出错, operatePsnId = " + psnId + ", pubId = " + pubId + ", pubDB = " + pubDB
          + ", operate = " + collectOpt, e);
    }
    return map;
  }

  /**
   * 获取评论数量 {"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****"}
   */
  @RequestMapping("/data/pub/opt/commentnumber")
  public Object getCommentNumber(PubCommentVO pubCommentVO) {
    Map<String, Long> map = new HashMap<>();
    Long pubCommentsCount = null;
    try {
      if (pubCommentVO.getDes3PubId() != null) {
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubCommentsCount = pubSnsDetailService.getCommentNumber(pubCommentVO.getPubId());
      }
    } catch (Exception e) {
      logger.error("获取评论数量失败" + pubCommentVO.getPubId(), e);
    }
    map.put("pubCommentsCount", pubCommentsCount);
    return map;
  }

  /**
   * 查看相似全文信息 {"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/similarlist")
  public Object getSimilarList(@RequestBody PubOperateVO pubOperateVO) {
    List<PubFulltextSimilarVO> similarInfoList = null;
    try {
      similarInfoList = pubFullTextService.getSimilarInfo(pubOperateVO.getPubId());
    } catch (Exception e) {
      logger.error("查看其他类似全文信息数据接口出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    return similarInfoList;
  }

  /**
   * 成果详情页面-全文认领
   * {"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","ownerDes3PsnId":"****"}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/getpubdetailrcmdpubft")
  public Object getPubDetailRcmdPubft(@RequestBody PubOperateVO pubOperateVO) {
    try {
      String ownerDes3PsnId = pubOperateVO.getOwnerDes3PsnId();
      Long ownerPsnId = Long.valueOf(Des3Utils.decodeFromDes3(ownerDes3PsnId));
      if (pubOperateVO.getPsnId() != null && pubOperateVO.getPsnId().equals(ownerPsnId)
          && pubOperateVO.getPubId() != null) {
        pubFulltextPsnRcmdService.getPubRcmdFulltext(pubOperateVO);
        if (pubOperateVO.getPubRcmdft() != null) {
          return pubOperateVO.getPubRcmdft();
        }
      }
    } catch (Exception e) {
      logger.error("成果详情页面-全文认领-数据接口出错,psnId= " + pubOperateVO.getPsnId(), e);
    }
    return null;
  }

  /**
   * 获取基准库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * 
   * @return
   */
  @PostMapping(value = "/data/pub/sns/statistics")
  public Object findSNSPubStatistics(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(pubOperateVO.getDes3PubId())) {
        boolean hasAward = false;
        boolean hasCollected = false;
        Long pubId = pubOperateVO.getPubId();
        Long psnId = pubOperateVO.getPsnId();
        PubStatisticsPO sta = newPubStatisticsService.get(pubId);
        Long readCount = newPubStatisticsService.getSnsPubReadCounts(pubId);
        if (sta != null) {
          data.put("result", "success");
          data.put("readCount", sta.getReadCount() == null ? 0 + readCount : sta.getReadCount() + readCount);
          data.put("citedCount", sta.getRefCount() == null ? 0 : sta.getRefCount());
          data.put("commentCount", sta.getCommentCount() == null ? 0 : sta.getCommentCount());
          data.put("awardCount", sta.getAwardCount() == null ? 0 : sta.getAwardCount());
          data.put("shareCount", sta.getShareCount() == null ? 0 : sta.getShareCount());
        } else {
          data.put("result", "success");
          data.put("readCount", 0 + readCount);
          data.put("citedCount", 0);
          data.put("commentCount", 0);
          data.put("awardCount", 0);
          data.put("shareCount", 0);
        }
        if (!NumberUtils.isNullOrZero(psnId)) {
          hasAward = pubLikeService.checkHasAwardPub(psnId, pubId);
          hasCollected = collectedPubService.hasCollectedPub(psnId, pubId, PubDbEnum.SNS);
        }
        data.put("hasAward", hasAward);
        data.put("hasCollected", hasCollected);
        PubFullTextPO fulltext = pubFullTextService.get(pubId);
        if (fulltext != null) {
          data.put("hasfulltext", 1);
          Integer permission =
              pubFullTextService.getFullTextPermission(pubId, fulltext.getFileId(), psnId != null ? psnId : 0);
          data.put("fullTextPermission", permission);
        } else {
          data.put("hasfulltext", 0);
        }
      } else {
        data.put("result", "error");
        data.put("errmsg", "pubId is null");
      }
    } catch (Exception e) {
      logger.error("获取个人库成果操作统计数数据接口出错, pubId = " + pubOperateVO.getPubId(), e);
      data.put("result", "error");
    }
    return data;
  }

  /**
   * 人员是否已赞过或收藏过该基准库成果 {"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D", "des3PsnId": ""}
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/sns/optstatus")
  public Object hasAwardedPdwhPub(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    boolean hasAward = false;
    boolean hasCollected = false;
    try {
      Long psnId = pubOperateVO.getPsnId();
      Long pubId = pubOperateVO.getPubId();
      if (psnId != null && pubId != null && psnId > 0 && pubId > 0) {
        if (pubOperateVO.getNeedLikeStatus()) {
          hasAward = pubLikeService.checkHasAwardPub(psnId, pubId);
        }
        if (pubOperateVO.getNeedCollectStatus()) {
          hasCollected = collectedPubService.hasCollectedPub(psnId, pubId, PubDbEnum.SNS);
        }
      }
      result.put("status", "success");
    } catch (Exception e) {
      logger.error("获取成果操作状态接口出错,PubId=" + pubOperateVO.getPubId(), e);
      result.put("status", "error");
    }
    result.put("hasAward", hasAward);
    result.put("hasCollected", hasCollected);
    return result;
  }

  public boolean checkParams(Long psnId, String content) {
    if (psnId == 0 || StringUtils.isBlank(content)) {
      logger.error("评论内容为空或用户未登录,psnId=" + psnId + ",content=" + content);
      return false;
    }
    return true;
  }

  /*
   * 校验成果是否存在
   */
  public boolean checkPubId(Long pubId) {
    PubSnsPO pub = pubSnsService.get(pubId);
    if (pub == null) {
      logger.error("成果不存在,pubId=" + pubId);
      return false;
    }
    return true;
  }

  /**
   * 查询成果拥有者id 参数pubId
   *
   * @param params
   * @return
   */
  @RequestMapping(value = "/data/pub/query/pubdes3psnId")
  public Object queryPubOwnerPsnId(@RequestBody PubOperateVO pubOperateVOs) {
    try {
      String des3PubId = pubOperateVOs.getDes3PubId();
      Long pubId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3PubId));
      if (pubId != null) {
        Long psnId = pubSnsDetailService.getPubOwnerPsnId(pubId);
        return Des3Utils.encodeToDes3(Objects.toString(psnId));
      }
    } catch (Exception e) {
      logger.error("查询成果详情 异常", e);
    }

    return null;
  }

  @RequestMapping(value = "/data/pub/psnhasprivatepub")
  @ResponseBody()
  public Object psnHasPrivatePub(String des3PsnId) {
    Map<String, Object> result = new HashMap<String, Object>();
    Boolean psnHasPrivatePub = false;
    if (StringUtils.isNotBlank(des3PsnId)) {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
      if (psnId > 0) {
        psnHasPrivatePub = pubSnsDetailService.getPsnHasPrivatePub(psnId);
        result.put("hasPrivatePub", psnHasPrivatePub);
      }
    }
    return result;
  }

  /**
   * 个人库成果更改权限操作 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"*****","operate":"1"}
   * 
   * @param pubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/permission")
  public Object updatePermissionOpt(@RequestBody PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pubOperateVO.getPsnId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && checkPubId(pubId)) {
        String result = pubSnsDetailService.updatePubPermission(psnId, pubId);
        map.put("result", result);
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果更改权限操作数据接口出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }
}
