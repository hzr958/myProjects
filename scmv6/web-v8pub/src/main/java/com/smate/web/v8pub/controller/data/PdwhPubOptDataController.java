package com.smate.web.v8pub.controller.data;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubCommentService;
import com.smate.web.v8pub.service.pdwh.PdwhPubLikeService;
import com.smate.web.v8pub.service.pdwh.PdwhPubShareService;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.pdwh.PdwhPubViewService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.searchimport.ImportOtherPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.solr.SolrIndexDifService;
import com.smate.web.v8pub.vo.PubCommentVO;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 基准库成果操作数据接口控制器 所有数据接口，请求返回参数都为json格式
 * 
 * @author LIJUN
 * @date 2018年8月16日
 */

@RestController
public class PdwhPubOptDataController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubLikeService pdwhPubLikeService;
  @Autowired
  private PdwhPubCommentService pdwhPubCommentService;
  @Autowired
  private PdwhPubShareService pdwhPubShareService;
  @Autowired
  private PdwhPubViewService pdwhPubViewService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private ImportOtherPubService importOtherPubService;
  @Autowired
  private SolrIndexDifService solrIndexDifService;

  /**
   * 判断基准库成果是否存在 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * 
   * @param pdwhPubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/ajaxpdwhisexists")
  public Object pdwhIsExists(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      PubPdwhPO pdwhPub = pubPdwhService.get(pdwhPubId);
      if (pdwhPub == null) {
        map.put("result", "error");
        map.put("errmsg", "not exists");
      } else if (pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
        map.put("result", "error");
        map.put("errmsg", "is deleted");
      } else {
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果赞操作数据接口出错,pdwhPubId=" + pdwhPubId, e);
    }
    return map;
  }

  /**
   * 基准库成果赞/取消赞 操作
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******","operate":"1"}
   * 
   * @param pdwhPubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/pdwhlike")
  public Object pdwhLikeOpt(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pdwhPubOperateVO.getPsnId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      if (psnId > 0L && checkPdwhPubId(pdwhPubId)) {
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubLikeService.pdwhLikeOpt(pdwhPubOperateVO);
        int awardTimes = newPdwhPubStatisticsService.getAwardCounts(pdwhPubId);
        if (pdwhPubOperateVO.getOperate() == 1) {
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
      logger.error("基准库成果赞操作数据接口出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }

    return map;
  }

  /**
   * 基准库成果评论操作
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******","content":"评论内容"}
   * 
   * @param pdwhPubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/pdwhcomment")
  public Object pdwhCommentOpt(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pdwhPubOperateVO.getPsnId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      String content = pdwhPubOperateVO.getContent();
      // 判断psnId,评论内容
      if (!checkParams(psnId, content) || !checkPdwhPubId(pdwhPubId)) {
        map.put("result", "error");
      } else {
        pdwhPubOperateVO.setPsnId(psnId);
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubCommentService.pdwhCommentOpt(pdwhPubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果评论操作数据接口出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }
    return map;
  }

  /**
   * 基准库成果分享回调
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******","comment":"分享评论","platform":"3"}
   * 
   * @return
   */
  @PostMapping(value = "/data/pub/opt/pdwhshare")
  public Object pdwhShareOpt(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pdwhPubOperateVO.getPsnId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      if (!checkPdwhPubId(pdwhPubId)) {
        map.put("result", "error");
      } else {
        pdwhPubOperateVO.setPsnId(psnId);
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubShareService.pdwhShareOpt(pdwhPubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果分享回调操作数据接口出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }
    return map;
  }

  /**
   * 基准库成果查看
   * 
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   * 
   * @param pubOperateVO
   * @return
   */
  @PostMapping(value = "/data/pub/opt/pdwhview")
  public Object pdwhViewOpt(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = pdwhPubOperateVO.getPsnId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      // 自己阅读自己的记录不予以保存
      if (psnId.equals(pdwhPubOperateVO.getReadPsnId())) {
        map.put("result", "error");
      } else if (!checkPdwhPubId(pdwhPubId)) {
        map.put("result", "error");
      } else {
        pdwhPubOperateVO.setPsnId(psnId);
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubViewService.pdwhViewOpt(pdwhPubOperateVO);
        // 更新solr
        if (pdwhPubId != null && pdwhPubId != 0L) {
          PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailService.getByPubId(pdwhPubId);
          PubPdwhPO pubPdwh = pubPdwhService.get(pdwhPubId);
          // 判断是否为专利
          if (PublicationTypeEnum.isPatent(pubPdwhDetailDOM.getPubType())) {
            solrIndexDifService.indexPatent(pubPdwhDetailDOM, null, pubPdwh.getPublishYear(), pubPdwh.getPublishMonth(),
                pubPdwh.getPublishDay());
          } else {
            solrIndexDifService.indexPublication(pubPdwhDetailDOM, null, pubPdwh.getPublishYear(),
                pubPdwh.getPublishMonth(), pubPdwh.getPublishDay());
          }
        }
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果查看增加记录数据接口出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }

    return map;
  }

  /**
   * 基准库成果评论列表 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******"}
   * 
   * @param pubCommentVO
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/commentpdwhlist")
  public Object pdwhPubCommentList(PubCommentVO pubCommentVO) {
    Map<String, Object> map = new HashMap<>();
    Long psnId = pubCommentVO.getPsnId();
    try {
      if (psnId == 0L) {
        pubCommentVO.setIsLogin(false);
      } else {
        pubCommentVO.setIsLogin(true);
      }
      LocaleContextHolder.setLocale(Locale.CHINA);
      if (pubCommentVO.getDes3PubId() != null && checkPdwhPubId(pubCommentVO.getPubId())) {
        pubCommentVO.setPsnId(psnId);
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubPdwhDetailService.getPdwhComment(pubCommentVO);
      } else {
        logger.error("获取基准库评论列表数据接口出错,pub_id不存在:" + pubCommentVO.getPubId());
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("获取基准库评论列表数据接口出错" + pubCommentVO.getPubId(), e);
    }
    map.put("isLogin", pubCommentVO.getIsLogin());
    map.put("page", pubCommentVO.getPage());

    return map;
  }

  /**
   * 获取基准库成果操作统计数 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * 
   * @return
   */
  @PostMapping(value = "/data/pub/pdwhstatistics")
  public Object findPdwhPubStatistics(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      Long psnId = pdwhPubOperateVO.getPsnId();
      if (StringUtils.isNotBlank(pdwhPubOperateVO.getDes3PdwhPubId())) {
        boolean hasAward = false;
        boolean hasCollected = false;
        Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
        PdwhPubStatisticsPO sta = newPdwhPubStatisticsService.get(pdwhPubId);
        Long readCount = newPubStatisticsService.getSnsPubReadCounts(pdwhPubId);
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
          hasAward = pdwhPubLikeService.checkHasAwardPdwhPub(pdwhPubOperateVO);
          hasCollected = pubPdwhService.hasCollectedPdwhPub(psnId, pdwhPubId);
        }
        data.put("hasAward", hasAward);
        data.put("hasCollected", hasCollected);
      } else {
        data.put("result", "error");
        data.put("errmsg", "pubId is null");
      }
    } catch (Exception e) {
      logger.error("获取基准库成果操作统计数数据接口出错, pubId = " + pdwhPubOperateVO.getPdwhPubId(), e);
      data.put("result", "error");
    }
    return data;
  }

  /**
   * 基准库获取评论数量 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3PsnId":"******"}
   */
  @RequestMapping("/data/pub/opt/pdwhcommentnumber")
  public Object getPdwhCommentNumber(@RequestBody PubCommentVO pubCommentVO) {
    Map<String, Long> map = new HashMap<>();
    Long pubCommentsCount = null;
    try {
      if (pubCommentVO.getDes3PubId() != null) {
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubCommentsCount = pubPdwhDetailService.getPdwhCommentNumber(pubCommentVO.getPubId());
      }
    } catch (Exception e) {
      logger.error("获取评论数量数据接口出错" + pubCommentVO.getPubId(), e);
    }
    map.put("pubCommentsCount", pubCommentsCount);
    return map;
  }

  /**
   * 查看pdwh相似全文信息 {"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D"}
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/opt/pdwhsimilarlist")
  public Object getSimilarList(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    List<PubFulltextSimilarVO> similarInfoList = null;
    try {
      similarInfoList = pubFullTextService.getPdwhSimilarInfo(pdwhPubOperateVO.getPdwhPubId());
    } catch (Exception e) {
      logger.error("查看pdwh其他类似全文信息数据接口出错,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
    }

    return similarInfoList;
  }

  /**
   * 人员是否已赞过或收藏过该基准库成果 {"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D", "des3PsnId": ""}
   * 
   * @return
   */
  @RequestMapping(value = "/data/pub/pdwh/optstatus")

  public Object hasAwardedPdwhPub(@RequestBody PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> result = new HashMap<String, Object>();
    boolean hasAward = false;
    boolean hasCollected = false;
    try {
      Long psnId = pdwhPubOperateVO.getPsnId();
      Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
      if (psnId != null && pdwhPubId != null && psnId > 0 && pdwhPubId > 0 && checkPdwhPubId(pdwhPubId)) {
        if (pdwhPubOperateVO.getNeedLikeStatus()) {
          hasAward = pdwhPubLikeService.checkHasAwardPdwhPub(pdwhPubOperateVO);
        }
        if (pdwhPubOperateVO.getNeedCollectStatus()) {
          hasCollected = pubPdwhService.hasCollectedPdwhPub(psnId, pdwhPubId);
        }
        result.put("status", "success");
      } else {
        result.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("获取成果操作状态接口出错,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
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

  // 校验基准库成果是否存在
  public boolean checkPdwhPubId(Long pdwhPubId) {
    PubPdwhPO pdwhPub = pubPdwhService.get(pdwhPubId);
    if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      logger.error("基准库成果不存在或已被删除,pdwhPubId=" + pdwhPubId);
      return false;
    }
    return true;
  }

}
