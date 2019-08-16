package com.smate.web.mobile.controller.data.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
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
public class MobileGroupMainDataController {
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
  @RequestMapping(value = "/data/grp/baseinfo", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String reqGroupMembersData(GroupMainDTO dto) {
    List responseData = new ArrayList();
    HashMap<String, Object> result = new HashMap<String, Object>();
    dto.setDes3PsnId(Des3Utils.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_BASEINFO,
            RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(result)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(result.get("status"), ""))) {
          responseData.add(result.get("grpInfo"));
          // 获取相关联的项目信息
          if ("11".equals(Objects.toString(result.get("grpCategory"), ""))) {
            MultiValueMap<String, String> relationPrjParam = new LinkedMultiValueMap<String, String>();
            relationPrjParam.add("des3GrpId", des3GrpId);
            HashMap<String, Object> resultMap = (HashMap<String, Object>) restTemplate.postForObject(
                domainMobile + GrpApiConsts.GET_GRP_RELATION_PRJ_INFO,
                RestUtils.buildPostRequestEntity(relationPrjParam), Map.class);
            if (MapUtils.isNotEmpty(resultMap)
                && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
              responseData.add(resultMap.get("prjInfo"));
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("获取移动端群组详情异常，grpId={}", dto.getDes3GrpId(), e);
      result = result == null ? new HashMap<String, Object>() : result;
    }
    return AppActionUtils.buildReturnInfo(responseData, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")));
  }


  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/dynlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object loadGrpDynList(GroupMainDTO dto) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("showJsonDynInfo", "true");
        params.add("page.pageNo", Objects.toString(dto.getPage().getPageNo(), "1"));
        params.add("page.totalCount", Objects.toString(dto.getPage().getTotalCount(), "0"));
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_DYN_LIST,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          result.put("dynList", resultMap.get("dynInfo"));
          result.put("totalCount", resultMap.get("totalCount"));
        }
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态列表异常，grpId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3PsnId(), e);
    }
    return result;
  }



  /**
   * 群组动态赞操作
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/awarddyn", produces = "application/json;charset=UTF-8")
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
   * 发布新的群组动态
   * 
   * @param dto
   * @return
   */
  @RequestMapping(value = "/data/grp/publishdyn", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object doPublishGrpDyn(GroupMainDTO dto) {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (checkPublishDynParams(dto, psnId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", StringUtils.trim(dto.getDes3GrpId()));
        params.add("dynContent", StringUtils.trim(dto.getDynText()));
        params.add("des3ResId", StringUtils.trim(dto.getDes3ResId()));
        params.add("tempType", StringUtils.trim(dto.getTempType()));
        params.add("resType", StringUtils.trim(dto.getResType()));
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
   * 加载群组动态评论列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/commentlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object loadGrpDynComments(GroupMainDTO dto) {
    Map<String, Object> result = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId)
          && StringUtils.isNotBlank(dto.getDes3DynId())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("des3DynId", dto.getDes3DynId());
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_COMMENTS_LIST,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态评论列表异常，grpId={}, dynId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3DynId(), psnId, e);
      result.put("result", "error");
    }
    return result;
  }



  /**
   * 评论群组动态
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/commentdyn", produces = "application/json;charset=UTF-8")
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
   * 单个动态详情信息
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/dyn/detail", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object loadGrpDynDetails(GroupMainDTO dto) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      String des3GrpId = dto.getDes3GrpId();
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(des3GrpId) && NumberUtils.isNotNullOrZero(psnId)
          && StringUtils.isNotBlank(dto.getDes3DynId())) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GroupId", des3GrpId);
        params.add("des3CurrentPsnId", Des3Utils.encodeToDes3(psnId.toString()));
        params.add("des3DynId", dto.getDes3DynId());
        params.add("showJsonDynInfo", "true");
        HashMap<String, Object> resultMap =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GRP_DYN_DETAILS,
                RestUtils.buildPostRequestEntity(params), Map.class);
        if (MapUtils.isNotEmpty(resultMap)
            && GrpApiConsts.API_RESULT_SUCCESS.equalsIgnoreCase(Objects.toString(resultMap.get("status"), ""))) {
          result.put("dynInfo", resultMap.get("dynInfo"));
          result.put("psnName", resultMap.get("psnName"));
          result.put("avatars", resultMap.get("avatars"));
        }
        result.put("status", "success");
      }
    } catch (Exception e) {
      logger.error("获取移动端群组动态详情异常，grpId={}, psnId={}", dto.getDes3GrpId(), dto.getDes3PsnId(), e);
    }
    return AppActionUtils.buildReturnInfo(result, 1,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")));
  }


  /**
   * 置顶群组操作
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/opt/sticky", produces = "application/json;charset=UTF-8")
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
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")));
  }


  /**
   * 退出群组操作
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/opt/quit", produces = "application/json;charset=UTF-8")
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
    return AppActionUtils.buildReturnInfo(result, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")));
  }



  private boolean checkPublishDynParams(GroupMainDTO dto, Long currentPsnId) {
    boolean hasText = StringUtils.isNotBlank(dto.getDes3GrpId()) && StringUtils.isNotBlank(dto.getDynText());
    boolean hasRes = StringUtils.isNotBlank(dto.getDes3ResId()) && StringUtils.isNotBlank(dto.getResType());
    return StringUtils.isNotBlank(dto.getDes3GrpId()) && (hasText || hasRes)
        && NumberUtils.isNotNullOrZero(currentPsnId);
  }



}


