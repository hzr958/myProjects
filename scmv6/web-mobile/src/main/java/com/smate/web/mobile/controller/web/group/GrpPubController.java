package com.smate.web.mobile.controller.web.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.vo.GroupPubVO;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;

@Controller
public class GrpPubController {
  final protected Logger logger = LoggerFactory.getLogger(GroupController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 群组成果/文献首页
   * 
   * @param pubVO
   * @return
   */
  @RequestMapping(value = {"/grp/mobile/grppubmain", "/grp/outside/mobile/grppubmain"})
  public ModelAndView grpPubMain(GroupPubVO pubVO) {
    ModelAndView model = new ModelAndView();
    try {
      HashMap<String, Object> result = getGroupCategory(pubVO.getDes3GrpId());
      Integer grpCategory = (Integer) result.get("grpCategory");
      Integer psnRole = (Integer) result.get("psnRole");
      if (grpCategory != 11) {// 不是项目群组的都是查询全部成果
        pubVO.setShowPrjPub(0);
        pubVO.setShowRefPub(0);
      }
      pubVO.setPsnRole(psnRole);
      pubVO.setGrpCategory(grpCategory);
      // 群组存在或隐私，模块隐私判断start
      String isExist = (String) result.get("isExist");
      String isPrivate = (String) result.get("isPrivate");
      if ("0".equals(isExist) || "1".equals(isPrivate)) {// 隐私或者群组不存在跳到提示页面
        model.addObject("isExist", isExist);
        model.addObject("isPrivate", isPrivate);
        model.setViewName("/group/main/mobile_grp_notexists_private");
        return model;
      }
      boolean modelIsPrivate = modelIsPrivate(pubVO, (HashMap<String, Object>) result.get("grpControl"));// 是否哟访问改模块的权限，没有就跳到群组首页
      if (modelIsPrivate) {
        model.setViewName("forward:/grp/main?des3GrpId=" + pubVO.getDes3GrpId());
        return model;
      }
      // 群组存在或隐私，模块隐私判断end
      model.addObject("pubVO", pubVO);
      model.addObject("grpControl", result.get("grpControl"));
      model.addObject("proposerCount", result.get("proposerCount"));
      model.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);
      model.setViewName("/group/grpPub/grp_pub_main");
    } catch (Exception e) {
      logger.error("进入群组成果/文献页面出错，des3GrpId={}", pubVO.getDes3GrpId(), e);
    }
    return model;
  }

  private boolean modelIsPrivate(GroupPubVO pubVO, Map<String, Object> mapObj) {
    boolean isPrivate = false;
    if (pubVO.getPsnRole() == 9) {// 组外成员

      Integer showPrjPub = pubVO.getShowPrjPub();
      Integer showRefPub = pubVO.getShowRefPub();
      String isIndexPubOpen = (String) mapObj.get("isIndexPubOpen");
      String isPrjPubShow = (String) mapObj.get("isPrjPubShow");
      String isPrjRefShow = (String) mapObj.get("isPrjRefShow");

      if (showPrjPub != 1 && showRefPub != 1 && "0".equals(isIndexPubOpen)) {// 成果模块是否隐藏
        isPrivate = true;
      } else if (showPrjPub == 1 && showRefPub != 1 && "0".equals(isPrjPubShow)) {// 成果模块是否隐藏
        isPrivate = true;
      } else if (showPrjPub != 1 && showRefPub == 1 && "0".equals(isPrjRefShow)) {// 文献模块是否隐藏
        isPrivate = true;
      }
    }
    return isPrivate;
  }

  private HashMap<String, Object> getGroupCategory(String des3GrpId) throws Exception {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", des3GrpId);
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    HashMap<String, Object> result = postData(params, GrpApiConsts.GET_GRP_CONTROL);
    return result;
  }

