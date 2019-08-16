package com.smate.web.mobile.controller.data.group;

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

import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.number.NumberUtils;
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
public class MobileGroupMemberDataController {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainMobile}")
  private String domainMobile;



  /**
   * 获取群组成员列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/memberlist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String reqGrpMembersListData(GrpMemberDTO dto) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        params.add("page.pageNo", Objects.toString(dto.getPage().getPageNo(), "1"));
        params.add("page.totalCount", Objects.toString(dto.getPage().getTotalCount(), "0"));
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_MEMBERS,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("获取群组人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
      result = result == null ? new HashMap<String, Object>() : result;
    }
    return AppActionUtils.buildReturnInfo(result.get("infoList"),
        NumberUtils.toInt(Objects.toString(result.get("totalCount"), "0")),
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")));
  }



  /**
   * 获取群组成员申请列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/applylist", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String reqGrpApplyMembersData(GrpMemberDTO dto) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      String des3GrpId = dto.getDes3GrpId();
      if (StringUtils.isNotBlank(des3GrpId)) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("des3GrpId", des3GrpId);
        params.add("psnId", psnId.toString());
        params.add("page.pageSize", "1000");
        result = (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.GET_GRP_APPLY_MEMBERS,
            RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("获取群组申请人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
      result = result == null ? new HashMap<String, Object>() : result;
    }
    return AppActionUtils.buildReturnInfo(result.get("infoList"),
        NumberUtils.toInt(Objects.toString(result.get("totalCount"), "0")),
        AppActionUtils.changeResultStatus(Objects.toString(result.get("status"), "error")));
  }



  /**
   * 获取群组成员申请列表
   * 
   * @param dto
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/data/grp/dealapply", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object dealMembersApply(GrpMemberDTO dto) {
    Map<String, Object> result = new HashMap<String, Object>();
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
        result =
            (HashMap<String, Object>) restTemplate.postForObject(domainMobile + GrpApiConsts.DEAL_GRP_MEMBERS_APPLY,
                RestUtils.buildPostRequestEntity(params), Map.class);
      }
    } catch (Exception e) {
      logger.error("获取群组人员信息异常, currentPsnId={}, groupId={}", dto.getPsnId(), dto.getGrpId(), e);
      result = result == null ? new HashMap<String, Object>() : result;
    }
    return AppActionUtils.buildReturnInfo(null, 0,
        AppActionUtils.changeResultStatus(Objects.toString(result.get("result"), "error")));
  }



}
