package com.smate.web.mobile.controller.web.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.mobile.group.dto.GrpMemberDTO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;

/**
 * 科研之友移动端群组controller
 * 
 * @author wsn
 * @date May 8, 2019
 */
@Controller
public class MobileGroupMemberController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;

  @RequestMapping(value = {"/grp/member/main", "/grp/outside/member/main"})
  public ModelAndView showGroupMembers(GrpMemberDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      HashMap<String, Object> result = getGroupCategory(dto.getDes3GrpId());
      Integer grpCategory = (Integer) result.get("grpCategory");
      Integer psnRole = (Integer) result.get("psnRole");
      dto.setPsnRole(psnRole);
      dto.setGrpCategory(grpCategory);
      dto.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
      // 群组存在或隐私，模块隐私判断start
      String isExist = (String) result.get("isExist");
      String isPrivate = (String) result.get("isPrivate");
      if ("0".equals(isExist) || "1".equals(isPrivate)) {// 隐私或者群组不存在跳到提示页面
        view.addObject("isExist", isExist);
        view.addObject("isPrivate", isPrivate);
        view.setViewName("/group/main/mobile_grp_notexists_private");
        return view;
      }
      boolean modelIsPrivate = modelIsPrivate(dto, (HashMap<String, Object>) result.get("grpControl"));// 是否哟访问改模块的权限，没有就跳到群组首页
      if (modelIsPrivate) {
        view.setViewName("forward:/grp/main?des3GrpId=" + dto.getDes3GrpId());
        return view;
      }
      // 群组存在或隐私，模块隐私判断end
      view.addObject("grpDTO", dto);
      view.addObject("grpControl", result.get("grpControl"));
      view.addObject("proposerCount", result.get("proposerCount"));
      view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);
      view.setViewName("/group/member/mobile_grp_members_main");
    } catch (Exception e) {
      logger.error("进入群组成员页面出错，des3GrpId={}", dto.getDes3GrpId(), e);
    }
    return view;
  }

  private boolean modelIsPrivate(GrpMemberDTO dto, Map<String, Object> mapObj) {
    boolean isPrivate = false;
    if (dto.getPsnRole() == 9) {// 组外成员
      String isIndexMemberOpen = (String) mapObj.get("isIndexMemberOpen");
      if ("0".equals(isIndexMemberOpen)) {// 成员模块是否隐藏
        isPrivate = true;
      }
    }
    return isPrivate;
  }

  /**
   * 加载成员模块
   * 
   * @param dto
   * @return
   */
  @RequestMapping("/grp/member/ajaxpage")
  public ModelAndView showGroupMembersListPage(GrpMemberDTO dto) {
    ModelAndView view = new ModelAndView();
    dto.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    view.addObject("grpDTO", dto);
    view.setViewName("/group/member/mobile_grp_member_model");
    return view;
  }



  /**
   * 获取群组成员列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/grp/member/ajaxlist", "/grp/outside/member/ajaxlist"})
  public ModelAndView ajaxGrpMembersList(GrpMemberDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        params.add("page.pageNo", Objects.toString(dto.getPage().getPageNo(), "1"));
        params.add("page.totalCount", Objects.toString(dto.getPage().getTotalCount(), "0"));
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_MEMBERS,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("grpMembers", resultMap.get("infoList"));
          view.addObject("totalCount", resultMap.get("totalCount"));
        }
      }
    } catch (Exception e) {
      logger.error("获取群组人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
    }
    view.addObject("pageNo", Objects.toString(dto.getPage().getPageNo(), "1"));
    view.setViewName("/group/member/mobile_grp_member_list");
    return view;
  }



  /**
   * 获取群组成员申请列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/member/ajaxapply")
  public ModelAndView ajaxGrpApplyMembers(GrpMemberDTO dto) {
    ModelAndView view = new ModelAndView();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        params.add("page.pageSize", "1000");
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_APPLY_MEMBERS,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          view.addObject("psnInfoList", resultMap.get("infoList"));
          view.addObject("totalCount", resultMap.get("totalCount"));
        }
      }
    } catch (Exception e) {
      logger.error("获取群组申请人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
    }
    view.setViewName("/group/member/mobile_grp_member_req_model");
    return view;
  }



  /**
   * 获取群组成员申请列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/apply/ajaxdeal")
  @ResponseBody
  public Object dealWithMembersApply(GrpMemberDTO dto) {
    Map<String, String> result = new HashMap<String, String>();
    String status = "error";
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      String des3GrpId = dto.getDes3GrpId();
      Integer optType = dto.getDisposeType();
      String targetPsnIds = dto.getTargetPsnIds();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId) && optType != null
          && StringUtils.isNotBlank(targetPsnIds)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        params.add("disposeType", optType.toString());
        params.add("targetPsnIds", targetPsnIds);
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.DEAL_GRP_MEMBERS_APPLY,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)) {
          status = Objects.toString(resultMap.get("result"), "error");
        }
      }
    } catch (Exception e) {
      logger.error("获取群组人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
    }
    result.put("result", status);
    return result;
  }

  private HashMap<String, Object> getGroupCategory(String des3GrpId) throws Exception {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", des3GrpId);
    Long psnId = SecurityUtils.getCurrentUserId();
    params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    HashMap<String, Object> result = postData(params, GrpApiConsts.GET_GRP_CONTROL);
    return result;
  }

  public HashMap<String, Object> postData(MultiValueMap<String, Object> params, String url) throws Exception {
    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + url, params, Map.class);
    return result;
  }
}
