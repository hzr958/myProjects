package com.smate.web.mobile.controller.web.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import com.smate.web.mobile.group.vo.MobileGroupFileVO;
import com.smate.web.mobile.utils.RestUtils;
import com.smate.web.mobile.v8pub.consts.GrpApiConsts;
import com.smate.web.mobile.v8pub.consts.MobileGrpFileApiConsts;

/**
 * @description 移动端群组文件控制器
 * @author xiexing
 * @date 2019年5月10日
 */
@Controller
public class MobileGroupFileController {
  private static final Logger logger = LoggerFactory.getLogger(MobileGroupFileController.class);

  @Value("${domainMobile}")
  private String domainMobile;

  @Autowired
  private RestTemplate restTemplate;

  /**
   * 进入群组文件首页
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = {"/grp/main/grpfilemain", "/grp/outside/main/grpfilemain"})
  public ModelAndView loadGrpFileMain(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    try {
      HashMap<String, Object> result = getGroupCategory(groupFile);
      Integer grpCategory = (Integer) result.get("grpCategory");
      // 群组存在或隐私，模块隐私判断start
      String isExist = (String) result.get("isExist");
      String isPrivate = (String) result.get("isPrivate");
      Integer psnRole = (Integer) result.get("psnRole");
      groupFile.setPsnRole(psnRole);
      if ("0".equals(isExist) || "1".equals(isPrivate)) {// 隐私或者群组不存在跳到提示页面
        view.addObject("isExist", isExist);
        view.addObject("isPrivate", isPrivate);
        view.setViewName("/group/main/mobile_grp_notexists_private");
        return view;
      }
      boolean modelIsPrivate = modelIsPrivate(groupFile, (HashMap<String, Object>) result.get("grpControl"));// 是否哟访问改模块的权限，没有就跳到群组首页
      if (modelIsPrivate) {
        view.setViewName("forward:/grp/main?des3GrpId=" + groupFile.getDes3GrpId());
        return view;
      }
      // 群组存在或隐私，模块隐私判断end
      groupFile.setGrpCategory(grpCategory);
      view.addObject("groupFile", groupFile);
      view.addObject("grpControl", result.get("grpControl"));
      view.addObject("proposerCount", result.get("proposerCount"));
      view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);
      view.setViewName("/group/file/mobile_grp_file_main");
    } catch (Exception e) {
      logger.error("进入群组文件页面出错，des3GrpId={}", groupFile.getDes3GrpId(), e);
    }
    return view;
  }

  private boolean modelIsPrivate(MobileGroupFileVO groupFile, Map<String, Object> mapObj) {
    boolean isPrivate = false;
    if (groupFile.getPsnRole() == 9) {// 组外成员
      Integer courseFileType = groupFile.getCourseFileType() == null ? 0 : groupFile.getCourseFileType();
      Integer workFileType = groupFile.getWorkFileType() == null ? 0 : groupFile.getWorkFileType();
      String isIndexFileOpen = (String) mapObj.get("isIndexFileOpen");
      String isWorkFileShow = (String) mapObj.get("isWorkFileShow");
      String isCurwareFileShow = (String) mapObj.get("isCurwareFileShow");
      if (workFileType != 1 && courseFileType != 2 && "0".equals(isIndexFileOpen)) {// 文件模块是否隐藏
        isPrivate = true;
      } else if (workFileType == 1 && courseFileType != 2 && "0".equals(isWorkFileShow)) {// 作业模块是否隐藏
        isPrivate = true;
      } else if (workFileType != 1 && courseFileType == 2 && "0".equals(isCurwareFileShow)) {// 课件模块是否隐藏
        isPrivate = true;
      }
    }
    return isPrivate;
  }

  /**
   * 获取群组文件列表
   * 
   * @return
   */
  @RequestMapping(value = {"/grp/mobile/querygrpfilelist", "/grp/outside/mobile/querygrpfilelist"})
  public ModelAndView queryGrpFileList(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    Long psnId = SecurityUtils.getCurrentUserId();
    Long grpId = groupFile.getGrpId();
    if (checkParam(grpId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("grpId", grpId.toString());
        params.add("searchKey", groupFile.getSearchKey());
        params.add("searchGrpFileMemberId", groupFile.getMemberId() != null ? groupFile.getMemberId().toString() : "");
        params.add("des3GrpLabelIds", groupFile.getDes3GrpLabelId());
        if (groupFile.getWorkFileType() != null) {
          params.add("workFileType", groupFile.getWorkFileType().toString());
        }
        if (groupFile.getCourseFileType() != null) {
          params.add("courseFileType", groupFile.getCourseFileType().toString());
        }
        params.add("page.pageNo",
            groupFile.getPage().getPageNo() != null ? groupFile.getPage().getPageNo().toString() : "");
        Map<String, Object> result =
            restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_GRP_FILE_LIST,
                RestUtils.buildPostRequestEntity(params), Map.class);
        Map<String, Object> resultMap =
            result != null && result.get("results") != null ? (Map<String, Object>) result.get("results") : null;
        if (resultMap != null && resultMap.size() > 0) {
          view.addObject("fileList", resultMap.get("commentlist"));
          view.addObject("totalCount", resultMap.get("total"));
        }
      } catch (Exception e) {
        logger.error("获取群组文件出错,groupFile={}", groupFile, e);
      }
    }
    view.setViewName("/group/file/mobile_grp_file_list");
    return view;
  }

  /**
   * 进入条件筛选页面
   * 
   * @return
   */
  @RequestMapping(value = {"/grp/mobile/showconditions", "/grp/outside/mobile/showconditions"})
  public ModelAndView showConditions(MobileGroupFileVO groupFile) {
    ModelAndView view = new ModelAndView();
    Long grpId = groupFile.getGrpId();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNotNullOrZero(grpId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("grpId", grpId.toString());
        params.add("psnId", psnId.toString());
        if (groupFile.getWorkFileType() != null && groupFile.getWorkFileType() == 1) {
          params.add("grpFileType", "1");
        } else if (groupFile.getCourseFileType() != null && groupFile.getCourseFileType() == 2) {
          params.add("grpFileType", "2");
        } else {
          params.add("grpFileType", "0");
        }
        Map<String, Object> result = restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.QUERY_CONDITIONS,
            RestUtils.buildPostRequestEntity(params), Map.class);
        view.addObject("psnInfos", result.get("psnInfos"));
        view.addObject("grpLabels", result.get("grpLabels"));
        view.addObject("groupFile", groupFile);
        view.addObject("isLogin", SecurityUtils.getCurrentUserId() > 0 ? true : false);

      } catch (Exception e) {
        logger.error("获取群组文件查询条件出错,grpId={}", grpId, e);
      }
    }
    view.setViewName("/group/file/mobile_grp_file_conditions");
    return view;
  }

  /**
   * 移动端群组文件分享给联系人
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = "/grp/file/sharetofriend")
  @ResponseBody
  public Object shareToFriend(MobileGroupFileVO groupFile) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (checkParam(groupFile, psnId)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("grpId", groupFile.getGrpId().toString());
        params.add("des3GrpFileId", groupFile.getDes3ResId());
        params.add("des3ReceiverIds", groupFile.getDes3RecieverIds());
        params.add("fileNames", groupFile.getFileName());
        params.add("textContent", groupFile.getLeaveMsg());
        Map<String, Object> resultMap =
            restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.FILE_SHARE_TO_FRIEND,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (resultMap != null && "success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          result.put("status", "success");
          result.put("msg", "share success");
        } else {
          throw new Exception("分享群组文件到联系人失败");
        }
      } catch (Exception e) {
        logger.error("分享群组文件到联系人失败,psnId={}", psnId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    return result;
  }

  /**
   * 移动端群组文件分享到群组动态
   * 
   * @param groupFile
   * @return
   */
  @RequestMapping(value = "/grp/file/sharetogrp")
  @ResponseBody
  public Object shareToGrp(MobileGroupFileVO groupFile) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    if (checkParam(psnId, groupFile)) {
      try {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("psnId", psnId.toString());
        params.add("groupId", groupFile.getGrpId().toString());
        params.add("des3ResId", groupFile.getDes3ResId());
        params.add("des3ReceiverGrpId", groupFile.getDes3GrpIds());
        params.add("dynContent", groupFile.getLeaveMsg());
        params.add("tempType", "GRP_SHAREFILE");
        params.add("resType", "grpfile");
        Map<String, Object> resultMap =
            restTemplate.postForObject(domainMobile + MobileGrpFileApiConsts.FILE_SHARE_TO_GRP,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (resultMap != null && "success".equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          result.put("status", "success");
          result.put("msg", "share success");
        } else {
          throw new Exception("分享群组文件到群组失败");
        }
      } catch (Exception e) {
        logger.error("分享群组文件到群组失败,psnId={}", psnId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    return result;
  }

  /**
   * 参数校验-群组文件分享到群组动态
   * 
   * @param psnId
   * @param groupFile
   * @return
   */
  public boolean checkParam(Long psnId, MobileGroupFileVO groupFile) {
    Long grpId = groupFile.getGrpId();
    String des3FileId = groupFile.getDes3ResId();
    String des3GrpIds = groupFile.getDes3GrpIds();
    if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(grpId) && StringUtils.isNotEmpty(des3FileId)
        && (StringUtils.isNotEmpty(des3GrpIds))) {
      return true;
    }
    return false;
  }


  /**
   * 参数校验-群组文件分享到联系人
   * 
   * @param groupFile
   * @param psnId
   * @return
   */
  public boolean checkParam(MobileGroupFileVO groupFile, Long psnId) {
    Long grpId = groupFile.getGrpId();
    String des3FileId = groupFile.getDes3ResId();
    String des3ReceiverIds = groupFile.getDes3RecieverIds();
    String receiverEmails = groupFile.getReceiverEmails();
    if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(grpId) && StringUtils.isNotEmpty(des3FileId)
        && (StringUtils.isNotEmpty(des3ReceiverIds) || StringUtils.isNotEmpty(receiverEmails))) {
      return true;
    }
    return false;
  }


  /**
   * 收藏群组文件
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/grp/collect/ajaxfile", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object collectGrpDynFile(MobileGroupFileVO groupFile) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      if (StringUtils.isNotBlank(groupFile.getDes3GrpFileId()) && NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpFileId", groupFile.getDes3GrpFileId());
        params.add("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
        result = restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_COLLECT_FILE,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("收藏群组文件异常，fileId={}, psnId={}", groupFile.getDes3GrpFileId(), psnId, e);
      result = result == null ? new HashMap<String, Object>() : result;
      result.put("result", "error");
    }
    return result;
  }


  /**
   * 参数校验
   * 
   * @param grpId
   * @param psnId
   * @return
   */
  public boolean checkParam(Long grpId) {
    if (NumberUtils.isNotNullOrZero(grpId)) {
      return true;
    }
    return false;
  }

  private HashMap<String, Object> getGroupCategory(MobileGroupFileVO groupFile) throws Exception {
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
    params.add("des3GrpId", groupFile.getDes3GrpId());
    params.add("workFileType", groupFile.getWorkFileType());
    params.add("courseFileType", groupFile.getCourseFileType());
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

