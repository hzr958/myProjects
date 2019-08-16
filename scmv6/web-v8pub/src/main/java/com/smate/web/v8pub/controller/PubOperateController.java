package com.smate.web.v8pub.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.sns.CollectedPubService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubCommentService;
import com.smate.web.v8pub.service.sns.PubDeleteService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubLikeService;
import com.smate.web.v8pub.service.sns.PubQuoteService;
import com.smate.web.v8pub.service.sns.PubShareService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.sns.PubViewService;
import com.smate.web.v8pub.service.sns.fulltextpsnrcmd.PubFulltextPsnRcmdService;
import com.smate.web.v8pub.service.sns.indexurl.PubIndexUrlService;
import com.smate.web.v8pub.vo.PubCommentVO;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;
import com.smate.web.v8pub.vo.PubQuoteVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 个人库成果操作 控制类
 * 
 * @author yhx
 *
 */
@RestController
public class PubOperateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  private Page<?> page = new Page<>();
  @Autowired
  private PubLikeService pubLikeService;
  @Autowired
  private PubCommentService pubCommentService;
  @Autowired
  private PubShareService pubShareService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PsnPubService psnPubService;
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
  @Resource
  private PubQuoteService pubQuoteService;
  @Resource
  private PubDeleteService pubDeleteService;
  @Resource
  private PubPdwhService pubPdwhService;
  @Resource
  private PubAssignLogService pubAssignLogService;
  @Autowired
  private PubIndexUrlService pubIndexUrlService;
  @Autowired
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Value("${domainscm}")
  private String domain;

  /**
   * 个人库成果删除操作
   * 
   */
  @RequestMapping(value = "/pub/opt/delete", produces = "application/json;charset=UTF-8")
  public String deleteOpt(@ModelAttribute("des3PubIds") String des3PubIds) {
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    Map<String, Object> map = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(des3PubIds) && SecurityUtils.getCurrentUserId() != null) {
      String[] tmpPubIds = StringUtils.split(des3PubIds, ",");
      int deleteCount = 0;
      for (String des3PubId : tmpPubIds) {
        Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
        PubSnsPO pubSns = pubSnsService.get(pubId);
        Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubId);
        if (pubSns == null || !pubOwnerPsnId.equals(SecurityUtils.getCurrentUserId())) {
          if (pubSns == null) {
            map.put("result", "exist");
            map.put("msg", iszhCN ? "成果不存在" : "The publication is not exists.");
          } else {
            map.put("result", "warn");
            map.put("msg", iszhCN ? "权限不足" : "You are not eligible to delete publication.");
          }
        } else {
          String result = pubDeleteService.deletePub(des3PubId, pubOwnerPsnId);
          String status = JacksonUtils.jsonToMap(result).get("status").toString();
          if ("SUCCESS".equals(status)) {
            deleteCount += 1;
          }
        }
      }
      if (deleteCount > 0) {
        map.put("result", "success");
        map.put("count", deleteCount);
        map.put("msg", iszhCN ? "删除操作成功" : "Deleted successfully.");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "操作失败" : "Operated failed");
      }
    } else {
      map.put("result", "error");
      map.put("msg", iszhCN ? "操作失败" : "Operated failed");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 个人库成果赞/取消赞 操作 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","operate":"1"}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxlike", method = RequestMethod.POST)
  public String likeOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    if (pubId == null) {
      pubId = Long.parseLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId()));
    }
    try {
      if (psnId > 0 && snsPubIsExist(pubId)) {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubLikeService.likeOpt(pubOperateVO);
        int awardTimes = newPubStatisticsService.getAwardCounts(pubId);
        map.put("awardTimes", awardTimes);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果赞操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 个人库成果评论 操作 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","content":"评论内容"}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxcomment", method = RequestMethod.POST)
  public String commentOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    try {
      String content = pubOperateVO.getContent();
      // 判断psnId,评论内容及成果是否存在
      if (!checkParams(psnId, content) || !snsPubIsExist(pubId)) {
        map.put("result", "error");
      } else {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubCommentService.commentOpt(pubOperateVO);
        map.put("result", "success");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果评论操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 个人库成果分享回调 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","comment":"分享评论","platform":"3"}
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxshare", method = RequestMethod.POST)
  public Object shareOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (!snsPubIsExist(pubId)) {
        map.put("result", "error");
      } else {
        pubOperateVO.setPsnId(psnId);
        pubOperateVO.setPubId(pubId);
        pubShareService.shareOpt(pubOperateVO);
        int shareTimes = newPubStatisticsService.getShareCounts(pubId);
        map.put("result", "success");
        map.put("shareTimes", shareTimes);
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("个人库成果分享回调操作出错,psnId=" + psnId + ",pubId=" + pubId, e);
    }
    return map;
  }

  /**
   * 个人库成果查看
   * 请求参数{"des3PubId":"JvUzHyT7%2BGJLpMS85Izpow%3D%3D","des3ReadPsnId":"gdC9pv0cs%2BvIFzRJNrcXAA%3D%3D"}
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxview", method = RequestMethod.POST)
  public String viewOpt(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
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
    String result = JacksonUtils.jsonObjectSerializer(map);
    return result;
  }

  /**
   * 个人库成果评论列表
   * 
   * @param pubCommentVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxcommentlist")
  public ModelAndView pubCommentList(PubCommentVO pubCommentVO) {
    ModelAndView view = new ModelAndView();
    Map<String, Object> map = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (pubCommentVO.getPage().getParamPageNo() > 1) {
        // 同步分页数据
        pubCommentVO.setPageNo(pubCommentVO.getPage().getParamPageNo());
      }
      if (psnId == 0l) {
        pubCommentVO.setIsLogin(false);
      } else {
        pubCommentVO.setIsLogin(true);
      }
      if (StringUtils.isNotBlank(pubCommentVO.getDes3PubId())) {
        pubCommentVO.setPsnId(psnId);
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubSnsDetailService.getPubComment(pubCommentVO);
      }
    } catch (Exception e) {
      logger.error("获取个人库评论列表出错" + pubCommentVO.getPubId(), e);
    }
    map.put("isLogin", pubCommentVO.getIsLogin());
    map.put("page", pubCommentVO.getPage());
    view.addObject("map", map);
    view.setViewName("/pub/pubdetails/pub_comment");
    return view;
  }

  /**
   * 收藏或删除个人收藏的成果
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxCollect", method = RequestMethod.POST)
  public Object dealCollectedPub(PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long getPubId = pubOperateVO.getPubId();
    Long pubId = getPubId == 0 ? Long.parseLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3PubId())) : getPubId;
    boolean isPdwh = pubOperateVO.getPubDb().equalsIgnoreCase("PDWH");
    if (isPdwh && !pdwhPubIsExist(pubId)) {
      // 校验基准库成果
      logger.error("论文不存在");
      map.put("result", "not_exists");
      return map;
    }
    if (!isPdwh && !snsPubIsExist(pubId)) {
      // 校验个人库结果
      logger.error("论文不存在");
      map.put("result", "not_exists");
      return map;
    }
    pubOperateVO.setPsnId(psnId);
    pubOperateVO.setPubId(pubId);
    if (StringUtils.isNotEmpty(pubOperateVO.getPubDb()) && pubOperateVO.getCollectOperate() != null) {
      map = collectedPubService.dealCollectedPub(pubOperateVO);
    } else {
      logger.error("删除收藏论文出错");
      map.put("result", "error");
    }
    return map;
  }

  /**
   * 获取评论数量
   */
  @RequestMapping(value = "/pub/opt/ajaxcommentnumber", method = RequestMethod.POST)
  public String ajaxGetCommentNumber(PubOperateVO pubOperateVO) {
    PubCommentVO pubCommentVO = new PubCommentVO();
    pubCommentVO.setDes3PubId(pubOperateVO.getDes3PubId());
    Long pubCommentsCount = null;
    try {
      pubCommentVO.setPsnId(SecurityUtils.getCurrentUserId());
      if (StringUtils.isNotBlank(pubCommentVO.getDes3PubId())) {
        pubCommentVO.setPubId(pubCommentVO.getPubId());
        pubCommentsCount = pubSnsDetailService.getCommentNumber(pubCommentVO.getPubId());
      }
    } catch (Exception e) {
      logger.error("获取评论数量失败" + pubCommentVO.getPubId(), e);
    }
    String result = JacksonUtils.jsonObjectSerializer(pubCommentsCount);
    return result;
  }

  /**
   * 查看相似全文信息
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxsimilarlist")
  public ModelAndView getSimilarList(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    try {
      List<PubFulltextSimilarVO> similarInfoList = pubFullTextService.getSimilarInfo(pubOperateVO.getPubId());
      view.addObject("similarInfoList", similarInfoList);
    } catch (Exception e) {
      logger.error("查看其他类似全文信息出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("/pub/pubdetails/pub_similar_fulltext_list");
    return view;
  }

  /**
   * 首页-全文认领模块
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping("/pub/opt/ajaxgetrcmdpubfulltext2")
  public ModelAndView getRcmdPubFulltext2(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      pubOperateVO.setPsnId(psnId);
      page = pubFulltextPsnRcmdService.getRcmdFulltext(pubOperateVO, page);
      if (pubOperateVO.getIsAll() == 0) {
        Long confirmCount = pubAssignLogService.queryPubConfirmCount(psnId);
        view.addObject("confirmCount", confirmCount);
      }
      if (CollectionUtils.isEmpty(page.getResult())) {
        if (pubOperateVO.getIsAll() == 0) {
          view.addObject("randomModule", "no");
        }
      }
      view.addObject("page", page);
    } catch (Exception e) {
      logger.error("首页-全文认领模块-出错,psnId= " + psnId, e);
    }
    if (pubOperateVO.getIsAll() == 1) {
      view.setViewName("/pub/pubfulltext/home_pub_fulltext_list");
      return view;
    }
    view.setViewName("/pub/pubfulltext/home_pub_fulltext");
    return view;
  }

  /**
   * 个人主页-成果模块-全文认领(本人才能查看)
   * 
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxgetrcmdpubfulltext")
  public ModelAndView getRcmdPubFulltext(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    try {
      if (pubOperateVO.getPsnId().equals(SecurityUtils.getCurrentUserId())) {
        page = pubFulltextPsnRcmdService.getRcmdFulltext(pubOperateVO, page);
        Long confirmCount = pubAssignLogService.queryPubConfirmCount(pubOperateVO.getPsnId());
        view.addObject("confirmCount", confirmCount);
      }
    } catch (Exception e) {
      logger.error("个人主页-成果模块-全文认领-出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject("page", page);
    if (pubOperateVO.getIsAll() == 1) {
      view.setViewName("/pub/pubfulltext/pub_rcmd_fulltext_list");
      return view;
    }
    view.setViewName("/pub/pubfulltext/pub_rcmd_fulltext");
    return view;
  }

  /**
   * 成果详情页面-全文认领
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxgetpubdetailrcmdpubft")
  public ModelAndView getPubDetailRcmdPubft(PubOperateVO pubOperateVO) {
    ModelAndView view = new ModelAndView();
    try {
      String ownerDes3PsnId = pubOperateVO.getOwnerDes3PsnId();
      if (StringUtils.isNotBlank(ownerDes3PsnId)) {
        pubOperateVO.setPsnId(Long.valueOf(Des3Utils.decodeFromDes3(ownerDes3PsnId)));
        if (pubOperateVO.getPsnId() != null && pubOperateVO.getPsnId().equals(SecurityUtils.getCurrentUserId())
            && pubOperateVO.getPubId() != null) {
          pubFulltextPsnRcmdService.getPubRcmdFulltext(pubOperateVO);
          if (pubOperateVO.getPubRcmdft() != null) {
            view.addObject("pubRcmdft", pubOperateVO.getPubRcmdft());
            view.addObject("totalCount", pubOperateVO.getTotalCount());
            view.setViewName("/pub/pubdetails/pubdetail_rcmd_fulltext");
            return view;
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果详情页面-全文认领-出错,psnId= " + SecurityUtils.getCurrentUserId(), e);
    }
    return null;
  }

  /**
   * 确认全文认领 是这篇成果的全文
   * 
   * @param ids
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxConfirmPubft")
  public String confirmPub(@ModelAttribute("ids") String ids) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(ids) && psnId != null && psnId > 0L) {
        PubOperateVO pubOperateVO = new PubOperateVO();
        pubOperateVO.setIds(ids);
        pubOperateVO.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        map = restTemplate.postForObject(domainscm + "/data/pub/opt/confirmpubft", pubOperateVO, Map.class);
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 检查认领的成果
   * 
   * @param id
   * @return
   */
  /*
   * private boolean checkConfirmPub(Long id) { if (NumberUtils.isNotNullOrZero(id)) {
   * PubFulltextPsnRcmd psnRcmd = pubFulltextPsnRcmdService.get(id); if (psnRcmd != null &&
   * NumberUtils.isNotNullOrZero(psnRcmd.getSrcPubId())) { if (psnRcmd.getDbId().equals(1)) { //
   * 说明是基准库的全文，那么srcPubId是基准库的id PubPdwhPO pubPdwhPO = pubPdwhService.get(psnRcmd.getSrcPubId()); if
   * (pubPdwhPO != null && pubPdwhPO.getStatus().equals(PubPdwhStatusEnum.DEFAULT)) { //
   * 基准库成果状态正常才进行操作 return true; } } else { // 说明是个人库的全文，那么srcPubId是个人库的id PubSnsPO pubSnsPO =
   * pubSnsService.get(psnRcmd.getSrcPubId()); if (pubSnsPO != null &&
   * pubSnsPO.getStatus().equals(PubSnsStatusEnum.DEFAULT)) { // 基准库成果状态正常才进行操作 return true; } } } }
   * return false; }
   */

  /**
   * 确认全文认领 不是这篇成果的全文
   * 
   * @param ids
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxRejectPubft")
  public String rejectPub(@ModelAttribute("ids") String ids) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(ids) && SecurityUtils.getCurrentUserId() != null) {
        List<Long> idList = new ArrayList<Long>();
        idList = ServiceUtil.splitStrToLong(ids);
        if (!CollectionUtils.isEmpty(idList)) {
          for (Long id : idList) {
            this.pubFulltextPsnRcmdService.rejectPubft(id);// 不是这篇成果的全文
            map.put("result", "success");
          }
        }
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 上传全文 更新成果全文附件信息
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxupdatefulltext", method = RequestMethod.POST)
  public String ajaxUpdatePubFulltext(PubOperateVO pubOperateVO) {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Long pubId = pubOperateVO.getPubId();
      Long fileId = 0L;
      if (StringUtils.isNotBlank(pubOperateVO.getDes3FileId())) {
        fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubOperateVO.getDes3FileId()));
      }
      Long psnId = SecurityUtils.getCurrentUserId();
      if (pubId != 0L && fileId != 0L && psnId > 0) {
        map = pubFullTextService.uploadPubFulltext(pubId, fileId, psnId);
        map.put("result", "true");
      } else {
        map.put("result", "false");
        map.put("msg", "参数不正确");
      }
    } catch (Exception e) {
      map.put("result", "false");
      map.put("fullTextImagePath", "");
      map.put("downUrl", "");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  public boolean checkParams(Long psnId, String content) {
    if (psnId == 0 || StringUtils.isBlank(content)) {
      logger.error("评论内容为空或用户未登录,psnId=" + psnId + ",content=" + content);
      return false;
    }
    return true;
  }

  /**
   * 个人主页-成果模块-引用
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxpubquote")
  public ModelAndView pubQuote(@ModelAttribute("des3PubId") String des3PubId) {
    ModelAndView view = new ModelAndView();
    try {
      if (StringUtils.isNotBlank(des3PubId)) {
        List<PubQuoteVO> result = pubQuoteService.findPubQuote(Long.valueOf(Des3Utils.decodeFromDes3(des3PubId)));
        view.addObject("pubQuoteList", result);
      }
    } catch (Exception e) {
      logger.error("个人主页-成果模块-引用-出错,pubId= " + Des3Utils.decodeFromDes3(des3PubId), e);
    }
    view.setViewName("/pub/main/pub_quote");
    return view;
  }

  /**
   * 确认成果认领
   * 
   * @param pubId
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxAffirmPubComfirm")
  public String ajaxAffirmPubComfirm(@ModelAttribute("pubId") String pubId) {
    String SERVER_URL = domainscm + "/data/pub/affirmconfirm";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 判断pdwhPubId是否有效
        if (pubPdwhService.get(pdwhPubId) != null) {
          // 调用接口进行成果认领业务
          // 设置请求头部
          HttpHeaders requestHeaders = new HttpHeaders();
          requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
          // 构建成果保存对象
          Map<String, Object> data = new HashMap<>();
          data.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
          data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())));
          HttpEntity<String> requestEntity =
              new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
          Map<String, Object> res =
              (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
          if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
            map.put("result", "success");
          }
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 忽略成果认领
   * 
   * @param pubId
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxIgnorePubComfirm")
  public String ajaxIgnorePubComfirm(@ModelAttribute("pubId") String pubId) {
    String SERVER_URL = domainscm + "/data/pub/ignoreconfirm";
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long pdwhPubId = Long.valueOf(pubId);
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 判断pdwhPubId是否有效
        if (pubPdwhService.get(pdwhPubId) != null) {
          // 调用接口进行成果认领业务
          // 设置请求头部
          HttpHeaders requestHeaders = new HttpHeaders();
          requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
          // 构建成果保存对象
          Map<String, Object> data = new HashMap<>();
          data.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
          data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(SecurityUtils.getCurrentUserId())));
          HttpEntity<String> requestEntity =
              new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
          Map<String, Object> res =
              (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
          if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
            map.put("result", "success");
          }
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 添加成果-同时认领多条成果
   * 
   * @param des3PubIds
   * @return
   */
  @RequestMapping(value = "/pub/opt/ajaxAffirmPubListComfirm")
  public String ajaxAffirmPubListComfirm(@ModelAttribute("des3PubIds") String des3PubIds) {
    String SERVER_URL = domainscm + "/data/pub/affirmconfirm";
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId == null || psnId == 0L) {// 没有登录
      map.put("result", "error");
      return JacksonUtils.mapToJsonStr(map);
    }
    Integer successTotal = 0;
    Integer errorTotal = 0;
    for (String des3PubId : des3PubIds.split(",")) {
      Long pdwhPubId = Long.valueOf(Des3Utils.decodeFromDes3(des3PubId));
      if (!NumberUtils.isNullOrZero(pdwhPubId)) {
        // 判断pdwhPubId是否有效
        if (pubPdwhService.get(pdwhPubId) != null) {
          try {
            // 构建成果保存对象
            Map<String, Object> data = new HashMap<>();
            data.put("des3PubId", des3PubId);
            data.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
            // 调用接口进行成果认领业务
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> requestEntity =
                new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
            Map<String, Object> res =
                (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
            if (!CollectionUtils.isEmpty(res) && "SUCCESS".equals(res.get("status"))) {
              successTotal++;
            }
          } catch (Exception e) {
            map.put("result", "error");
            logger.error("基准库成果-导入到我的成果库出错,psnId= " + SecurityUtils.getCurrentUserId() + ";pdwhpubId:" + des3PubId);
          }
        } else {
          errorTotal++;
        }
      } else {
        errorTotal++;
      }
    }
    if (errorTotal == 0) {
      map.put("result", "success");
    } else if (errorTotal != 0 && successTotal != 0) {
      map.put("result", "error");
      map.put("message", "有" + successTotal + "导入成功，" + errorTotal + "导入不成功");
    } else if (errorTotal != 0 && successTotal == 0) {
      map.put("result", "error");
      map.put("message", "所有成果导入不成功");
    }
    return JacksonUtils.mapToJsonStr(map);
  }

  /**
   * 生成个人库成果的短地址
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping(value = "/pub/details/ajaxview")
  public String scholarView(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String urlType = ShortUrlConst.A_TYPE;
      String pubShortUrl = getPubShortUrl(pubOperateVO.getPubId(), urlType);
      if (StringUtils.isNotEmpty(pubShortUrl)) {
        map.put("result", 2);
        map.put("shortUrl", pubShortUrl);
      } else {
        map.put("result", 1);
      }
    } catch (Exception e) {
      map.put("result", 0);
      logger.error("请求成果短地址错误 pubId: " + pubOperateVO.getPubId() + e);
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 生成基准库成果的短地址
   * 
   * @param pubOperateVO
   * @return
   */
  @RequestMapping("/pub/details/ajaxpdwhview")
  public String pdwhview(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String urlType = ShortUrlConst.S_TYPE;
      String pubShortUrl = getPubShortUrl(pubOperateVO.getPubId(), urlType);
      if (StringUtils.isNotEmpty(pubShortUrl)) {
        map.put("result", 2);
        map.put("shortUrl", pubShortUrl);
      } else {
        map.put("result", 1);
      }
    } catch (Exception e) {
      map.put("result", 0);
      logger.error("请求成果短地址错误 pubId: " + pubOperateVO.getPubId() + e);
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  /**
   * 检查个人库库成果是否存在---不进行超时校验
   * 
   * @return
   */
  @RequestMapping(value = "/pub/outside/ajaxsnsisexists")
  @ResponseBody()
  public String pubSnsIsExists(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    try {
      if (StringUtils.isNotBlank(des3PubId)) {
        Long pubId = null;
        if (StringUtils.isNumeric(des3PubId)) {
          pubId = NumberUtils.toLong(des3PubId);
        } else {
          pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
        }
        if (NumberUtils.isNotNullOrZero(pubId) && snsPubIsExist(pubId)) {
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
   * 根据pubId 获取成果短地址
   * 
   * @param pubId
   * @param urlType
   * @return
   * @throws Exception
   */
  public String getPubShortUrl(Long pubId, String urlType) throws Exception {
    String shortUrl = "";
    if (urlType.equals(ShortUrlConst.A_TYPE)) {
      PubIndexUrl pubIndexUrl = pubIndexUrlService.get(pubId);
      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        shortUrl = domain + "/" + urlType + "/" + pubIndexUrl.getPubIndexUrl();
      }
    }
    if (urlType.equals(ShortUrlConst.S_TYPE)) {
      PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlService.get(pubId);
      if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
        shortUrl = domain + "/" + urlType + "/" + pdwhPubIndexUrl.getPubIndexUrl();
      }
    }
    return shortUrl;
  }

  @RequestMapping(value = {"/pub/ajaxCheckPub", "/data/pub/ajaxCheckPub"})
  public String checkPub(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long pubId = pubOperateVO.getPubId();
    try {
      if (StringUtils.isNotBlank(pubOperateVO.getDes3PubId())) {
        if (!snsPubIsExist(pubId)) {
          map.put("status", "isDel");
        } else {
          // 判断是否是隐私成果
          isPrivate(map, psnId, pubId);
        }
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      map.put("status", "error");
    }
    String result = JacksonUtils.mapToJsonStr(map);
    return result;
  }

  private void isPrivate(Map<String, Object> map, Long psnId, Long pubId) {
    // 获取成果拥有者
    Long pubOwnerPsnId = getPubOwnerId(pubId);
    // 获取成果权限
    Integer permission = getPubPermission(pubId, pubOwnerPsnId);
    if (permission == 4) {
      if (psnId > 0 && psnId.equals(pubOwnerPsnId)) {
        map.put("status", "isSelfPrivate");
      } else {
        map.put("status", "isNotSelfPrivate");
      }
    } else {
      map.put("status", "success");
    }
  }

  private Integer getPubPermission(Long pubId, Long pubOwnerPsnId) {
    Integer permission = 7;
    PsnConfigPub cnfPub = new PsnConfigPub();
    cnfPub.getId().setPubId(pubId);
    PsnConfigPub cnfPubExists = psnCnfService.get(pubOwnerPsnId, cnfPub);
    if (cnfPubExists != null) {
      permission = cnfPubExists.getAnyUser();
    }
    return permission;
  }

  private Long getPubOwnerId(Long pubId) {
    Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubId);
    if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubId);
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    return pubOwnerPsnId;
  }

  /*
   * 校验基准库成果是否存在
   */
  public boolean pdwhPubIsExist(Long pubId) {
    if (NumberUtils.isNullOrZero(pubId)) {
      return false;
    }
    PubPdwhPO pdwhPub = pubPdwhService.get(pubId);
    if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      return false;
    }
    return true;
  }

  /*
   * 校验个人库库成果是否存在
   */
  public boolean snsPubIsExist(Long pubId) {
    if (NumberUtils.isNullOrZero(pubId)) {
      return false;
    }
    PubSnsPO pubSnsPO = pubSnsService.get(pubId);
    if (pubSnsPO == null || pubSnsPO.getStatus().equals(PubSnsStatusEnum.DELETED)) {
      return false;
    }
    return true;
  }

  public Page<?> getPage() {
    return page;
  }

  public void setPage(Page<?> page) {
    this.page = page;
  }
}
