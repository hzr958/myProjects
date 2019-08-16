package com.smate.web.mobile.controller.web.pub;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.v8pub.consts.PubApiInfoConsts;
import com.smate.web.mobile.v8pub.service.PubRecommendRestService;
import com.smate.web.mobile.v8pub.vo.pdwh.PubRecommendVO;
import com.smate.web.mobile.v8pub.vo.sns.PubOperateVO;

/*
 * 成果推荐
 */
@Controller
public class PubRecommendController extends WeChatBaseController {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubRecommendRestService pubRecommendRestService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;


  /**
   * 论文推荐-论文发现-论文收藏重定向方法，用于保持和PC端一致，用于处理SCM-24317
   * 
   * @return
   */
  @RequestMapping("/pub/mobile/applypapermain")
  public String applyPaperMain(String toPaperType) {
    String redUrl = "";
    switch (toPaperType == null ? "recommend" : toPaperType) {
      case "recommend":
        redUrl = "/pub/mobile/pubrecommendmain";
        break;
      case "find":
        redUrl = "/pub/find/area";
        break;
      case "collect":
        redUrl = "/pub/collect/main";
        break;
    }
    return "redirect:" + domainMobile + redUrl;
  }

  /**
   * 移动端推荐论文主页面
   * 
   * @return
   */
  @RequestMapping("/pub/mobile/pubrecommendmain")
  public ModelAndView pubMobileRecommendMain(ModelAndView model, PubRecommendVO pubVO) {
    pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getSearchPsnKey()));
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        pubRecommendRestService.pubRecommendConditional(psnId, pubVO);
      }
    } catch (Exception e) {
      logger.error("进入推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    model.addObject("pubVO", pubVO);
    model.setViewName("pub/recommendpub/recommendMain");
    return model;
  }

  @RequestMapping(value = "/pub/mobile/ajaxconditions")
  public ModelAndView pubRecommendConditional(PubRecommendVO pubVO) {
    pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getSearchPsnKey()));
    ModelAndView model = new ModelAndView();
    model.setViewName("pub/recommendpub/mobile_pub_conditions");
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        pubRecommendRestService.pubRecommendConditional(psnId, pubVO);
      }
    } catch (Exception e) {
      logger.error("获取推荐条件设置出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    model.addObject("pubVO", pubVO);
    return model;
  }

  /**
   * 论文推荐查询接口
   * 
   * @param pubQueryDTO
   * @return
   */
  @RequestMapping(value = "/pub/pubrecommend/ajaxpubList")
  public ModelAndView searchPubList(PubRecommendVO pubVO) {
    ModelAndView model = new ModelAndView();
    model.setViewName("pub/recommendpub/mobilePubList");
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (psnId > 0) {
        pubVO.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        // SCM-21373 其他类型的论文改为除期刊、专利、会议以外的论文
        String searchPubType = pubVO.getSearchPubType();
        if (StringUtils.isNotBlank(searchPubType) && searchPubType.contains("7")) {
          searchPubType = searchPubType + ",1,2,8,10,11";
          pubVO.setSearchPubType(searchPubType);
        }
        String searchPsnKey = pubVO.getSearchPsnKey();
        if (StringUtils.isBlank(searchPsnKey) || "[]".equals(searchPsnKey)) {
          pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getDefultKeyJson()));
        }
        String searchArea = pubVO.getSearchArea();
        if (StringUtils.isBlank(searchArea)) {
          pubVO.setSearchArea(pubVO.getDefultArea());
        }
        pubRecommendRestService.searchPubList(pubVO);
        model.addObject("pubVO", pubVO);
      }
    } catch (Exception e) {
      logger.error("查询推荐论文列表出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    return model;
  }

  /**
   * 移动端编辑科技领域
   * 
   * @return
   */
  @RequestMapping(value = "/pub/mobile/editcoditionsarea")
  public ModelAndView editConditionsArea(PubRecommendVO pubVO) {
    ModelAndView model = new ModelAndView();
    model.setViewName("pub/recommendpub/edit_area_conditions");
    try {
      pubVO.setPsnId(SecurityUtils.getCurrentUserId());
      if (pubVO.getPsnId() > 0) {
        pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getSearchPsnKey()));
        pubRecommendRestService.getAllScienceArea(pubVO);
      }
    } catch (Exception e) {
      logger.error("进入移动端论文推荐编辑科技领域页面出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    model.addObject("pubVO", pubVO);
    return model;
  }

  /**
   * 移动端编辑关键词
   * 
   * @return
   */
  @RequestMapping(value = "/pub/mobile/editcoditionskeyword")
  public ModelAndView editCoditionsKeyWord(PubRecommendVO pubVO) {
    ModelAndView model = new ModelAndView();
    model.setViewName("pub/recommendpub/edit_keyword_conditions");
    try {
      pubVO.setPsnId(SecurityUtils.getCurrentUserId());
      if (pubVO.getPsnId() > 0) {
        pubVO.setSearchPsnKey(HtmlUtils.htmlUnescape(pubVO.getSearchPsnKey()));
        pubRecommendRestService.getPsnAllKeyWord(pubVO);
      }
    } catch (Exception e) {
      logger.error("进入移动端论文推荐编辑关键词页面出错， psnId = " + SecurityUtils.getCurrentUserId(), e);
    }
    model.addObject("pubVO", pubVO);
    return model;
  }

  /**
   * 添加科技领域
   */
  @RequestMapping(value = "/pub/mobile/ajaxsavepsnareas", produces = "application/json; charset=utf-8")
  @ResponseBody
  public String ajaxAddScienArea(PubRecommendVO pubVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId > 0) {
        if (StringUtils.isNotBlank(pubVO.getDefultArea())) {
          return pubRecommendRestService.addMobileScienAreas(Des3Utils.encodeToDes3(psnId.toString()),
              pubVO.getDefultArea());
        } else {
          resultMap.put("result", "error");
          resultMap.put("msg", "至少选择一个科技领域");
        }
      }
    } catch (Exception e) {
      logger.error("论文科技领域出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  /**
   * 增加关键词
   */
  @RequestMapping(value = "/pub/mobile/ajaxsavepsnkeywords")
  @ResponseBody
  public String ajaxAddKeyWord(PubRecommendVO pubVO) {
    Map<String, String> resultMap = new HashMap<>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (psnId > 0 && StringUtils.isNotBlank(pubVO.getDefultKeyJson())) {
        return pubRecommendRestService.addMobileKeyWords(Des3Utils.encodeToDes3(psnId.toString()),
            pubVO.getDefultKeyJson());
      }
    } catch (Exception e) {
      logger.error("论文推荐添加关键词出错， psnId = " + psnId, e);
      resultMap.put("result", "0");
    }
    return JacksonUtils.jsonObjectSerializer(resultMap);
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/pub/mobile/ajaxuninterested", method = RequestMethod.POST)
  @ResponseBody
  public Object notViewPubRecommend(PubOperateVO pubOperateVO) {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId > 0L) {
      try {
        pubOperateVO.setPsnId(psnId);
        map = restTemplate.postForObject(domainscm + PubApiInfoConsts.NOT_VIEW_PDWH, pubOperateVO, Map.class);
      } catch (Exception e) {
        logger.error("mobile首页动态推荐论文不感兴趣操作出错,psnId=" + psnId + ",pubId=" + pubOperateVO.getPubId(), e);
        map.put("errmsg", "调用远程数据接口异常");
      }
    } else {
      logger.error("mobilepsnId,psnId={},pubId={}", psnId, pubOperateVO);
      map.put("errmsg", "参数校验不通过");
    }
    return map;
  }

}
