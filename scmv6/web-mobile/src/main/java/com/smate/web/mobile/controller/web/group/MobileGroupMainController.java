package com.smate.web.mobile.controller.web.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.spring.SpringUtils;
import com.smate.web.mobile.group.dto.GroupMainDTO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;

/**
 * 移动端群组首页
 * 
 * @author wsn
 * @date May 10, 2019
 */
@Controller
public class MobileGroupMainController {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;

  /**
   * 获取群组基本信息，进入群组详情页面
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/grp/main", "/grp/outside/main"})
  public ModelAndView showGroupMembers(GroupMainDTO dto) {
    ModelAndView view = new ModelAndView();
    dto.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        if (!CommonUtils.compareIntegerValue(dto.getDetails(), 1)) {
          params.add("isViewGrpDetail", "1");
          params.add("viewIp", SpringUtils.getRemoteAddr());
        }
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_BASEINFO,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("grpInfo", resultMap.get("grpInfo"));
          view.addObject("proposerCount", resultMap.get("proposerCount"));
        }
        // 群组存在或隐私start
        String isExist = Objects.toString(resultMap.get("isExist"));
        String isPrivate = Objects.toString(resultMap.get("isPrivate"));
        if ("0".equals(isExist) || "1".equals(isExist)) {// 隐私或者群组不存在跳到提示页面
          view.addObject("isExist", isExist);
          view.addObject("isPrivate", isPrivate);
          view.setViewName("/group/main/mobile_grp_notexists_private");
          return view;
        }
        // 群组存在或隐私end

        view.addObject("status", resultMap.get("status"));
        view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);

      }
    } catch (Exception e) {
      logger.error("进入移动端群组详情异常，grpId={}", dto.getDes3GrpId(), e);
    }
    view.addObject("dto", dto);
    if (CommonUtils.compareIntegerValue(dto.getDetails(), 1)) {
      view.setViewName("/group/main/mobile_grp_details");
    } else {
      view.setViewName("/group/main/mobile_grp_homepage_main");
    }
    return view;
  }

  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/grp/list/ajaxdyn", "/grp/outside/list/ajaxdyn"},
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object loadGrpDynList(GroupMainDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("showJsonDynInfo", "true");
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("page.pageNo", Objects.toString(dto.getPage().getPageNo(), "1"));
        params.add("page.totalCount", Objects.toString(dto.getPage().getTotalCount(), "0"));
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_DYN_LIST,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("dynList", resultMap.get("dynInfo"));
          view.addObject("totalCount", resultMap.get("totalCount"));
        }
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态列表异常，grpId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3PsnId(), e);
    }
    view.addObject("dto", dto);
    view.setViewName("/group/main/mobile_grp_dyn_list");
    return view;
  }


  /**
   * 显示群组关联的项目的相关信息
   * 
   * @param dto
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/grp/relationprj/info", "/grp/outside/relationprj/info"})
  @ResponseBody
  public ModelAndView showGrpRelationPrjInfo(GroupMainDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_RELATION_PRJ_INFO,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("prjInfo", resultMap.get("prjInfo"));
        }
      }
    } catch (Exception e) {
      logger.error("进入移动端群组详情异常，grpId={}", dto.getDes3GrpId(), e);
    }
    view.addObject("dto", dto);
    view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);
    view.setViewName("/group/main/mobile_grp_relation_prjinfo");
    return view;
  }



  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/dynopt/ajaxaward")
  @ResponseBody
  public Object ajaxDealWithDynAward(GroupMainDTO dto) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long dynId = NumberUtils.toLong(Des3Utils.decodeFromDes3(dto.getDes3DynId()), 0L);
      if (NumberUtils.isNotNullOrZero(dynId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3DynId", dto.getDes3DynId());
        params.add("des3PsnId", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_AWARD,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("群组动态赞操作异常，grpId={}, dynId={}", dto.getDes3DynId(), dto.getDes3GrpId(), e);
      result = result == null ? new HashMap<String, Object>() : result;
      result.put("result", "error");
    }
    return result;
  }


  /**
   * 群组动态发布页面
   * 
   * @param dto
   * @return
   */
  @RequestMapping("/grp/dyn/publish")
  public ModelAndView publishGrpDyn(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    ModelAndView view = new ModelAndView();
    try {
      if (NumberUtils.isNotNullOrZero(psnId) && StringUtils.isNotBlank(dto.getDes3GrpId())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3Psnid", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_PUBLISH_BASE_INFO,
                RestUtils.buildPostRequestEntity(params), Map.class);
        view.addObject("psnInfo", resultMap);
      }
    } catch (Exception e) {
      logger.error("进入移动端群组动态发布页面异常， psnId = {}, grpId={}", psnId, dto.getDes3GrpId(), e);
    }
    view.addObject("dto", dto);
    view.setViewName("/group/main/mobile_grp_dyn_publish");
    return view;
  }

  /**
   * 发布新的群组动态
   * 
   * @param dto
   * @return
   */
  @RequestMapping(value = "/grp/dyn/dopublish", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object doPublishGrpDyn(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (checkPublishDynParams(dto, psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", dto.getDes3GrpId().trim());
        params.add("dynContent", dto.getDynText().trim());
        params.add("des3ResId", dto.getDes3ResId().trim());
        params.add("tempType", dto.getTempType().trim());
        params.add("resType", dto.getResType().trim());
        params.add("des3Psnid", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
        result = (Map<String, String>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_PUBLISH,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端发布群组动态异常， psnId = {}, grpId={}", psnId, dto.getDes3GrpId(), e);
      result.put("result", "error");
    }
    return result;
  }



  /**
   * 进入动态详情页面
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/dyn/details", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object loadGrpDynDetails(GroupMainDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId)
          && StringUtils.isNotBlank(dto.getDes3DynId())) {
        dto.setDes3PsnId(Des3Utils.encodeToDes3(psnId.toString()));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("showJsonDynInfo", "true");
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("des3DynId", dto.getDes3DynId());
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_DETAILS,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("dynInfo", resultMap.get("dynInfo"));
          view.addObject("psnName", resultMap.get("psnName"));
          view.addObject("avatars", resultMap.get("avatars"));
        }
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态列表异常，grpId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3PsnId(), e);
    }
    view.addObject("dto", dto);
    view.setViewName("/group/main/mobile_grp_dyn_detail");
    return view;
  }



  /**
   * 加载群组动态评论列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/dyn/ajaxcomments", produces = "application/json;charset=UTF-8")
  public Object loadGrpDynComments(GroupMainDTO dto) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId)
          && StringUtils.isNotBlank(dto.getDes3DynId())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("des3DynId", dto.getDes3DynId());
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_COMMENTS_LIST,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("commentList", resultMap.get("dynCommentList"));
        }
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态评论列表异常，grpId={}, dynId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3DynId(), psnId, e);
    }
    view.addObject("dto", dto);
    view.setViewName("/group/main/mobile_grp_dyn_comments");
    return view;
  }



  /**
   * 评论群组动态
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/dyn/docomment", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object doCommentGrpDyn(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(dto.getDes3GrpId()) && StringUtils.isNotBlank(dto.getDes3DynId())
          && (StringUtils.isNotBlank(dto.getCommentContent()) || StringUtils.isNotBlank(dto.getDes3ResId()))) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", dto.getDes3GrpId().trim());
        if (StringUtils.isNotBlank(dto.getDes3ResId())) {
          params.add("commentResId", Des3Utils.decodeFromDes3(dto.getDes3ResId()));
        }
        if (StringUtils.isNotBlank(dto.getCommentContent())) {
          params.add("commentContent", dto.getCommentContent().trim());
        }
        params.add("des3DynId", dto.getDes3DynId().trim());
        params.add("des3PsnId", Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
        result = (Map<String, String>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_DO_COMMENT,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端评论群组动态异常， psnId = {}, grpId={}, dynId={}, comment={}, resId={}", psnId, dto.getDes3GrpId(),
          dto.getDes3DynId(), dto.getCommentContent(), dto.getDes3ResId(), e);
      result.put("result", "error");
    }
    return result;
  }


  /**
   * 置顶群组操作
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/opt/ajaxsticky", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object stickyGrp(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(dto.getDes3GrpId()) && NumberUtils.isNotNullOrZero(psnId)
          && StringUtils.isNotBlank(dto.getStickyOpt())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", dto.getDes3GrpId().trim());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("setTopOpt", dto.getStickyOpt().trim());
        result = (Map<String, String>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_OPT_STICKY,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端置顶群组操作异常, des3GrpId={}, psnId={}", dto.getDes3GrpId(), psnId, e);
      result.put("result", "error");
    }
    return result;
  }


  /**
   * 退出群组操作
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/opt/ajaxquit", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object quitGrp(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(dto.getDes3GrpId()) && NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", dto.getDes3GrpId().trim());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("delType", "98");
        result = (Map<String, String>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_OPT_QUIT,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("移动端退出群组操作异常, des3GrpId={}, psnId={}", dto.getDes3GrpId(), psnId, e);
      result.put("result", "error");
    }
    return result;
  }


  private boolean checkPublishDynParams(GroupMainDTO dto, Long currentPsnId) {
    boolean hasText = StringUtils.isNotBlank(dto.getDes3GrpId()) && StringUtils.isNotBlank(dto.getDynText());
    boolean hasRes = StringUtils.isNotBlank(dto.getDes3ResId()) && StringUtils.isNotBlank(dto.getResType());
    return StringUtils.isNotBlank(dto.getDes3GrpId()) && (hasText || hasRes)
        && NumberUtils.isNotNullOrZero(currentPsnId);
  }

}
