package com.smate.web.v8pub.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.po.sns.psn.RecommendScienceArea;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.CollectedPubService;
import com.smate.web.v8pub.service.sns.PubRecommendService;
import com.smate.web.v8pub.vo.CollectPubVO;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryInSolrVO;

/**
 * 应用 - 论文功能
 * 
 * @author zll
 *
 */
@Controller
public class PubAppsController {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private CollectedPubService collectedPubService;

  @Resource
  private PubRecommendService pubRecommendService;

  /**
   * 应用 - 论文主页 -论文收藏
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxpapermain")
  public ModelAndView getPaperMain() {
    ModelAndView view = new ModelAndView();
    Calendar cal = Calendar.getInstance();
    view.addObject("currentYear", cal.get(Calendar.YEAR));
    view.setViewName("/pub/main/new_paper_main");
    return view;
  }

  /**
   * 请求收藏列表数据
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxCollectedPubs")
  public ModelAndView getPubs(CollectPubVO collectPubVO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    try {
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      pubQueryDTO.setPubType(collectPubVO.getPubType());
      pubQueryDTO.setIncludeType(collectPubVO.getIncludeType());
      pubQueryDTO.setPublishYear(collectPubVO.getPublishYear());
      pubQueryDTO.setOrderBy(collectPubVO.getOrderBy());
      pubQueryDTO.setPageNo(collectPubVO.getPage().getPageNo());
      pubQueryDTO.setSearchKey(collectPubVO.getSearchKey());
      // 根据关联信息去不同的库查找数据
      pubListVO.setPubQueryDTO(pubQueryDTO);
      pubQueryDTO.setServiceType("pubCollectedList");
      pubListVO.getPubQueryDTO().setSearchPsnId(SecurityUtils.getCurrentUserId());
      collectedPubService.getShowPubList(pubListVO);
      if ((pubListVO.getTotalCount() / pubQueryDTO.getPageSize()) > 1000) {// 最多显示一千页
        pubListVO.setTotalCount(1000 * pubQueryDTO.getPageSize());
      }
    } catch (Exception e) {
      logger.error("请求论文收藏列表失败", e);
    }
    view.addObject(pubListVO);
    view.setViewName("/pub/main/paper_main_list");
    return view;

  }

  /**
   * 应用 - 论文
   * 
   * @return
   */
  @RequestMapping(value = "/pub/applypapermain")
  public ModelAndView applyPaperMain(String toPaperType) {
    ModelAndView view = new ModelAndView();
    view.addObject("toPaperType", toPaperType);
    view.setViewName("/pub/main/paper_main");
    return view;
  }

