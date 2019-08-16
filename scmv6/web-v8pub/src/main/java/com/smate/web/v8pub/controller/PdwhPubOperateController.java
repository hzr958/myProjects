package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.po.pdwh.PdwhPubStatisticsPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubCommentService;
import com.smate.web.v8pub.service.pdwh.PdwhPubLikeService;
import com.smate.web.v8pub.service.pdwh.PdwhPubQuoteService;
import com.smate.web.v8pub.service.pdwh.PdwhPubShareService;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.pdwh.PdwhPubViewService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.solr.SolrIndexDifService;
import com.smate.web.v8pub.vo.PubCommentVO;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;
import com.smate.web.v8pub.vo.PubQuoteVO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 基准库成果操作 控制类
 * 
 * @author yhx
 *
 */
@Controller
public class PdwhPubOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private RestTemplate restTemplate;

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
  private SolrIndexDifService solrIndexDifService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Resource
  private PdwhPubQuoteService pdwhPubQuoteService;

  /**
   * 基准库成果赞/取消赞 操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   * 
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxpdwhlike", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object pdwhLikeOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      if (psnId > 0 && checkPdwhPubId(pdwhPubId)) {
        pdwhPubOperateVO.setPsnId(psnId);
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
      logger.error("基准库成果赞操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }

    return map;
  }

  /**
   * 基准库成果评论操作 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   * 
   * @param pdwhPubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxpdwhcomment", method = RequestMethod.POST)
  @ResponseBody
  public String pdwhCommentOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
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
      logger.error("基准库成果评论操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 基准库成果分享回调 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"3"}
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxpdwhshare", method = RequestMethod.POST)
  @ResponseBody
  public String pdwhShareOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      if (!checkPdwhPubId(pdwhPubId)) {
        map.put("result", "error");
      } else {
        pdwhPubOperateVO.setPsnId(psnId);
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubShareService.pdwhShareOpt(pdwhPubOperateVO);
        int shareTimes = newPdwhPubStatisticsService.getShareCounts(pdwhPubId);
        map.put("result", "success");
        map.put("shareTimes", shareTimes);
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果分享回调操作出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 基准库成果查看
   * 
   * 请求参数{"des3PdwhPubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxviewpdwh", method = RequestMethod.POST)
  @ResponseBody
  public String pdwhViewOpt(PdwhPubOperateVO pdwhPubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pdwhPubId = pdwhPubOperateVO.getPdwhPubId();
    try {
      // 自己阅读自己的记录不予以保存
      if (psnId.equals(pdwhPubOperateVO.getReadPsnId())) {
        map.put("result", "error");
      } else {
        pdwhPubOperateVO.setPsnId(psnId);
        pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
        pdwhPubViewService.pdwhViewOpt(pdwhPubOperateVO);

        // 更新solr
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (pdwhPubId != null && pdwhPubId != 0L) {
          params.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId + ""));
          restTemplate.postForObject(domainscm + "/data/pub/sorlupdate", params, Map.class);
        }

        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("基准库成果查看增加记录出错,psnId=" + psnId + ",pdwhPubId=" + pdwhPubId, e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 基准库成果评论列表
   * 
   * @param pubCommentVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxcommentlistpdwh")
  public ModelAndView pdwhPubCommentList(PubCommentVO pubCommentVO) {
    ModelAndView view = new ModelAndView();
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId == 0l) {
        pubCommentVO.setIsLogin(false);
      } else {
        pubCommentVO.setIsLogin(true);
      }
      if (StringUtils.isNotBlank(pubCommentVO.getDes3PubId())) {
        pubCommentVO.setPsnId(psnId);
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubPdwhDetailService.getPdwhComment(pubCommentVO);
      }
    } catch (Exception e) {
      logger.error("获取基准库评论列表出错" + pubCommentVO.getPubId(), e);
    }
    map.put("isLogin", pubCommentVO.getIsLogin());
    map.put("page", pubCommentVO.getPage());
    view.addObject("map", map);
    view.setViewName("/pub/pubdetails/pdwh_pubComment");
    return view;
  }

  /**
   * 获取基准库成果操作统计数
   * 
   * @return
   */
  @RequestMapping("/pub/opt/ajaxstatistics")
  @ResponseBody
  public String findPdwhPubStatistics(PubCommentVO pubCommentVO) {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(pubCommentVO.getDes3PubId())) {
        PdwhPubStatisticsPO sta = newPdwhPubStatisticsService.get(pubCommentVO.getPubId());
        Long readCount = newPubStatisticsService.getSnsPubReadCounts(pubCommentVO.getPubId());
        if (sta != null) {
          data.put("result", "success");
          data.put("readCount", sta.getReadCount() == null ? 0 + readCount : sta.getReadCount() + readCount);
          data.put("citedCount", sta.getRefCount() == null ? 0 : sta.getRefCount());
        } else {
          data.put("result", "success");
          data.put("readCount", 0 + readCount);
          data.put("citedCount", 0);
        }
      } else {
        data.put("result", "pubId is null");
      }
    } catch (Exception e) {
      logger.error("获取基准库成果操作统计数出错, pubId = " + pubCommentVO.getPubId(), e);
      data.put("result", "error");
    }
    String result = JacksonUtils.jsonObjectSerializer(data);
    return result;
  }

  /**
   * 基准库获取评论数量
   */
  @RequestMapping("/pub/opt/ajaxcommentnumberpdwh")
  @ResponseBody
  public String ajaxGetPdwhCommentNumber(PubOperateVO pubOperateVO) {
    PubCommentVO pubCommentVO = new PubCommentVO();
    pubCommentVO.setDes3PubId(pubOperateVO.getDes3PubId());
    Long pubCommentsCount = null;
    try {
      pubCommentVO.setPsnId(SecurityUtils.getCurrentUserId());
      if (StringUtils.isNotBlank(pubCommentVO.getDes3PubId())) {
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubCommentsCount = pubPdwhDetailService.getPdwhCommentNumber(pubCommentVO.getPubId());
      }
    } catch (Exception e) {
      logger.error("获取评论数量失败" + pubCommentVO.getPubId(), e);
    }
    String result = JacksonUtils.jsonObjectSerializer(pubCommentsCount);
    return result;
  }

  /**
   * 查看pdwh相似全文信息
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxpdwhsimilarlist")
  public ModelAndView getSimilarList(PdwhPubOperateVO pdwhPubOperateVO) {
    ModelAndView view = new ModelAndView();
    try {
      List<PubFulltextSimilarVO> similarInfoList =
          pubFullTextService.getPdwhSimilarInfo(pdwhPubOperateVO.getPdwhPubId());
      view.addObject("similarInfoList", similarInfoList);
    } catch (Exception e) {
      logger.error("查看pdwh其他类似全文信息出错,pdwhPubId=" + pdwhPubOperateVO.getPdwhPubId(), e);
    }
    view.setViewName("/pub/pubdetails/pub_similar_fulltext_list");
    return view;
  }

  /**
   * 更新基准库成果的sorl信息
   * 
   * @param jsonData
   * @return
   */
  @RequestMapping(value = "/data/pub/sorlupdate", produces = "application/json;charset=UTF-8")
  @ResponseBody()
  public String updatePdwhPubSorl(@RequestBody String jsonData) {
    String result = "";
    Map<String, String> map = JacksonUtils.jsonMapUnSerializer(jsonData);
    String des3PdwhPubId = map.get("des3PubId");
    if (StringUtils.isNotBlank(des3PdwhPubId)) {
      Long pdwhPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PdwhPubId));
      String pubIndexUrl = map.get("pubIndexUrl");
      result = solrIndexDifService.updateSolr(pdwhPubId, pubIndexUrl);
    }
    return result;
  }

  /**
   * 检查基准库成果是否存在---可以同步进行校验超时
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxpdwhisexists")
  @ResponseBody()
  public String pdwhIsExists(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(des3PubId)) {
        Long pubId = null;
        if (StringUtils.isNumeric(des3PubId)) {
          pubId = NumberUtils.toLong(des3PubId);
        } else {
          pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
        }
        if (NumberUtils.isNotNullOrZero(pubId) && checkPdwhPubId(pubId)) {
          resultMap.put("result", "success");
        } else {
          resultMap.put("result", "error");
        }
      } else {
        resultMap.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("检查基准库成果是否存在,pubId= " + Des3Utils.decodeFromDes3(des3PubId), e);
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }

  /**
   * 检查基准库成果是否存在---不进行超时校验
   * 
   * @return
   */
  @RequestMapping(value = "/pub/outside/ajaxpdwhisexists")
  @ResponseBody()
  public String pdwhIsExists2(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(des3PubId)) {
        Long pubId = null;
        if (StringUtils.isNumeric(des3PubId)) {
          pubId = NumberUtils.toLong(des3PubId);
        } else {
          pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
        }
        if (NumberUtils.isNotNullOrZero(pubId) && checkPdwhPubId(pubId)) {
          resultMap.put("result", "success");
        } else {
          resultMap.put("result", "error");
        }
      } else {
        resultMap.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("检查基准库成果是否存在,pubId= " + Des3Utils.decodeFromDes3(des3PubId), e);
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }

  /**
   * 基准库成果引用
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxpdwhpubquote")
  public ModelAndView pdwhPubQuote(@ModelAttribute("des3PubId") String des3PubId) {
    ModelAndView view = new ModelAndView();
    try {
      if (StringUtils.isNotBlank(des3PubId)) {
        Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
        if (NumberUtils.isNotNullOrZero(pubId) && checkPdwhPubId(pubId)) {
          List<PubQuoteVO> result = pdwhPubQuoteService.findPubQuote(pubId);
          view.addObject("pubQuoteList", result);
        } else {
          view.addObject("result", "error");
        }
      } else {
        view.addObject("result", "error");
      }
    } catch (Exception e) {
      logger.error("基准库成果引用-出错,pubId= " + Des3Utils.decodeFromDes3(des3PubId), e);
    }
    view.setViewName("/pub/main/pub_quote");
    return view;
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
      logger.error("基准库成果不存在,pdwhPubId=" + pdwhPubId);
      return false;
    }
    return true;
  }

}