  /**
   * 获取群组成果/文献列表
   * 
   * @param pubVO
   * @return
   */
  @RequestMapping(value = {"/grp/mobile/grppublist", "/grp/outside/mobile/grppublist"})
  public ModelAndView grpPubList(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      MultiValueMap<String, Object> params = getGrpPubListParam(pubVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.GRP_PUB_LIST);
      if (result != null && result.get("results") != null) {
        HashMap<String, Object> resultMap = (HashMap<String, Object>) result.get("results");
        view.addObject("pubList", resultMap.get("commentlist"));
        view.addObject("totalCount", resultMap.get("total"));
      }
    } catch (Exception e) {
      logger.error("获取推荐群组列表出错,psnid={}", psnId, e);
    }
    view.setViewName("/group/grpPub/grp_pub_list");
    return view;
  }

  /**
   * 群组动态分享获取群组成果或个人成果列表
   * 
   * @param pubVO
   * @return
   */
  @RequestMapping(value = "/grp/mobile/grpsharepublist")
  public ModelAndView grpSharePubList(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      MultiValueMap<String, Object> params = getShareGrpPubListParam(pubVO);// 获取参数
      HashMap<String, Object> result = postData(params, GrpApiConsts.GRP_SHARE_PUB_LIST);
      if (result != null && result.get("results") != null) {
        HashMap<String, Object> resultMap = (HashMap<String, Object>) result.get("results");
        view.addObject("pubList", resultMap.get("commentlist"));
        view.addObject("totalCount", resultMap.get("total"));
      }
    } catch (Exception e) {
      logger.error("获取推荐群组列表出错,psnid={}", psnId, e);
    }
    view.setViewName("/group/grpPub/grp_publish_dyn_pub_list");
    return view;
  }

  /**
   * 发现群组条件设置
   * 
   * @param groupVO
   * @return
   */
  @RequestMapping(value = {"/grp/mobile/grouppubconditions", "/grp/outside/mobile/grouppubconditions"})
  public ModelAndView groupPubConditions(GroupPubVO pubVO) {
    ModelAndView view = new ModelAndView();
    pubVO.setRecentYear5(getNearYear(pubVO.getCurrentYear(), 5));// 近5年
    pubVO.setRecentYear10(getNearYear(pubVO.getCurrentYear(), 10));// 近10年
    view.addObject("pubVO", pubVO);
    view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);
    view.setViewName("/group/grpPub/grp_pub_conditions");
    return view;
  }

  private String getNearYear(Integer currentYear, int num) {
    String recentYear = Objects.toString(currentYear);
    for (int i = 1; i < num; i++) {
      recentYear += "," + Objects.toString(currentYear - i);
    }
    return recentYear;
  }

  /**
   * 构造群组成果列表接口参数
   * 
   * @param pubVO
   * @return
   */
  private MultiValueMap<String, Object> getGrpPubListParam(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", pubVO.getDes3GrpId());
    params.add("showPrjPub", pubVO.getShowPrjPub());
    params.add("showRefPub", pubVO.getShowRefPub());
    params.add("searchKey", pubVO.getSearchKey());
    params.add("publishYear", pubVO.getPublishYear());
    params.add("pubType", pubVO.getPubType());
    params.add("includeType", pubVO.getIncludeType());// 收录类型
    params.add("orderBy", pubVO.getOrderBy());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", pubVO.getPage().getParamPageNo().toString());
    return params;
  }

  /**
   * 构造群组动态分享群组成果列表接口参数
   * 
   * @param pubVO
   * @return
   */
  private MultiValueMap<String, Object> getShareGrpPubListParam(GroupPubVO pubVO) {
    Long psnId = SecurityUtils.getCurrentUserId();
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    if (!"myPub".equals(pubVO.getPubListType())) {// 区分群组成果或者我的成果
      params.add("des3GrpId", pubVO.getDes3GrpId());
    }
    params.add("searchKey", pubVO.getSearchKey());
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    params.add("pageNo", pubVO.getPage().getParamPageNo().toString());
    return params;
  }

  public HashMap<String, Object> postData(MultiValueMap<String, Object> params, String url) throws Exception {
    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + url, params, Map.class);
    return result;
  }



  /**
   * 发布群组动态选择群组成果
   * 
   * @param pubVO
   * @return
   */
  @RequestMapping("/grp/publish/pub")
  public ModelAndView grpDynPublishPubList(GroupPubVO pubVO) {
    ModelAndView model = new ModelAndView();
    try {
      HashMap<String, Object> result = getGroupCategory(pubVO.getDes3GrpId());
      Integer grpCategory = (Integer) result.get("grpCategory");
      Integer psnRole = (Integer) result.get("psnRole");
      if (grpCategory != 11) {// 不是项目群组的都是查询全部成果
        pubVO.setShowPrjPub(0);
        pubVO.setShowRefPub(0);
      }
      pubVO.setPsnRole(psnRole);
      pubVO.setGrpCategory(grpCategory);
      model.addObject("pubVO", pubVO);
      model.addObject("grpControl", result.get("grpControl"));
      model.setViewName("/group/grpPub/grp_publish_dyn_pub_main");
    } catch (Exception e) {
      logger.error("发布群组动态进入选择群组成果/文献页面，des3GrpId={}", pubVO.getDes3GrpId(), e);
    }
    return model;
  }
}