  /**
   * 应用 - 论文主页 -论文推荐
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxpubrecommendmain")
  public ModelAndView getPubRecommendMain() {
    ModelAndView view = new ModelAndView();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        pubRecommendService.initScienAreaAndCode(psnId);
      }
    } catch (Exception e) {
      logger.error("进入推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.setViewName("/pub/main/pub_Recommend_Main");
    return view;
  }

  /**
   * 论文推荐条件（左边栏）
   * 
   * @return
   */
  @RequestMapping(value = "/pub/ajaxrecommendLeftshow")
  public ModelAndView showRecommendFundCOnditions(PubQueryDTO pubQueryDTO) {
    ModelAndView view = new ModelAndView();
    try {
      if (SecurityUtils.getCurrentUserId() > 0) {
        StringBuilder scienceAreaIds = new StringBuilder();
        pubQueryDTO.setSearchPsnId(SecurityUtils.getCurrentUserId());
        pubRecommendService.pubRecommendConditionsShow(pubQueryDTO);
        List<RecommendScienceArea> areaList = pubQueryDTO.getAreaList();
        for (RecommendScienceArea rsa : areaList) {
          scienceAreaIds.append("," + rsa.getScienceAreaId());
        }
        pubQueryDTO.setScienceAreaIds(scienceAreaIds.toString());
      }
    } catch (Exception e) {
      logger.error("论文推荐条件出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject(pubQueryDTO);
    view.setViewName("/pub/main/recommend_left_conditions");
    return view;
  }

  @RequestMapping(value = "/pub/ajaxrecommend")
  public ModelAndView searchPubList(PubQueryInSolrVO pubQueryInSolrVO) {
    ModelAndView view = new ModelAndView();
    PubListVO pubListVO = new PubListVO();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      if (SecurityUtils.getCurrentUserId() > 0) {
        pubQueryDTO.setSearchArea(pubQueryInSolrVO.getSearchArea());
        // SCM-21373 其他类型的论文改为除期刊、专利、会议以外的论文
        String searchPubType = pubQueryInSolrVO.getSearchPubType();
        if (StringUtils.isNotBlank(searchPubType) && searchPubType.contains("7")) {
          searchPubType = searchPubType + ",1,2,8,10";
          pubQueryInSolrVO.setSearchPubType(searchPubType);
        }
        pubQueryDTO.setSearchPubType(pubQueryInSolrVO.getSearchPubType());
        pubQueryDTO.setSearchPubYear(pubQueryInSolrVO.getSearchPubYear());
        pubQueryDTO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubQueryInSolrVO.getSearchPsnKey()));
        pubQueryDTO.setOrderBy(pubQueryInSolrVO.getOrderBy());
        pubQueryDTO.setPageNo(pubQueryInSolrVO.getPage().getPageNo());
        pubQueryDTO.setPageSize(pubQueryInSolrVO.getPage().getPageSize());
        pubListVO.setPubQueryDTO(pubQueryDTO);
        pubListVO.getPubQueryDTO().setSearchPsnId(SecurityUtils.getCurrentUserId());
        pubListVO.getPubQueryDTO().setServiceType("recommendListInSolr");
        pubRecommendService.pubRecommendListSearch(pubListVO);
      }
    } catch (Exception e) {
      logger.error("查询推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    view.addObject(pubListVO);
    view.setViewName("/pub/main/pub_recommend_list");
    return view;
  }

  /**
   * 添加科技领域
   */
  @RequestMapping(value = "/pub/pubconditions/ajaxAddScienArea", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxAddScienArea(@ModelAttribute("addPsnAreaCode") String addPsnAreaCode) {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (SecurityUtils.getCurrentUserId() > 0 && StringUtils.isNotBlank(addPsnAreaCode)) {
        resultMap = pubRecommendService.pubEditScienArea(SecurityUtils.getCurrentUserId(), addPsnAreaCode);
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 删除科技领域
   */
  @RequestMapping(value = "/pub/pubconditions/ajaxDeleteScienArea", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxDeleteScienArea(@ModelAttribute("deletePsnAreaCode") String deletePsnAreaCode) {
    Map<String, String> resultMap = new HashMap<>();
    try {
      if (SecurityUtils.getCurrentUserId() > 0 && StringUtils.isNotBlank(deletePsnAreaCode)) {
        resultMap = pubRecommendService.pubDeleteScienArea(SecurityUtils.getCurrentUserId(), deletePsnAreaCode);
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 保存关键词
   */
  @RequestMapping(value = "/pub/pubconditions/ajaxAddKeyWord", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxAddKeyWord(@ModelAttribute("addKeyWord") String addKeyWord) {
    Map<String, Object> resultMap = new HashMap<>();
    try {
      if (SecurityUtils.getCurrentUserId() > 0) {
        addKeyWord = StringEscapeUtils.unescapeHtml4(addKeyWord).toLowerCase();
        resultMap = pubRecommendService.pubSaveKeyWord(SecurityUtils.getCurrentUserId(), addKeyWord);
      }
    } catch (Exception e) {
      logger.error("论文推荐添加关键词出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 删除关键词
   */
  @RequestMapping(value = "/pub/pubconditions/ajaxDeleteKeyWord", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String ajaxDeleteKeyWord(@ModelAttribute("deletePsnKeyWord") String deletePsnKeyWord) {
    Map<String, String> resultMap = new HashMap<>();
    try {
      if (SecurityUtils.getCurrentUserId() > 0 && StringUtils.isNotBlank(deletePsnKeyWord)) {
        resultMap = pubRecommendService.pubDeleteKeyWord(SecurityUtils.getCurrentUserId(), deletePsnKeyWord);
      }
    } catch (Exception e) {
      logger.error("论文推荐删除关键词出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
      resultMap.put("result", "0");
    }

    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

}
