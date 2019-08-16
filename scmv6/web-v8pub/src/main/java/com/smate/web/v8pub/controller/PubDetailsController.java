package com.smate.web.v8pub.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.util.URLDecoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.pub.vo.PubSEO;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.mobile.SmateMobileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubStatisticsService;
import com.smate.web.v8pub.service.pdwh.PubAssignLogService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.pubdetailquery.PubDetailHandleService;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.region.ConstRegionService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubPdwhSnsRelationService;
import com.smate.web.v8pub.service.sns.PubSnsDetailService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.PubStatisticsService;
import com.smate.web.v8pub.service.sns.fulltext.PubUrlService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PersonService;
import com.smate.web.v8pub.untils.PubDetailVoUtil;
import com.smate.web.v8pub.utils.PubLocaleUtils;
import com.smate.web.v8pub.vo.sns.PubOperateVO;

/**
 * 成果详情页面
 * 
 * @author yhx
 *
 */
@Controller
public class PubDetailsController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubDetailHandleService pubDetailHandleService;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PubSnsDetailService pubSnsDetailService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private ConstRegionService ConstRegionService;
  @Autowired
  private PdwhPubStatisticsService newPdwhPubStatisticsService;
  @Autowired
  private PubUrlService pubURlService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PubAssignLogService pubAssignLogService;
  @Autowired
  private PubPdwhSnsRelationService pubPdwhSnsRelationService;
  @Value("${domainscm}")
  private String domain;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private PersonService personService;

  /**
   * 个人库成果详情页面 {"pubId":"1000050000021"}
   * 
   * @return
   */
  @RequestMapping("/pub/details")
  public ModelAndView pubDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "snsPub");
    if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID)) || !checkPubId(pubOperateVO.getPubId())) {
      view.setViewName("pub/pubdetails/pub_not_exists");
      return view;
    }

    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      // 移动端跳转
      if (midJump(pubOperateVO)) {
        return null;
      }
      // 判断是否登录
      if (psnId != null && psnId > 0L) {
        pubDetailVO.setIsLogin(true);
        pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (pubDetailVO != null) {
          pubDetailVO.setPsnId(psnId);
          // 初始化统计信息
          newPubStatisticsService.initStatistics(pubDetailVO);
          // 成果拥有者
          Long pubOwnerPsnId = buildOwnerPsnId(pubDetailVO, pubOperateVO);
          String name = personService.getName(pubOwnerPsnId);
          view.addObject("username", name);
          if (psnId.equals(pubOwnerPsnId)) {
            pubDetailVO.setOwner(true);
          }
          pubSnsDetailService.checkPubAuthority(pubDetailVO);
          boolean shareView = false;
          if (StringUtils.isNotBlank(pubOperateVO.getDes3relationid())) {// 分享id不为空，用于判断隐私分享
            shareView = pubSnsDetailService.sharePubView(pubOperateVO.getDes3relationid(), pubDetailVO.getPubId(),
                pubOwnerPsnId, psnId);
          }
          // 登录且非本人 隐私成果： "该论文由于个人隐私设置，无法查看。请查看其它论文"
          if (pubDetailVO.getPermission() == 4 && pubDetailVO.getIsOwner() != true && !shareView) {
            view.addObject("pubDetailVO", pubDetailVO);
            view.setViewName("pub/pubdetails/pub_no_authority");
            return view;
          }
          // 处理全文
          buildfullTextDownloadUrl(pubDetailVO, false);
          // 初始化收藏信息
          buildPubCollect(pubDetailVO, PubDbEnum.SNS);
          // 构建个人成果信息
          buildSnsBaseInfo(pubDetailVO);
          // SCM-23853 成果详情页面根据身份判断是否出现按钮
          boolean isShowButton = isShowButton(pubDetailVO, "sns");
          pubDetailVO.setShowButton(isShowButton);
          view.addObject("pubDetailVO", pubDetailVO);
          PubSEO pubSEO = buildPubSEO(pubDetailVO);
          view.addObject("pubSEO", pubSEO);
        } else {
          view.setViewName("pub/pubdetails/pub_not_exists");
          return view;
        }
      } else {
        return new ModelAndView(
            "redirect:" + domain + "/pub/outside/details?des3PubId=" + params.get(V8pubConst.DESC_PUB_ID));
      }
    } catch (Exception e) {
      logger.error("个人库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("pub/pubdetails/pub_details_main");
    return view;
  }

  private PubSEO buildPubSEO(PubDetailVO pubDetailVO) {
    PubSEO pubSEO = new PubSEO();
    pubSEO.setDescription(pubDetailVO.getSummary());
    pubSEO.setTitle(pubDetailVO.getTitle());
    pubSEO.setKeywords(pubDetailVO.getKeywords());
    return pubSEO;
  }

  /**
   * 获取成果关联的人员信息
   *
   * @param pubDetailVO
   * @return
   */
  @RequestMapping(value = "/pub/ajaxgetrelationpsn", produces = "application/json; charset=utf-8")
  @ResponseBody
  public String getPubRelationPsn(PubDetailVO pubDetailVO) {
    List<String> psninfo = new ArrayList<>();
    if (StringUtils.isNotEmpty(pubDetailVO.getDes3PubId())) {
      try {
        Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubDetailVO.getDes3PubId()));
        Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubDetailVO.getDes3PsnId()));
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (pubId != null && pubId != 0L) {
          String keyword = URLDecoderUtil.decode(pubDetailVO.getKeywords(), "utf-8");
          psninfo = pubPdwhSnsRelationService.getPubRelationPsninfo(pubId, keyword, psnId, currentUserId);
        }
      } catch (Exception e) {
        logger.error("获取成果关联的人员信息,des3PubId={}", pubDetailVO.getDes3PubId(), e);
      }
    } else {
    }
    return JacksonUtils.listToJsonStr(psninfo);
  }

  /**
   * 获取成果详情,返回json数据 目前只取三个字段,需要更多字段请添加入map即可
   * 
   * @param des3PubId
   * @return
   */
  @RequestMapping(value = "/data/pub/details/forshare", produces = "application/json; charset=utf-8")
  @ResponseBody
  public String getDataForShare(String des3PubId, String type, String des3GrpId) {
    Map<String, String> result = new HashMap<String, String>();
    if (StringUtils.isNotEmpty(des3PubId)) {
      try {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(V8pubConst.DESC_PUB_ID, des3PubId);
        params.put("serviceType", "snsPub");
        if ("pdwh".equals(type)) {
          params.put("serviceType", "pdwhPub");
        }
        if (StringUtils.isNotBlank(des3GrpId)) {
          params.put("des3GrpId", "des3GrpId");
        }
        PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (Objects.nonNull(pubDetailVO)) {
          result.put("title", pubDetailVO.getTitle());
          result.put("authorNames", pubDetailVO.getAuthorNames());
          result.put("briefDesc", pubDetailVO.getBriefDesc());
          result.put("hasFullText", String.valueOf(pubDetailVO.getFullText() != null ? true : false));
          result.put("thumbnailPath",
              pubDetailVO.getFullText() != null
                  ? StringUtils.defaultIfEmpty(pubDetailVO.getFullText().getThumbnailPath(),
                      "/resmod/images_v5/images2016/file_img1.jpg")
                  : "/resmod/images_v5/images2016/file_img.jpg");
          result.put("status", "success");
          result.put("publishYear",
              pubDetailVO.getPublishYear() != null ? pubDetailVO.getPublishYear().toString() : "");
          result.put("msg", "get data success");
        } else {
          result.put("status", "success");
          result.put("msg", "resource is not exists");
        }
      } catch (Exception e) {
        logger.error("获取成果详情出错,des3PubId={}", des3PubId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify error");
    }
    return JacksonUtils.mapToJsonStr(result);
  }


  @RequestMapping(value = "/data/pub/checkpdwhPub", produces = "application/json; charset=utf-8")
  @ResponseBody
  public String checkPdwhPubIsDel(String des3pdwhPubId) {
    Map<String, String> result = new HashMap<String, String>();
    String pdwhPubId = Des3Utils.decodeFromDes3(des3pdwhPubId);
    if (StringUtils.isNotEmpty(pdwhPubId)) {
      PubPdwhPO pdwhPub = pubPdwhService.get(Long.parseLong(pdwhPubId));
      if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
        result.put("status", "is del");
      } else {
        result.put("status", "not del");
      }
    }
    return JacksonUtils.mapToJsonStr(result);
  }

  private Long buildOwnerPsnId(PubDetailVO pubDetailVO, PubOperateVO pubOperateVO) {
    Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubDetailVO.getPubId());
    if (!NumberUtils.isNullOrZero(pubOperateVO.getGrpId())) {
      GroupPubPO groupPub = groupPubService.getByGrpIdAndPubId(pubOperateVO.getGrpId(), pubDetailVO.getPubId());
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubDetailVO.getPubId());
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    if (!NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      pubDetailVO.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(pubOwnerPsnId)));
    }
    pubDetailVO.setPubOwnerPsnId(pubOwnerPsnId);
    return pubOwnerPsnId;
  }

  // 处理全文
  private void buildfullTextDownloadUrl(PubDetailVO pubDetailVO, boolean isShortUrl) {
    String fullTextDownloadUrl = pubFullTextService.getSnsFullTextDownloadUrl(pubDetailVO.getPubId(), isShortUrl);
    pubDetailVO.setFullTextDownloadUrl(fullTextDownloadUrl);
    // 全文图片地址
    String fulltextImageUrl = pubFullTextService.getFulltextImageUrl(pubDetailVO.getPubId());
    if (StringUtils.isBlank(fulltextImageUrl)) {
      pubDetailVO.setFulltextImageUrl(V8pubConst.PUB_DEFAULT_NOT_FULLTEXT_IMG_NEW);
    } else {
      pubDetailVO.setFulltextImageUrl(fulltextImageUrl);
    }
  }

  /**
   * 站外个人库成果详情页面
   *
   * @return
   */
  @RequestMapping("/pub/outside/details")
  public ModelAndView pubOutsideDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO();
    // 解密成果ID,接口那边返回成果信息的时候，有返回这种ID的
    if (StringUtils.isNotBlank(pubOperateVO.getDes3Id())) {
      String[] pubId = Des3Utils.decodeFromDes3(pubOperateVO.getDes3Id()).split(",");
      String pubIdStr = pubId[0].split("\\|").length > 1 ? pubId[0].split("\\|")[1] : pubId[0];
      pubOperateVO.setDes3PubId(StringUtils.isNotBlank(pubIdStr) ? Des3Utils.encodeToDes3(pubIdStr) : "");
    }
    params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
    params.put("serviceType", "snsPub");
    if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID)) || !checkPubId(pubOperateVO.getPubId())) {
      view.setViewName("pub/pubdetails/pub_not_exists");
      return view;
    }
    // 判断是否登录
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      // 移动端跳转
      if (midJump(pubOperateVO)) {
        return null;
      }
      if (psnId == 0L || psnId == null) {
        pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (pubDetailVO != null) {
          pubDetailVO.setPsnId(psnId);
          pubDetailVO.setIsLogin(false);
          // 初始化统计信息
          newPubStatisticsService.initStatistics(pubDetailVO);
          // 成果拥有者
          buildOwnerPsnId(pubDetailVO, pubOperateVO);
          pubSnsDetailService.checkPubAuthority(pubDetailVO);
          // 登录且非本人 隐私成果： "该论文由于个人隐私设置，无法查看。请查看其它论文"
          if (pubDetailVO.getPermission() == 4) {
            view.addObject("pubDetailVO", pubDetailVO);
            view.setViewName("pub/pubdetails/pub_no_authority");
            return view;
          }
          // 处理全文
          buildfullTextDownloadUrl(pubDetailVO, true);
          // 构建个人成果信息
          buildSnsBaseInfo(pubDetailVO);
          view.addObject("pubDetailVO", pubDetailVO);
          PubSEO pubSEO = buildPubSEO(pubDetailVO);
          view.addObject("pubSEO", pubSEO);
          view.setViewName("pub/pubdetails/outside_pub_details_main");
        } else {
          view.setViewName("pub/pubdetails/pub_not_exists");
          return view;
        }
      } else {
        return new ModelAndView("redirect:" + domain + "/pub/details?des3PubId=" + params.get(V8pubConst.DESC_PUB_ID));
      }
    } catch (Exception e) {
      logger.error("站外个人库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    return view;
  }

  // 构建个人成果信息
  private void buildSnsBaseInfo(PubDetailVO pubDetailVO) {
    pubSnsDetailService.getPsnAvatars(pubDetailVO);
    pubDetailVO.setDoiUrl(buildDoiUrl(pubDetailVO.getDoi()));
    // 获取拥有者信息
    pubSnsDetailService.getPubOwnerInfo(pubDetailVO);
    // 构建附件信息
    fulltextAttachment(pubDetailVO);
    // 其他类似全文条数
    Long snsSimilarCount = pubFullTextService.getSimilarCount(pubDetailVO.getPubId());
    pubDetailVO.setSnsSimilarCount(snsSimilarCount);
    pubURlService.builPubShortUrl(pubDetailVO);// 构建短地址SEO的
    // 单位信息
    String insNames = pubSnsDetailService.getPubInsName(pubDetailVO.getPubId());
    pubDetailVO.setInsNames(insNames);

    // 摘要换行符的增加
    String summary = pubDetailVO.getSummary();
    summary = summary.replace("&lt;br/&gt;", "<br/>").replace("&lt;br&gt;", "<br>");
    pubDetailVO.setSummary(summary);
  }

  /**
   * 基准库成果详情页面{"pubId":"21000000021"}
   * 
   * @return
   */
  @RequestMapping("/pub/details/pdwh")
  public ModelAndView pubPdwhDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    try {
      // 站外的成果详情参数处理
      dealOutsidePubUrlParams(pubOperateVO);
      params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "pdwhPub");
      if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID))
          || !checkPdwhPubId(pubOperateVO.getPubId())) {
        view.setViewName("pub/pubdetails/pub_not_exists");
        return view;
      }
      // 判断是否登录
      Long psnId = SecurityUtils.getCurrentUserId();
      // 移动端跳转
      if (midPdwhJump(pubOperateVO)) {
        return null;
      }
      if (psnId != null && psnId > 0L) {
        pubDetailVO.setIsLogin(true);
        pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (pubDetailVO != null) {
          // 基准库构建基本信息
          buildPdwhBaseInfo(pubDetailVO, psnId);
          // 初始化统计信息
          newPdwhPubStatisticsService.pdwhInitStatistics(pubDetailVO);
          // 初始化收藏信息
          buildPubCollect(pubDetailVO, PubDbEnum.PDWH);
          // SCM-23853 成果详情页面根据身份判断是否出现按钮
          boolean isShowButton = isShowButton(pubDetailVO, "pdwh");
          pubDetailVO.setShowButton(isShowButton);
          view.addObject("pubDetailVO", pubDetailVO);
          PubSEO pubSEO = buildPubSEO(pubDetailVO);
          view.addObject("pubSEO", pubSEO);

        } else {
          view.setViewName("pub/pubdetails/pub_not_exists");
          return view;
        }
      } else {
        return new ModelAndView(
            "redirect:" + domain + "/pub/outside/pdwhdetails?des3PubId=" + params.get(V8pubConst.DESC_PUB_ID));
      }
    } catch (Exception e) {
      logger.error("基准库库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("pub/pubdetails/pdwhpub_details_main");
    return view;
  }

  /**
   * 站外基准库成果详情页面
   * 
   * @return
   */
  @RequestMapping("/pub/outside/pdwhdetails")
  public ModelAndView pubPdwhOutsideDetails(PubOperateVO pubOperateVO) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    PubDetailVO pubDetailVO = new PubDetailVO<>();
    try {
      // 站外的成果详情参数处理
      dealOutsidePubUrlParams(pubOperateVO);
      params.put(V8pubConst.DESC_PUB_ID, pubOperateVO.getDes3PubId());
      params.put("serviceType", "pdwhPub");
      if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID))
          || !checkPdwhPubId(pubOperateVO.getPubId())) {
        view.setViewName("pub/pubdetails/pub_not_exists");
        return view;
      }
      // 判断是否登录
      Long psnId = SecurityUtils.getCurrentUserId();
      // 移动端跳转
      if (midPdwhJump(pubOperateVO)) {
        return null;
      }
      if (psnId == 0L || psnId == null) {
        pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        if (pubDetailVO != null) {
          pubDetailVO.setIsLogin(false);
          // 构建基本信息
          buildPdwhBaseInfo(pubDetailVO, psnId);
          // 初始化统计信息
          newPdwhPubStatisticsService.pdwhInitStatistics(pubDetailVO);
          view.addObject("pubDetailVO", pubDetailVO);
          PubSEO pubSEO = buildPubSEO(pubDetailVO);
          view.addObject("pubSEO", pubSEO);
        } else {
          view.setViewName("pub/pubdetails/pub_not_exists");
          return view;
        }
      } else {
        return new ModelAndView(
            "redirect:" + domain + "/pub/details/pdwh?des3PubId=" + params.get(V8pubConst.DESC_PUB_ID));
      }
    } catch (Exception e) {
      logger.error("站外基准库成果详情页面获取数据出错,pubId=" + pubOperateVO.getPubId(), e);
    }
    view.setViewName("pub/pubdetails/outside_pdwhpub_details_main");
    return view;
  }

  // 构建基准库基本信息
  private void buildPdwhBaseInfo(PubDetailVO pubDetailVO, Long psnId) {
    pubDetailVO.setPsnId(psnId);
    pubSnsDetailService.getPsnAvatars(pubDetailVO);
    pubDetailVO.setDoiUrl(buildDoiUrl(pubDetailVO.getDoi()));
    // 构建作者信息
    pubPdwhDetailService.buildPdwhPsnInfo(pubDetailVO);
    // 基准库成果全文下载
    String pdwhFullTextDownloadUrl = pubFullTextService.getPdwhFullTextDownloadUrl(pubDetailVO.getPubId(), false);
    pubDetailVO.setPdwhFullTextDownloadUrl(pdwhFullTextDownloadUrl);
    if (StringUtils.isNotBlank(pdwhFullTextDownloadUrl)) {
      pubDetailVO.setFulltextImageUrl(pubFullTextService.getPdwhFullTextUrl(pubDetailVO.getPubId()));
    }
    // 其他类似全文条数
    Long pdwhSimilarCount = pubFullTextService.getPdwhSimilarCount(pubDetailVO.getPubId());
    pubDetailVO.setPdwhSimilarCount(pdwhSimilarCount);
    // 构建单位信息
    String insNames = pubPdwhDetailService.getPubInsName(pubDetailVO.getPubId());
    pubDetailVO.setInsNames(insNames);

    // 摘要换行符的增加
    String summary = pubDetailVO.getSummary();
    summary = summary.replace("&lt;br/&gt;", "<br/>").replace("&lt;br&gt;", "<br>");
    pubDetailVO.setSummary(summary);
    // SCM-23380 全站检索：站外点评论实现建议调整
    String type = SpringUtils.getRequest().getParameter("type");
    if (StringUtils.isNotBlank(type)) {
      pubDetailVO.setType(type);
    }
  }

  private void dealOutsidePubUrlParams(PubOperateVO pubOperateVO) throws Exception {
    String param = SpringUtils.getRequest().getParameter("param");
    if (StringUtils.isNotBlank(param) && param.matches("(.+)\\,([0-9]+),(zh_cn|en_us)")) {
      param = StringUtils.replace(param, "_iris_plus_", "%2B");
      param = StringUtils.replace(param, "_iris_slash_", "%2F");
      // 设置语言
      String language = param.substring(param.lastIndexOf(',') + 1);
      if ("en_us".equalsIgnoreCase(language)) {
        LocaleContextHolder.setLocale(Locale.US, true);
      } else {
        LocaleContextHolder.setLocale(Locale.CHINA, true);
      }

      String temp = param.substring(0, param.lastIndexOf(','));
      int index = temp.lastIndexOf(',');
      String des3Id = temp.substring(0, index);
      pubOperateVO.setPubId(Long.valueOf(ServiceUtil.decodeFromDes3(des3Id)));
      pubOperateVO.setDes3PubId(des3Id);
    }
  }

  /**
   * 是否已经收藏
   * 
   * @param pubDetailVO
   */
  private void buildPubCollect(PubDetailVO pubDetailVO, PubDbEnum pubDb) {
    try {
      boolean isCollect = newPubStatisticsService.isCollectedPub(pubDetailVO.getPsnId(), pubDetailVO.getPubId(), pubDb);
      pubDetailVO.setIsCollection(isCollect == true ? 1 : 0);
    } catch (Exception e) {
      logger.error("查询成果是否已经收藏出错 pubId=" + pubDetailVO.getPubId() + ",psnId=" + pubDetailVO.getPsnId());
    }
  }

  /**
   * 基准库成果查看，不带评论操作等.
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping("/pub/details/viewPdwhSimple")
  public ModelAndView viewPdwhSimple(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "pdwhPub");
      params.put(V8pubConst.DESC_PUB_ID, des3PubId);
      Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(des3PubId));
      if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID)) || !checkPdwhPubId(pubId)) {
        view.setViewName("pub/pubdetails/pub_not_exists");
        return view;
      }
      try {
        PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        pubDetailVO.setRecordFrom(PubSnsRecordFromEnum.IMPORT_FROM_PDWH);
        Locale locale = PubLocaleUtils.getLocale(pubDetailVO.getTitle());
        pubURlService.buildPubUrl(pubDetailVO);
        Map countryInfo = ConstRegionService.buildCountryAndCityName(pubDetailVO.getCountryId(), locale);
        if (countryInfo.get("countryName") != null) {
          pubDetailVO.setCountryName(String.valueOf(countryInfo.get("countryName")));
        }
        if (countryInfo.get("cityName") != null) {
          pubDetailVO.setCountryName(String.valueOf(countryInfo.get("cityName")));
        }
        view.addObject("pubDetailVO", pubDetailVO);
        PubSEO pubSEO = new PubSEO();
        pubSEO.setTitle(pubDetailVO.getTitle());
        pubSEO.setDescription(pubDetailVO.getSummary());
        pubSEO.setKeywords(pubDetailVO.getKeywords());
        view.addObject("pubSEO", pubSEO);
      } catch (Exception e) {
        e.printStackTrace();
      }
      view.setViewName("pub/pubdetails/pub_details_simple_main");
      return view;
    } else {
      view.setViewName("pub/pubdetails/pub_not_exists");
      return view;
    }
  }

  /**
   * 个人成果查看，不带评论操作等.
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping("/pub/details/viewSimple")
  public ModelAndView viewSimple(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> params = new HashMap<>();
    ModelAndView view = new ModelAndView();
    if (StringUtils.isNotBlank(des3PubId)) {
      params.put("serviceType", "snsPub");
      params.put(V8pubConst.DESC_PUB_ID, des3PubId);
      Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(des3PubId));
      if (StringUtils.isBlank((String) params.get(V8pubConst.DESC_PUB_ID)) || !checkPubId(pubId)) {
        view.setViewName("pub/pubdetails/pub_not_exists");
        return view;
      }
      try {
        PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(params);
        Long psnId = SecurityUtils.getCurrentUserId();
        pubDetailVO.setPsnId(psnId);
        Locale locale = PubLocaleUtils.getLocale(pubDetailVO.getTitle());
        pubURlService.buildPubUrl(pubDetailVO);
        pubURlService.builPubShortUrl(pubDetailVO);// 构建短地址SEO的
        Map countryInfo = ConstRegionService.buildCountryAndCityName(pubDetailVO.getCountryId(), locale);
        if (countryInfo.get("countryName") != null) {
          pubDetailVO.setCountryName(String.valueOf(countryInfo.get("countryName")));
        }
        if (countryInfo.get("cityName") != null) {
          pubDetailVO.setCountryName(String.valueOf(countryInfo.get("cityName")));
        }
        // this.fulltextAttachment(pubDetailVO);
        pubDetailVO.setListInfo(PubDetailVoUtil.fillListInfo(pubDetailVO));
        view.addObject("pubDetailVO", pubDetailVO);
        PubSEO pubSEO = new PubSEO();
        pubSEO.setTitle(pubDetailVO.getTitle());
        pubSEO.setDescription(pubDetailVO.getSummary());
        pubSEO.setKeywords(pubDetailVO.getKeywords());
        view.addObject("pubSEO", pubSEO);
      } catch (Exception e) {
        e.printStackTrace();
      }
      view.setViewName("pub/pubdetails/pub_details_simple_main");
      return view;
    } else {
      view.setViewName("pub/pubdetails/pub_not_exists");
      return view;
    }
  }

  /**
   * 校验分享的成果是否有权限
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/ajaxCheckSharePubPermissions", method = RequestMethod.POST)
  @ResponseBody
  public String checkSharePubPermissions(@ModelAttribute("des3PubId") String des3PubId) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> params = new HashMap<>();
    params.put("serviceType", "snsPub");
    params.put(V8pubConst.DESC_PUB_ID, des3PubId);
    try {
      PubDetailVO pubDetailVO = pubDetailHandleService.queryPubDetail(params);
      pubSnsDetailService.checkPubAuthority(pubDetailVO);
      Boolean status = true;
      if (pubDetailVO.getPermission() == 4) {
        status = false;
      }
      Long currentUserId = SecurityUtils.getCurrentUserId();
      Long pubOwnerPsnId = pubDetailVO.getPubOwnerPsnId();
      map.put("result", status);
      map.put("isOwner", currentUserId.equals(pubOwnerPsnId) ? true : false);
    } catch (Exception e) {
      logger.error("校验分享的成果权限出错, des3PubId = " + des3PubId, e);
      map.put("result", "error");
    }
    return JacksonUtils.jsonObjectSerializer(map);
  }


  @SuppressWarnings("unchecked")
  private void fulltextAttachment(PubDetailVO pubDetailVO) {
    List<Accessory> accessorys = pubDetailVO.getAccessorys();
    List<Accessory> newAccessorys = new ArrayList<Accessory>();
    Long psnId = pubDetailVO.getPsnId();
    if (CollectionUtils.isNotEmpty(accessorys)) {
      for (int i = 0; i < accessorys.size(); i++) {
        Accessory as = accessorys.get(i);
        String fileExt = FileUtils.getFileNameExtensionStr(as.getFileName());
        String fileType = FileUtils.SYMBOL_DOT + fileExt;
        as.setFileType(fileType);
        if (as.getPermission() == null) {
          as.setPermission(0);
        }
        if (psnId != 0 && psnId.equals(pubDetailVO.getPubOwnerPsnId()) && !"0".equals(as.getPermission().toString())) {
          as.setFileUrl(fileDownUrlService.getDownloadAttachmentUrl(FileTypeEnum.FULLTEXT_ATTACHMENT, as.getFileId(),
              pubDetailVO.getPubId()));
          as.setIsUpdown(true);
        } else if ("0".equals(as.getPermission().toString())) {
          as.setFileUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.FULLTEXT_ATTACHMENT, as.getFileId()));
          as.setIsUpdown(true);
        }
        if (as.getIsUpdown()) {
          newAccessorys.add(as);
        }

      }
      pubDetailVO.setAccessorys(newAccessorys);
    }
  }

  /**
   * 详情页面是否直接显示 这是我的成果 按钮
   * 
   * @param pubDetailVO
   * @return
   */
  private boolean isShowButton(PubDetailVO pubDetailVO, String type) {
    Long pdwhPubId = null;
    if ("pdwh".equals(type)) {
      pdwhPubId = pubDetailVO.getPubId();
    } else {
      // 个人成果 如果连接到 pdwh成果 ，该pdwh成果有推荐给当前用户的 也直接显示 这是我的成果按钮.否则显示 更多
      pdwhPubId = pubPdwhSnsRelationService.getPdwhIdBySnsId(pubDetailVO.getPubId());
      if (pdwhPubId == null) {// 不是基准库关联成果
        return false;
      }
    }
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(pubDetailVO.getPsnId());
    pubAssignLogService.queryPubConfirmIdList(pubQueryDTO);
    Long count = pubQueryDTO.getTotalCount();
    if (count != null && count > 0L) {
      for (Long pubId : pubQueryDTO.getPubIds()) {
        if (pdwhPubId.equals(pubId))
          return true;
      }
    }
    return false;
  }

  private String buildDoiUrl(String doi) {
    String resultDoi = "";
    if (StringUtils.isNotBlank(doi)) {
      resultDoi = "http://dx.doi.org/" + doi;
    }
    return resultDoi;
  }

  /*
   * 校验成果是否被删除
   */
  public boolean checkPubId(Long pubId) {
    PubSnsPO pub = pubSnsService.get(pubId);
    if (pub == null || (pub != null && "DELETED".equals(pub.getStatus().toString()))) {
      return false;
    } else {
      return true;
    }
  }

  // 校验基准库成果是否存在
  public boolean checkPdwhPubId(Long pdwhPubId) {
    PubPdwhPO pdwhPub = pubPdwhService.get(pdwhPubId);
    if (pdwhPub == null || pdwhPub.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      return false;
    }
    return true;
  }

  /**
   * 判断当前系统是不是移动端
   * 
   * @param httpServletRequest
   * @return
   */
  private boolean judgmentIsMid(HttpServletRequest httpServletRequest) {
    String ua = httpServletRequest.getHeader("User-Agent");
    String regex =
        "ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini";
    Pattern p = Pattern.compile(regex);
    return p.matcher(ua.toLowerCase()).find();
  }

  /**
   * 个人库移动端跳转
   * 
   * @param pubOperateVO
   * @return
   * @throws IOException
   */
  protected boolean midJump(PubOperateVO pubOperateVO) throws IOException {
    // 判断当前系统环境是否是移动端
    boolean isMid = judgmentIsMid(SpringUtils.getRequest());
    // 是否是微信的浏览器
    boolean isWechat = SmateMobileUtils.isWeChatBrowser(SpringUtils.getRequest().getHeader("User-Agent"));
    if (isMid || isWechat) {
      SpringUtils.getResponse().sendRedirect(domainMobile + "/pub/outside/details/snsnonext?des3PubId="
          + URLEncoder.encode(pubOperateVO.getDes3PubId(), "utf-8"));
      return true;
    }
    return false;
  }

  /**
   * 基准库移动端跳转
   * 
   * @param pubOperateVO
   * @return
   * @throws IOException
   */
  protected boolean midPdwhJump(PubOperateVO pubOperateVO) throws IOException {
    // 判断当前系统环境是否是移动端
    boolean isMid = judgmentIsMid(SpringUtils.getRequest());
    // 是否是微信的浏览器
    boolean isWechat = SmateMobileUtils.isWeChatBrowser(SpringUtils.getRequest().getHeader("User-Agent"));
    if (isMid || isWechat) {
      SpringUtils.getResponse().sendRedirect(
          domainMobile + "/pub/details/pdwh?des3PubId=" + URLEncoder.encode(pubOperateVO.getDes3PubId(), "utf-8"));
      return true;
    }
    return false;
  }
}
